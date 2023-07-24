package com.zzz.migrationtoolkit.core.task;

import java.util.concurrent.Callable;

/**
 * @author: Zzz
 * @date: 2023/7/24 15:52
 * @description:
 */
public interface ITask<T> extends Callable<T> {

    public void stop();

}
