package com.zzz.migrationtoolkit.core.worker.impl;

import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import com.zzz.migrationtoolkit.core.generator.ISQLGenerator;
import com.zzz.migrationtoolkit.core.generator.SQLGeneratorFactory;
import com.zzz.migrationtoolkit.core.task.ExpDataParallaTask;
import com.zzz.migrationtoolkit.core.task.ITask;
import com.zzz.migrationtoolkit.core.task.TaskRunner;
import com.zzz.migrationtoolkit.core.worker.AbstractProcessWorker;
import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
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
public class UserDataReadWorker extends AbstractProcessWorker {

    private DataBaseConnInfo destDbci;
    private List<String> sourceTypeList = null;
    private List<String> destTypeList = null;
    //缓存线程
    private int threadSize = 1;


    public UserDataReadWorker(TaskDetail taskDetail, ProcessWorkQueue sourceWorkQueue, ProcessWorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "UserDataWriteWorker");
        this.destDbci = taskDetail.getTargetDataBase().getDbci();
        this.threadSize = taskDetail.getCoreConfig().getReadDataThreadSize();
    }

    @Override
    public ProcessWorkResultEntity call() throws Exception {

        ProcessWorkResultEntity processWorkResultEntity = new ProcessWorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;
        MigrationTable migrationTable = null;
        String tableName = "";
        String condition = "";
        String dbName = "";
        int tableNum = 0;
        while (true) {
            try {
                if (stopWork) {
                    break;
                }
                ProcessWorkEntity processWork = this.sourceWorkQueue.takeWork();
                if (!processWork.getWorkType().equals(WorkType.READ_TABLE_USERDATA)) {
                    continue;
                }
                if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                    break;
                }
                if (dataBaseExecutor == null) {
                    dataBaseExecutor = DataBaseExecutorFactory.getSourceInstance(taskDetail);
                }
                migrationTable = (MigrationTable) processWork.getMigrationObj();

                tableName = migrationTable.getSourceTable().getTableName();
                dbName = taskDetail.getSourceDataBase().getDbci().getDbName();
                //只迁移数据情况
                if (taskDetail.getMigrationTableType().equals(MigrationConstant.MIGRATION_ONLY_METADATA)) {
                    //因为只迁移了表数据，导致没有执行表结构迁移，所以taskDetail的表列信息是空的
                    if (migrationTable != null && migrationTable.getMigrationColumnList() != null
                            && migrationTable.getMigrationColumnList().size() == 0) {
                        //补充列信息
                        migrationTable.setColumnDetailForMigrationTable(dataBaseExecutor);
                    }
                }
                List<MigrationColumn> columnList = migrationTable.getMigrationColumnList();
                ISQLGenerator sqlGenerator = SQLGeneratorFactory.newSourceInstance(taskDetail);
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
                    ColumnEntity sourceColumnInfo = null;
                    ColumnEntity destColumnInfo = null;
                    sourceTypeList = new ArrayList<>();
                    destTypeList = new ArrayList<>();
                    for (int i = 0; i < columnList.size(); i++) {
                        sourceColumnInfo = columnList.get(i).getSourceColumn();
                        sourceTypeList.add(sourceColumnInfo.getColumnType().getDataTypeName().toUpperCase());

                        destColumnInfo = columnList.get(i).getDestColumn();
                        destTypeList.add(destColumnInfo.getColumnType().getDataTypeName().toUpperCase());
                    }
                    //进行迁移
                    migrationTable.setStartTime(new Date());

                    int parallel = threadSize;

                    TaskRunner taskRunner = new TaskRunner(parallel);
                    long start = 0;
                    long batchSize = totalDataCount / parallel;
                    List<ITask> taskList = new ArrayList<ITask>();
                    for (int i = 0; i < parallel; i++) {
                        start = i * batchSize;
                        if (i == parallel - 1) {
                            batchSize = totalDataCount - start;
                        }
                        String sql = sqlGenerator.getSourceLimitSelectSql(readDataSql, start, batchSize);
                        ITask task = new ExpDataParallaTask(sql, migrationTable, taskDetail, stopWork, targetWorkQueue);
                        taskRunner.syncRun(task);
                        taskList.add(task);
                    }
                    if (taskList != null) {
                        //等待所有线程执行完毕
                        taskRunner.waitThreadRun(taskList);
                    }
                }
                tableNum++;
            } catch (Exception e) {
                if (migrationTable != null) {
                    migrationTable.appendResultMsg(e.getMessage());
                }
                processWorkResultEntity.setResultMsg("read data has error !");
                processWorkResultEntity.setNormalFinished(false);
                log.error(e.getMessage());
            } finally {
                dataBaseExecutor.closeExecutor();
                sourceTypeList = null;
                destTypeList = null;
            }
        }
        return processWorkResultEntity;
    }


}
