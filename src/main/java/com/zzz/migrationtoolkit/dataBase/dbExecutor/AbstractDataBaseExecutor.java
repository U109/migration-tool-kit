package com.zzz.migrationtoolkit.dataBase.dbExecutor;

import com.zzz.migrationtoolkit.common.utils.CloseObjUtil;
import com.zzz.migrationtoolkit.dataBase.ConnectionManager;
import com.zzz.migrationtoolkit.dataBase.IDataBaseExecutor;
import com.zzz.migrationtoolkit.entity.dataBaseConnInfoEntity.DataBaseConnInfo;
import com.zzz.migrationtoolkit.entity.dataBaseElementEntity.ColumnEntity;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:11
 * @description:
 */
@Slf4j
public abstract class AbstractDataBaseExecutor implements IDataBaseExecutor {

    public DataBaseConnInfo dataBaseConnInfo;

    public AbstractDataBaseExecutor(){

    }

    public AbstractDataBaseExecutor(DataBaseConnInfo dbci){
        this.dataBaseConnInfo = dbci;
    }


    public Connection getConnection() throws Exception {
        return ConnectionManager.getConnection(dataBaseConnInfo);
    }

    @Override
    public void closeExecutor(){

    };

}
