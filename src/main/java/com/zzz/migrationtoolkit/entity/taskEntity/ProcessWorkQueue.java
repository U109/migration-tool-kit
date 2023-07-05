package com.zzz.migrationtoolkit.entity.taskEntity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:05
 * @description:
 */
public class ProcessWorkQueue {

    private BlockingQueue<ProcessWorkEntity> queue;

    public ProcessWorkQueue(int workQueueSize) {
        this.queue = new ArrayBlockingQueue<ProcessWorkEntity>(workQueueSize);
    }

    public String putWork(ProcessWorkEntity entity) {
        try {
            queue.put(entity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    public ProcessWorkEntity takeWork() throws Exception {
        return queue.take();
    }

    public void clear() {
        queue.clear();
    }
}
