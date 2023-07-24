package com.zzz.migrationtoolkit.dataBase.dbExecutor;

import com.zzz.migrationtoolkit.common.utils.CloseObjUtil;
import com.zzz.migrationtoolkit.dataBase.ConnectionManager;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:11
 * @description:
 */
@Slf4j
public abstract class AbstractDataBaseExecutor implements IDataBaseExecutor {

    public DataBaseConnInfo dataBaseConnInfo;

    public Connection connection;

    public AbstractDataBaseExecutor() {

    }

    public AbstractDataBaseExecutor(DataBaseConnInfo dbci) {
        this.dataBaseConnInfo = dbci;
        try {
            this.connection = ConnectionManager.getConnection(dataBaseConnInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            connection = ConnectionManager.getConnection(dataBaseConnInfo);
        }
        return connection;
    }

    @Override
    public long getTableDataCount(String countSql) {
        long result = -1L;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(countSql);
            while (resultSet.next()) {
                result = resultSet.getLong(1);
            }
        } catch (Exception e) {
        } finally {
            CloseObjUtil.closeAll(statement, resultSet);
        }
        return result;
    }

    @Override
    public void closeExecutor() {
        CloseObjUtil.closeAll(connection);
    }
}
