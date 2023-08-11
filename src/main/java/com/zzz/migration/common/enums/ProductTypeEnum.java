package com.zzz.migration.common.enums;

import java.util.Arrays;

/**
 * 数据库产品类型的枚举定义
 * @author zhangzhongzhen
 */
public enum ProductTypeEnum {
  /**
   * 未知数据库类型
   */
  UNKNOWN(0),

  /**
   * MySQL数据库类型
   */
  MYSQL(1),

  /**
   * Oracle数据库类型
   */
  ORACLE(2);
  private final int index;

  ProductTypeEnum(int idx) {
    this.index = idx;
  }

  public int getIndex() {
    return index;
  }


}
