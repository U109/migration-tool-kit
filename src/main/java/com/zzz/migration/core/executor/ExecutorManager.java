package com.zzz.migration.core.executor;

import com.zzz.migration.common.constants.CommonConstant;
import com.zzz.migration.common.constants.TaskResultConstant;
import com.zzz.migration.common.constants.TaskStatusConstant;
import com.zzz.migration.core.cache.TaskCache;
import com.zzz.migration.core.executor.impl.AbstractBaseExecutor;
import com.zzz.migration.core.executor.impl.tableExecutor.TableStructureExecutor;
import com.zzz.migration.core.executor.impl.tableExecutor.TableUserDataMigrationExecutor;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:40
 * @description:
 */
@Data
@Slf4j
public class ExecutorManager implements Runnable {

    private String taskExecutorManagerId;
    private String taskExecutorManagerName;
    private TaskDetail taskDetail;
    /**
     * 执行任务所有执行器
     */
    private List<AbstractBaseExecutor> executorList;
    /**
     * 一级执行器
     */
    private TableStructureExecutor tableStructureExecutor;
    protected FutureTask<WorkResultEntity> tableMetaDataFutureTask;

    private TableUserDataMigrationExecutor tableUserDataMigrationExecutor;
    protected FutureTask<WorkResultEntity> tableUserDataFutureTask;

    private boolean executorStop = false;

    public ExecutorManager(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
        this.taskExecutorManagerId = taskDetail.getTaskId();
        this.taskExecutorManagerName = "{" + taskDetail + " executorManager}";
        //根据不同需求，选兵点将
        initExecutorList();
    }

    /**
     * 初始化执行器列表
     */
    private void initExecutorList() {
        if (taskDetail == null) {
            log.error("任务启动器初始化失败");
            return;
        }
        this.executorList = new ArrayList<>();
        //迁移对象列表
        List<String> kinds = taskDetail.getMigrationObjTypeList();
        if (kinds.contains(CommonConstant.MIGRATION_OBJ_TABLE)) {
            //表迁移类型：结构、数据
            tableStructureExecutor = new TableStructureExecutor(taskDetail);
            addToExecutorList(tableStructureExecutor);
            tableUserDataMigrationExecutor = new TableUserDataMigrationExecutor(taskDetail);
            addToExecutorList(tableUserDataMigrationExecutor, true);
        }
    }

    /**
     * 注册执行器
     *
     * @param executor 需要注册的执行器
     */
    private void addToExecutorList(AbstractBaseExecutor executor) {
        addToExecutorList(executor, false);
    }

    /**
     * 注册执行器
     *
     * @param executor  需要注册的执行器
     * @param isCascade 是否缓存
     */
    private void addToExecutorList(AbstractBaseExecutor executor, boolean isCascade) {
        if (executorList == null) {
            executorList = new ArrayList<>();
        }
        if (executorList.size() > 0) {
            AbstractBaseExecutor preExecutor = executorList.get(executorList.size() - 1);
            executor.setPreExecutor(preExecutor);
            preExecutor.setNextExecutor(executor);
            if (isCascade) {
                //当前执行器设置队列
                executor.setSourceExecutorQueue(taskDetail.getCoreConfig().getWorkQueueSize());
                //将执行器初始化给readManager
                executor.getReadProcessManager().setSourceWorkQueue(executor.getSourceExecutorQueue());
                //初始化前一个执行器
                preExecutor.setTargetExecutorQueue(executor.getSourceExecutorQueue());
                //设置写manager的target队列不为空，例如TableStructureWriteManager中的getTargetWorkQueue()
                preExecutor.getWriteProcessManager().setTargetWorkQueue(executor.getSourceExecutorQueue());
            }
        }
        executorList.add(executor);
    }

    //启动所有执行器
    @Override
    public void run() {
        boolean startFlag = true;
        if (!executorStop) {
            //进入该方法，Executor初始化完毕，任务开始执行
            TaskCache.updateTaskDetail(taskDetail.getTaskId(), TaskStatusConstant.TASK_RUNNING, null, null);
            log.info("TaskExecutorManager starting...");
        }
        //一级执行器启动
        if (tableStructureExecutor != null && !executorStop) {
            startFlag = CommonConstant.SUCCESS.equals(tableStructureExecutor.startExecutor());
        }
        //启动数据迁移执行器
        if (tableUserDataMigrationExecutor != null && startFlag && !executorStop) {
            startFlag = CommonConstant.SUCCESS.equals(tableUserDataMigrationExecutor.startExecutor());
        }

        //一级执行器返回结果
        if (tableStructureExecutor != null && startFlag && !executorStop) {
            if (CommonConstant.SUCCESS.equals(tableStructureExecutor.waitExecutor())) {
                log.info("migration " + tableStructureExecutor.getExecutorType() + " success !");
            } else {
                log.error("migration " + tableStructureExecutor.getExecutorType() + " fail !");
            }
        }

        //一级执行器迁移数据返回结果
        if (tableUserDataMigrationExecutor != null && startFlag && !executorStop) {
            if (CommonConstant.SUCCESS.equals(tableUserDataMigrationExecutor.waitExecutor())) {
                log.info("migration " + tableUserDataMigrationExecutor.getExecutorType() + " success !");
            } else {
                log.error("migration " + tableUserDataMigrationExecutor.getExecutorType() + " fail !");
            }
        }

        if (!startFlag) {
            throw new RuntimeException("Task migration has error! Migration Task execute failed!");
        } else {
            if (!executorStop) {
                TaskCache.updateTaskDetail(taskDetail.getTaskId(), TaskStatusConstant.TASK_FINISHED, TaskResultConstant.SUCCESS, null);
            }
        }
    }

    /**
     * 管理器收到停止指令
     *
     * @return String
     */
    public String stopTask() {
        String returnMsg = CommonConstant.RETURN_CODE_OK;
        executorStop = true;

        for (AbstractBaseExecutor executor : executorList) {
            String stopResultMsg = executor.stopExecutor();
            if (!CommonConstant.RETURN_CODE_OK.equals(stopResultMsg)) {
                if (CommonConstant.RETURN_CODE_OK.equals(returnMsg)) {
                    returnMsg = stopResultMsg;
                } else {
                    returnMsg += "\n" + stopResultMsg;
                }
            }
        }
        if (CommonConstant.RETURN_CODE_OK.equals(returnMsg)) {
            TaskCache.updateTaskDetail(taskDetail.getTaskId(), TaskStatusConstant.TASK_STOPPED, TaskResultConstant.STOP_SUCCESS, null);
            log.info(taskDetail.toString() + " stop success !");
        } else {
            TaskCache.updateTaskDetail(taskDetail.getTaskId(), TaskStatusConstant.TASK_EXCEPTION, TaskResultConstant.STOP_FAIL, null);
            log.error(taskDetail.toString() + " stop fail !");
        }
        return returnMsg;
    }

}
