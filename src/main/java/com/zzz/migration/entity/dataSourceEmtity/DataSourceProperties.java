package com.zzz.migration.entity.dataSourceEmtity;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author: Zzz
 * @date: 2023/8/4 14:15
 * @description:
 */
@Data
public class DataSourceProperties {

    private String dbType;
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private String driverPath;
    private String dbName;
    private String schemaName;

    private Long connectionTimeout = TimeUnit.SECONDS.toMillis(60);
    private Long maxLifeTime = TimeUnit.MINUTES.toMillis(30);

}
