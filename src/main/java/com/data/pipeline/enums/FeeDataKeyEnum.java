package com.data.pipeline.enums;

/**
 * @author zhangyux
 * @since 2024/8/6 10:18
 */
public enum FeeDataKeyEnum {
    METHOD("method"),
    LOG_NO("logNo"),
    AMOUNT("amount"),
    PAYEE_USER_CODE("payeeUserCode"),
    MERC_ID("mercId"),
    PAY_MODE("payMode"),
    BUSI_CODE("busiCode"),
    TRAN_TYPE("tranType"),
    BUSI_TYPE("busiType"),
    CARD_AREA("cardArea"),
    BANK_ACCOUNT_TYPE("bankAccountType"),
    CARD_ORG("cardOrg");

    private final String key;

    FeeDataKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
