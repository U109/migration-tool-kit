package com.zzz.migrationtoolkit;

import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MigrationToolkitApplicationTests {

//    @Test
//    void contextLoads() {
//        InitContext.initContext();
//
//        TaskDetail taskDetail = new TaskDetail();
//
//        taskDetail.setTaskName("测试迁移任务");
//        taskDetail.setCoreConfig(InitContext.coreConfig);
//        MigrationDBConnEntity sourceDB = new MigrationDBConnEntity(DataBaseConstant.MYSQL);
//        sourceDB.setConnectionName(DataBaseConstant.MYSQL);
//        sourceDB.setDatabaseFlag("0");
//        sourceDB.setDatabaseType(DataBaseConstant.MYSQL);
//        sourceDB.setDbci(InitContext.DBConnectionMap.get(DataBaseConstant.MYSQL).get("aaa"));
//        MigrationDBConnEntity destDB = new MigrationDBConnEntity(DataBaseConstant.MYSQL);
//        destDB.setConnectionName(DataBaseConstant.MYSQL);
//        destDB.setDatabaseFlag("1");
//        destDB.setDatabaseType(DataBaseConstant.MYSQL);
//        destDB.setDbci(InitContext.DBConnectionMap.get(DataBaseConstant.MYSQL).get("bbb"));
//
//        taskDetail.setSourceDataBase(sourceDB);
//        taskDetail.setTargetDataBase(destDB);
//
//
//        MigrationTable migrationTable = new MigrationTable();
//        TableEntity source = new TableEntity();
//        source.setTableId("sourceTable");
//        source.setTableName("mb_user");
//        migrationTable.setSourceTable(source);
//        TableEntity dest = new TableEntity();
//        dest.setTableId("sourceTable");
//        dest.setTableName("mb_user");
//        migrationTable.setDestTable(dest);
//
//        taskDetail.getTableDetailMap().put("TABLE",migrationTable);
//
//        String task = TaskOperator.createTask(taskDetail);
//        TaskOperator.startTask(taskDetail.getTaskId());
//        System.out.println(task);
//    }

    @Test
   void contextLoads() {
        DataSourceProcess.initDBConnections();
    }


}
