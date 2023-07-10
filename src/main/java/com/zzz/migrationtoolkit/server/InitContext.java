package com.zzz.migrationtoolkit.server;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.constants.FilePathContent;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.entity.taskEntity.CoreConfig;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: Zzz
 * @date: 2023/7/4 14:08
 * @description:
 */
public class InitContext {

    public static Map<String, List<Map<String, DataBaseConnInfo>>> DBConnectionMap;
    public static CoreConfig coreConfig = new CoreConfig();

    public static void initContext() {
        DataSourceProcess.initDBConnections();
        initCoreConfig();
    }

    private static void initCoreConfig() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(FilePathContent.CORE_CONFIG_PATH));
            coreConfig.setReadDataThreadSize((int) properties.get("readDataThreadSize"));
            coreConfig.setWriteDataThreadSize((int) properties.get("writeDataThreadSize"));
            coreConfig.setWriteDataCommitSize((int) properties.get("writeDataCommitSize"));
            coreConfig.setFetchDataSize((int) properties.get("fetchDataSize"));
            coreConfig.setWorkQueueSize((int) properties.get("workQueueSize"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
