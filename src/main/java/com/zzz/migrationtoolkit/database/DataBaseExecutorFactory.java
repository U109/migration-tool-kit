package com.zzz.migrationtoolkit.database;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.database.executor.AbstractDataBaseExecutor;
import com.zzz.migrationtoolkit.database.executor.MySqlDataBaseExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author zhangzhongzhen
 * 数据库执行器工厂
 */
public final class DataBaseExecutorFactory {

  private static final Map<String, Callable<AbstractDataBaseExecutor>> DATABASE_EXECUTOR_MAPPER  = new HashMap<>() {
    {
      put(DataBaseConstant.MYSQL, MySqlDataBaseExecutor::new);
    }
  };

  public static AbstractDataBaseExecutor getDatabaseInstance(String type) {
    Callable<AbstractDataBaseExecutor> callable = DATABASE_EXECUTOR_MAPPER.get(type);
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

  private DataBaseExecutorFactory() {
    throw new IllegalStateException();
  }

}
