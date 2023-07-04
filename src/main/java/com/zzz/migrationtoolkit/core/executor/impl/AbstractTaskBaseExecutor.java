package com.zzz.migrationtoolkit.core.executor.impl;

import com.zzz.migrationtoolkit.core.executor.ITaskExecutor;
import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.TaskExecutorStarter;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:46
 * @description: 基础执行器
 */
public abstract class AbstractTaskBaseExecutor implements ITaskExecutor {

    public final static Logger logger = LoggerFactory.getLogger(AbstractTaskBaseExecutor.class);

    public String executorName;
    public String executorType;
    public TaskDetail taskDetail;
    //每个执行器具有自己启动的能力
    public TaskExecutorStarter starter;
    //执行器读数据管理器
    protected AbstractBaseProcessManager readProcessManager;
    //写数据管理器
    protected AbstractBaseProcessManager writeProcessManager;
    //中转队列
    protected ProcessWorkQueue readToWriteExecutorQueue = null;

    public boolean executorStop = false;

    public AbstractTaskBaseExecutor() {
    }

    public AbstractTaskBaseExecutor(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    @Override
    public void initFirstExecutor() {

    }

    @Override
    public String startExecutor() {
        return null;
    }

    @Override
    public String stopExecutor() {
        return null;
    }

    @Override
    public String waitExecutor() {
        return null;
    }

    public String startManager() {
        return "ok";
    }


    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getExecutorType() {
        return executorType;
    }

    public void setExecutorType(String executorType) {
        this.executorType = executorType;
    }

    public TaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public TaskExecutorStarter getStarter() {
        return starter;
    }

    public void setStarter(TaskExecutorStarter starter) {
        this.starter = starter;
    }

    public boolean isExecutorStop() {
        return executorStop;
    }

    public void setExecutorStop(boolean executorStop) {
        this.executorStop = executorStop;
    }
}
