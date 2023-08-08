package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import com.zzz.migrationtoolkit.database.executor.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.databaseElementEntity.TableEntity;
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
    private Map<String, MigrationColumn> columnDetailMap;
    private List<MigrationColumn> migrationColumnList;
    private long readDataFailCount;
    private long writeDataFailCount;

    public void setColumnDetailForMigrationTable(IDataBaseExecutor dataBaseExecutor) {
        List<ColumnEntity> columnEntityList = dataBaseExecutor.getColumnEntityList(getSourceTable().getTableName());
        Map<String, MigrationColumn> migrationColumnMap = new HashMap<>();
        List<MigrationColumn> migrationColumnList = new ArrayList<>();

        for (ColumnEntity columnEntity : columnEntityList) {
            MigrationColumn migrationColumn = new MigrationColumn(columnEntity);
            migrationColumnMap.put(columnEntity.getColumnName(), migrationColumn);
            migrationColumnList.add(migrationColumn);
        }
        this.setColumnDetailMap(migrationColumnMap);
        this.setMigrationColumnList(migrationColumnList);
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
