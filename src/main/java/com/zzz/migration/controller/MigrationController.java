package com.zzz.migration.controller;

import com.zzz.migration.common.vo.ResultMessage;
import com.zzz.migration.config.SwaggerConfig;
import com.zzz.migration.service.MigrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangzhongzhen wrote on 2023/8/11
 * @version 1.0
 */
@Api(tags = {"迁移管理接口"})
@RestController
@RequestMapping(value = SwaggerConfig.API_PREFIX_MIGRATION)
public class MigrationController {

    @Resource
    private MigrationService migrationService;
    @ApiOperation(value = "根据任务ID进行启动任务")
    @PostMapping(value = "/startMigration/{taskId}")
    public ResultMessage<String> startMigration(@PathVariable("taskId") String taskId){
        return  migrationService.startMigration(taskId);
    }

}
