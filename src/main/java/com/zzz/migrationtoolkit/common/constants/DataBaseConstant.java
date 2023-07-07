package com.zzz.migrationtoolkit.common.constants;

/**
 * @author: Zzz
 * @date: 2023/7/7 15:03
 * @description:
 */
public class DataBaseConstant {

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
