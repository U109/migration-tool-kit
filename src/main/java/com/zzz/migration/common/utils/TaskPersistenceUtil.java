package com.zzz.migration.common.utils;

import com.zzz.migration.common.constants.FilePathContent;
import com.zzz.migration.entity.taskEntity.TaskDetail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Zzz
 * @date: 2023/7/10 15:45
 * @description: 任务持久化
 */
@Slf4j
@Data
public class TaskPersistenceUtil {

    public void saveTaskInfo(TaskDetail taskDetail) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        String filePath = FilePathContent.TASK_FILE_FOLDER + File.separator + taskDetail.getTaskId() + ".zmt";
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.flush();
            oos.writeObject(taskDetail);
        } catch (Exception e) {
            log.error("saveTaskInfo error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<TaskDetail> getAllTaskInfo() {
        File f = new File(FilePathContent.TASK_FILE_FOLDER + File.separator);
        File[] fileList = f.listFiles();

        List<TaskDetail> taskDetailList = new ArrayList<>();

        assert fileList != null;
        for (File file : fileList) {
            if (file.isFile()) {
                TaskDetail taskDetail = readTaskInfo(FilePathContent.TASK_FILE_FOLDER + File.separator + file.getName());
                taskDetailList.add(taskDetail);
            }
        }
        return taskDetailList;
    }

    private TaskDetail readTaskInfo(String filePath) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        TaskDetail taskDetail = null;
        try {
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            taskDetail = (TaskDetail) ois.readObject();
        } catch (Exception e) {
            log.error("readTaskInfo error : " + e.getMessage());
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return taskDetail;
    }

    public void setFolder(String taskPersistenceFolder) {
    }
}
