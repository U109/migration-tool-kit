package com.zzz.migration.database;

import com.zzz.migration.common.enums.ProductTypeEnum;
import com.zzz.migration.common.utils.DatabaseAwareUtils;
import com.zzz.migration.database.generator.impl.AbstractSQLGenerator;
import com.zzz.migration.database.generator.impl.MySqlSQLGenerator;
import com.zzz.migration.entity.dataSourceEmtity.DataSourceProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author zhangzhongzhen
 * 数据库Sql生成器工厂
 */
public final class SQLGeneratorFactory {

    private static final Map<ProductTypeEnum, Callable<AbstractSQLGenerator>> DATABASE_GENERATOR_MAPPER = new HashMap<>() {
        {
            put(ProductTypeEnum.MYSQL, MySqlSQLGenerator::new);
        }
    };

    public static AbstractSQLGenerator getDatabaseInstance(DataSourceProperties properties) {
        String dbType = properties.getDbType();
        Callable<AbstractSQLGenerator> callable = DATABASE_GENERATOR_MAPPER.get(DatabaseAwareUtils.getDatabaseTypeByString(dbType));
        if (null != callable) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new UnsupportedOperationException(
                String.format("Unknown database type (%s)", dbType));
    }

    private SQLGeneratorFactory() {
        throw new IllegalStateException();
    }

}
