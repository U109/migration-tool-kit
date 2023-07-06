package com.zzz.migrationtoolkit;

import com.zzz.migrationtoolkit.core.manager.impl.MetaDataReadManager;
import com.zzz.migrationtoolkit.core.scheduler.TaskScheduler;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SpringBootTest
class MigrationToolkitApplicationTests {

    @Test
    void contextLoads() {

        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId("One001");
        taskDetail.setTaskName("Test TaskDetail");
        taskDetail.getMigrationObjTypeList().add("TABLE");
        taskDetail.setTaskStatus("init...");
        taskDetail.getTableDetailMap().put("table@@@test", new MigrationObj());
        TaskScheduler.startTask(taskDetail);
    }

}
