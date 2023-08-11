package com.zzz.migration.database.executor;

import com.zzz.migration.entity.databaseElementEntity.ColumnEntity;
import com.zzz.migration.entity.migrationObjEntity.TableColumn;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:19
 * @description:
 */
public interface IDataBaseExecutor extends AutoCloseable{

    List<ColumnEntity> getColumnEntityList(String tableName);

    void executeSQL(String executeSQL) throws Exception;

    void closeExecutor();

    long getTableDataCount(String countSql);

    ResultSet getResultSet(String sql, int fetchSize);

    List<Object> convertReadDataTypes(List<TableColumn> tableColumnList, ResultSet rs);

    long executeInsertSql(String insertSql, List<List<Object>> dataList, List<TableColumn> columnList) throws Exception;
}
