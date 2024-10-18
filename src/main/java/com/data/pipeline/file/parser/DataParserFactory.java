package com.data.pipeline.file.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangyux
 * @since 2024/8/6 9:37
 */
public class DataParserFactory {

    private static final Map<String, AbstractDataParser> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put("Impl.calculateOrderTradeFeeAmtV1", new OrderTradeFeeAmtV1Parser());
        PARSER_MAP.put("Impl.calculateOrderTradeFee:", new OrderTradeFeeParser());
        PARSER_MAP.put("Impl.calculatePositiveTradeFee:289", new PositiveTradeFeeParser());
        PARSER_MAP.put("Impl.calculatePositiveTradeFee:74", new OutOrgFeeParser());
    }

    public static AbstractDataParser createParserForLine(String line) {
        for (Map.Entry<String, AbstractDataParser> entry : PARSER_MAP.entrySet()) {
            if (line.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
