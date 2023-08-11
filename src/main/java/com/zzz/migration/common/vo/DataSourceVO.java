package com.zzz.migration.common.vo;

import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/8/4 16:23
 * @description:
 */
@Data
public class DataSourceVO {

    private String connName;
    private String url;
    private String username;
    private String password;
    private String driverName;
    private String dbName;
    private String schemaName;

}
