package com.zzz.migration.core.worker.impl;

import com.zzz.migration.common.constants.CommonConstant;
import com.zzz.migration.common.constants.MigrationConstant;
import com.zzz.migration.common.utils.DataSourceUtil;
import com.zzz.migration.core.worker.AbstractBaseWorker;
import com.zzz.migration.database.DataBaseExecutorFactory;
import com.zzz.migration.database.executor.IDataBaseExecutor;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.migrationObjEntity.MigrationTable;
import com.zzz.migration.entity.taskEntity.*;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:50
 * @description:
 */
public class TableStructureReadWorker extends AbstractBaseWorker {

    private final DataSourceProperties properties;

    public TableStructureReadWorker(TaskDetail taskDetail, WorkQueue sourceWorkQueue, WorkQueue targetWorkQueue) {
        super(taskDetail, sourceWorkQueue, targetWorkQueue, "MetaDataReadWorker");
        this.properties = taskDetail.getSourceDataBase().getProperties();
    }

    @Override
    public WorkResultEntity call() throws Exception {

        WorkResultEntity workResultEntity = new WorkResultEntity();
        IDataBaseExecutor dataBaseExecutor = null;

        while (true) {
            //任务块
            WorkEntity processWork = null;
            //迁移表对象
            MigrationTable migrationTable = null;

            if (stopWork) {
                break;
            }

            processWork = this.sourceWorkQueue.takeWork();

            if (!processWork.getWorkType().equals(WorkType.READ_TABLE_METADATA)) {
                continue;
            }
            if (processWork.getWorkContentType().equals(WorkContentType.WORK_FINISHED)) {
                break;
            }
            //如果仅迁移表数据
            if (taskDetail.getMigrationTableType().equals(MigrationConstant.MIGRATION_ONLY_USERDATA)) {
                processWork.setWorkType(WorkType.READ_TABLE_USERDATA);
                targetWorkQueue.putWork(processWork);
                continue;
            }


            if (dataBaseExecutor == null) {
                dataBaseExecutor = DataBaseExecutorFactory.getDatabaseInstance(properties);
            }
            migrationTable = (MigrationTable) processWork.getMigrationObj();
            migrationTable.setResultMsg("");


            //补充列信息
            migrationTable.setColumnDetailForMigrationTable(dataBaseExecutor);

            //进行设置写任务块
            processWork.setWorkType(WorkType.WRITE_TABLE_METADATA);
            targetWorkQueue.putWork(processWork);
            workResultEntity.setResultMsg(CommonConstant.SUCCESS);
        }
        return workResultEntity;
    }
}
