package com.zzz.migrationtoolkit.common.utils;

/**
 * @author: Zzz
 * @date: 2023/7/14 16:39
 * @description:
 */
public class CloseObjUtil {
    public static void closeAll(AutoCloseable... args) {

        for (AutoCloseable arg : args) {
            if (arg != null) {
                try {
                    arg.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
