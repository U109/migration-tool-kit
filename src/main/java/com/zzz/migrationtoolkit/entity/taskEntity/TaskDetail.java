package com.zzz.migrationtoolkit.entity.taskEntity;

import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:18
 * @description: 迁移任务实体类
 */
public class TaskDetail implements Serializable {

    //任务id
    private String taskId;

    //任务名称
    private String taskName;

    private Date startTime;
    private Date endTime;
    private String taskStatus;
    private String failMsg;
    private Map<String, MigrationObj> tableDetailMap;
    //存储迁移对象的类型
    private List<String> migrationObjTypeList = new ArrayList<>();


    public List<String> getMigrationObjTypeList() {
        return migrationObjTypeList;
    }

    public void setMigrationObjTypeList(List<String> migrationObjTypeList) {
        this.migrationObjTypeList = migrationObjTypeList;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }

    public void appendFailMsg(String failMsg) {
        String oldFailMsg = getFailMsg();
        if (failMsg != null && !"".equals(failMsg)) {
            if (!"".equals(oldFailMsg)) {
                oldFailMsg += "\n";
            }
            setFailMsg(oldFailMsg + failMsg);
        }
    }

    @Override
    public String toString() {
        return " [ TaskName : " + getTaskName() + " ] ";
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Map<String, MigrationObj> getTableDetailMap() {
        if (tableDetailMap == null) {
            tableDetailMap = new HashMap<>();
        }
        return tableDetailMap;
    }

    public void setTableDetailMap(Map<String, MigrationObj> tableDetailMap) {
        this.tableDetailMap = tableDetailMap;
    }
}
