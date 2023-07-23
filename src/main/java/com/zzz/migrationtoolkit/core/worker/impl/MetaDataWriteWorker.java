package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.core.generator.SQLGeneratorFactory;
import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:50
 * @description:
 */
@Slf4j
public class MetaDataWriteWorker extends AbstractProcessWorker {

    private DataBaseConnInfo destDbci;

    public MetaDataWriteWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "MetaDataWriteWorker");
        this.destDbci = taskDetail.getTargetDataBase().getDbci();
    }

    @Override
    public ProcessWorkResultEntity call() {

        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();

        IDataBaseExecutor dataBaseExecutor = null;

        while (true) {

            String executeSql = "";
            MigrationTable migrationTable = null;
            try {
                if (stopWork) {
                    break;
                }
                ProcessWorkEntity processWork = this.sourceWorkQueue.takeWork();
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
                    dataBaseExecutor = DataBaseExecutorFactory.getDestInstance(taskDetail);
                }

                ISQLGenerator sqlGenerator = SQLGeneratorFactory.newDestInstance(taskDetail);
                //TODO 重建表操作
                executeSql = sqlGenerator.getTableCreateSQL(destDbci, migrationTable, taskDetail);
                log.info(executeSql);
                dataBaseExecutor.executeSQL(executeSql);

                if (targetWorkQueue != null) {
                    processWork.setWorkType(WorkType.READ_TABLE_USERDATA);
                    targetWorkQueue.putWork(processWork);
                } else {
                    migrationTable.setFinish(true);
                }
            } catch (Exception e) {
                log.error("存在异常 ： " + e.getMessage());
            }
        }
        if (dataBaseExecutor != null) {
            dataBaseExecutor.closeExecutor();
        }
        return processWorkResultEntity;
    }
}
