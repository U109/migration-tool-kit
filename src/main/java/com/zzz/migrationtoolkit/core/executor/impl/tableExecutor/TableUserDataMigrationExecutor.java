package com.zzz.migrationtoolkit.core.executor.impl.tableExecutor;

import com.zzz.migrationtoolkit.core.executor.impl.AbstractTaskBaseExecutor;
import com.zzz.migrationtoolkit.core.worker.impl.TaskExecutorStarter;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkContentType;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkType;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:45
 * @description:
 */
public class TableUserDataMigrationExecutor extends AbstractTaskBaseExecutor {

    public TableUserDataMigrationExecutor() {
    }

    public TableUserDataMigrationExecutor(TaskDetail taskDetail) {
        super(taskDetail);
        executorType = "TableUserDataExecutor";
        executorName = taskDetail + "[表数据迁移执行器]";

    }

    @Override
    public TaskExecutorStarter initStarter() {
        starter = new TaskExecutorStarter(taskDetail.getTableDetailMap(),this.readProcessManager, WorkType.READ_TABLE_USERDATA, WorkContentType.TABLE_STARTED);
        starter.setStarterName("TableUserDataStarter");
        this.readProcessManager.setSourceWorkQueue();
        return starter;
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {
        return null;
    }
}
