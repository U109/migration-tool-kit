package com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ConnectionVO;

/**
 * @author zhangzhongzhen
 */
public class OracleConnInfo extends DataBaseConnInfo {

    private String dbDriver = DataBaseConstant.ORACLE_DB_DRIVER;
    private String dbUrl = DataBaseConstant.ORACLE_DB_URL;

    public OracleConnInfo() {
    }

    public OracleConnInfo(ConnectionVO connection) {
        this.connName = connection.getConnname();
        this.paramStr = connection.getConnParam();
        this.host = connection.getHost();
        this.port = connection.getPort();
        this.dbName = connection.getDbname();
        this.dbType = connection.getDbtype();
        this.username = connection.getUsername();
        this.password = connection.getPassword();
        this.schema = connection.getSchema();
    }

    public String getUrl() {
        return dbUrl + host + ":" + port + "/" + dbName + paramStr;
    }

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

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

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

