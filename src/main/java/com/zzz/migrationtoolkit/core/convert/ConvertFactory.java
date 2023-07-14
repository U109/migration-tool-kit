package com.zzz.migrationtoolkit.core.convert;

import com.zzz.migrationtoolkit.common.utils.ReflectUtil;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:44
 * @description:
 */
public class ConvertFactory {

    public static ISourceDataTypeConvert getSourceInstance(String sourceDBType, String targetDBType) {
        ISourceDataTypeConvert convert = null;
        String className = "com.zzz.migrationtoolkit.core.convert." + sourceDBType + "To" + targetDBType + "DataTypeConvert";
        Object obj = ReflectUtil.getObjByClass(className);
        if (obj instanceof ISourceDataTypeConvert) {
            convert = (ISourceDataTypeConvert) obj;
        }
        return convert;

    }
}
