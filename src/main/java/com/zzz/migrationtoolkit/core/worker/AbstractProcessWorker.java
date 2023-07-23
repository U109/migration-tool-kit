package com.zzz.migrationtoolkit.core.worker;

import com.zzz.migrationtoolkit.common.constants.CommonConstant;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:44
 * @description:
 */
@Data
public abstract class AbstractProcessWorker implements IProcessWorker {

    protected ProcessWorkQueue sourceWorkQueue;
    protected ProcessWorkQueue targetWorkQueue;
    protected TaskDetail taskDetail;
    protected String workerType;
    protected boolean stopWork = false;
    //源数据库类型
    protected String sourceDBType;
    protected String targetDBType;

    public AbstractProcessWorker() {
    }

    public AbstractProcessWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue
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

    public String getWorkName(int i) {
        return taskDetail.getTaskId() + "::" + taskDetail.getTaskName() + "::Thread-[" + i + "]";
    }

}
