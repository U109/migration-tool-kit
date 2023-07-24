package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.core.generator.SQLGeneratorFactory;
import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/24 13:41
 * @description:
 */
@Slf4j
public class UserDataWriteWorker extends AbstractProcessWorker {

    private String dbName;
    private String dbType;

    public UserDataWriteWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "UserDataWriteWorker");
        this.dbName = taskDetail.getTargetDataBase().getDbci().getDbName();
        this.dbType = taskDetail.getTargetDataBase().getDbci().getDbType();
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {
        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;
        MigrationTable migrationTable = null;
        String tableName = "";

        while (true) {
            try {
                if (stopWork) {
                    break;
                }
                ProcessWorkEntity processWork = this.sourceWorkQueue.takeWork();
                if (!processWork.getWorkType().equals(WorkType.WRITE_TABLE_USERDATA)) {
                    continue;
                }
                if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                    break;
                }
                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getDestInstance(taskDetail);
                }
                migrationTable = (MigrationTable) processWork.getMigrationObj();

                List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
                List<List<Object>> dataList = processWork.getDataList();

                ISQLGenerator sqlGenerator = SQLGeneratorFactory.newDestInstance(taskDetail);

                String insertSql = sqlGenerator.getTargetDataInsertSql(migrationTable, dbName);
                log.info("insert data sql : " + insertSql);

                int dataCount = dataList.size();

                try {
                    long errDataCount = dataBaseExecutor.executeInsertSql(insertSql, dataList, columnList);
                    //插入成功后更新条数
                    synchronized (UserDataWriteWorker.class) {
                        String message = "";
                        if (dataCount - errDataCount > 0) {
                            migrationTable.updateSuccessDataCount(dataCount, false, true);
                            message = Thread.currentThread().getName() + " : insert into " + tableName + " : " + (dataCount - errDataCount) + " datas success!";
                        }
                        if (errDataCount != 0) {
                            migrationTable.updateFailedDataCount(errDataCount, true);
                        }
                    }
                } catch (Exception e) {
                    synchronized (UserDataWriteWorker.class) {
                        migrationTable.updateFailedDataCount(dataCount, true);
                    }
                    e.printStackTrace();
                } finally {
                    dataList = null;
                }

            } catch (Exception e) {
                synchronized (UserDataWriteWorker.class) {
                    if (migrationTable != null) {
                        migrationTable.appendResultMsg("write data has error");
                        migrationTable.setMigrationResult(MigrationConstant.MIGRATION_RESULT_FAIL);
                    }
                }
                processWorkResultEntity.setResultMsg("write data has error");
                processWorkResultEntity.setNormalFinished(false);
                e.printStackTrace();
            } finally {
                if (dataBaseExecutor != null){
                    dataBaseExecutor.closeExecutor();
                }
            }
        }
        return processWorkResultEntity;
    }
}
