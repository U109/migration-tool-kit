package com.zzz.migrationtoolkit.core.manager.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.MetaDataReadWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
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
        //TODO 1
        this.workerNum = taskDetail.getCoreConfig().getReadDataThreadSize();
        this.workType = "READ_TABLE_METADATA";
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {
        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            MetaDataReadWorker metaDataReadWorker = new MetaDataReadWorker(taskDetail, getSourceWorkQueue(), getTargetWorkQueue());
            FutureTask<ProcessWorkResultEntity> futureTask = new FutureTask<ProcessWorkResultEntity>(metaDataReadWorker);
            workerList.add(metaDataReadWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(metaDataReadWorker.getWorkName(i));
            thread.start();
        }
        String resultMsg = "";
        for (FutureTask<ProcessWorkResultEntity> futureTask : futureTaskList) {
            ProcessWorkResultEntity result = futureTask.get();
            results.add(result);
            if (result.isNormalFinished()) {
                returnWorkNum++;
            } else {
                resultMsg = "".equals(resultMsg) ? "ERROR_READ_OBJ" + " " + result.getResultMsg() : "";
            }
        }
        processWorkResultEntity.setNormalFinished(returnWorkNum == workerList.size());
        processWorkResultEntity.setResultMsg(resultMsg);
        return new ProcessWorkResultEntity();
    }

}
