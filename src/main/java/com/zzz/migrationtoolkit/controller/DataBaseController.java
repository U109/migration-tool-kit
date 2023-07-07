package com.zzz.migrationtoolkit.controller;

import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.service.DataBaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getDataBaseNotExist")
    public ResultMessage<List<String>> getDataBaseNotExist(){
        return dataBaseService.dataBaseNotExist();
    }
}
