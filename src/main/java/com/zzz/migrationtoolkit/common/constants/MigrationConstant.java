package com.zzz.migrationtoolkit.common.constants;

import java.util.Date;

/**
 * @author: Zzz
 * @date: 2023/7/24 14:08
 * @description:
 */
public class MigrationConstant {

    public static final String MIGRATION_ONLY_METADATA = "meta-data";
    public static final String MIGRATION_ONLY_USERDATA = "user-data";
    public static final String MIGRATION_DATA = "meta-data+user-data";

    public static final Date TASK_DEFAULT_DATE = new Date(00000L);
    public static final String MIGRATION_NO_RESULT = "no-result";
    public static final String MIGRATION_RESULT_FAIL = "result-fail";
    public static final String MIGRATION_RESULT_SUCCESS = "result-success";
}
