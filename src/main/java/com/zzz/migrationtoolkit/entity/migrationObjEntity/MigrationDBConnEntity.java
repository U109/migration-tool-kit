package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/10 10:58
 * @description: 数据库连接实体类
 */
@Data
public class MigrationDBConnEntity {

    String connectionId;
    String connectionName;
    String databaseType;
    /**
     * 数据库标志位
     * 0：源
     * 1：目标
     */
    String databaseFlag;

    DataBaseConnInfo dbci;

    public MigrationDBConnEntity() {
    }

    public MigrationDBConnEntity(String databaseType) {
        this.databaseType = databaseType;
    }
}
