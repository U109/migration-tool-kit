package com.zzz.migration.common.utils;

import com.zzz.migration.common.constants.DataBaseConstant;
import com.zzz.migration.common.enums.ProductTypeEnum;
import com.zzz.migration.entity.dataSourceEmtity.CloseableDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型识别工具类
 *
 * @author zhangzhongzhen
 */
public final class DatabaseAwareUtils {

    private static final Map<String, ProductTypeEnum> PRODUCT_NAME_MAP;


    static {
        PRODUCT_NAME_MAP = new HashMap<>();
        PRODUCT_NAME_MAP.put(DataBaseConstant.MYSQL, ProductTypeEnum.MYSQL);
        PRODUCT_NAME_MAP.put(DataBaseConstant.ORACLE, ProductTypeEnum.ORACLE);

    }

    /**
     * 获取数据库的产品名
     *
     * @param dataSource 数据源
     * @return 数据库产品名称字符串
     */
    public static ProductTypeEnum getDatabaseTypeByDataSource(CloseableDataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            String productName = connection.getMetaData().getDatabaseProductName();
            ProductTypeEnum type = PRODUCT_NAME_MAP.get(productName);
            if (null == type) {
                throw new IllegalStateException("Unable to detect database type from data source instance");
            }
            return type;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ProductTypeEnum getDatabaseTypeByString(String dbType) {
        ProductTypeEnum type = PRODUCT_NAME_MAP.get(dbType);
        if (null == type) {
            throw new IllegalStateException("Unable to query database type from string");
        }
        return type;
    }

    private DatabaseAwareUtils() {
    }
}
