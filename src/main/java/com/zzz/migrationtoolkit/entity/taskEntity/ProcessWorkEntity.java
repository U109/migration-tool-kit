package com.zzz.migrationtoolkit.entity.taskEntity;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import lombok.Data;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 17:07
 * @description:
 */
@Data
public class ProcessWorkEntity {

    private String workType;

    private String workContentType;
    //任务块中封装的迁移对象
    private MigrationObj migrationObj;
    //任务块中封装的数据列表
    private List<List<Object>> dataList;

    public ProcessWorkEntity() {
    }

    public ProcessWorkEntity(String workType, String workContentType, MigrationObj migrationObj) {
        this.workType = workType;
        this.workContentType = workContentType;
        this.migrationObj = migrationObj;
    }

}
