package com.zzz.migrationtoolkit.entity.dataTypeEntity;

import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/10 11:24
 * @description: 源端与目标端类型映射关系
 */
@Data
public class DataTypeMapped {

    private DataType sourceDataType;
    private DataType destDataType;
    private String length; //长度
    private String precision; //精度

}
