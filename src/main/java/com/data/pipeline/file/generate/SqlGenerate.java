package com.data.pipeline.file.generate;

/**
 * @author zhangyux
 * @since 2024/8/2 9:51
 */
public class SqlGenerate {

    // 模板SQL
    private static final String SQL_TEMPLATE = "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.BUSI_CODE = '00490004', t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s' AND t.MERC_ID = '%s' AND t.BUSI_CODE = '01000016' AND t.BUSI_TYPE = '04';";

    private static final String SQL_ROLLBACK_TEMPLATE = "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.BUSI_CODE = '01000016', t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s' AND t.MERC_ID = '%s' AND t.BUSI_CODE = '00490004' AND t.BUSI_TYPE = '04';";

    /**
     * 生成SQL
     *
     * @param rateId 费率ID
     * @param mercId 商户ID
     * @return SQL
     */
    public static String generateSql(String rateId, String mercId) {
        return String.format(SQL_TEMPLATE, rateId, mercId);
    }

    /**
     * 生成SQL
     *
     * @param rateId 费率ID
     * @param mercId 商户ID
     * @return SQL
     */
    public static String generateRollbackSql(String rateId, String mercId) {
        return String.format(SQL_ROLLBACK_TEMPLATE, rateId, mercId);
    }
}
