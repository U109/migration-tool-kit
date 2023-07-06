package com.zzz.migrationtoolkit.core.scheduler;

import com.zzz.migrationtoolkit.core.coreManager.TaskManager;
import com.zzz.migrationtoolkit.core.executor.TaskExecutorManager;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:15
 * @description: 任务调度器
 */
public class TaskScheduler {

    private static Map<String, TaskExecutorManager> taskExecutorContext = TaskManager.taskExecutorContextMap;

    public static void startTask(TaskDetail taskDetail) {
        taskDetail.setTaskStatus("启动中。。。");
        TaskExecutorManager taskExecutorManager = new TaskExecutorManager(taskDetail);
        //更新executor管理器中最新执行器
        taskExecutorContext.put(taskDetail.getTaskId(), taskExecutorManager);
        //启动执行器管理者
        Thread taskExecutorThread = new Thread(taskExecutorManager);
        taskExecutorThread.setName(taskExecutorManager.getTaskExecutorManagerName());
        taskExecutorThread.start();
        try {
            taskExecutorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
