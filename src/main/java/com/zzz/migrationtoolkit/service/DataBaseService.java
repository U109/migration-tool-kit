package com.zzz.migrationtoolkit.service;

import com.zzz.migrationtoolkit.common.constants.DataBaseConstant;
import com.zzz.migrationtoolkit.common.vo.ResultMessage;
import com.zzz.migrationtoolkit.server.InitContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/7 14:59
 * @description:
 */
@Service
public class DataBaseService {

    public ResultMessage<List<String>> dataBaseNotExist() {
        List<String> dbList = new ArrayList<>();
        for (String db : DataBaseConstant.database) {
            if (!InitContext.DBConnectionMap.containsKey(db)) {
                dbList.add(db);
            }
        }
        return new ResultMessage<List<String>>().success(dbList);
    }
}
