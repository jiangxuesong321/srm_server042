package com.cmoc.modules.srm.utils;

import java.lang.reflect.Constructor;

/**
 * 数据类型转换工具类
 * @author
 */
public class DataConvertUtil {

    /**
     * Object[] 转 Java Bean
     * @param objectArray 原数组
     * @param clazz 目标 Bean
     * @param <T>
     * @return
     */
    public static <T> T objectArrayToBean(Object[] objectArray, Class<T> clazz) throws Exception {
        if (objectArray == null || objectArray.length == 0){
            return null;
        }
        Class<?>[] tClass = null;
        Constructor<?>[] constructors = clazz.getConstructors();
        for (int i = 0; i < constructors.length; i++){
            Constructor<?> constructor = constructors[i];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == objectArray.length){
                tClass = parameterTypes;
                break;
            }
        }
        return clazz.getConstructor(tClass).newInstance(objectArray);
    }
}
