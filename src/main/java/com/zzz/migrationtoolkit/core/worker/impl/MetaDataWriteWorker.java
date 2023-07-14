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
            String executeSQL = "";
            MigrationTable migrationTable = null;
            try {
                if (stopWork) {
                    break;
                }
                ProcessWorkEntity processWork = null;

                processWork = this.sourceWorkQueue.takeWork();

                migrationTable = (MigrationTable) processWork.getMigrationObj();

                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getDestInstance(taskDetail);
                }

                ISQLGenerator sqlGenerator = SQLGeneratorFactory.newDestInstance(taskDetail);

                executeSQL = sqlGenerator.getTableCreateSQL(destDbci, migrationTable, taskDetail);
                log.info(executeSQL);
                dataBaseExecutor.executeSQL(executeSQL);


                if (targetWorkQueue != null) {
                    processWork.setWorkType(WorkType.WRITE_TABLE_USERDATA);
                    targetWorkQueue.putWork(processWork);
                } else {
                    migrationTable.setFinish(true);
                }

            } catch (Exception e) {
               log.error("存在异常 ： " + e.getMessage());
            }
        }

        dataBaseExecutor.closeExecutor();
        return processWorkResultEntity;
    }
}
