package com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:00
 * @description:
 */
@Data
public class DataBaseConnInfo implements Cloneable, Serializable {
    String connName;
    String dbType;
    String host;
    String port;
    String username;
    String password;
    String dbName;
    String paramStr;
    String schema;
    String comment;

    public DataBaseConnInfo() {
    }

    public DataBaseConnInfo(DataBaseConnInfo dataBaseConnInfo) {
        this.connName = dataBaseConnInfo.getConnName();
        this.dbType = dataBaseConnInfo.getDbType();
        this.host = dataBaseConnInfo.getHost();
        this.port = dataBaseConnInfo.getPort();
        this.username = dataBaseConnInfo.getUsername();
        this.password = dataBaseConnInfo.getPassword();
        this.dbName = dataBaseConnInfo.getDbName();
        this.paramStr = dataBaseConnInfo.getParamStr();
        this.schema = dataBaseConnInfo.getSchema();
        this.comment = dataBaseConnInfo.getComment();
    }

    @Override
    public DataBaseConnInfo clone() {
        try {
            return (DataBaseConnInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
