package com.zzz.migration.handler.taskHandler;

import com.zzz.migration.core.cache.TaskCache;
import com.zzz.migration.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:08
 * @description: 任务操作
 */
public class TaskOperator {

    public static void startTask(String taskId) {

    }

    public static String createTask(TaskDetail taskDetail){
        TaskCache.createTask(taskDetail);
        return "Success";
    }
}
