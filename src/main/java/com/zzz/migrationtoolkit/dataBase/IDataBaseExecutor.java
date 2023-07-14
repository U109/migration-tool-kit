package com.zzz.migrationtoolkit.dataBase;

import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;

import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:19
 * @description:
 */
public interface IDataBaseExecutor extends AutoCloseable{

    List<ColumnEntity> getColumnEntityList(String tableName);

    void executeSQL(String executeSQL) throws Exception;

}
