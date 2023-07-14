package com.zzz.migrationtoolkit.core.executor.impl;

import com.zzz.migrationtoolkit.core.executor.ITaskExecutor;
import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.core.worker.impl.TaskExecutorStarter;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:46
 * @description: 基础执行器
 */
@Data
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

    //上一个执行器
    public AbstractTaskBaseExecutor preExecutor;
    //下一个执行器
    public AbstractTaskBaseExecutor nextExecutor;
    //执行器源队列，承自上一个执行器转交下来的任务
    public ProcessWorkQueue sourceExecutorQueue;
    //执行器的目标队列，交接给下一个执行器的任务
    public ProcessWorkQueue targetExecutorQueue;

    //执行器读数据管理器
    protected AbstractBaseProcessManager readProcessManager;
    protected FutureTask<ProcessWorkResultEntity> readFutureTask = null;
    //写数据管理器
    protected AbstractBaseProcessManager writeProcessManager;
    protected FutureTask<ProcessWorkResultEntity> writeFutureTask = null;
    //中转队列
    protected ProcessWorkQueue readToWriteExecutorQueue = new ProcessWorkQueue(10);

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
        String returnMsg = "OK";
        this.executorStop = true;
        if (null != starter) {
            starter.setStop(true);
        }
        String readStopReturnMsg = readProcessManager.stopWorker();
        String writeStopReturnMsg = writeProcessManager.stopWorker();
        if (!readStopReturnMsg.equals(returnMsg) || !writeStopReturnMsg.equals(returnMsg)) {
            returnMsg = "FAIL";
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
                ProcessWorkResultEntity starterResults = starterFutureTask.get();
                if (!starterResults.isNormalFinished()) {
                    returnFlag = "WARNING";
                }
            } catch (Exception e) {
                returnFlag = "FAILED";
                e.printStackTrace();
            }
        }
        try {
            ProcessWorkResultEntity executorResults = executorFutureTask.get();
            if (!executorResults.isNormalFinished()) {
                returnFlag = "".equals(returnFlag) ? "WARNING" : returnFlag;
            }
        } catch (Exception e) {
            returnFlag = "FAILED";
            e.printStackTrace();
        }
        return "".equals(returnFlag) ? "SUCCESS" : returnFlag;
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

    public void setSourceExecutorQueue(int workQueueSize) {
        this.sourceExecutorQueue = new ProcessWorkQueue(workQueueSize);
    }
}
