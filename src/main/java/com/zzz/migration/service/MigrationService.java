package com.zzz.migration.service;

import com.zzz.migration.common.vo.ResultMessage;
import com.zzz.migration.core.scheduler.TaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangzhongzhen wrote on 2023/8/12
 * @version 1.0
 */
@Slf4j
@Service
public class MigrationService {

    @Resource
    private TaskScheduler taskScheduler;

    public ResultMessage<String> startMigration(String taskId) {
        log.info("start migration taskId:{}", taskId);
        taskScheduler.startTask(taskId);
        return new ResultMessage<String>().success("任务启动成功");
    }

}
