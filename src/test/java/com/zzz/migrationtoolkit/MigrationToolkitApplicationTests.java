package com.zzz.migrationtoolkit;

import com.zzz.migrationtoolkit.core.manager.impl.MetaDataReadManager;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SpringBootTest
class MigrationToolkitApplicationTests {

    @Test
    void contextLoads() throws Exception {

        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId("taskDetail");
        taskDetail.setTaskName("Test");
        MetaDataReadManager manager = new MetaDataReadManager(taskDetail,new ProcessWorkQueue(10),new ProcessWorkQueue(10));
        FutureTask<String> futureTask = new FutureTask<String>(manager);
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }

}
