package com.zzz.migrationtoolkit.core.worker;

import com.zzz.migrationtoolkit.common.constants.CommonConstant;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:44
 * @description:
 */
@Data
public abstract class AbstractBaseWorker implements IWorker {

    protected WorkQueue sourceWorkQueue;
    protected WorkQueue targetWorkQueue;
    protected TaskDetail taskDetail;
    protected String workerType;
    protected boolean stopWork = false;
    //源数据库类型
    protected String sourceDBType;
    protected String targetDBType;

    public AbstractBaseWorker() {
    }

    public AbstractBaseWorker(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue
            , String workerType) {
        this.sourceWorkQueue = sourceWorkQueue;
        this.targetWorkQueue = targetWorkQueue;
        this.taskDetail = taskDetail;
        this.workerType = workerType;
        this.sourceDBType = taskDetail.getSourceDataBase().getDatabaseType();
        this.targetDBType = taskDetail.getTargetDataBase().getDatabaseType();
    }

    @Override
    public String stopWorker() {
        this.stopWork = true;
        return CommonConstant.STOP;
    }

    public String getWorkerName(int i) {
        return taskDetail.getTaskId() + "::" + taskDetail.getTaskName() + "::Thread-[" + i + "]";
    }

}
