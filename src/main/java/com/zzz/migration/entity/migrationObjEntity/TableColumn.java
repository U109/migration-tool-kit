package com.zzz.migration.entity.migrationObjEntity;

import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/17 9:40
 * @description:
 */
@Data
public class TableColumn implements Serializable {

    private String columnId;
    private ColumnEntity sourceColumn;
    private ColumnEntity destColumn;
    private boolean  migrationFlag = true;

    public TableColumn() {
    }

    public TableColumn(ColumnEntity columnEntity) {
        this.sourceColumn = (ColumnEntity) columnEntity.clone();
        this.destColumn = new ColumnEntity(columnEntity.getColumnName(),columnEntity.getColumnType());
    }
}
