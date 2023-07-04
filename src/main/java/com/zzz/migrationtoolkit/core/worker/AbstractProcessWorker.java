package com.zzz.migrationtoolkit.core.worker;

import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:44
 * @description:
 */
public abstract class AbstractProcessWorker implements IProcessWorker {

    protected ProcessWorkQueue sourceWorkQueue;
    protected ProcessWorkQueue targetWorkQueue;
    protected TaskDetail taskDetail;

    protected boolean stopWork = false;

    public AbstractProcessWorker() {
    }

    public AbstractProcessWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        this.sourceWorkQueue = sourceWorkQueue;
        this.targetWorkQueue = targetWorkQueue;
        this.taskDetail = taskDetail;
    }

    @Override
    public String stopWorker() {
        this.stopWork = true;
        return "stop";
    }

    public String getWorkName(int i) {
        return taskDetail.getTaskId() + "::" + taskDetail.getTaskName() + "::Thread-[" + i + "]";
    }

}
