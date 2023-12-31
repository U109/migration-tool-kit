package com.zzz.migration.entity.dataTypeEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: Zzz
 * @date: 2023/7/10 11:22
 * @description: 从源库到目标库中数据的匹配类型
 */
@Data
public class DataTypeMapping implements Serializable {

    private String mappingId;
    private String mappingName;
    //类型对应表中具体类型的对应信息
    private Map<String, DataTypeMapped> dataTypeMapped;

    private String sourceDBName;
    private String destDBName;


}
