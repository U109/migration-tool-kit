package com.zzz.migration.core.worker.impl;

import com.zzz.migration.common.constants.MigrationConstant;
import com.zzz.migration.common.utils.DataSourceUtil;
import com.zzz.migration.database.DataBaseExecutorFactory;
import com.zzz.migration.database.SQLGeneratorFactory;
import com.zzz.migration.database.generator.ISQLGenerator;
import com.zzz.migration.core.worker.AbstractBaseWorker;
import com.zzz.migration.database.executor.IDataBaseExecutor;
import com.zzz.migration.entity.dataSourceEmtity.CloseableDataSource;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.migrationObjEntity.TableColumn;
import com.zzz.migration.entity.migrationObjEntity.MigrationTable;
import com.zzz.migration.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/24 13:41
 * @description:
 */
@Slf4j
public class TableDataWriteWorker extends AbstractBaseWorker {

    private final String dbName;
    private final DataSourceProperties properties;

    public TableDataWriteWorker(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "UserDataWriteWorker");
        this.dbName = taskDetail.getTargetDataBase().getProperties().getDbName();
        this.properties = taskDetail.getTargetDataBase().getProperties();
    }

    @Override
    public WorkResultEntity call() throws Exception {
        WorkResultEntity workResultEntity = new WorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;
        MigrationTable migrationTable = null;
        String tableName = "";

        while (true) {
            try {
                if (stopWork) {
                    break;
                }
                WorkEntity processWork = this.sourceWorkQueue.takeWork();
                if (!processWork.getWorkType().equals(WorkType.WRITE_TABLE_USERDATA)) {
                    continue;
                }
                if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                    break;
                }
                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getDatabaseInstance(properties);
                }
                migrationTable = (MigrationTable) processWork.getMigrationObj();

                List<TableColumn> columnList = migrationTable.getTableColumnList();
                List<List<Object>> dataList = processWork.getDataList();

                ISQLGenerator sqlGenerator = SQLGeneratorFactory.getDatabaseInstance(properties);

                String insertSql = sqlGenerator.getTargetDataInsertSql(migrationTable, dbName);
                log.info("insert data sql : " + insertSql);

                int dataCount = dataList.size();

                try {
                    long errDataCount = dataBaseExecutor.executeInsertSql(insertSql, dataList, columnList);
                    //插入成功后更新条数
                    synchronized (TableDataWriteWorker.class) {
                        if (dataCount - errDataCount > 0) {
                            migrationTable.updateSuccessDataCount(dataCount, false, true);
                        }
                        if (errDataCount != 0) {
                            migrationTable.updateFailedDataCount(errDataCount, true);
                        }
                    }
                } catch (Exception e) {
                    synchronized (TableDataWriteWorker.class) {
                        migrationTable.updateFailedDataCount(dataCount, true);
                    }
                    e.printStackTrace();
                }

            } catch (Exception e) {
                synchronized (TableDataWriteWorker.class) {
                    if (migrationTable != null) {
                        migrationTable.appendResultMsg("write data has error");
                        migrationTable.setMigrationResult(MigrationConstant.MIGRATION_RESULT_FAIL);
                    }
                }
                workResultEntity.setResultMsg("write data has error");
                workResultEntity.setNormalFinished(false);
                e.printStackTrace();
            } finally {
                if (dataBaseExecutor != null){
                    dataBaseExecutor.closeExecutor();
                }
            }
        }
        return workResultEntity;
    }
}
