package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:30
 * @description:
 */
public class ColumnEntity {

    private String columnId;
    private String columnName;
    private DataType columnType;
    private boolean isAutoIncrement = false;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public DataType getColumnType() {
        return columnType;
    }

    public void setColumnType(DataType columnType) {
        this.columnType = columnType;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }
}
