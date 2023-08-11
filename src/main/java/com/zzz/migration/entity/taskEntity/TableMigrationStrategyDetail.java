package com.zzz.migration.entity.taskEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/17 10:03
 * @description:
 */
@Data
public class TableMigrationStrategyDetail implements Serializable {
    private String tableName;
    private boolean migrationType;
}
