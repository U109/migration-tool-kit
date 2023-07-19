package com.zzz.migrationtoolkit.handler.taskHandler;

import com.zzz.migrationtoolkit.core.coreManager.context.TaskCache;
import com.zzz.migrationtoolkit.core.scheduler.TaskScheduler;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.server.InitContext;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:08
 * @description: 任务操作
 */
public class TaskOperator {

    public static void startTask(String taskId) {
        TaskScheduler.startTask(taskId);
    }

    public static String createTask(TaskDetail taskDetail){
        TaskCache.createTask(taskDetail);
        return "Success";
    }
}
