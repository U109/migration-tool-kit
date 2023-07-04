package com.zzz.migrationtoolkit.core.manager;

import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:11
 * @description:
 */
public abstract class AbstractBaseProcessManager implements IProcessManager {

    protected TaskDetail taskDetail;
    protected int workerNum = 0;
    //worker工作的数据源队列
    protected ProcessWorkQueue sourceWorkQueue;
    //worker工作的数据目标队列，如果是末端队列，则为null
    protected ProcessWorkQueue targetWorkQueue = null;

    //manager启动的所有worker
    protected List<AbstractProcessWorker> workerList = new ArrayList<AbstractProcessWorker>();
    protected List<FutureTask<String>> futureTaskList = new ArrayList<>();
    public boolean stopWork = false;

    @Override
    public String startWorker() {
        return null;
    }

    @Override
    public String stopWorker() {
        return null;
    }

    public AbstractBaseProcessManager() {
    }

    public AbstractBaseProcessManager(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        this.taskDetail = taskDetail;
        this.setSourceWorkQueue(sourceWorkQueue);
        this.setTargetWorkQueue(targetWorkQueue);
    }

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public ProcessWorkQueue getSourceWorkQueue() {
        return sourceWorkQueue;
    }

    public void setSourceWorkQueue(ProcessWorkQueue sourceWorkQueue) {
        this.sourceWorkQueue = sourceWorkQueue;
    }

    public ProcessWorkQueue getTargetWorkQueue() {
        return targetWorkQueue;
    }

    public void setTargetWorkQueue(ProcessWorkQueue targetWorkQueue) {
        this.targetWorkQueue = targetWorkQueue;
    }

    public boolean isStopWork() {
        return stopWork;
    }

    public void setStopWork(boolean stopWork) {
        this.stopWork = stopWork;
    }
}
