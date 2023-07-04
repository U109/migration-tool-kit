package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.concurrent.TimeUnit;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:50
 * @description:
 */
public class MetaDataReadWorker extends AbstractProcessWorker {


    public MetaDataReadWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
    }

    @Override
    public String call() throws Exception {

        while (true) {
            //任务块
            ProcessWorkEntity processWork = null;
            MigrationTable migrationTable = null;


            if (stopWork) {
                break;
            }



            for (int i = 0; i < 10; i++) {
                System.out.println("read worker...");
                TimeUnit.SECONDS.sleep(1);
                if (i == 9){
                    stopWork = true;
                }
            }


//            processWork = this.sourceWorkQueue.takeWork();
//
//            System.out.println(processWork.getMigrationObj().getObjId());

        }
        return "worker";
    }
}
