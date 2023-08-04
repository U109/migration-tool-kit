package com.zzz.migrationtoolkit.dataBase.convert;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationColumn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:46
 * @description:
 */
public interface ISourceDataTypeConvert {
    List<Object> convertReadDataTypes(List<MigrationColumn> columnList, ResultSet rs);

    void convertWriteDataTypes(String dataTypeName, PreparedStatement preparedStatement, int parameterIndex, List<Object> dataRow, int j) throws SQLException;
}
