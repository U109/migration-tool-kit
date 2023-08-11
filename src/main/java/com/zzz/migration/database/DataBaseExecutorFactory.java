package com.zzz.migration.database;

import com.zzz.migration.common.enums.ProductTypeEnum;
import com.zzz.migration.common.utils.DataSourceUtil;
import com.zzz.migration.common.utils.DatabaseAwareUtils;
import com.zzz.migration.database.executor.AbstractDataBaseExecutor;
import com.zzz.migration.database.executor.MySqlDataBaseExecutor;
import com.zzz.migration.entity.dataSourceEmtity.CloseableDataSource;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;
import com.zzz.migration.entity.dataSourceEmtity.WrapHikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author zhangzhongzhen
 * 数据库执行器工厂
 */
public final class DataBaseExecutorFactory {

    private static final Map<ProductTypeEnum, Function<WrapHikariDataSource, AbstractDataBaseExecutor>> DATABASE_EXECUTOR_MAPPER = new HashMap<>() {
        private static final long serialVersionUID = -5278835613240515265L;

        {
            put(ProductTypeEnum.MYSQL, MySqlDataBaseExecutor::new);
        }
    };

    public static AbstractDataBaseExecutor getDatabaseInstance(WrapHikariDataSource dataSource) {
        ProductTypeEnum type = DatabaseAwareUtils.getDatabaseTypeByDataSource(dataSource);
        if (!DATABASE_EXECUTOR_MAPPER.containsKey(type)) {
            throw new RuntimeException(String.format("Unsupported database type (%s)", type));
        }
        return DATABASE_EXECUTOR_MAPPER.get(type).apply(dataSource);
    }

    public static AbstractDataBaseExecutor getDatabaseInstance(DataSourceProperties properties) {
        return getDatabaseInstance(DataSourceUtil.createDataSource(properties));
    }

    private DataBaseExecutorFactory() {
        throw new IllegalStateException();
    }

}
