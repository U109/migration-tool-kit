package com.zzz.migration.database.executor;

import com.zzz.migration.common.utils.CloseObjUtil;
import com.zzz.migration.database.convert.IDataConvert;
import com.zzz.migration.entity.dataSourceEmtity.CloseableDataSource;
import com.zzz.migration.entity.dataSourceEmtity.WrapHikariDataSource;
import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migration.entity.dataTypeEntity.DataType;
import com.zzz.migration.entity.migrationObjEntity.TableColumn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:26
 * @description:
 */
@Slf4j
public class MySqlDataBaseExecutor extends AbstractDataBaseExecutor {

    private Statement statement;
    private ResultSet rs;
    private IDataConvert IConvert;
    private PreparedStatement preparedStatement;

    public MySqlDataBaseExecutor(WrapHikariDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<ColumnEntity> getColumnEntityList(String tableName) {
        List<ColumnEntity> columnEntityList = new ArrayList<>();
        ResultSet colRet = null;//表元数据信息

        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            colRet = metaData.getColumns(properties.getDbName(), properties.getSchemaName(), tableName, "%");
            while (colRet.next()) {
                String columnName = colRet.getString("COLUMN_NAME");
                String columnType = colRet.getString("TYPE_NAME").toUpperCase();
                long columnLength = colRet.getInt("COLUMN_SIZE");
                int columnPosition = colRet.getInt("DECIMAL_DIGITS");
                String autoIncrement = colRet.getString("IS_AUTOINCREMENT");

                //处理TIMESTAMP(6)这种
                if ((columnType.contains("(")) && (columnType.contains(")"))) {
                    int start = columnType.indexOf("(");
                    columnType = columnType.substring(0, start);
                }

                ColumnEntity columnEntity = new ColumnEntity(columnName, new DataType(columnType, columnLength, columnPosition));

                if (StringUtils.isNoneBlank(autoIncrement) && StringUtils.equals(autoIncrement, "YES")) {
                    columnEntity.setAutoIncrement(true);
                }
                columnEntityList.add(columnEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseObjUtil.closeAll(colRet);
        }
        return columnEntityList;
    }

    @Override
    public void executeSQL(String sql) throws Exception {
        Statement statement = null;
        try {
            statement = getConnection().createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            CloseObjUtil.closeAll(statement);
        }
    }

    @Override
    public void closeExecutor() {
        super.closeExecutor();
        CloseObjUtil.closeAll(statement, preparedStatement, rs);
    }

    @Override
    public ResultSet getResultSet(String sql, int fetchSize) {
        try {
            statement = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(fetchSize);
            rs = statement.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public List<Object> convertReadDataTypes(List<TableColumn> columnList, ResultSet rs) {
        return IConvert.convertReadDataTypes(columnList, rs);
    }

    @Override
    public long executeInsertSql(String insertSql, List<List<Object>> dataList, List<TableColumn> columnList) throws Exception {


        try {
            List<DataType> columnTypeList = new ArrayList<>();
            for (TableColumn columnInfo : columnList) {
                columnTypeList.add(columnInfo.getDestColumn().getColumnType());
            }

            preparedStatement = getConnection().prepareStatement(insertSql);

            for (int i = 0; i < dataList.size(); i++) {
                List<Object> dataRow = dataList.get(i);

                int columnTypeListSize = columnTypeList.size();

                for (int j = 0; j < columnTypeListSize; j++) {
                    DataType type = columnTypeList.get(j);

                    int parameterIndex = j + 1;
                    if (dataRow.get(j) == null) {
                        preparedStatement.setObject(parameterIndex, null);
                        continue;
                    }
                    IConvert.convertWriteDataTypes(type.getDataTypeName(), preparedStatement, parameterIndex, dataRow, j);
                }
                preparedStatement.addBatch();
                dataList.remove(i--);
            }
            preparedStatement.executeBatch();
            return 0;
        } catch (Exception e) {
            log.error("write data error : " + e.getMessage());
            throw e;
        } finally {
            preparedStatement.clearBatch();
            preparedStatement.clearParameters();
            preparedStatement.close();
        }
    }

    @Override
    public void close() throws Exception {

    }
}
