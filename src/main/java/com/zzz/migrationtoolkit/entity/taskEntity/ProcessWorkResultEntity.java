package com.zzz.migrationtoolkit.entity.taskEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/5 10:14
 * @description:
 */
@Data
public class ProcessWorkResultEntity implements Serializable {

    private boolean isNormalFinished = true;

    private String resultMsg = "";

    public ProcessWorkResultEntity() {
    }

    public ProcessWorkResultEntity(boolean isNormalFinished, String resultMsg) {
        this.isNormalFinished = isNormalFinished;
        this.resultMsg = resultMsg;
    }

}
