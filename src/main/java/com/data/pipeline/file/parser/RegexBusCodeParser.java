package com.data.pipeline.file.parser;

import com.data.pipeline.entity.BusCodeEntity;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexBusCodeParser {

    public static final String REGEX = "\"busCode\":\"(.*?)\",\\s*\"tranId\":\"(.*?)\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static List<BusCodeEntity> parseData(List<String> absolutePathContent) {
        return absolutePathContent.stream()
                .map(RegexBusCodeParser::extractBusCodeEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static BusCodeEntity extractBusCodeEntity(String input) {
        Matcher matcher = PATTERN.matcher(input);
        if (matcher.find()) {
            BusCodeEntity busCodeEntity = new BusCodeEntity();
            busCodeEntity.setBusCode(matcher.group(1));
            busCodeEntity.setTranId(matcher.group(2));
            return busCodeEntity;
        }
        return null;
    }
}
