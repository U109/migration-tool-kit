package com.zzz.migrationtoolkit.core.scheduler;

import com.zzz.migrationtoolkit.core.executor.TaskExecutorManager;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:15
 * @description: 任务调度器
 */
public class TaskScheduler {

    public static void startTask(TaskDetail taskDetail) {
        taskDetail.setTaskStatus("启动中。。。");
        if (taskDetail != null){
            TaskExecutorManager taskExcutorManager = new TaskExecutorManager(taskDetail);

        }
    }
}
