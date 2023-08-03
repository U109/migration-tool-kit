package com.zzz.migrationtoolkit.entity.taskEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/5 10:14
 * @description:
 */
@Data
public class WorkResultEntity implements Serializable {

    private boolean isNormalFinished = true;

    private String resultMsg = "";

    public WorkResultEntity() {
    }

    public WorkResultEntity(boolean isNormalFinished, String resultMsg) {
        this.isNormalFinished = isNormalFinished;
        this.resultMsg = resultMsg;
    }

}
