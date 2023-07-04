package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.TableEntity;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:26
 * @description:
 */
public class MigrationTable extends MigrationObj {

    private TableEntity sourceTable;
    private TableEntity destTable;

    public TableEntity getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(TableEntity sourceTable) {
        this.sourceTable = sourceTable;
    }

    public TableEntity getDestTable() {
        return destTable;
    }

    public void setDestTable(TableEntity destTable) {
        this.destTable = destTable;
    }
}
