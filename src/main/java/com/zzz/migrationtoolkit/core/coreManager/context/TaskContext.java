package com.zzz.migrationtoolkit.core.coreManager.context;

import com.zzz.migrationtoolkit.core.coreManager.TaskManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/6 9:39
 * @description: 任务容器
 */
public class TaskContext {
    /**
     * 任务管理者
     */
    public TaskManager taskManager;

    /**
     * 存放任务缓存信息
     */
    public static Map<String,TaskCache> taskCacheMap = new HashMap<>();
}
