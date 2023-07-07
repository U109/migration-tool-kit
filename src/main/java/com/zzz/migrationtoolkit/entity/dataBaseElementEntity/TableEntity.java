package com.zzz.migrationtoolkit.entity.dataBaseElementEntity;

import lombok.Data;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:27
 * @description: 数据量表实体
 */
@Data
public class TableEntity {

    private String tableId;
    private String tableName;
    private long dataCount;
    private List<ColumnEntity> columnList;
}
