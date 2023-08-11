package com.zzz.migration.core.executor.impl;

import com.zzz.migration.common.constants.CommonConstant;
import com.zzz.migration.core.executor.IExecutor;
import com.zzz.migration.core.manager.AbstractBaseManager;
import com.zzz.migration.core.worker.impl.TaskExecutorStarter;
import com.zzz.migration.entity.taskEntity.WorkQueue;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:46
 * @description: 基础执行器
 */
@Data
public abstract class AbstractBaseExecutor implements IExecutor {

    public final static Logger logger = LoggerFactory.getLogger(AbstractBaseExecutor.class);

    public String executorName;
    public String executorType;
    public TaskDetail taskDetail;
    //每个执行器具有自己启动的能力
    public TaskExecutorStarter starter;
    public FutureTask<WorkResultEntity> starterFutureTask;

    //Executor自身结果
    public FutureTask<WorkResultEntity> executorFutureTask;

    //上一个执行器
    public AbstractBaseExecutor preExecutor;
    //下一个执行器
    public AbstractBaseExecutor nextExecutor;
    //执行器源队列，承自上一个执行器转交下来的任务
    public WorkQueue sourceExecutorQueue;
    //执行器的目标队列，交接给下一个执行器的任务
    public WorkQueue targetExecutorQueue;

    //执行器读数据管理器
    protected AbstractBaseManager readProcessManager;
    protected FutureTask<WorkResultEntity> readFutureTask = null;
    //写数据管理器
    protected AbstractBaseManager writeProcessManager;
    protected FutureTask<WorkResultEntity> writeFutureTask = null;
    //中转队列
    protected WorkQueue readToWriteExecutorQueue = null;

    public boolean executorStop = false;

    public AbstractBaseExecutor() {
    }

    public AbstractBaseExecutor(TaskDetail taskDetail) {
        this.taskDetail = taskDetail;
        this.setReadToWriteExecutorQueue(taskDetail.getCoreConfig().getWorkQueueSize());
    }

    @Override
    public void initFirstExecutor() {

    }

    @Override
    public String startExecutor() {

        logger.info(executorName + " start ...");

        String returnFlag = CommonConstant.SUCCESS;

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
        String returnMsg = CommonConstant.OK;
        this.executorStop = true;
        if (null != starter) {
            starter.setStop(true);
        }
        String readStopReturnMsg = readProcessManager.stopWorker();
        String writeStopReturnMsg = writeProcessManager.stopWorker();
        if (!readStopReturnMsg.equals(returnMsg) || !writeStopReturnMsg.equals(returnMsg)) {
            returnMsg = CommonConstant.FAIL;
        }
        return returnMsg;
    }

    /**
     * 等待返回结果
     */
    @Override
    public String waitExecutor() {
        String returnFlag = "";
        if (starter != null) {
            //对启动器进行启动判断
            try {
                WorkResultEntity starterResults = starterFutureTask.get();
                if (!starterResults.isNormalFinished()) {
                    returnFlag = CommonConstant.WARNING;
                }
            } catch (Exception e) {
                returnFlag = CommonConstant.FAIL;
                e.printStackTrace();
            }
        }
        try {
            WorkResultEntity executorResults = executorFutureTask.get();
            if (!executorResults.isNormalFinished()) {
                returnFlag = "".equals(returnFlag) ? CommonConstant.WARNING : returnFlag;
            }
        } catch (Exception e) {
            returnFlag = CommonConstant.FAIL;
            e.printStackTrace();
        }
        return "".equals(returnFlag) ? CommonConstant.SUCCESS : returnFlag;
    }

    /**
     * 启动Executor中的Manager
     */
    public WorkResultEntity startManager() {
        //启动读取Manager
        if (!executorStop) {
            readFutureTask = new FutureTask<WorkResultEntity>(readProcessManager);
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

    public void setSourceExecutorQueue(int workQueueSize) {
        this.sourceExecutorQueue = new WorkQueue(workQueueSize);
    }

    public void setReadToWriteExecutorQueue(int workQueueSize) {
        this.readToWriteExecutorQueue = new WorkQueue(workQueueSize);
    }
}
