package com.zzz.migrationtoolkit.dataBase;

import com.zzz.migrationtoolkit.common.utils.ReflectUtil;
import com.zzz.migrationtoolkit.dataBase.convert.ConvertFactory;
import com.zzz.migrationtoolkit.entity.taskEntity.TaskDetail;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:23
 * @description:
 */
public class DataBaseExecutorFactory {

    private static IDataBaseExecutor executor = null;


    public static IDataBaseExecutor getSourceInstance(TaskDetail taskDetail) {
        String className = "com.zzz.migrationtoolkit.dataBase.dbExecutor." +
                taskDetail.getSourceDataBase().getProperties().getDbType() + "DataBaseExecutor";

        Object obj = ReflectUtil.getObjByClass(className, taskDetail.getSourceDataBase().getDbci(),
                ConvertFactory.getSourceInstance(taskDetail.getSourceDataBase().getDbci().getDbType(),
                        taskDetail.getTargetDataBase().getDatabaseType()));
        if (obj instanceof IDataBaseExecutor) {
            executor = (IDataBaseExecutor) obj;
        } else {
            executor = null;
        }
        return executor;
    }

    public static IDataBaseExecutor getDestInstance(TaskDetail taskDetail) {
        String className = "com.zzz.migrationtoolkit.dataBase.dbExecutor." +
                taskDetail.getTargetDataBase().getDbci().getDbType() + "DataBaseExecutor";

        Object obj = ReflectUtil.getObjByClass(className, taskDetail.getTargetDataBase().getDbci(),
                ConvertFactory.getSourceInstance(taskDetail.getTargetDataBase().getDbci().getDbType(),
                        taskDetail.getTargetDataBase().getDatabaseType()));
        if (obj instanceof IDataBaseExecutor) {
            executor = (IDataBaseExecutor) obj;
        } else {
            executor = null;
        }
        return executor;
    }
}
