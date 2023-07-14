package com.zzz.migrationtoolkit.core.coreManager.context;

import com.zzz.migrationtoolkit.core.persistence.TaskPersistence;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.Hashtable;

/**
 * @author: Zzz
 * @date: 2023/7/6 9:41
 * @description: 用于缓存所有任务
 * <p>
 * 存放静态信息
 * TaskExecutor存放动态信息
 */
public class TaskCache {

    private static Hashtable<String, TaskDetail> taskCache;

    private static TaskPersistence taskPersistence;

    public synchronized static void updateTaskDetail(String taskId, String taskStatus, String taskResult, Integer deskMigrationObjCount) {
        TaskDetail taskDetail = findTask(taskId);
        taskDetail.updateTaskDetail(taskStatus, taskResult, deskMigrationObjCount);
        taskPersistence.saveTaskInfo(taskDetail);
    }

    public static TaskDetail findTask(String taskId) {
        return taskCache.get(taskId);
    }

    public static void createTask(TaskDetail task) {
        TaskDetail taskDetail = task.clone();
        taskDetail.setFailMsg("");
        taskCache.put(taskDetail.getTaskId(), taskDetail);
        taskPersistence.saveTaskInfo(taskDetail);
    }
}
