package com.zzz.migrationtoolkit.entity.taskEntity;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:07
 * @description:
 */
public class ProcessWorkEntity {

    private String workType;

    private String workContentType;
    //任务块中封装的迁移对象
    private MigrationObj migrationObj;
    //任务块中封装的数据列表
    private List<List<Object>> dataList;

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkContentType() {
        return workContentType;
    }

    public void setWorkContentType(String workContentType) {
        this.workContentType = workContentType;
    }

    public MigrationObj getMigrationObj() {
        return migrationObj;
    }

    public void setMigrationObj(MigrationObj migrationObj) {
        this.migrationObj = migrationObj;
    }

    public List<List<Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<Object>> dataList) {
        this.dataList = dataList;
    }
}
