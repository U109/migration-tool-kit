package com.zzz.migrationtoolkit.server;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.constants.FilePathContent;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.MySqlConnInfo;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataTypeMapping;
import com.zzz.migrationtoolkit.entity.taskEntity.CoreConfig;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;
import com.zzz.migrationtoolkit.handler.dataTypeHandler.DataTypeMappingProcess;

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

    public static Map<String, Map<String, DataBaseConnInfo>> DBConnectionMap;
    public static CoreConfig coreConfig = new CoreConfig();
    public static Map<String, List<DataType>> DataType;
    public static Map<String, DataTypeMapping> SourceDataTypeMapping;
    public static Map<String, DataTypeMapping> UserDataTypeMapping;

    public static void initContext() {
        DataSourceProcess.initDBConnections();
        DataTypeMappingProcess.initDataTypeMapping();
        initCoreConfig();
    }

    private static void initCoreConfig() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(FilePathContent.CORE_CONFIG_PATH));
            coreConfig.setReadDataThreadSize(Integer.parseInt((String) properties.get("readDataThreadSize")));
            coreConfig.setWriteDataThreadSize(Integer.parseInt((String) properties.get("writeDataThreadSize")));
            coreConfig.setWriteDataCommitSize(Integer.parseInt((String) properties.get("writeDataCommitSize")));
            coreConfig.setFetchDataSize(Integer.parseInt((String) properties.get("fetchDataSize")));
            coreConfig.setWorkQueueSize(Integer.parseInt((String) properties.get("workQueueSize")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataBaseConnInfo getDataBaseInfoFromName(String typename, String dataBaseName) {
        if (DBConnectionMap.containsKey(typename)) {
            Map<String, DataBaseConnInfo> dataBaseConnInfoMap = DBConnectionMap.get(typename);
            return dataBaseConnInfoMap.get("aaa");
        }
        return null;
    }

}
