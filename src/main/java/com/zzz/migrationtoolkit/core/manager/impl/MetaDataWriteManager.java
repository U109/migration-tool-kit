package com.zzz.migrationtoolkit.core.manager.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

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
        this.workerNum = 1;
    }

    @Override
    public String call() throws Exception {
        return null;
    }
}
