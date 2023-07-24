package com.zzz.migrationtoolkit.core.executor;

import com.zzz.migrationtoolkit.common.constants.CommonConstant;
import com.zzz.migrationtoolkit.common.constants.TaskResultConstant;
import com.zzz.migrationtoolkit.common.constants.TaskStatusConstant;
import com.zzz.migrationtoolkit.core.coreManager.context.TaskCache;
import com.zzz.migrationtoolkit.core.executor.impl.AbstractTaskBaseExecutor;
import com.zzz.migrationtoolkit.core.executor.impl.tableExecutor.TableMetaDataMigrationExecutor;
import com.zzz.migrationtoolkit.core.executor.impl.tableExecutor.TableUserDataMigrationExecutor;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class TaskExecutorManager implements Runnable {

    private String taskExecutorManagerId;
    private String taskExecutorManagerName;
    private TaskDetail taskDetail;
    /**
     * 执行任务所有执行器
     */
    private List<AbstractTaskBaseExecutor> executorList;
    /**
     * 一级执行器
     */
    private TableMetaDataMigrationExecutor tableMetaDataMigrationExecutor;
    protected FutureTask<ProcessWorkResultEntity> tableMetaDataFutureTask;

    private TableUserDataMigrationExecutor tableUserDataMigrationExecutor;
    protected FutureTask<ProcessWorkResultEntity> tableUserDataFutureTask;

    private boolean executorStop = false;

    public TaskExecutorManager() {
    }

    public TaskExecutorManager(TaskDetail taskDetail) {
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
            tableMetaDataMigrationExecutor = new TableMetaDataMigrationExecutor(taskDetail);
            addToExecutorList(tableMetaDataMigrationExecutor);

            tableUserDataMigrationExecutor = new TableUserDataMigrationExecutor(taskDetail);
            addToExecutorList(tableUserDataMigrationExecutor, true);
        }
    }

    /**
     * 注册执行器
     *
     * @param executor 需要注册的执行器
     */
    private void addToExecutorList(AbstractTaskBaseExecutor executor) {
        addToExecutorList(executor, false);
    }

    /**
     * 注册执行器
     *
     * @param executor  需要注册的执行器
     * @param isCascade 是否缓存
     */
    private void addToExecutorList(AbstractTaskBaseExecutor executor, boolean isCascade) {
        if (executorList == null) {
            executorList = new ArrayList<>();
        }
        if (executorList.size() > 0) {
            AbstractTaskBaseExecutor preExecutor = executorList.get(executorList.size() - 1);
            executor.setPreExecutor(preExecutor);
            preExecutor.setNextExecutor(executor);
            if (isCascade) {
                //当前执行器设置队列
                executor.setSourceExecutorQueue(taskDetail.getCoreConfig().getWorkQueueSize());
                //将执行器初始化给readManager
                executor.getReadProcessManager().setSourceWorkQueue(executor.getSourceExecutorQueue());
                //初始化前一个执行器
                preExecutor.setTargetExecutorQueue(executor.getSourceExecutorQueue());
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
        if (tableMetaDataMigrationExecutor != null && !executorStop) {
            startFlag = CommonConstant.SUCCESS.equals(tableMetaDataMigrationExecutor.startExecutor());
        }
        //启动数据迁移执行器
        if (tableUserDataMigrationExecutor != null && startFlag && !executorStop) {
            startFlag = CommonConstant.SUCCESS.equals(tableUserDataMigrationExecutor.startExecutor());
        }

        //一级执行器返回结果
        if (tableMetaDataMigrationExecutor != null && startFlag && !executorStop) {
            if (CommonConstant.SUCCESS.equals(tableMetaDataMigrationExecutor.waitExecutor())) {
                log.info("migration " + tableMetaDataMigrationExecutor.getExecutorType() + " success !");
            } else {
                log.error("migration " + tableMetaDataMigrationExecutor.getExecutorType() + " fail !");
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

        for (AbstractTaskBaseExecutor executor : executorList) {
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
