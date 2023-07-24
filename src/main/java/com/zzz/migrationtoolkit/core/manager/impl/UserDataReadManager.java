package com.zzz.migrationtoolkit.core.manager.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.MetaDataReadWorker;
import com.zzz.migrationtoolkit.core.worker.impl.UserDataReadWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkType;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/24 11:26
 * @description:
 */
public class UserDataReadManager  extends AbstractBaseProcessManager {

    public UserDataReadManager() {
    }

    public UserDataReadManager(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
        this.workerNum = taskDetail.getCoreConfig().getReadDataThreadSize();
        this.workType = WorkType.READ_TABLE_USERDATA;
    }

    @Override
    public ProcessWorkResultEntity call() {
        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            UserDataReadWorker userDataReadWorker = new UserDataReadWorker(taskDetail, getSourceWorkQueue(), getTargetWorkQueue());
            FutureTask<ProcessWorkResultEntity> futureTask = new FutureTask<ProcessWorkResultEntity>(userDataReadWorker);
            workerList.add(userDataReadWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(userDataReadWorker.getWorkerName(i));
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
                resultMsg = "".equals(resultMsg) ? "ERROR_READ_OBJ" + " " + result.getResultMsg() : "";
            }
        }

        processWorkResultEntity.setNormalFinished(returnWorkNum == workerList.size());
        processWorkResultEntity.setResultMsg(resultMsg);
        return processWorkResultEntity;
    }

}

