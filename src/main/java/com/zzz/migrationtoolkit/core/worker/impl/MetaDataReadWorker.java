package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.concurrent.TimeUnit;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:50
 * @description:
 */
public class MetaDataReadWorker extends AbstractProcessWorker {


    public MetaDataReadWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "MetaDataReadWorker");
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {

        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;

        while (true) {
            //任务块
            ProcessWorkEntity processWork = null;
            //迁移表对象
            MigrationTable migrationTable = null;

            if (stopWork) {
                break;
            }

            processWork = this.sourceWorkQueue.takeWork();

            if (dataBaseExecutor == null) {
                dataBaseExecutor = DataBaseExecutorFactory.getSourceInstance(taskDetail);
            }

            migrationTable = (MigrationTable) processWork.getMigrationObj();

            migrationTable.setResultMsg("");

//            processWork.setWorkType("READ_TABLE_USERDATA");
//            targetWorkQueue.putWork(processWork);


            //补充列信息
            migrationTable.setColumnDetailForMigrationTable(dataBaseExecutor);
            for (int i = 0; i < 6; i++) {
                System.out.println("read worker...");
                TimeUnit.SECONDS.sleep(1);
                if (i == 5) {
                    stopWork = true;
                }
            }

//            processWork = this.sourceWorkQueue.takeWork();
//
//            System.out.println(processWork.getMigrationObj().getObjId());

        }
        return new ProcessWorkResultEntity();
    }
}
