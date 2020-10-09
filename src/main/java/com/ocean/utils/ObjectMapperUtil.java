package com.ocean.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperUtil {

    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        // 如果是空对象的时候,不抛异常(如果使用了mybatis懒加载必须设置)
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
    }

    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }
}
