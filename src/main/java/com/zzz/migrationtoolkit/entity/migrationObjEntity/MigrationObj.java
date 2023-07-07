package com.zzz.migrationtoolkit.entity.migrationObjEntity;

import lombok.Data;

import java.io.Serializable;

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
    boolean isFinish = false;

}
