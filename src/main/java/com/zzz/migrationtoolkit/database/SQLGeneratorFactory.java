package com.zzz.migrationtoolkit.database;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.database.generator.impl.AbstractSQLGenerator;
import com.zzz.migrationtoolkit.database.generator.impl.MySqlSQLGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author zhangzhongzhen
 * 数据库Sql生成器工厂
 */
public final class SQLGeneratorFactory {

    private static final Map<String, Callable<AbstractSQLGenerator>> DATABASE_GENERATOR_MAPPER = new HashMap<>() {
        {
            put(DataBaseConstant.MYSQL, MySqlSQLGenerator::new);
        }
    };

    public static AbstractSQLGenerator getDatabaseInstance(String type) {
        Callable<AbstractSQLGenerator> callable = DATABASE_GENERATOR_MAPPER.get(type);
        if (null != callable) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new UnsupportedOperationException(
                String.format("Unknown database type (%s)", type));
    }

    private SQLGeneratorFactory() {
        throw new IllegalStateException();
    }

}
