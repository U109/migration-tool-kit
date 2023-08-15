package com.zzz.migration.database.convert;

import com.zzz.migration.common.constants.ColumnConstant;
import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migration.entity.migrationObjEntity.TableColumn;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhongzhen
 */
public class MySqlToMySqlDataTypeConvert implements IDataConvert {
    @Override
    public List<Object> convertReadDataTypes(List<TableColumn> columnList, ResultSet rs) {
        int colCount = columnList.size();

        List<Object> data = new ArrayList<>();

        try {
            for (int i = 1; i <= colCount; i++) {
                ColumnEntity columnInfo = columnList.get(i - 1).getSourceColumn();

                String type = columnInfo.getColumnType().getDataTypeName().toUpperCase();

                if (type.equals(ColumnConstant.TYPE_BIT)) {
                    if (rs.getString(i) != null) {
                        if ("0".equals(rs.getString(i))) {
                            data.add(0);
                        } else if ("1".equals(rs.getString(i))) {
                            data.add(1);
                        } else {
                            data.add(new String(rs.getBytes(i)));
                        }
                    } else {
                        data.add(null);
                    }
                } else if (type.equals(ColumnConstant.TYPE_BINARY) || type.equals(ColumnConstant.TYPE_VARBINARY)) {
                    if (rs.getBlob(i) != null) {
                        Blob blob = rs.getBlob(i);
                        if ((int) blob.length() > 0) {
                            data.add(blob.getBytes(1, (int) blob.length()));
                        } else {
                            data.add("".getBytes());
                        }
                    } else {
                        data.add(null);
                    }
                } else {
                    data.add(rs.getObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void convertWriteDataTypes(String type, PreparedStatement preparedStatement, int parameterIndex, List<Object> dataRow, int j) throws SQLException {
        type = type.toUpperCase();
        if (type.equals(ColumnConstant.TYPE_CHAR)) {
            if (dataRow.get(j) instanceof byte[]) {
                preparedStatement.setString(parameterIndex, new String((byte[]) dataRow.get(j)));
            } else {
                preparedStatement.setString(parameterIndex, dataRow.get(j).toString());
            }
        } else if (type.equals(ColumnConstant.TYPE_VARCHAR)) {
            if (dataRow.get(j) instanceof byte[]) {
                preparedStatement.setString(parameterIndex, new String((byte[]) dataRow.get(j)));
            } else {
                preparedStatement.setString(parameterIndex, dataRow.get(j).toString());
            }
        } else {
            preparedStatement.setObject(parameterIndex, dataRow.get(j));
        }
    }
}
