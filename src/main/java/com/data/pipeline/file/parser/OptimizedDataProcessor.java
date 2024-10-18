package com.data.pipeline.file.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OptimizedDataProcessor {

    private final List<Map<String, String>> dataList = new ArrayList<>();

    private static Map<String, String> buildResult(AbstractDataParser parser) {
        Map<String, String> map = parser.parseData(parser.getMethodName());
        map.putIfAbsent("method", parser.getMethodName());
        return map;
    }

    public void processFileData(List<String> lines) {
        lines.stream().map(DataParserFactory::createParserForLine).filter(Objects::nonNull)
            .map(OptimizedDataProcessor::buildResult).forEach(dataList::add);
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }
}
