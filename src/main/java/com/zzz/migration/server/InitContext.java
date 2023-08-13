package com.zzz.migration.server;

import com.zzz.migration.common.constants.FilePathContent;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.dataTypeEntity.DataType;
import com.zzz.migration.entity.dataTypeEntity.DataTypeMapping;
import com.zzz.migration.entity.taskEntity.CoreConfig;
import com.zzz.migration.handler.databaseHandler.DataSourceProcess;
import com.zzz.migration.handler.dataTypeHandler.DataTypeMappingProcess;

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

    public static Map<String, Map<String, DataSourceProperties>> DataSourceMap;
    public static CoreConfig coreConfig = new CoreConfig();
    public static Map<String, List<DataType>> DataType;
    public static Map<String, DataTypeMapping> SourceDataTypeMapping;
    public static Map<String, DataTypeMapping> UserDataTypeMapping;

    public static void initContext() {
        DataSourceMap = DataSourceProcess.readDataSourceFromXml();
        //初始化源数据类型对应表
        SourceDataTypeMapping = DataTypeMappingProcess.readSourceDataTypeMapping();
        //初始化用户定制数据库对应类型
        UserDataTypeMapping = DataTypeMappingProcess.readSourceDataTypeMapping();
        //初始化指定数据库所有数据类型
        DataType = DataTypeMappingProcess.readDataType();
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

}
