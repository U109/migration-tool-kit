package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/17 9:40
 * @description:
 */
@Data
public class MigrationColumn implements Serializable {

    private String columnId;
    private ColumnEntity sourceColumn;
    private ColumnEntity destColumn;
    private boolean  migrationFlag = true;

    public MigrationColumn() {
    }

    public MigrationColumn(ColumnEntity columnEntity) {
        this.sourceColumn = (ColumnEntity) columnEntity.clone();
        this.destColumn = new ColumnEntity(columnEntity.getColumnName(),columnEntity.getColumnType());
    }
}
