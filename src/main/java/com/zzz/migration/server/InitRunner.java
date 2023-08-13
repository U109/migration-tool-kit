package com.zzz.migration.server;

import com.zzz.migration.common.constants.FilePathContent;
import com.zzz.migration.core.cache.TaskCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author: Zzz
 * @date: 2023/7/7 11:04
 * @description:
 */
@Slf4j
@Component
public class InitRunner implements CommandLineRunner {

    @Override
    public void run(String... args){
        log.info("initRunner start");
        InitContext.initContext();
        TaskCache.init(FilePathContent.TASK_FILE_FOLDER);
    }
}
