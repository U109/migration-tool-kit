package com.zzz.migrationtoolkit.entity.taskEntity;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:07
 * @description:
 */
@Data
public class WorkEntity implements Serializable {

    private WorkType workType;

    private WorkContentType workContentType;
    //任务块中封装的迁移对象
    private MigrationObj migrationObj;
    //任务块中封装的数据列表
    private List<List<Object>> dataList;

    public WorkEntity() {
    }

    public WorkEntity(WorkType workType, WorkContentType workContentType, MigrationObj migrationObj) {
        this.workType = workType;
        this.workContentType = workContentType;
        this.migrationObj = migrationObj;
    }

}
