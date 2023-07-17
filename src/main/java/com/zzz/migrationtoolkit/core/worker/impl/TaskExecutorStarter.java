package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.core.manager.AbstractBaseProcessManager;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkContentType;
import com.zzz.migrationtoolkit.entity.taskEntity.WorkType;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:53
 * @description:
 */
@Data
public class TaskExecutorStarter implements Callable<ProcessWorkResultEntity> {

    private String starterName;
    //需要拆分的内容
    private Map<String, MigrationObj> detailMap;
    //下一个流程管理器
    private AbstractBaseProcessManager manager;

    private WorkType workType;

    private WorkContentType workContentType;

    private boolean isStop = false;

    public TaskExecutorStarter() {
    }

    public TaskExecutorStarter(Map<String, MigrationObj> detailMap, AbstractBaseProcessManager manager, WorkType workType, WorkContentType workContentType) {
        this.detailMap = detailMap;
        this.manager = manager;
        this.workContentType = workContentType;
        this.workType = workType;
    }

    //任务分发
    @Override
    public ProcessWorkResultEntity call() throws Exception {
        ProcessWorkResultEntity result = new ProcessWorkResultEntity();
        //差分任务，进行封装
        for (MigrationObj migrationObj : detailMap.values()) {
            if (isStop) {
                break;
            }
            //起始队列中放入任务块
            manager.getSourceWorkQueue().putWork(new ProcessWorkEntity(workType, workContentType, migrationObj));
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
