package com.zzz.migrationtoolkit.entity.dataSourceEmtity;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author 张忠振
 */
@Slf4j
public class WrapHikariDataSource implements CloseableDataSource {

  private final HikariDataSource hikariDataSource;
  private final URLClassLoader urlClassLoader;

  public WrapHikariDataSource(HikariDataSource hikariDataSource, URLClassLoader urlClassLoader) {
    this.hikariDataSource = Objects.requireNonNull(hikariDataSource);
    this.urlClassLoader = Objects.requireNonNull(urlClassLoader);
  }

  @Override
  public Connection getConnection() throws SQLException {
    return hikariDataSource.getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return hikariDataSource.getConnection(username, password);
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return hikariDataSource.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return hikariDataSource.isWrapperFor(iface);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return hikariDataSource.getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    hikariDataSource.setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    hikariDataSource.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return hikariDataSource.getLoginTimeout();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return hikariDataSource.getParentLogger();
  }

  @Override
  public void close() {
    hikariDataSource.close();
    try {
      urlClassLoader.close();
    } catch (IOException e) {
      log.warn(e.getMessage(), e);
    }
  }
}
