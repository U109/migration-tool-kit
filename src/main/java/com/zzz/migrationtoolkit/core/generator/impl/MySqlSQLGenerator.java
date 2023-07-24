package com.zzz.migrationtoolkit.core.generator.impl;

import com.zzz.migrationtoolkit.common.constants.SystemConstant;
import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author: Zzz
 * @date: 2023/7/14 17:01
 * @description:
 */
public class MySqlSQLGenerator extends AbstractSQLGenerator {

    private static final String identifier = "`";

    @Override
    public String getTableCreateSQL(DataBaseConnInfo destDbci, MigrationTable migrationTable, TaskDetail taskDetail) {
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
        return "select " + makeColumnContract(identifier, columnList) + " from " + makeIdentifier(identifier, dbName, tableName);
    }

    @Override
    public String getSourceDataCountSql(String tableName, String dbName) {
        return "select count(*) from " + makeIdentifier(identifier, dbName, tableName);
    }

    @Override
    public String getSourceLimitSelectSql(String readDataSql, long start, long batchSize) {
        StringBuffer sb = new StringBuffer();
        sb.append(readDataSql).append(" limit ").append(Long.parseLong(String.valueOf(start)))
                .append(" ,").append(Long.parseLong(String.valueOf(batchSize)));
        return sb.toString();
    }

    @Override
    public String getTargetDataInsertSql(MigrationTable migrationTable, String dbName) {
        String columnString = makeColumnString(migrationTable);
        return "insert into " + makeIdentifierNoDb(identifier, migrationTable.getDestTable().getTableName())
                + "(" + makeTargetColumnString(identifier, migrationTable.getMigrationColumnList()) + ") values(" +
                columnString + ")";
    }


    public String makeColumnString(MigrationTable migrationTable) {
        StringBuffer sb = new StringBuffer();
        List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
        for (int i = 0; i < columnList.size(); i++) {
            sb.append("?");
            if (i < columnList.size()) {
                sb.append(SystemConstant.COMMA);
            }
        }
        return sb.toString();
    }


}
