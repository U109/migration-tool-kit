package com.zzz.migration.database.generator.impl;

import com.zzz.migration.common.constants.SystemConstant;
import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migration.entity.migrationObjEntity.TableColumn;
import com.zzz.migration.entity.migrationObjEntity.MigrationTable;
import com.zzz.migration.entity.taskEntity.TaskDetail;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:01
 * @description:
 */
public class MySqlSQLGenerator extends AbstractSQLGenerator {

    private static final String IDENTIFIER = "`";

    @Override
    public String getTableCreateSQL(MigrationTable migrationTable, TaskDetail taskDetail) {
        List<TableColumn> columnList = migrationTable.getTableColumnList();
        String tableName = migrationTable.getDestTable().getTableName();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE");
        sb.append(" ").append(tableName).append(System.getProperty("line.separator"));
        sb.append("(").append(System.getProperty("line.separator"));
        StringJoiner sj = new StringJoiner(",");
        for (TableColumn column : columnList) {
            ColumnEntity destColumn = column.getDestColumn();
            sj.add(getFieldDefinition(destColumn));
        }
        sb.append(sj).append(")").append(System.getProperty("line.separator"));
        return sb.toString();
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
    public String getSourceLimitSelectSql(String readDataSql, long start, long batchSize) {
        return readDataSql + " limit " + Long.parseLong(String.valueOf(start)) +
                " ," + Long.parseLong(String.valueOf(batchSize));
    }

    @Override
    public String getTargetDataInsertSql(MigrationTable migrationTable, String dbName) {
        String columnString = makeColumnString(migrationTable);
        return "insert into " + makeIdentifierNoDb(IDENTIFIER, migrationTable.getDestTable().getTableName())
                + "(" + makeTargetColumnString(IDENTIFIER, migrationTable.getTableColumnList()) + ") values(" +
                columnString + ")";
    }


    public String makeColumnString(MigrationTable migrationTable) {
        StringBuilder sb = new StringBuilder();
        List<TableColumn> columnList = migrationTable.getTableColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append("?");
            if (i < columnList.size() - 1) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }


}
