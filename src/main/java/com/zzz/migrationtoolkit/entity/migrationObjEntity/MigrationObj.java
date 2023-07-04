package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:23
 * @description: 迁移对象基类
 */
public class MigrationObj implements Serializable {
    String objId;
    String objName;
    String objType;
    boolean isFinish = false;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
