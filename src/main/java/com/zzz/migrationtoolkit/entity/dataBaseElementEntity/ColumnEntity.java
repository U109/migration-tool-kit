package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:30
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnEntity implements Cloneable, Serializable {

    private String columnId;
    private String columnName;
    private DataType columnType;
    private boolean isAutoIncrement = false;

    public ColumnEntity(String columnName, DataType dataType) {
        this.columnName = columnName;
        this.columnType = dataType;
    }

    @Override
    public ColumnEntity clone() {
        try {
            return (ColumnEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
