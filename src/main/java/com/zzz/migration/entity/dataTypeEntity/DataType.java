package com.zzz.migration.entity.dataTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:32
 * @description:
 */
@Data
@AllArgsConstructor
public class DataType implements Serializable {

    private String dataTypeName;
    //数据类型的值
    private String dataTypeValue;
    //长度
    private long columnLength;
    //精度
    private Integer columnPrecision;

    public DataType() {
    }

    public DataType(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public DataType(String dataTypeName, long columnLength, int columnPosition) {
        this.dataTypeName = dataTypeName;
        this.columnLength = columnLength;
        this.columnPrecision = columnPosition;
    }
}

