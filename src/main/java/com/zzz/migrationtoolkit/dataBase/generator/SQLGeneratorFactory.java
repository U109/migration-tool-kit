package com.zzz.migrationtoolkit.dataBase.generator;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.dataBase.generator.impl.MySqlSQLGenerator;
import com.zzz.migrationtoolkit.dataBase.generator.impl.OracleSQLGenerator;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:58
 * @description:
 */
public class SQLGeneratorFactory {

    public static ISQLGenerator newDestInstance(TaskDetail taskDetail) {
        String dbType = taskDetail.getTargetDataBase().getDbci().getDbType();
        ISQLGenerator sqlGenerator = null;
        if (dbType.equals(DataBaseConstant.MYSQL)) {
            sqlGenerator = new MySqlSQLGenerator();
        } else if (dbType.equals(DataBaseConstant.ORACLE)) {
            sqlGenerator = new OracleSQLGenerator();
        }
        return sqlGenerator;
    }

    public static ISQLGenerator newSourceInstance(TaskDetail taskDetail) {
        String dbType = taskDetail.getSourceDataBase().getDbci().getDbType();
        ISQLGenerator sqlGenerator = null;
        if (dbType.equals(DataBaseConstant.MYSQL)) {
            sqlGenerator = new MySqlSQLGenerator();
        } else if (dbType.equals(DataBaseConstant.ORACLE)) {
            sqlGenerator = new OracleSQLGenerator();
        }
        return sqlGenerator;
    }



}
