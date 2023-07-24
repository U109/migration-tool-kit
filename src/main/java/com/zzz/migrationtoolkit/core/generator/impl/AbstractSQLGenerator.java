package com.zzz.migrationtoolkit.core.generator.impl;

import com.zzz.migrationtoolkit.common.constants.ColumnConstant;
import com.zzz.migrationtoolkit.common.constants.SystemConstant;
import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
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
        String dataTypeName = columnType.getDataTypeValue();
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
     *
     * @param identifier
     * @param columnList
     * @return
     */
    public String makeColumnContract(String identifier, List<MigrationColumn> columnList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append(makeIdentifier(identifier, columnList.get(i - 1).getSourceColumn().getColumnName()));
            if (i < columnList.size()) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }

    /**
     * 获取包围符的字符串
     *
     * @param identifier
     * @param dbName
     * @param tableName
     * @return
     */
    public String makeIdentifier(String identifier, String dbName, String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append(makeIdentifier(identifier, dbName)).append(makeIdentifier(identifier, SystemConstant.DOT))
                .append(makeIdentifier(identifier, tableName));
        return sb.toString();
    }

    public String makeIdentifier(String identifier, String name) {
        StringBuffer sb = new StringBuffer();
        sb.append(identifier).append(name).append(identifier);
        return sb.toString();
    }

    public String makeIdentifierNoDb(String identifier, String... args) {
        StringBuffer sb = new StringBuffer();
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
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append(makeIdentifier(identifier, columnList.get(i).getDestColumn().getColumnName()));
            if (i < columnList.size()) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }

}
