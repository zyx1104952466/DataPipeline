package com.data.pipeline.file.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OrderTradeFeeAmtV1Parser
 *
 * @author zhangyux
 * @since 2024/8/5 23:24
 */
public class OrderTradeFeeAmtV1Parser extends AbstractDataParser {

    private static final Pattern CAL_FEE_PARAM_PATTERN = Pattern.compile("CalcFeeParam\\[([^\\]]*)\\]");
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(\\w+):(\\S+)");

    @Override
    public Map<String, String> parseData(String data) {
        // Parse the data
        Matcher matcher = CAL_FEE_PARAM_PATTERN.matcher(data);
        if (matcher.find()) {
            String calcFeeParamStr = matcher.group(1);
            return parseCalcFeeParam(calcFeeParamStr);
        }
        return new HashMap<>();
    }

    @Override
    public String getMethodName() {
        return "calculateOrderTradeFeeAmtV1";
    }

    private Map<String, String> parseCalcFeeParam(String calcFeeParamStr) {
        String trimmed = calcFeeParamStr.substring("CalcFeeParam[".length(), calcFeeParamStr.length() - 1);
        Map<String, String> params = new HashMap<>();
        Matcher matcher = KEY_VALUE_PATTERN.matcher(trimmed);
        while (matcher.find()) {
            params.put(matcher.group(1), matcher.group(2));
        }
        return params;
    }
}
