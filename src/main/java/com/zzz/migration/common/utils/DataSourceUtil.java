package com.zzz.migration.common.utils;

import com.zaxxer.hikari.HikariDataSource;
import com.zzz.migration.entity.dataSourceEmtity.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Properties;
/**
 * DataSource工具类
 *
 * @author tang
 */
@Slf4j
public final class DataSourceUtil {

  public static final int MAX_THREAD_COUNT = 10;
  public static final int MAX_TIMEOUT_MS = 60000;

  /**
   * 创建于指定数据库连接描述符的连接池
   *
   * @param properties 数据库连接描述符
   * @return HikariDataSource连接池
   */
  public static WrapHikariDataSource createDataSource(DataSourceProperties properties) {
    Properties parameters = new Properties();
    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setPoolName(properties.getDbType() + "_Source_DB_Connection");
    hikariDataSource.setJdbcUrl(properties.getUrl());
    if (properties.getDriverClassName().contains("oracle")) {
      hikariDataSource.setConnectionTestQuery("SELECT 'Hello' from DUAL");
      // https://blog.csdn.net/qq_20960159/article/details/78593936
      System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
      // Oracle在通过jdbc连接的时候需要添加一个参数来设置是否获取注释
      parameters.put("remarksReporting", "true");
    } else if (properties.getDriverClassName().contains("db2")) {
      hikariDataSource.setConnectionTestQuery("SELECT 1 FROM SYSIBM.SYSDUMMY1");
    } else {
      hikariDataSource.setConnectionTestQuery("SELECT 1");
    }
    hikariDataSource.setMaximumPoolSize(MAX_THREAD_COUNT);
    hikariDataSource.setMinimumIdle(MAX_THREAD_COUNT);
    hikariDataSource.setMaxLifetime(properties.getMaxLifeTime());
    hikariDataSource.setConnectionTimeout(properties.getConnectionTimeout());
    hikariDataSource.setIdleTimeout(MAX_TIMEOUT_MS);

    hikariDataSource.setJdbcUrl(properties.getUrl());
    hikariDataSource.setDriverClassName(properties.getDriverClassName());
    hikariDataSource.setUsername(properties.getUsername());
    hikariDataSource.setPassword(properties.getPassword());
    // 设置连接时区
//    hikariDataSource.addDataSourceProperty("serverTimezone", "时区名称");
    // 设置字符集
//    hikariDataSource.addDataSourceProperty("characterEncoding", "字符集名称");

    return new WrapHikariDataSource(hikariDataSource,properties);
  }

  private DataSourceUtil() {
    throw new IllegalStateException("Illegal State");
  }

}
