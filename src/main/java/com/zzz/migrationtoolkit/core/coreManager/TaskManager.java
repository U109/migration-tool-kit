package com.zzz.migrationtoolkit.core.coreManager;

import com.zzz.migrationtoolkit.core.coreManager.context.TaskContext;
import com.zzz.migrationtoolkit.core.executor.TaskExecutorManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/6 9:38
 * @description: 任务管理者
 * 初始化任务环境，管理taskContext
 * 任务管理
 */
public class TaskManager {

    /**
     * 任务容器
     */
    public static TaskContext taskContext;

    public static Map<String, TaskExecutorManager> taskExecutorContextMap = new HashMap<>();

    public TaskManager() {
        taskContext = new TaskContext();
        taskContext.taskManager = this;
    }


}