package com.zzz.migrationtoolkit.core.coreManager.context;

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


    public static void updateTaskDetail(String taskId, String 正在执行, Object o, Object o1) {
    }
}
