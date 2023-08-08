package com.zzz.migrationtoolkit.database.generator;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:57
 * @description:
 */
public interface ISQLGenerator {

    String getTableCreateSQL(MigrationTable migrationTable, TaskDetail taskDetail);

    String dropTargetTable(String tableName, String dbName);

    String getSourceDataSelectSql(String tableName, String dbName, List<MigrationColumn> columnList);

    String getSourceDataCountSql(String tableName, String dbName);

    String getSourceLimitSelectSql(String readDataSql, long start, long batchSize);

    String getTargetDataInsertSql(MigrationTable migrationTable, String dbName);
}
