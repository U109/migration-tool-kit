package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataType;
import lombok.Data;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:30
 * @description:
 */
@Data
public class ColumnEntity {

    private String columnId;
    private String columnName;
    private DataType columnType;
    private boolean isAutoIncrement = false;

}
