package com.zzz.migrationtoolkit.core.manager;

import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
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
public abstract class AbstractBaseProcessManager implements IProcessManager {

    protected WorkType workType;

    protected TaskDetail taskDetail;
    protected int workerNum = 0;
    //返回worker数量
    protected int returnWorkNum = 0;
    protected List<ProcessWorkResultEntity> results = new ArrayList<>();
    //worker工作的数据源队列
    protected ProcessWorkQueue sourceWorkQueue;
    //worker工作的数据目标队列，如果是末端队列，则为null
    protected ProcessWorkQueue targetWorkQueue = null;

    //manager启动的所有worker
    protected List<AbstractProcessWorker> workerList = new ArrayList<>();
    protected List<FutureTask<ProcessWorkResultEntity>> futureTaskList = new ArrayList<>();
    public boolean stopWork = false;

    @Override
    public String startWorker() {
        return null;
    }

    @Override
    public String stopWorker() {
        String resultMsg = "RETURN_OK";
        for (AbstractProcessWorker worker : workerList) {
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

    public AbstractBaseProcessManager() {
    }

    public AbstractBaseProcessManager(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        this.taskDetail = taskDetail;
        this.setSourceWorkQueue(sourceWorkQueue);
        this.setTargetWorkQueue(targetWorkQueue);
    }

    public void setSourceWorkQueue() {
        this.sourceWorkQueue = new ProcessWorkQueue(taskDetail.getCoreConfig().getWorkQueueSize());
    }

    @Override
    public String finishedQueue() {
        for (int i = 0; i < workerNum; i++) {
            ProcessWorkEntity processWork = new ProcessWorkEntity();

            processWork.setWorkType(this.workType);
            processWork.setWorkContentType(WorkContentType.WORK_FINISHED);

            this.getSourceWorkQueue().putWork(processWork);
        }
        return "SUCCESS";
    }
}
