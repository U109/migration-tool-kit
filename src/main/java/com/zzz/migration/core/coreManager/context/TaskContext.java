package com.zzz.migration.core.coreManager.context;

import com.zzz.migration.common.constants.FilePathContent;
import com.zzz.migration.core.coreManager.TaskManager;

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

    private static String currentPersistPath = "";
    /**
     * 存放任务缓存信息
     */
    public static Map<String, TaskCache> taskCacheMap = new HashMap<>();


    public TaskCache getTaskCache() {
        if ((taskCacheMap.containsKey(currentPersistPath)) && taskCacheMap.get(currentPersistPath) != null) {
            return taskCacheMap.get(currentPersistPath);
        } else {
            TaskCache taskCache = new TaskCache();
            taskCache.init(currentPersistPath);
            taskCacheMap.put(currentPersistPath, taskCache);
            return taskCache;
        }
    }

    public void initTaskContext() {
        currentPersistPath = FilePathContent.TASK_FILE_FOLDER;
    }
}
