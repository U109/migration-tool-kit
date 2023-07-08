package com.zzz.migrationtoolkit.server;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;

import java.util.List;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:08
 * @description:
 */
public class InitContext {

    public static Map<String, List<Map<String, DataBaseConnInfo>>> DBConnectionMap;

    public static void initContext() {
        DataSourceProcess.initDBConnections();
    }

}
