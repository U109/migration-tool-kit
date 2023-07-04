package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:27
 * @description: 数据量表实体
 */
public class TableEntity {

    private String tableId;
    private String tableName;
    private long dataCount;
    private List<ColumnEntity> columnList;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getDataCount() {
        return dataCount;
    }

    public void setDataCount(long dataCount) {
        this.dataCount = dataCount;
    }

    public List<ColumnEntity> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnEntity> columnList) {
        this.columnList = columnList;
    }
}
