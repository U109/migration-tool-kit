package com.zzz.migration.common.utils;

import java.lang.reflect.Constructor;

/**
 * @author: Zzz
 * @date: 2023/7/10 17:32
 * @description:
 */
public class ReflectUtil {

    @SafeVarargs
    public static <T> Object getObjByClass(T... args) {
        if (args.length == 0) {
            return null;
        }
        Object object = null;
        try {
            if (args.length == 1) {
                object = Class.forName((String) args[0]).getDeclaredConstructor().newInstance();
            } else {
                Class<?> clazz = Class.forName((String) args[0]);
                Class<?>[] classArray = new Class[args.length - 1];
                Object[] params = new Object[args.length - 1];
                for (int i = 0; i < classArray.length; i++) {
                    classArray[i] = args[i + 1].getClass();
                    params[i] = args[i + 1];
                }
                Constructor<?> constructor = clazz.getConstructor(classArray);
                //构造器创建实例对象
                object = constructor.newInstance(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


}
