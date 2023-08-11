package com.zzz.migration.common.constants;

/**
 * @author zhangzhongzhen wrote on 2023/8/12
 * @version 1.0
 */
public final class DataBaseConstant {

    public static final String MYSQL = "MySql";
    public static final String ORACLE = "Oracle";
    public static final String SQLSERVER = "SqlServer";
    public static final String POSTGRESQL = "Postgresql";
    public static final String DB2 = "DB2";

    public static String[] database = {MYSQL, ORACLE, SQLSERVER, POSTGRESQL, DB2};

    public static final String MYSQL_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String ORACLE_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String SQLSERVER_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String POSTGRESQL_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB2_DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_DB_URL = "jdbc:mysql://";
    public static final String ORACLE_DB_URL = "jdbc:mysql://";
    public static final String SQLSERVER_DB_URL = "jdbc:mysql://";
    public static final String POSTGRESQL_DB_URL = "jdbc:mysql://";
    public static final String DB2_DB_URL = "jdbc:mysql://";
}
