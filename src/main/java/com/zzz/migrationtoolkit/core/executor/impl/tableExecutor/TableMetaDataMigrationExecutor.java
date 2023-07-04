package com.zzz.migrationtoolkit.core.executor.impl.tableExecutor;

import com.zzz.migrationtoolkit.core.executor.impl.AbstractTaskBaseExecutor;
import com.zzz.migrationtoolkit.core.manager.impl.MetaDataReadManager;
import com.zzz.migrationtoolkit.core.manager.impl.MetaDataWriteManager;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:45
 * @description:
 */
public class TableMetaDataMigrationExecutor extends AbstractTaskBaseExecutor {


    public TableMetaDataMigrationExecutor() {
    }

    public TableMetaDataMigrationExecutor(TaskDetail taskDetail) {
        super(taskDetail);
        super.executorType = "TableMetaDataExecutor";
        //初始化读表结构
        this.readProcessManager = new MetaDataReadManager(taskDetail, null, this.readToWriteExecutorQueue);
        //初始化写Manager
        this.writeProcessManager = new MetaDataWriteManager(taskDetail, this.readToWriteExecutorQueue, null);
    }


    @Override
    public String call() throws Exception {

        return null;
    }
}

