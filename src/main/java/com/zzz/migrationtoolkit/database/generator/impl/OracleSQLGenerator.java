package com.zzz.migrationtoolkit.database.generator.impl;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:03
 * @description:
 */
public class OracleSQLGenerator extends AbstractSQLGenerator {

    private static final String identifier = "\"";

    @Override
    public String getTableCreateSQL(DataBaseConnInfo destDbci, MigrationTable migrationTable, TaskDetail taskDetail) {
        return null;
    }

    @Override
    public String dropTargetTable(String tableName, String dbName) {
        return "drop table if exists " + dbName + "." + tableName;
    }

    @Override
    public String getSourceDataSelectSql(String tableName, String dbName, List<MigrationColumn> columnList) {
        return "select " + makeColumnContract(identifier, columnList) + " from " + makeIdentifier(identifier, dbName, tableName);
    }

    @Override
    public String getSourceDataCountSql(String tableName, String dbName) {
        return "select count(*) from " + makeIdentifier(identifier, dbName, tableName);
    }

    @Override
    public String getSourceLimitSelectSql(String readDataSql,  long start, long batchSize) {
        return null;
    }

    @Override
    public String getTargetDataInsertSql(MigrationTable migrationTable, String dbName) {
        return null;
    }

}
