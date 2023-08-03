package com.zzz.migrationtoolkit.entity.taskEntity;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:05
 * @description:
 */
public class WorkQueue implements Serializable {

    private final BlockingQueue<WorkEntity> queue;

    public WorkQueue(int workQueueSize) {
        this.queue = new ArrayBlockingQueue<>(workQueueSize);
    }

    public void putWork(WorkEntity entity) {
        try {
            queue.put(entity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WorkEntity takeWork() throws Exception {
        return queue.take();
    }

    public void clear() {
        queue.clear();
    }
}
