package com.zzz.migrationtoolkit.service;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;
import com.zzz.migrationtoolkit.server.InitContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/7 14:59
 * @description:
 */
@Service
public class DataBaseService {

    public ResultMessage<List<String>> dataBaseType() {
        List<String> databaseList = Arrays.asList(DataBaseConstant.database);
        return new ResultMessage<List<String>>().success(databaseList);
    }

    public ResultMessage<String> testDataBaseConnection(ConnectionVO connectionVO) {
        try {
            boolean conn = DataSourceProcess.testConnection(connectionVO);
            if (!conn) {
                return new ResultMessage<String>().fail();
            }
            return new ResultMessage<String>().success();
        } catch (Exception e) {
            return new ResultMessage<String>().fail(e.getMessage());
        }
    }

    public ResultMessage<String> saveDataBaseConnection(ConnectionVO connection) {
        boolean save = DataSourceProcess.writeDataBaseConnection(connection);
        if (!save) {
            return new ResultMessage<String>().fail();
        }
        return new ResultMessage<String>().success();
    }
}
