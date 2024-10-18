package com.data.pipeline.file.parser;

import com.data.pipeline.entity.FeeEntity;
import com.data.pipeline.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegexParser {

    private static final Logger logger = LogManager.getLogger(RegexParser.class);

    public static List<FeeEntity> parseData(List<String> absolutePathContent) {
        return absolutePathContent.stream()
                .map(RegexParser::extractFeeEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static List<String> parseDataByString(List<String> absolutePathContent) {
        return absolutePathContent.stream()
                .map(RegexParser::extractString)
                .collect(Collectors.toList());
    }

    private static FeeEntity extractFeeEntity(String logLine) {
        // 提取 "计费参数：" 后的数据
        int startIndex = logLine.indexOf("计费参数：") + "计费参数：".length();
        int endIndex = logLine.length();
        String feeParamsJson = logLine.substring(startIndex, endIndex);

        try {
            return JsonUtil.parseJsonToType(feeParamsJson, new TypeReference<FeeEntity>() {});
        } catch (Exception e) {
            logger.error("Failed to parse the fee parameters: " + feeParamsJson, e);
        }
        return null;
    }

    private static String extractString(String logLine) {
        // 提取 "计费参数：" 后的数据
        int startIndex = logLine.indexOf("计费参数：") + "计费参数：".length();
        int endIndex = logLine.length();
        return logLine.substring(startIndex, endIndex);
    }
}
