package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import com.zzz.migrationtoolkit.database.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.database.SQLGeneratorFactory;
import com.zzz.migrationtoolkit.database.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.core.task.TableDataParallelTask;
import com.zzz.migrationtoolkit.core.task.ITask;
import com.zzz.migrationtoolkit.core.task.TaskRunner;
import com.zzz.migrationtoolkit.core.worker.AbstractBaseWorker;
import com.zzz.migrationtoolkit.database.executor.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migrationtoolkit.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/24 13:38
 * @description:
 */
@Slf4j
public class TableDataReadWorker extends AbstractBaseWorker {

    private final DataSourceProperties properties;
    //缓存线程
    private final int threadSize;

    public TableDataReadWorker(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "UserDataWriteWorker");
        this.properties = taskDetail.getSourceDataBase().getProperties();
        this.threadSize = taskDetail.getCoreConfig().getReadDataThreadSize();
    }


    @Override
    public WorkResultEntity call() throws Exception {

        WorkResultEntity workResultEntity = new WorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;
        MigrationTable migrationTable = null;
        String tableName;
        String dbName;
        while (true) {
            List<String> destTypeList = null;
            List<String> sourceTypeList = null;
            try {
                if (stopWork) {
                    break;
                }
                WorkEntity processWork = this.sourceWorkQueue.takeWork();
                if (!processWork.getWorkType().equals(WorkType.READ_TABLE_USERDATA)) {
                    continue;
                }
                if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                    break;
                }
                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getDatabaseInstance(properties.getDbType());
                }
                migrationTable = (MigrationTable) processWork.getMigrationObj();

                tableName = migrationTable.getSourceTable().getTableName();
                dbName = properties.getDbName();
                //只迁移数据情况
                if (taskDetail.getMigrationTableType().equals(MigrationConstant.MIGRATION_ONLY_METADATA)) {
                    //因为只迁移了表数据，导致没有执行表结构迁移，所以taskDetail的表列信息是空的
                    if (migrationTable.getMigrationColumnList() != null && migrationTable.getMigrationColumnList().size() == 0) {
                        //补充列信息
                        migrationTable.setColumnDetailForMigrationTable(dataBaseExecutor);
                    }
                }
                List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
                ISQLGenerator sqlGenerator = SQLGeneratorFactory.getDatabaseInstance(properties.getDbType());
                String readDataSql = sqlGenerator.getSourceDataSelectSql(tableName, dbName, columnList);
                log.info("select data sql : " + readDataSql);
                //查询数据总量
                String countSql = sqlGenerator.getSourceDataCountSql(tableName, dbName);
                long totalDataCount = dataBaseExecutor.getTableDataCount(countSql);
                //设置数据库总条数
                migrationTable.updateTotalDataCount(totalDataCount);
                //如果表数据为0，有可能结束
                if (totalDataCount == 0) {
                    migrationTable.setFinish(true);
                } else {
                    ColumnEntity sourceColumnInfo;
                    ColumnEntity destColumnInfo;
                    sourceTypeList = new ArrayList<>();
                    destTypeList = new ArrayList<>();
                    for (MigrationColumn migrationColumn : columnList) {
                        sourceColumnInfo = migrationColumn.getSourceColumn();
                        sourceTypeList.add(sourceColumnInfo.getColumnType().getDataTypeName().toUpperCase());

                        destColumnInfo = migrationColumn.getDestColumn();
                        destTypeList.add(destColumnInfo.getColumnType().getDataTypeName().toUpperCase());
                    }
                    //进行迁移
                    migrationTable.setStartTime(new Date());

                    int parallel = threadSize;

                    TaskRunner taskRunner = new TaskRunner(parallel);
                    long start;
                    long batchSize = totalDataCount / parallel;
                    List<ITask> taskList = new ArrayList<ITask>();
                    for (int i = 0; i < parallel; i++) {
                        start = i * batchSize;
                        if (i == parallel - 1) {
                            batchSize = totalDataCount - start;
                        }
                        String sql = sqlGenerator.getSourceLimitSelectSql(readDataSql, start, batchSize);
                        ITask<Long> task = new TableDataParallelTask(sql, migrationTable, taskDetail, stopWork, targetWorkQueue);
                        taskRunner.syncRun(task);
                        taskList.add(task);
                    }
                    //等待所有线程执行完毕
                    taskRunner.waitThreadRun(taskList);
                }
            } catch (Exception e) {
                if (migrationTable != null) {
                    migrationTable.appendResultMsg(e.getMessage());
                }
                workResultEntity.setResultMsg("read data has error !");
                workResultEntity.setNormalFinished(false);
                log.error(e.getMessage());
                e.printStackTrace();
            } finally {
                if (dataBaseExecutor != null) {
                    dataBaseExecutor.closeExecutor();
                }
                sourceTypeList = null;
                destTypeList = null;
            }
        }
        return workResultEntity;
    }


}
