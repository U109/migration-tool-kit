package com.zzz.migrationtoolkit.core.executor.impl;

import com.zzz.migrationtoolkit.core.executor.ITaskExecutor;
import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.TaskExecutorStarter;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;

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
    public FutureTask<ProcessWorkResultEntity> starterFutureTask;

    //Executor自身结果
    public FutureTask<ProcessWorkResultEntity> executorFutureTask;
    //执行器读数据管理器
    protected AbstractBaseProcessManager readProcessManager;
    protected FutureTask<ProcessWorkResultEntity> readFutureTask = null;
    //写数据管理器
    protected AbstractBaseProcessManager writeProcessManager;
    protected FutureTask<ProcessWorkResultEntity> writeFutureTask = null;
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
        logger.info(executorName + " start ...");
        String returnFlag = "SUCCESS";

        if (!executorStop) {
            //启动队列没有初始化，需要调用启动器
            if (this.readProcessManager.getSourceWorkQueue() == null) {
                starter = initStarter();
                starterFutureTask = new FutureTask<>(starter);
                Thread starterExecutorThread = new Thread(starterFutureTask);
                starterExecutorThread.setName(starter.getStarterName());
                starterExecutorThread.start();
            }
            executorFutureTask = new FutureTask<>(this);
            Thread taskExecutorThread = new Thread(executorFutureTask);
            taskExecutorThread.setName(this.executorName);
            taskExecutorThread.start();
        }
        return returnFlag;
    }

    public abstract TaskExecutorStarter initStarter();

    @Override
    public String stopExecutor() {
        return null;
    }

    @Override
    public String waitExecutor() {
        return null;
    }

    /**
     * 启动Executor中的Manager
     */
    public ProcessWorkResultEntity startManager() {
        //启动读取Manager
        if (!executorStop) {
            readFutureTask = new FutureTask<ProcessWorkResultEntity>(readProcessManager);
            new Thread(readFutureTask).start();
        }
        //启动写入Manager
        if (writeProcessManager != null) {
            if (!executorStop) {
                writeFutureTask = new FutureTask<>(writeProcessManager);
                new Thread(writeFutureTask).start();
            }
        }
        return null;
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
