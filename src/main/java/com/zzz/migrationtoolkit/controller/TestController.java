package com.zzz.migrationtoolkit.controller;

import com.zzz.migrationtoolkit.server.InitContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;

/**
 * @author: Zzz
 * @date: 2023/7/4 10:57
 * @description:
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String testAction(){
        int size = InitContext.DBConnectionMap.size();
        return "Map::size = " + size;
    }
}
