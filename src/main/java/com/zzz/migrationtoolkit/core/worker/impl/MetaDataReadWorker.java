package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;

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

            if (!processWork.getWorkType().equals(WorkType.READ_TABLE_METADATA)){
                continue;
            }
            if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)){
                break;
            }

            if (dataBaseExecutor == null) {
                dataBaseExecutor = DataBaseExecutorFactory.getSourceInstance(taskDetail);
            }


            migrationTable = (MigrationTable) processWork.getMigrationObj();


            migrationTable.setResultMsg("");

//            processWork.setWorkType("READ_TABLE_USERDATA");
//            targetWorkQueue.putWork(processWork);

            System.out.println("执行worker");

            //补充列信息
            migrationTable.setColumnDetailForMigrationTable(dataBaseExecutor);

            processWork.setWorkType(WorkType.WRITE_TABLE_METADATA);
            targetWorkQueue.putWork(processWork);
            processWorkResultEntity.setResultMsg("SUCCESS");
        }
        return processWorkResultEntity;
    }
}
