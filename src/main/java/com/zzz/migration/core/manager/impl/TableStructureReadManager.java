package com.zzz.migration.core.manager.impl;

import com.zzz.migration.core.manager.AbstractBaseManager;
import com.zzz.migration.core.worker.impl.TableStructureReadWorker;
import com.zzz.migration.entity.taskEntity.WorkQueue;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import com.zzz.migration.entity.taskEntity.WorkType;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:33
 * @description:
 */
public class TableStructureReadManager extends AbstractBaseManager {

    public TableStructureReadManager(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue);
        this.workerNum = taskDetail.getCoreConfig().getReadDataThreadSize();
        this.workType = WorkType.READ_TABLE_METADATA;
    }

    @Override
    public WorkResultEntity call() {
        WorkResultEntity workResultEntity = new WorkResultEntity();
        for (int i = 0; i < workerNum; i++) {
            if (stopWork) {
                break;
            }
            //定义worker
            TableStructureReadWorker tableStructureReadWorker = new TableStructureReadWorker(taskDetail, getSourceWorkQueue(), getTargetWorkQueue());
            FutureTask<WorkResultEntity> futureTask = new FutureTask<WorkResultEntity>(tableStructureReadWorker);
            workerList.add(tableStructureReadWorker);
            futureTaskList.add(futureTask);

            Thread thread = new Thread(futureTask);
            thread.setName(tableStructureReadWorker.getWorkerName(i));
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
                resultMsg = "".equals(resultMsg) ? "ERROR_READ_OBJ" + " " + result.getResultMsg() : "";
            }
        }

        workResultEntity.setNormalFinished(returnWorkNum == workerList.size());
        workResultEntity.setResultMsg(resultMsg);
        return workResultEntity;
    }

}
