package com.zzz.migration.entity.migrationObjEntity;

import com.zzz.migration.common.constants.MigrationConstant;
import com.zzz.migration.database.executor.IDataBaseExecutor;
import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migration.entity.databaseElementEntity.TableEntity;
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
    private Map<String, TableColumn> columnDetailMap;
    private List<TableColumn> tableColumnList;
    private long readDataFailCount;
    private long writeDataFailCount;

    public void setColumnDetailForMigrationTable(IDataBaseExecutor dataBaseExecutor) {
        List<ColumnEntity> columnEntityList = dataBaseExecutor.getColumnEntityList(getSourceTable().getTableName());
        Map<String, TableColumn> migrationColumnMap = new HashMap<>();
        List<TableColumn> tableColumnList = new ArrayList<>();

        for (ColumnEntity columnEntity : columnEntityList) {
            TableColumn tableColumn = new TableColumn(columnEntity);
            migrationColumnMap.put(columnEntity.getColumnName(), tableColumn);
            tableColumnList.add(tableColumn);
        }
        this.setColumnDetailMap(migrationColumnMap);
        this.setTableColumnList(tableColumnList);
    }

    public void updateTotalDataCount(long totalDataCount) {
        this.getSourceTable().setDataCount(totalDataCount);
    }

    public synchronized void updateSuccessDataCount(int dataCount, boolean b, boolean isLastWorker) {
        this.getDestTable().setDataCount(this.getDestTable().getDataCount() + dataCount);
        //成功条数与源数据库条数一致，迁移完成
        if (this.getSourceTable().getDataCount() == this.getDestTable().getDataCount() + this.getFailCount()) {
            if (this.getWriteDataFailCount() != 0) {
                this.appendResultMsg("[" + this.writeDataFailCount + "]条数据插入失败");
                this.setMigrationResult(MigrationConstant.MIGRATION_RESULT_FAIL);
            }
            if (this.getFailCount() == 0) {
                this.setMigrationResult(MigrationConstant.MIGRATION_RESULT_SUCCESS);
                this.setFinish(isLastWorker);
                this.setEndTime(new Date());
            }
        }
    }

    public void updateFailedDataCount(long errDataCount, boolean isLastWorker) {
        setWriteDataFailCount(getWriteDataFailCount() + errDataCount);
        if (getSourceTable().getDataCount() == getDestTable().getDataCount() + getFailCount()) {
            if (getWriteDataFailCount() != 0) {
                setMigrationResult(MigrationConstant.MIGRATION_RESULT_FAIL);
                appendResultMsg("[" + writeDataFailCount + "]条数据插入失败");
            }
        }
    }

    public long getFailCount() {
        return this.readDataFailCount + this.writeDataFailCount;
    }

}
