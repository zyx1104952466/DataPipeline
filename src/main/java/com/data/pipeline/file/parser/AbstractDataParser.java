package com.data.pipeline.file.parser;

import com.data.pipeline.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyux
 * @since 2024/8/5 23:25
 */
public abstract class AbstractDataParser {

    private static final Logger logger = LogManager.getLogger(AbstractDataParser.class);

    public abstract Map<String, String> parseData(String data);

    public abstract String getMethodName();

    public Map<String, String> extractAndAddJsonData(String data) {
        int startIndex = data.indexOf("{");
        int endIndex = data.indexOf("}", startIndex) + 1;
        String jsonString = data.substring(startIndex, endIndex);
        try {
            return JsonUtil.objectMapper.readValue(jsonString, new TypeReference<Map<String, String>>() {});
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON string to Map for line: {}", data, e);
        }
        return new HashMap<>();
    }
}
