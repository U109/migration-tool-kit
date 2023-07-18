package com.zzz.migrationtoolkit.handler.dataTypeHandler;

import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataTypeMapping;
import com.zzz.migrationtoolkit.server.InitContext;

import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/18 18:09
 * @description:
 */
public class DataTypeMappingProcess {
    
    public static void initDataTypeMapping(){
        InitContext.DataTypeMapping = readSourceDataTypeMapping();
    }

    private static Map<String, DataTypeMapping> readSourceDataTypeMapping() {
        return null;
    }
}
