package com.zzz.migrationtoolkit.entity.taskEntity;

/**
 * @author: Zzz
 * @date: 2023/7/5 10:14
 * @description:
 */
public class ProcessWorkResultEntity {

    private boolean isNormalFinished = true;

    private String resultMsg = "";

    public ProcessWorkResultEntity() {
    }

    public ProcessWorkResultEntity(boolean isNormalFinished, String resultMsg) {
        this.isNormalFinished = isNormalFinished;
        this.resultMsg = resultMsg;
    }

    public boolean isNormalFinished() {
        return isNormalFinished;
    }

    public void setNormalFinished(boolean normalFinished) {
        isNormalFinished = normalFinished;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
