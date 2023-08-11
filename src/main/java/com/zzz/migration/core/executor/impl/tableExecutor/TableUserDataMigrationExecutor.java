package com.zzz.migration.core.executor.impl.tableExecutor;

import com.zzz.migration.core.executor.impl.AbstractBaseExecutor;
import com.zzz.migration.core.manager.impl.UserDataReadManager;
import com.zzz.migration.core.manager.impl.UserDataWriteManager;
import com.zzz.migration.core.worker.impl.TaskExecutorStarter;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import com.zzz.migration.entity.taskEntity.WorkContentType;
import com.zzz.migration.entity.taskEntity.WorkType;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:45
 * @description:
 */
public class TableUserDataMigrationExecutor extends AbstractBaseExecutor {

    public TableUserDataMigrationExecutor(TaskDetail taskDetail) {
        super(taskDetail);
        executorType = "TableUserDataExecutor";
        executorName = taskDetail + "[表数据迁移执行器]";

        this.readProcessManager = new UserDataReadManager(taskDetail, null, this.readToWriteExecutorQueue);
        this.writeProcessManager = new UserDataWriteManager(taskDetail, this.readToWriteExecutorQueue, null);

    }

    @Override
    public TaskExecutorStarter initStarter() {
        starter = new TaskExecutorStarter(taskDetail.getTableDetailMap(), this.readProcessManager, WorkType.READ_TABLE_USERDATA, WorkContentType.TABLE_STARTED);
        starter.setStarterName("TableUserDataStarter");
        this.readProcessManager.setSourceWorkQueue();
        return starter;
    }

    @Override
    public WorkResultEntity call() throws Exception {
        boolean resultFlag = true;

        String resultMsg = "";

        //启动失败，直接返回
        WorkResultEntity startManagerResultEntity = startManager();
        if (startManagerResultEntity != null) {
            return startManagerResultEntity;
        }
        //等待两个流程返回结果
        //读取元数据Manager返回结果，所有的源数据已经读取完毕，结束该manager下所有线程
        try {
            WorkResultEntity result = readFutureTask.get();
            //记录返回的结果标志位，true标识正常
            resultFlag = result.isNormalFinished();
            //记录返回错误消息
            resultMsg = (result.isNormalFinished()) ? "" : "ReadUserData has error, " + result.getResultMsg() + "\n";
            taskDetail.appendFailMsg(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            writeProcessManager.finishedQueue();
        }
        //写入元数据Manager返回结果，所有的源数据已经读取完毕，结束该manager下所有线程
        try {
            WorkResultEntity result = writeFutureTask.get();
            //记录返回的结果标志位，true标识正常
            resultFlag = result.isNormalFinished();
            //记录返回错误消息
            resultMsg = (result.isNormalFinished()) ? "" : "WriteMetaData has error, " + result.getResultMsg() + "\n";
            taskDetail.appendFailMsg(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new WorkResultEntity(resultFlag, resultMsg);
    }
}
