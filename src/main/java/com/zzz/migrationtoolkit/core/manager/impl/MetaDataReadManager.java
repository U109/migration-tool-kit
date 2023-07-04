package com.zzz.migrationtoolkit.core.manager.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.MetaDataReadWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:33
 * @description:
 */
public class MetaDataReadManager extends AbstractBaseProcessManager {

    public MetaDataReadManager() {
    }

    public MetaDataReadManager(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
        this.workerNum = 1;
    }

    @Override
    public String call() throws Exception {
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            MetaDataReadWorker metaDataReadWorker = new MetaDataReadWorker(taskDetail, this.getSourceWorkQueue(), this.getTargetWorkQueue());
            FutureTask<String> futureTask = new FutureTask<>(metaDataReadWorker);
            workerList.add(metaDataReadWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(metaDataReadWorker.getWorkName(i));
            thread.start();
        }
        StringBuilder result = new StringBuilder();
        for (FutureTask<String> futureTask : futureTaskList) {
            result.append(futureTask.get());
        }
        return result.toString();
    }

}
