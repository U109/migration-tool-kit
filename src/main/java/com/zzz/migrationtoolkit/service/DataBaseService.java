package com.zzz.migrationtoolkit.service;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ConnectionVO;
import com.zzz.migrationtoolkit.common.vo.DataSourceVO;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.handler.dataBaseHandler.DataSourceProcess;
import com.zzz.migrationtoolkit.server.InitContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            DataSourceProcess.writeDBConnectionXML(connection);
        } catch (Exception e) {
            log.error("database connection info save error : " + e.getMessage());
            return new ResultMessage<String>().fail(e.getMessage());
        }
        return new ResultMessage<String>().success();
    }

    public ResultMessage<List<DataSourceVO>> getDataBaseConnectionInfo() {
        //要保持最新数据
        InitContext.initContext();
        List<DataSourceVO> dataBaseConnInfoList = DataSourceProcess.getDataSourceList();
        return new ResultMessage<List<DataSourceVO>>().success(dataBaseConnInfoList);
    }
}
