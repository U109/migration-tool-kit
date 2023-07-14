package com.zzz.migrationtoolkit.core.generator.impl;

import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:01
 * @description:
 */
public class MySqlSQLGenerator extends AbstractSQLGenerator {

    @Override
    public String getTableCreateSQL(DataBaseConnInfo destDbci, MigrationTable migrationTable, TaskDetail taskDetail) {
        return null;
    }
}
