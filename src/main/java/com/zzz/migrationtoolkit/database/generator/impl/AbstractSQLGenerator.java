package com.zzz.migrationtoolkit.database.generator.impl;

import com.zzz.migrationtoolkit.common.constants.ColumnConstant;
import com.zzz.migrationtoolkit.common.constants.SystemConstant;
import com.zzz.migrationtoolkit.database.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:02
 * @description:
 */
public abstract class AbstractSQLGenerator implements ISQLGenerator {


    public String getFieldDefinition(ColumnEntity column) {
        StringBuilder sb = new StringBuilder();
        String columnName = column.getColumnName();
        DataType columnType = column.getColumnType();
        String dataTypeName = columnType.getDataTypeName();
        long length = columnType.getColumnLength();
        Integer precision = columnType.getColumnPrecision();
        sb.append(columnName).append(" ");
        if (dataTypeName != null && !"".equals(dataTypeName)) {
            if (dataTypeName.equals(ColumnConstant.TYPE_VARCHAR)) {
                if (length > 0) {
                    sb.append(ColumnConstant.TYPE_VARCHAR).append("(").append(length).append(")");
                }
            } else {
                sb.append(dataTypeName);
            }
        }
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    /**
     * 构建列字符串
     */
    public String makeColumnContract(String identifier, List<MigrationColumn> columnList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append(makeIdentifier(identifier, columnList.get(i).getSourceColumn().getColumnName()));
            if (i < columnList.size() - 1) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }

    /**
     * 获取包围符的字符串
     */
    public String makeIdentifier(String identifier, String dbName, String tableName) {
        return makeIdentifier(identifier, dbName) + SystemConstant.DOT +
                makeIdentifier(identifier, tableName);
    }

    public String makeIdentifier(String identifier, String name) {
        return identifier + name + identifier;
    }

    public String makeIdentifierNoDb(String identifier, String... args) {
        StringBuilder sb = new StringBuilder();
        if (args == null || args.length == 0) {
            return "";
        }
        for (String arg : args) {
            sb.append(makeIdentifier(identifier, arg)).append(SystemConstant.DOT);
        }
        String returnStr = sb.toString();
        return (returnStr.length() > 1) ? returnStr.substring(0, returnStr.length() - 1) : returnStr;
    }


    public String makeTargetColumnString(String identifier, List<MigrationColumn> columnList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append(makeIdentifier(identifier, columnList.get(i).getDestColumn().getColumnName()));
            if (i < columnList.size() - 1) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }

}
