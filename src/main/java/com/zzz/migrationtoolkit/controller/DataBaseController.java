package com.zzz.migrationtoolkit.controller;

import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
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
     * 获取没有添加数据源的数据库列表
     *
     * @return [Mysql, Oracle...]
     */
    @GetMapping("/getDataBaseNotExist")
    public ResultMessage<List<String>> getDataBaseNotExist() {
        return dataBaseService.dataBaseNotExist();
    }

    @PostMapping("/testConnection")
    public ResultMessage<String> testDataBaseConnection(@RequestBody ConnectionVO connectionVO) {
        return dataBaseService.testDataBaseConnection(connectionVO);
    }
}
