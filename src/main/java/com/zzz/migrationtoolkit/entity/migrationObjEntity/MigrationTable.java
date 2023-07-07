package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.TableEntity;
import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:26
 * @description:
 */
@Data
public class MigrationTable extends MigrationObj {

    private TableEntity sourceTable;
    private TableEntity destTable;

}
