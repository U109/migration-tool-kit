package com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:04
 * @description:
 */
public class MySqlConnInfo extends DataBaseConnInfo {

    private String connName;
    private String dbName;
    private String dbDriver = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://";

    @Override
    public String getConnName() {
        return connName;
    }

    @Override
    public void setConnName(String connName) {
        this.connName = connName;
    }

    @Override
    public String getDbType() {
        return dbType;
    }

    @Override
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public String getDbDriver() {
        return dbDriver;
    }

    @Override
    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    @Override
    public String getDbUrl() {
        return dbUrl;
    }

    @Override
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getParamStr() {
        return paramStr;
    }

    @Override
    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getComment() {
        return super.getComment();
    }

    @Override
    public void setComment(String comment) {
        super.setComment(comment);
    }
}
