package com.zzz.migrationtoolkit.dataBase;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:13
 * @description:
 */
public class ConnectionManager {

    public static Connection getConnection(DataBaseConnInfo dataBaseConnInfo) throws Exception {
        String dbType = dataBaseConnInfo.getDbType();
        MySqlConnInfo mySqlConnInfo = null;
        if (DataBaseConstant.MYSQL.equals(dbType)){
            mySqlConnInfo = (MySqlConnInfo) dataBaseConnInfo;
            Class.forName(mySqlConnInfo.getDbDriver());
        }
        return DriverManager.getConnection(mySqlConnInfo.getUrl(), mySqlConnInfo.getUsername(), mySqlConnInfo.getPassword());
    }

}
