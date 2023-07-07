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
    String dbDriver;
    String dbUrl;
    String host;
    String port;
    String username;
    String password;
    String dbName;
    String paramStr;
    String schema;
    String comment;

    @Override
    public DataBaseConnInfo clone() {
        try {
            return (DataBaseConnInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
