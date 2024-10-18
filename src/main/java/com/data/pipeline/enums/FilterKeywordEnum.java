package com.data.pipeline.enums;

public enum FilterKeywordEnum {
    START_TRANSACTION_DETAIL("开始,流水详情:"),
    EXTERNAL_CARD_FEE_CALCULATION("外卡计算手续费开始，参数："),
    MERCHANT_FEE_CONTENT("获取商户交易手续费(计费规则缓存)内容:"),
    OUT_ORG_FEE_CONTENT("获取接入机构交易手续费(计费规则缓存)内容:");

    private final String keyword;

    FilterKeywordEnum(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
