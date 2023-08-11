package com.zzz.migration.database.generator.impl;

import com.zzz.migration.entity.migrationObjEntity.TableColumn;
import com.zzz.migration.entity.migrationObjEntity.MigrationTable;
import com.zzz.migration.entity.taskEntity.TaskDetail;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:03
 * @description:
 */
public class OracleSQLGenerator extends AbstractSQLGenerator {

    private static final String IDENTIFIER = "\"";

    @Override
    public String getTableCreateSQL(MigrationTable migrationTable, TaskDetail taskDetail) {
        return null;
    }

    @Override
    public String dropTargetTable(String tableName, String dbName) {
        return "drop table if exists " + dbName + "." + tableName;
    }

    @Override
    public String getSourceDataSelectSql(String tableName, String dbName, List<TableColumn> columnList) {
        return "select " + makeColumnContract(IDENTIFIER, columnList) + " from " + makeIdentifier(IDENTIFIER, dbName, tableName);
    }

    @Override
    public String getSourceDataCountSql(String tableName, String dbName) {
        return "select count(*) from " + makeIdentifier(IDENTIFIER, dbName, tableName);
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
