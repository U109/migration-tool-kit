package com.zzz.migrationtoolkit.service;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;
import com.zzz.migrationtoolkit.server.InitContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/7 14:59
 * @description:
 */
@Slf4j
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
        try {
            DataSourceProcess.writeDataBaseConnection(connection);
        } catch (Exception e) {
            log.error("database connection info save error : " + e.getMessage());
            return new ResultMessage<String>().fail(e.getMessage());
        }
        return new ResultMessage<String>().success();
    }

    public ResultMessage<List<DataBaseConnInfo>> getDataBaseConnectionInfo() {
        //要保持最新数据
        InitContext.initContext();
        List<DataBaseConnInfo> dataBaseConnInfoList = DataSourceProcess.getDataBaseConnectionInfo();
        return new ResultMessage<List<DataBaseConnInfo>>().success(dataBaseConnInfoList);
    }
}
