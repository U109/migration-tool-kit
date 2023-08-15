package com.zzz.migration.core.task;

import com.zzz.migration.common.utils.DataSourceUtil;
import com.zzz.migration.database.DataBaseExecutorFactory;
import com.zzz.migration.database.executor.IDataBaseExecutor;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.migrationObjEntity.MigrationTable;
import com.zzz.migration.entity.taskEntity.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/24 16:06
 * @description:
 */
@Slf4j
public class TableDataParallelTask implements ITask<Long> {

    private final String sql;
    private final MigrationTable migrationTable;
    private final TaskDetail taskDetail;
    private int fetchSize = 0;
    private int commitSize = 0;
    private final boolean stopWork;
    private final WorkQueue targetWorkQueue;

    private final DataSourceProperties properties;

    public TableDataParallelTask(String sql, MigrationTable migrationTable, TaskDetail taskDetail,
                                 boolean stopWork, WorkQueue targetWorkQueue) {
        this.sql = sql;
        this.migrationTable = migrationTable;
        this.taskDetail = taskDetail;
        this.fetchSize = taskDetail.getCoreConfig().getFetchDataSize();
        this.commitSize = taskDetail.getCoreConfig().getWriteDataCommitSize();
        this.stopWork = stopWork;
        this.targetWorkQueue = targetWorkQueue;
        this.properties = taskDetail.getSourceDataBase().getProperties();
    }


    @Override
    public Long call() throws Exception {
        IDataBaseExecutor dataBaseExecutor = DataBaseExecutorFactory.getDatabaseInstance(properties);
        //记录错误数
        long errorDataNum = 0;
        //记录已经读取的数据条数
        long cacheSize = 0;
        try {
            ResultSet rs = dataBaseExecutor.getResultSet(sql, fetchSize);
            //数据块
            List<List<Object>> cacheList = new ArrayList<>();

            List<Object> data = null;
            while (rs != null && rs.next() && !stopWork) {
                try {
                    data = dataBaseExecutor.convertReadDataTypes(migrationTable.getTableColumnList(), rs);
                    if (data != null) {
                        cacheList.add(data);
                    }
                    //释放
                    data = null;
                } catch (Exception e) {
                    errorDataNum++;
                    e.printStackTrace();
                    migrationTable.appendResultMsg(e.getMessage());
                }
                if (++cacheSize == commitSize) {
                    putData(cacheList);
                    cacheSize = 0;
                }
            }
            if (cacheList.size() > 0) {
                putData(cacheList);
            }
            if (errorDataNum != 0) {
                migrationTable.appendResultMsg("[" + errorDataNum + "条]数据读取失败！");
            }
        } catch (Exception e) {
            log.error("read data has error : " + e.getMessage());
            throw e;
        }
        return errorDataNum;
    }

    private void putData(List<List<Object>> cacheList) {
        WorkEntity workEntity = new WorkEntity();
        workEntity.setWorkType(WorkType.WRITE_TABLE_USERDATA);
        workEntity.setWorkContentType(WorkContentType.TABLE_STARTED);
        workEntity.setMigrationObj(migrationTable);

        List<List<Object>> subList = new ArrayList<>(cacheList.subList(0, cacheList.size()));

        workEntity.setDataList(subList);

        targetWorkQueue.putWork(workEntity);

        cacheList.clear();
    }

    @Override
    public void stop() {

    }
}
