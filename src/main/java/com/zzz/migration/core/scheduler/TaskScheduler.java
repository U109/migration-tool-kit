package com.zzz.migration.core.scheduler;

import com.zzz.migration.common.constants.TaskStatusConstant;
import com.zzz.migration.core.coreManager.TaskManager;
import com.zzz.migration.core.coreManager.context.TaskCache;
import com.zzz.migration.core.executor.ExecutorManager;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:15
 * @description: 任务调度器
 */
@Slf4j
@Component
public class TaskScheduler {

    private static final Map<String, ExecutorManager> TASK_EXECUTOR_CONTEXT = TaskManager.taskExecutorContextMap;

    /**
     * 启动任务
     * @param taskId 任务ID
     */
    public void startTask(String taskId) {
        //从缓存中读取Task
        TaskDetail taskDetail = TaskCache.findTask(taskId);
        //更新任务状态
        TaskCache.updateTaskDetail(taskId, TaskStatusConstant.TASK_STARTING, null, null);
        //创建任务执行器管理者，用来启动任务
        ExecutorManager executorManager = new ExecutorManager(taskDetail);
        //更新任务执行器管理器中最新执行器
        TASK_EXECUTOR_CONTEXT.put(taskDetail.getTaskId(), executorManager);
        //启动执行器管理者
        Thread taskExecutorThread = new Thread(executorManager);
        taskExecutorThread.setName(executorManager.getTaskExecutorManagerName());
        taskExecutorThread.start();
        try {
            //等待任务执行器线程（taskExecutorThread）执行完毕。
            taskExecutorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static String stopTask(String taskId) {
        TaskDetail taskDetail = null;
        String returnMsg;
        try {
            taskDetail = TaskCache.findTask(taskId);
            TaskCache.updateTaskDetail(taskId, TaskStatusConstant.TASK_NO_START, null, null);
            //执行器立即进入停止中
            taskDetail.setTaskStatus(TaskStatusConstant.TASK_STOPPING);
            //获取执行器管理器，执行停止任务
            returnMsg = TASK_EXECUTOR_CONTEXT.get(taskDetail.getTaskId()).stopTask();
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
