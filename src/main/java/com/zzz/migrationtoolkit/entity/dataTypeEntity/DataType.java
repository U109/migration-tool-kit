package com.zzz.migrationtoolkit.entity.dataTypeEntity;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:32
 * @description:
 */
public class DataType {
    //数据类型的值
    private String dataTypeValue;
    //长度
    private long columnLength;
    //精度
    private Integer columnPrecision;

    public String getDataTypeValue() {
        return dataTypeValue;
    }

    public void setDataTypeValue(String dataTypeValue) {
        this.dataTypeValue = dataTypeValue;
    }

    public long getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(long columnLength) {
        this.columnLength = columnLength;
    }

    public Integer getColumnPrecision() {
        return columnPrecision;
    }

    public void setColumnPrecision(Integer columnPrecision) {
        this.columnPrecision = columnPrecision;
    }
}
