package com.zzz.migrationtoolkit.dataBase.dbExecutor;

import com.zzz.migrationtoolkit.common.utils.CloseObjUtil;
import com.zzz.migrationtoolkit.core.convert.MySqlToMySqlDataTypeConvert;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:26
 * @description:
 */
@Slf4j
public class MySqlDataBaseExecutor extends AbstractDataBaseExecutor {


    private MySqlConnInfo mySqlConnInfo;
    private MySqlToMySqlDataTypeConvert mySqlToMySqlDataTypeConvert;

    public MySqlDataBaseExecutor(){

    }
    public MySqlDataBaseExecutor(MySqlConnInfo mySqlConnInfo,MySqlToMySqlDataTypeConvert mySqlToMySqlDataTypeConvert){
        super(mySqlConnInfo);
        this.mySqlConnInfo = mySqlConnInfo;
        this.mySqlToMySqlDataTypeConvert = mySqlToMySqlDataTypeConvert;
    }


    @Override
    public List<ColumnEntity> getColumnEntityList(String tableName) {
        List<ColumnEntity> columnEntityList = new ArrayList<>();
        ResultSet colRet = null;//表元数据信息

        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            colRet = metaData.getColumns(dataBaseConnInfo.getDbName(), dataBaseConnInfo.getSchema(), tableName, "%");
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
        }finally {
            CloseObjUtil.closeAll(statement);
        }
    }

    @Override
    public void closeExecutor() {

    }

    @Override
    public void close() throws Exception {

    }
}
