package com.zzz.migrationtoolkit.controller;

import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.service.DataBaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/7 11:16
 * @description:
 */
@RestController
@RequestMapping("/data-base")
public class DataBaseController {

    @Resource
    private DataBaseService dataBaseService;

    /**
     * 获取数据库列表
     *
     * 
     * @return [Mysql, Oracle...]
     */
    @GetMapping("/dataBaseType")
    public ResultMessage<List<String>> dataBaseType() {
        return dataBaseService.dataBaseType();
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
    public ResultMessage<List<DataBaseConnInfo>> getDataBaseConnectionInfo() {
        return dataBaseService.getDataBaseConnectionInfo();
    }

}
