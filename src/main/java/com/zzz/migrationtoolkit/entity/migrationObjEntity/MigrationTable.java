package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.TableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:26
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MigrationTable extends MigrationObj {

    private TableEntity sourceTable;
    private TableEntity destTable;

    public void setColumnDetailForMigrationTable(IDataBaseExecutor dataBaseExecutor) {

    }
}
