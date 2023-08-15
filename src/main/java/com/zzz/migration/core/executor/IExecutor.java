package com.zzz.migration.core.executor;

import com.zzz.migration.entity.taskEntity.WorkResultEntity;

import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:47
 * @description: 执行器接口
 */
public interface IExecutor extends Callable<WorkResultEntity> {

    public void initFirstExecutor();

    public String startExecutor();

    public String stopExecutor();

    public String waitExecutor();
}
