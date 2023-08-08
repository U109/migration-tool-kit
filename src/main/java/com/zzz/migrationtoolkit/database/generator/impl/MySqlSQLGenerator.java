package com.zzz.migrationtoolkit.database.generator.impl;

import com.zzz.migrationtoolkit.common.constants.SystemConstant;
import com.zzz.migrationtoolkit.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

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
        List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
        String tableName = migrationTable.getDestTable().getTableName();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE");
        sb.append(" ").append(tableName).append(System.getProperty("line.separator"));
        sb.append("(").append(System.getProperty("line.separator"));
        StringJoiner sj = new StringJoiner(",");
        for (MigrationColumn column : columnList) {
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
    public String getSourceDataSelectSql(String tableName, String dbName, List<MigrationColumn> columnList) {
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
                + "(" + makeTargetColumnString(IDENTIFIER, migrationTable.getMigrationColumnList()) + ") values(" +
                columnString + ")";
    }


    public String makeColumnString(MigrationTable migrationTable) {
        StringBuilder sb = new StringBuilder();
        List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append("?");
            if (i < columnList.size() - 1) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }


}
