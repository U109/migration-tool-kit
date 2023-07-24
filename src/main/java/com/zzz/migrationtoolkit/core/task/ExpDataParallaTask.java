package com.zzz.migrationtoolkit.core.task;

import com.zzz.migrationtoolkit.dataBase.DataBaseExecutorFactory;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationTable;
import com.zzz.migrationtoolkit.entity.taskEntity.*;
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
public class ExpDataParallaTask implements ITask<Long> {

    private String sql;
    private MigrationTable migrationTable;
    private TaskDetail taskDetail;
    private int fetchSize = 0;
    private int commitSize = 0;
    private boolean stopWork;
    private ProcessWorkQueue targetWorkQueue;


    public ExpDataParallaTask(String sql, MigrationTable migrationTable, TaskDetail taskDetail,
                              boolean stopWork, ProcessWorkQueue targetWorkQueue) {
        this.sql = sql;
        this.migrationTable = migrationTable;
        this.taskDetail = taskDetail;
        this.fetchSize = taskDetail.getCoreConfig().getFetchDataSize();
        this.commitSize = taskDetail.getCoreConfig().getWriteDataCommitSize();
        this.stopWork = stopWork;
        this.targetWorkQueue = targetWorkQueue;
    }


    @Override
    public Long call() throws Exception {
        IDataBaseExecutor dataBaseExecutor = DataBaseExecutorFactory.getSourceInstance(taskDetail);
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
                    data = dataBaseExecutor.convertReadDataTypes(migrationTable.getMigrationColumnList(), rs);
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
            if (cacheList != null && cacheList.size() > 0) {
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
        ProcessWorkEntity processWorkEntity = new ProcessWorkEntity();
        processWorkEntity.setWorkType(WorkType.WRITE_TABLE_USERDATA);
        processWorkEntity.setWorkContentType(WorkContentType.TABLE_STARTED);
        processWorkEntity.setMigrationObj(migrationTable);

        List<List<Object>> subList = new ArrayList<>(cacheList.subList(0, cacheList.size()));

        processWorkEntity.setDataList(subList);

        targetWorkQueue.putWork(processWorkEntity);

        cacheList.clear();
    }

    @Override
    public void stop() {

    }
}
