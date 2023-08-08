package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.database.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.database.SQLGeneratorFactory;
import com.zzz.migrationtoolkit.database.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.core.worker.AbstractBaseWorker;
import com.zzz.migrationtoolkit.database.executor.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:50
 * @description:
 */
@Slf4j
public class TableStructureWriteWorker extends AbstractBaseWorker {

    private final DataSourceProperties properties;

    public TableStructureWriteWorker(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "MetaDataWriteWorker");
        this.properties = taskDetail.getTargetDataBase().getProperties();
    }

    @Override
    public WorkResultEntity call() {

        WorkResultEntity workResultEntity = new WorkResultEntity();

        IDataBaseExecutor dataBaseExecutor = null;

        while (true) {

            String executeSql;
            MigrationTable migrationTable;
            try {
                if (stopWork) {
                    break;
                }
                WorkEntity processWork = this.sourceWorkQueue.takeWork();
                if (!processWork.getWorkType().equals(WorkType.WRITE_TABLE_METADATA)) {
                    if (processWork.getWorkType().equals(WorkType.READ_TABLE_USERDATA)) {
                        processWork.setWorkType(WorkType.READ_TABLE_USERDATA);
                        targetWorkQueue.putWork(processWork);
                    }
                    continue;
                }
                if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                    break;
                }

                migrationTable = (MigrationTable) processWork.getMigrationObj();

                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getDatabaseInstance(properties.getDbType());
                }

                ISQLGenerator sqlGenerator = SQLGeneratorFactory.getDatabaseInstance(properties.getDbType());
                //重建表操作
                if (taskDetail.isRebuildTable()) {
                    executeSql = sqlGenerator.dropTargetTable(migrationTable.getDestTable().getTableName(), properties.getDbName());
                    log.info("rebuild table : " + executeSql);
                    //串行执行drop，防止系统锁表
                    synchronized (TableStructureWriteWorker.class) {
                        dataBaseExecutor.executeSQL(executeSql);
                    }
                }
                executeSql = sqlGenerator.getTableCreateSQL(migrationTable, taskDetail);
                log.info("create table : " + executeSql);
                dataBaseExecutor.executeSQL(executeSql);

                if (targetWorkQueue != null) {
                    processWork.setWorkType(WorkType.READ_TABLE_USERDATA);
                    targetWorkQueue.putWork(processWork);
                } else {
                    migrationTable.setFinish(true);
                }
            } catch (Exception e) {
                log.error("存在异常 ： " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (dataBaseExecutor != null) {
            dataBaseExecutor.closeExecutor();
        }
        return workResultEntity;
    }
}
