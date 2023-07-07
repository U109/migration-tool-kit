package com.zzz.migrationtoolkit.server;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;

import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:08
 * @description:
 */
public class InitContext {

    public static Map<String, DataBaseConnInfo> DBConnectionMap;

    public static void initContext() {
        DataSourceProcess.initDBConnections();
    }

    public static void main(String[] args) {
        InitContext.initContext();
        DataBaseConnInfo dataBaseConnInfo = DBConnectionMap.get(DataBaseConstant.MYSQL);
        MySqlConnInfo mySqlConnInfo = (MySqlConnInfo) dataBaseConnInfo;
        System.out.println(mySqlConnInfo.getDbDriver());
    }
}
