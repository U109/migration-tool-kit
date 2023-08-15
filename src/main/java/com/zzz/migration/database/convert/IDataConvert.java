package com.zzz.migration.database.convert;

import com.zzz.migration.entity.migrationObjEntity.TableColumn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:46
 * @description:
 */
public interface IDataConvert {
    List<Object> convertReadDataTypes(List<TableColumn> columnList, ResultSet rs);

    void convertWriteDataTypes(String dataTypeName, PreparedStatement preparedStatement, int parameterIndex, List<Object> dataRow, int j) throws SQLException;
}
