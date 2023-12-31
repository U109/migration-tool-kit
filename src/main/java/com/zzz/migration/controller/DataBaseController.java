package com.zzz.migration.controller;

import com.zzz.migration.common.vo.ConnectionVO;
import com.zzz.migration.common.vo.DataSourceVO;
import com.zzz.migration.common.vo.ResultMessage;
import com.zzz.migration.config.SwaggerConfig;
import com.zzz.migration.service.DataBaseService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/7 11:16
 * @description:
 */
@Api(tags = {"数据源管理接口"})
@RestController
@RequestMapping(SwaggerConfig.API_PREFIX_DATABASE)
public class DataBaseController {

    @Resource
    private DataBaseService dataBaseService;

    /**
     * 获取数据库列表
     *
     * 
     * @return [Mysql, Oracle...]
     */
    @GetMapping("/getDataBaseType")
    public ResultMessage<List<String>> getDataBaseType() {
        return dataBaseService.getDataBaseType();
    }

    /**
     * 测试连接
     *
     * @param connectionVO connectionVO
     * @return ResultMessage
     */
    @PostMapping("/testConnection")
    public ResultMessage<String> testDataBaseConnection(@RequestBody ConnectionVO connectionVO) {
        return dataBaseService.testDataBaseConnection(connectionVO);
    }

    @PostMapping("/saveConnection")
    public ResultMessage<String> saveDataBaseConnection(@RequestBody ConnectionVO connectionVO) {
        return dataBaseService.saveDataBaseConnection(connectionVO);
    }

    @GetMapping("/getDataBaseConnInfoList")
    public ResultMessage<List<DataSourceVO>> getDataBaseConnectionInfo() {
        return dataBaseService.getDataBaseConnectionInfo();
    }

}
