package com.zzz.migrationtoolkit.core.worker;

import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkResultEntity;

import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:44
 * @description:
 */
public interface IProcessWorker extends Callable<ProcessWorkResultEntity> {

    public String stopWorker();
}

