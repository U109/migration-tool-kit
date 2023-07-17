package com.zzz.migrationtoolkit.core.generator.impl;

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

}
