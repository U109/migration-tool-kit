package com.zzz.migration.core.manager.impl;

import com.zzz.migration.core.manager.AbstractBaseManager;
import com.zzz.migration.core.worker.impl.TableDataWriteWorker;
import com.zzz.migration.entity.taskEntity.WorkQueue;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import com.zzz.migration.entity.taskEntity.WorkType;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/24 11:26
 * @description:
 */
public class UserDataWriteManager extends AbstractBaseManager {

    public UserDataWriteManager(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
        this.workerNum = taskDetail.getCoreConfig().getWriteDataThreadSize();
        this.workType = WorkType.WRITE_TABLE_USERDATA;
    }

    @Override
    public WorkResultEntity call() throws Exception {
        WorkResultEntity workResultEntity = new WorkResultEntity();
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            TableDataWriteWorker tableDataWriteWorker = new TableDataWriteWorker(taskDetail, getSourceWorkQueue(), getTargetWorkQueue());
            FutureTask<WorkResultEntity> futureTask = new FutureTask<>(tableDataWriteWorker);
            workerList.add(tableDataWriteWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(tableDataWriteWorker.getWorkerName(i));
            thread.start();
        }

        String resultMsg = "";

        for (FutureTask<WorkResultEntity> futureTask : futureTaskList) {
            WorkResultEntity result = null;
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
        workResultEntity.setNormalFinished(returnWorkNum == workerList.size());
        workResultEntity.setResultMsg(resultMsg);
        return workResultEntity;
    }
}
