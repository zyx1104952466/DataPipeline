package com.data.pipeline.file.parser;

import java.util.Map;

/**
 * @author zhangyux
 * @since 2024/8/5 23:43
 */
public class OutOrgFeeParser extends AbstractDataParser {

    @Override
    public Map<String, String> parseData(String data) {
        return extractAndAddJsonData(data);
    }

    @Override
    public String getMethodName() {
        return "CalOrgInNoTradeFeeServerImpl";
    }
}
