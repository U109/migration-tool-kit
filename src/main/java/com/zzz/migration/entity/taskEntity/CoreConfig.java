package com.zzz.migration.entity.taskEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/10 13:38
 * @description:
 */
@Data
public class CoreConfig implements Serializable {

    private int readDataThreadSize = 1;
    private int writeDataThreadSize = 1;
    //一次提交数据量
    private int writeDataCommitSize = 1000;
    private int fetchDataSize = 1000;
    private int workQueueSize = 10;


}
