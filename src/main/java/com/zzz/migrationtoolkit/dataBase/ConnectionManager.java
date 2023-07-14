package com.zzz.migrationtoolkit.dataBase;

import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.OracleConnInfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:13
 * @description:
 */
public class ConnectionManager {

    private static Connection connection;

    public static Connection getConnection(DataBaseConnInfo dataBaseConnInfo) throws Exception {
        String dbType = dataBaseConnInfo.getDbType();
        MySqlConnInfo mySqlConnInfo = null;
        if ("Oracle".equals(dbType)){
            mySqlConnInfo = (MySqlConnInfo) dataBaseConnInfo;
            Class.forName(mySqlConnInfo.getDbDriver());
        }
        connection = DriverManager.getConnection(mySqlConnInfo.getUrl(),mySqlConnInfo.getUsername(),mySqlConnInfo.getPassword());
        return connection;
    }

}
