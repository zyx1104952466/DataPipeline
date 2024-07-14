package com.data.pipeline.file;

import com.data.pipeline.entity.BusiCodeEntity;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BusiCodeParser {

    public static final String REGEX = "\"busiCode\":\"(.*?)\",\\s*\"tran\":\"(.*?)\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static List<BusiCodeEntity> parseData(List<String> absolutePathContent) {
        return absolutePathContent.stream()
                .map(BusiCodeParser::extractBusiCodeEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static BusiCodeEntity extractBusiCodeEntity(String input) {
        Matcher matcher = PATTERN.matcher(input);
        if (matcher.find()) {
            BusiCodeEntity busiCodeEntity = new BusiCodeEntity();
            busiCodeEntity.setBusiCode(matcher.group(1));
            busiCodeEntity.setTran(matcher.group(2));
            return busiCodeEntity;
        }
        return null;
    }
}
