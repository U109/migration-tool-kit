package com.zzz.migrationtoolkit;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.core.manager.impl.MetaDataReadManager;
import com.zzz.migrationtoolkit.core.scheduler.TaskScheduler;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationDBConnEntity;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import com.zzz.migrationtoolkit.entity.taskEntity.ProcessWorkQueue;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;
import com.zzz.migrationtoolkit.handler.taskHandler.TaskOperator;
import com.zzz.migrationtoolkit.server.InitContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@SpringBootTest
class MigrationToolkitApplicationTests {

    @Test
    void contextLoads() {
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskName("测试迁移任务");
        taskDetail.setCoreConfig(InitContext.coreConfig);
        MigrationDBConnEntity sourceDB = new MigrationDBConnEntity(DataBaseConstant.MYSQL);
        sourceDB.setConnectionName(DataBaseConstant.MYSQL);
        sourceDB.setDatabaseFlag("0");
        sourceDB.setDatabaseType(DataBaseConstant.MYSQL);
        sourceDB.setDbci(InitContext.DBConnectionMap.get(DataBaseConstant.MYSQL).get("aaa"));
        MigrationDBConnEntity destDB = new MigrationDBConnEntity(DataBaseConstant.MYSQL);
        destDB.setConnectionName(DataBaseConstant.MYSQL);
        destDB.setDatabaseFlag("1");
        destDB.setDatabaseType(DataBaseConstant.MYSQL);
        destDB.setDbci(InitContext.DBConnectionMap.get(DataBaseConstant.MYSQL).get("bbb"));

        taskDetail.setSourceDataBase(sourceDB);
        taskDetail.setTargetDataBase(destDB);

        TaskOperator.createTask()
    }

}
