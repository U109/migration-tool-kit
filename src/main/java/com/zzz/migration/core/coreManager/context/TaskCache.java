package com.zzz.migration.core.coreManager.context;

import com.zzz.migration.common.constants.CommonConstant;
import com.zzz.migration.common.utils.TaskPersistenceUtil;
import com.zzz.migration.entity.taskEntity.TaskDetail;

import java.util.Hashtable;
import java.util.List;

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

    private static TaskPersistenceUtil taskPersistenceUtil;

    public synchronized static void updateTaskDetail(String taskId, String taskStatus, String taskResult, Integer deskMigrationObjCount) {
        TaskDetail taskDetail = findTask(taskId);
        taskDetail.updateTaskDetail(taskStatus, taskResult, deskMigrationObjCount);
        taskPersistenceUtil.saveTaskInfo(taskDetail);
    }

    public static TaskDetail findTask(String taskId) {
        return taskCache.get(taskId);
    }

    public static void createTask(TaskDetail task) {
        TaskDetail taskDetail = task.clone();
        taskDetail.setFailMsg("");
        taskCache.put(taskDetail.getTaskId(), taskDetail);
        taskPersistenceUtil.saveTaskInfo(taskDetail);
    }

    public synchronized String init(String taskPersistenceFolder){
        String returnMsg = CommonConstant.RETURN_CODE_OK;
        try{
            if (taskCache == null){
                taskCache = new Hashtable<>();
            }
            taskPersistenceUtil = new TaskPersistenceUtil();
            taskPersistenceUtil.setFolder(taskPersistenceFolder);

            List<TaskDetail> allTaskInfo = taskPersistenceUtil.getAllTaskInfo();

            for (TaskDetail taskDetail : allTaskInfo) {
                taskCache.put(taskDetail.getTaskId(),taskDetail);
            }
        }catch (Exception e){
            returnMsg = CommonConstant.RETURN_CODE_ERROR;
        }
        return returnMsg;
    }
}
