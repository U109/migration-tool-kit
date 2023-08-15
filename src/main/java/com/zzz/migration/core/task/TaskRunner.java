package com.zzz.migration.core.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author: Zzz
 * @date: 2023/7/24 15:53
 * @description:
 */
public class TaskRunner {

    private final int maxThreads;

    private Map<ITask, Future<Long>> taskFutureMap = new HashMap<>();

    private ThreadPoolExecutor executor;

    public TaskRunner(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public Long taskRun(ITask task) {
        Long num = 0L;
        Future<Long> futureTask = runTask(task, false);
        try {
            num = futureTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public void syncRun(ITask task) {
        runTask(task, true);
    }

    public Future<Long> runTask(ITask task, boolean isSync) {
        if (executor == null) {
            executor = new ThreadPoolExecutor(maxThreads, maxThreads, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        Future<Long> future = executor.submit(task);
        if (isSync) {
            taskFutureMap.put(task, future);
        }
        return future;
    }

    public void stop() {
        if (taskFutureMap != null) {
            for (Map.Entry<ITask, Future<Long>> entry : taskFutureMap.entrySet()) {
                ITask task = entry.getKey();
                task.stop();
            }
        }
    }

    public void shutdown() {
        if (this.executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
        this.taskFutureMap = null;
    }

    public void waitThreadRun(List<ITask> tasks) {
        for (ITask task : tasks) {
            Future<Long> taskFuture = this.taskFutureMap.get(task);
            try {
                if (taskFuture != null) {
                    taskFuture.get();
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
                this.taskFutureMap.remove(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
