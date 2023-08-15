package com.zzz.migration.entity.dataSourceEmtity;

import javax.sql.DataSource;
import java.io.Closeable;

/**
 * @author 张忠振
 */
public interface CloseableDataSource extends DataSource, Closeable{
}
