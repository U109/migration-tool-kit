package com.zzz.migrationtoolkit.core.executor;

import com.zzz.migrationtoolkit.core.executor.impl.tableExecutor.TableMetaDataMigrationExecutor;
import com.zzz.migrationtoolkit.core.executor.impl.tableExecutor.TableUserDataMigrationExecutor;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:40
 * @description:
 */
public class TaskExecutorManager {

    private String taskExecutorManagerId;
    private String taskExecutorManagerName;
    private TaskDetail taskDetail;

    //一级执行器
    private TableMetaDataMigrationExecutor tableMetaDetaMigrationExecutor;

    private TableUserDataMigrationExecutor tableUserDataMigrationExecutor;

    public TaskExecutorManager(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

}
