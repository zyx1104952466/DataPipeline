package com.data.pipeline.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zhangyux
 * @since 2024/8/5 23:38
 */
public class JsonUtil {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 JSON 字符串转换为指定类型的对象。
     *
     * @param jsonStr JSON字符串
     * @param typeRef 类型引用，用于指定目标类型
     * @param <T> 目标类型
     * @return 转换后的对象
     * @throws JsonProcessingException 如果JSON解析失败
     */
    public static <T> T parseJsonToType(String jsonStr, TypeReference<T> typeRef) throws JsonProcessingException {
        return objectMapper.readValue(jsonStr, typeRef);
    }
}
