package com.zzz.migrationtoolkit.entity.dataTypeEntity;

import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:32
 * @description:
 */
@Data
public class DataType {

    //数据类型的值
    private String dataTypeValue;
    //长度
    private long columnLength;
    //精度
    private Integer columnPrecision;

}
