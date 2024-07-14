package com.data.pipeline.file;

import com.data.pipeline.entity.BusCodeEntity;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BusiCodeParser {

    public static final String REGEX = "\"busiCode\":\"(.*?)\",\\s*\"tran\":\"(.*?)\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static List<BusCodeEntity> parseData(List<String> absolutePathContent) {
        return absolutePathContent.stream()
                .map(BusiCodeParser::extractBusiCodeEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static BusCodeEntity extractBusiCodeEntity(String input) {
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
