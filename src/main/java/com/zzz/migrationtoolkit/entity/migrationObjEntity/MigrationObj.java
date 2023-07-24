package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:23
 * @description: 迁移对象基类
 */
@Data
public class MigrationObj implements Serializable {
    String objId;
    String objName;
    String objType;
    String resultMsg = "";
    String migrationResult = MigrationConstant.MIGRATION_NO_RESULT;
    boolean isFinish = false;

    Date startTime = MigrationConstant.TASK_DEFAULT_DATE;
    Date endTime = MigrationConstant.TASK_DEFAULT_DATE;

    public void appendResultMsg(String resultMsg) {
        if (resultMsg == null){
            resultMsg = "";
        }
        if (this.resultMsg != null && this.resultMsg.contains(resultMsg)) {
            return;
        }
        if (this.resultMsg != null && !"".equals(this.resultMsg)) {
            this.resultMsg += "\n";
        }
    }

    public void resetStatus() {
        setMigrationResult(MigrationConstant.MIGRATION_NO_RESULT);
        setResultMsg("");
        isFinish = false;
    }
}
