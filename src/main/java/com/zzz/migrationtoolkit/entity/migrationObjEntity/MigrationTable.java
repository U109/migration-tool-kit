package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.TableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

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
    private Map<String,MigrationColumn> columnDetailMap;
    private List<MigrationColumn>  migrationColumnList;

    public void setColumnDetailForMigrationTable(IDataBaseExecutor dataBaseExecutor) {
        List<ColumnEntity> columnEntityList = dataBaseExecutor.getColumnEntityList(getSourceTable().getTableName());
        Map<String,MigrationColumn> migrationColumnMap = new HashMap<>();
        List<MigrationColumn> migrationColumnList = new ArrayList<>();

        for (ColumnEntity columnEntity : columnEntityList) {
            MigrationColumn migrationColumn = new MigrationColumn(columnEntity);
            migrationColumnMap.put(columnEntity.getColumnName(),migrationColumn);
            migrationColumnList.add(migrationColumn);
        }
        this.setColumnDetailMap(migrationColumnMap);
        this.setMigrationColumnList(migrationColumnList);
    }
}
