package com.zzz.migrationtoolkit.core.generator.impl;

import com.zzz.migrationtoolkit.common.constants.ColumnConstant;
import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;

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
        if (dataTypeName != null && !"".equals(dataTypeName)){
            if (dataTypeName.equals(ColumnConstant.TYPE_VARCHAR)){
                if (length > 0){
                    sb.append(ColumnConstant.TYPE_VARCHAR).append("(").append(length).append(")");
                }
            }else {
                sb.append(dataTypeName);
            }
        }
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
