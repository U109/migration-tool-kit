package com.zzz.migrationtoolkit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.Connection;

/**
 * @author: Zzz
 * @date: 2023/7/4 10:57
 * @description:
 */
@Controller
public class TestController {

    @RequestMapping("/test/{tableName}")
    public void testAction(@PathVariable String tableName){


    }
}
