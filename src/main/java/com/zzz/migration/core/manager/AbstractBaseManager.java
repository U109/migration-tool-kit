package com.zzz.migration.core.manager;

import com.zzz.migration.common.constants.CommonConstant;
import com.zzz.migration.core.worker.AbstractBaseWorker;
import com.zzz.migration.entity.taskEntity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:11
 * @description:
 */
@Data
public abstract class AbstractBaseManager implements IManager {

    protected WorkType workType;

    protected TaskDetail taskDetail;
    protected int workerNum = 0;
    //返回worker数量
    protected int returnWorkNum = 0;
    protected List<WorkResultEntity> results = new ArrayList<>();
    //worker工作的数据源队列
    protected WorkQueue sourceWorkQueue;
    //worker工作的数据目标队列，如果是末端队列，则为null
    protected WorkQueue targetWorkQueue = null;

    //manager启动的所有worker
    protected List<AbstractBaseWorker> workerList = new ArrayList<>();
    protected List<FutureTask<WorkResultEntity>> futureTaskList = new ArrayList<>();
    public boolean stopWork = false;

    @Override
    public String startWorker() {
        return null;
    }

    @Override
    public String stopWorker() {
        String resultMsg = CommonConstant.OK;
        for (AbstractBaseWorker worker : workerList) {
            worker.stopWorker();
        }
        if (sourceWorkQueue != null) {
            sourceWorkQueue.clear();
        }
        if (targetWorkQueue != null) {
            targetWorkQueue.clear();
        }
        return resultMsg;
    }

    public AbstractBaseManager() {
    }

    public AbstractBaseManager(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        this.taskDetail = taskDetail;
        this.setSourceWorkQueue(sourceWorkQueue);
        this.setTargetWorkQueue(targetWorkQueue);
    }

    public void setSourceWorkQueue() {
        this.sourceWorkQueue = new WorkQueue(taskDetail.getCoreConfig().getWorkQueueSize());
    }

    @Override
    public String finishedQueue() {
        for (int i = 0; i < workerNum; i++) {
            WorkEntity processWork = new WorkEntity();

            processWork.setWorkType(this.workType);
            processWork.setWorkContentType(WorkContentType.WORK_FINISHED);

            this.getSourceWorkQueue().putWork(processWork);
        }
        return CommonConstant.SUCCESS;
    }
}
