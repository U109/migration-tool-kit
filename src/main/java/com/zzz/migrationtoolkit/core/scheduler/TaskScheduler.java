package com.zzz.migrationtoolkit.core.scheduler;

import com.zzz.migrationtoolkit.core.coreManager.TaskManager;
import com.zzz.migrationtoolkit.core.coreManager.context.TaskCache;
import com.zzz.migrationtoolkit.core.executor.TaskExecutorManager;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:15
 * @description: 任务调度器
 */
@Slf4j
public class TaskScheduler {

    private static Map<String, TaskExecutorManager> taskExecutorContext = TaskManager.taskExecutorContextMap;

    public static void startTask(String taskId) {
        TaskDetail taskDetail = TaskCache.findTask(taskId);

        TaskCache.updateTaskDetail(taskId,"启动中",null,null);

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


    public static String stopTask(String taskId) {
        TaskDetail taskDetail = null;
        String returnMsg = "OK";
        try {
            taskDetail = TaskCache.findTask(taskId);
            TaskCache.updateTaskDetail(taskId, "未开始", null, null);
            //执行器立即进入停止中
            taskDetail.setTaskStatus("停止中");
            //获取执行器管理器，执行停止任务
            returnMsg = taskExecutorContext.get(taskDetail.getTaskId()).stopTask();
        } catch (Exception e) {
            returnMsg = "taskScheduler error : " + e.getMessage();
            log.error(returnMsg);
            if (taskDetail != null) {
                taskDetail.setFailMsg(returnMsg);
            }
        }
        return returnMsg;
    }

}
