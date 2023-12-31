package com.zzz.migration.core.manager;

import com.zzz.migration.entity.taskEntity.WorkResultEntity;

import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:11
 * @description:
 */
public interface IManager extends Callable<WorkResultEntity> {

    //启动worker
    public String startWorker();

    public String stopWorker();

    public String finishedQueue();

}
