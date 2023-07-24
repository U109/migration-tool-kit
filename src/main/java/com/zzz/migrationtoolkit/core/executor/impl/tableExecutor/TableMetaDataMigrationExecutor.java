package com.zzz.migrationtoolkit.core.executor.impl.tableExecutor;

import com.zzz.migrationtoolkit.core.executor.impl.AbstractTaskBaseExecutor;
import com.zzz.migrationtoolkit.core.manager.impl.MetaDataReadManager;
import com.zzz.migrationtoolkit.core.manager.impl.MetaDataWriteManager;
import com.zzz.migrationtoolkit.core.worker.impl.TaskExecutorStarter;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkContentType;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkType;

import java.util.concurrent.ExecutionException;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:45
 * @description:
 */
public class TableMetaDataMigrationExecutor extends AbstractTaskBaseExecutor {


    public TableMetaDataMigrationExecutor() {
    }

    public TableMetaDataMigrationExecutor(TaskDetail taskDetail) {
        super(taskDetail);
        super.executorType = "TableMetaDataExecutor";
        super.executorName = taskDetail + "--[表结构执行器]";
        //初始化读表结构   TODO 这里source放置的null，target放置中转队列的目的
        this.readProcessManager = new MetaDataReadManager(taskDetail, null, this.readToWriteExecutorQueue);
        //初始化写Manager
        this.writeProcessManager = new MetaDataWriteManager(taskDetail, this.readToWriteExecutorQueue, null);
    }

    @Override
    public TaskExecutorStarter initStarter() {
        starter = new TaskExecutorStarter(taskDetail.getTableDetailMap(), this.readProcessManager, WorkType.READ_TABLE_METADATA, WorkContentType.TABLE_STARTED);
        starter.setStarterName("TableMetaDataStarter");
        this.readProcessManager.setSourceWorkQueue();
        return starter;
    }

    /**
     * 启动表结构迁移
     */
    @Override
    public ProcessWorkResultEntity call() {
        boolean resultFlag = true;

        String resultMsg = "";

        //启动失败，直接返回
        ProcessWorkResultEntity startManagerResultEntity = startManager();
        if (startManagerResultEntity != null) {
            return startManagerResultEntity;
        }
        //等待两个流程返回结果
        //读取元数据Manager返回结果，所有的源数据已经读取完毕，结束该manager下所有线程
        try {
            ProcessWorkResultEntity result = readFutureTask.get();
            //记录返回的结果标志位，true标识正常
            resultFlag = result.isNormalFinished();
            //记录返回错误消息
            resultMsg = (result.isNormalFinished()) ? "" : "ReadMetaData has error, " + result.getResultMsg() + "\n";
            taskDetail.appendFailMsg(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            writeProcessManager.finishedQueue();
        }
        //写入元数据Manager返回结果，所有的源数据已经读取完毕，结束该manager下所有线程
        try {
            ProcessWorkResultEntity result = writeFutureTask.get();
            //记录返回的结果标志位，true标识正常
            resultFlag = result.isNormalFinished();
            //记录返回错误消息
            resultMsg = (result.isNormalFinished()) ? "" : "WriteMetaData has error, " + result.getResultMsg() + "\n";
            taskDetail.appendFailMsg(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //告知后续流程结束
            if (writeProcessManager.getTargetWorkQueue() != null){
                this.nextExecutor.getReadProcessManager().finishedQueue();
            }
        }
        return new ProcessWorkResultEntity(resultFlag, resultMsg);
    }
}

