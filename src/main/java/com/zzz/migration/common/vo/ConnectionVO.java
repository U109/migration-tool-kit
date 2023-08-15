package com.zzz.migration.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangzhongzhen
 */
@Data
public class ConnectionVO implements Serializable {

    private String name;
    private String dbtype;
    private String host;
    private String port;
    private String username;
    private String password;
    private String dbname;
    private String schema;
    private String comment;
    private String connParam;
    private String driverPath;

}
