package com.zzz.migration.entity.databaseElementEntity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:27
 * @description: 数据量表实体
 */
@Data
public class TableEntity implements Serializable {

    private String tableId;
    private String tableName;
    private long dataCount;
    private List<ColumnEntity> columnList;
}
