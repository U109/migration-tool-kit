package com.zzz.migration.core.worker.impl;

import com.zzz.migration.core.manager.AbstractBaseManager;
import com.zzz.migration.entity.migrationObjEntity.MigrationObj;
import com.zzz.migration.entity.taskEntity.WorkEntity;
import com.zzz.migration.entity.taskEntity.WorkResultEntity;
import com.zzz.migration.entity.taskEntity.WorkContentType;
import com.zzz.migration.entity.taskEntity.WorkType;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:53
 * @description:
 */
@Data
public class TaskExecutorStarter implements Callable<WorkResultEntity> {

    private String starterName;
    //需要拆分的内容
    private Map<String, MigrationObj> detailMap;
    //下一个流程管理器
    private AbstractBaseManager manager;

    private WorkType workType;

    private WorkContentType workContentType;

    private boolean isStop = false;

    public TaskExecutorStarter() {
    }

    public TaskExecutorStarter(Map<String, MigrationObj> detailMap, AbstractBaseManager manager, WorkType workType, WorkContentType workContentType) {
        this.detailMap = detailMap;
        this.manager = manager;
        this.workContentType = workContentType;
        this.workType = workType;
    }

    //任务分发
    @Override
    public WorkResultEntity call() throws Exception {
        WorkResultEntity result = new WorkResultEntity();
        //差分任务，进行封装
        for (MigrationObj migrationObj : detailMap.values()) {
            if (isStop) {
                break;
            }
            //起始队列中放入任务块
            manager.getSourceWorkQueue().putWork(new WorkEntity(workType, workContentType, migrationObj));
        }
        //正常结束，需要向队列中添加完成标记
        if (!isStop) {
            manager.finishedQueue();
        } else {
            //接到指令，直接清空队列
            manager.getSourceWorkQueue().clear();
        }
        result.setNormalFinished(true);
        return result;
    }
}
