package com.zzz.migrationtoolkit.core.manager.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.MetaDataReadWorker;
import com.zzz.migrationtoolkit.core.worker.impl.MetaDataWriteWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkType;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:33
 * @description:
 */
public class MetaDataWriteManager extends AbstractBaseProcessManager {

    public MetaDataWriteManager() {
    }

    public MetaDataWriteManager(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
        this.workerNum = taskDetail.getCoreConfig().getWriteDataThreadSize();
        this.workType = WorkType.WRITE_TABLE_METADATA;
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {
        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            MetaDataWriteWorker metaDataWriteWorker = new MetaDataWriteWorker(taskDetail, getSourceWorkQueue(), getTargetWorkQueue());
            FutureTask<ProcessWorkResultEntity> futureTask = new FutureTask<>(metaDataWriteWorker);
            workerList.add(metaDataWriteWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(metaDataWriteWorker.getWorkName(i));
            thread.start();
        }

        String resultMsg = "";

        for (FutureTask<ProcessWorkResultEntity> futureTask : futureTaskList) {
            ProcessWorkResultEntity result = null;
            try {
                result = futureTask.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            results.add(result);
            if (result.isNormalFinished()) {
                returnWorkNum++;
            } else {
                resultMsg = "".equals(resultMsg) ? "ERROR_WRITE_OBJ" + " " + result.getResultMsg() : "";
            }
        }
        processWorkResultEntity.setNormalFinished(returnWorkNum == workerList.size());
        processWorkResultEntity.setResultMsg(resultMsg);
        return processWorkResultEntity;
    }
}
