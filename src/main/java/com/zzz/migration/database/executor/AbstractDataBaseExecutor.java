package com.zzz.migration.database.executor;

import com.zzz.migration.common.utils.CloseObjUtil;
import com.zzz.migration.entity.dataSourceEmtity.CloseableDataSource;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.dataSourceEmtity.WrapHikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:11
 * @description:
 */
@Slf4j
public abstract class AbstractDataBaseExecutor implements IDataBaseExecutor {

    public WrapHikariDataSource dataSource;
    public DataSourceProperties properties;

    public AbstractDataBaseExecutor(WrapHikariDataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "数据源非法,为null");
        this.properties = dataSource.getProperties();
    }

    @Override
    public long getTableDataCount(String countSql) {
        long result = -1L;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(countSql);
            while (resultSet.next()) {
                result = resultSet.getLong(1);
            }
        } catch (Exception e) {
        } finally {
            CloseObjUtil.closeAll(statement, resultSet,connection);
        }
        return result;
    }

    @Override
    public void closeExecutor() {
        CloseObjUtil.closeAll(dataSource);
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
           throw new IllegalArgumentException("数据源非法,为null");
        }
        return dataSource.getConnection();
    }

}
