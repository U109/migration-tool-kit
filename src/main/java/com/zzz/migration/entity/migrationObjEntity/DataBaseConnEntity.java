package com.zzz.migration.entity.migrationObjEntity;

import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/10 10:58
 * @description: 数据库连接实体类
 */
@Data
public class DataBaseConnEntity implements Serializable {

    String connectionId;
    String connectionName;
    String databaseType;
    /**
     * 数据库标志位
     * 0：源
     * 1：目标
     */
    String databaseFlag;

    DataSourceProperties properties;

    public DataBaseConnEntity() {
    }

    public DataBaseConnEntity(String databaseType) {
        this.databaseType = databaseType;
    }
}
