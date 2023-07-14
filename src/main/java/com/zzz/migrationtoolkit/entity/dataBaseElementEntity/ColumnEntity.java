package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:30
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnEntity {

    private String columnId;
    private String columnName;
    private DataType columnType;
    private boolean isAutoIncrement = false;

    public ColumnEntity(String columnName, DataType dataType) {
        this.columnName = columnName;
        this.columnType = dataType;
    }
}
