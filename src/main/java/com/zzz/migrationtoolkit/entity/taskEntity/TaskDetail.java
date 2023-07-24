package com.zzz.migrationtoolkit.entity.taskEntity;

import com.zzz.migrationtoolkit.common.constants.CommonConstant;
import com.zzz.migrationtoolkit.common.constants.MigrationConstant;
import com.zzz.migrationtoolkit.common.constants.TaskResultConstant;
import com.zzz.migrationtoolkit.common.constants.TaskStatusConstant;
import com.zzz.migrationtoolkit.entity.dataTypeEntity.DataTypeMapping;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationDBConnEntity;
import com.zzz.migrationtoolkit.entity.migrationObjEntity.MigrationObj;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Zzz
 * @date: 2023/7/4 16:18
 * @description: 迁移任务实体类
 */
@Data
public class TaskDetail implements Serializable, Cloneable {

    //任务id
    private String taskId;
    //任务名称
    private String taskName;
    private String taskDesc;
    //源数据库对象
    private MigrationDBConnEntity sourceDataBase;
    //目标数据库对象
    private MigrationDBConnEntity targetDataBase;
    private Date startTime;
    private Date endTime;
    private String taskStatus;
    private String failMsg;
    private Map<String, MigrationObj> tableDetailMap;
    /**
     * 存储迁移对象的类型
     */
    private List<String> migrationObjTypeList = new ArrayList<>();

    private boolean reCreateFlag = false;
    /**
     * 源库迁移对象个数
     */
    private int sourceMigrationObjCount;

    private int destMigrationObjCount;
    /**
     * 任务中数据对应类型
     */
    private DataTypeMapping dataTypeMapping;

    private CoreConfig coreConfig;

    private Long totalCount = 0L;
    /**
     * 已完成的数据量
     */
    private Long finishDataCount;

    private MigrationObj migrationObj;

    private String taskResult;

    /**
     * 这里标识迁移类型:表结构和表数据
     * 只迁移表结构
     * 只迁移表数据
     * 两者都进行迁移（默认）
     */
    private String migrationTableType = MigrationConstant.MIGRATION_DATA;

    private boolean rebuildTable = true;

    public TaskDetail() {
        this.taskId = String.valueOf(UUID.randomUUID());
        //默认迁移表
        this.migrationObjTypeList.add(CommonConstant.MIGRATION_OBJ_TABLE);
    }

    public void resetStatus() {
        this.setStartTime(null);
        this.setEndTime(null);
        setFailMsg("");
        setTaskResult(TaskResultConstant.STOP_SUCCESS);
        setTaskStatus(TaskStatusConstant.TASK_NO_START);
        finishDataCount = 0L;
        totalCount = 0L;
        List<Map<String, MigrationObj>> detailMapList = new ArrayList<>();
        detailMapList.add(this.getTableDetailMap());

        for (Map<String, MigrationObj> objMap : detailMapList) {
            if (objMap != null) {
                for (MigrationObj obj : objMap.values()) {
                    obj.resetStatus();
                }
            }
        }
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

    public Map<String, MigrationObj> getTableDetailMap() {
        if (tableDetailMap == null) {
            tableDetailMap = new HashMap<>();
        }
        return tableDetailMap;
    }

    public void updateTaskDetail(String taskStatus, String taskResult, Integer deskMigrationObjCount) {
        if (taskStatus != null) {
            this.setTaskStatus(taskStatus);
        }
        if (taskResult != null) {
            this.setTaskResult(taskResult);
        }
        if (deskMigrationObjCount != null) {
            this.setDestMigrationObjCount(deskMigrationObjCount);
        }
    }

    @Override
    public TaskDetail clone() {
        try {
            TaskDetail clone = (TaskDetail) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
