package com.data.pipeline.file.business.feeresultdiff;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangyux
 * @since 2024/11/11 上午9:25
 */
public class UrmtmfeeBaseDoubleFeeMinFeeTestCase extends BaseFileOperations {

    /**
     * 目标sql（修改数据的sql）
     */
    private static final String targetSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_INFO t SET t.MIN_FEE_AMT = 0, t.UPDATE_TIME = SYSDATE WHERE t.ID = '%s' AND t.MERC_ID = '%s' AND t.FEE_RAT1 = 0 AND t.FIX_FEE_AMT = 0;";

    /**
     * 回滚sql（回滚数据的sql）
     */
    private static final String targetRollBackSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_INFO t SET t.MIN_FEE_AMT = %s, t.UPDATE_TIME = SYSDATE WHERE t.ID = '%s';";

    /**
     * 触发更新的sql
     */
    private static final String targetBaseUpdateSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s';";


    @Test
    public void testGenerateSql() throws IOException {
        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(getDirectoryPath() + getSourceFileName());
        // 2、生成数据sql
        generateSqlScripts(list, targetSql, createTargetFilePath("_%s.sql"), UrmtmfeeBaseDoubleFeeMinFeeTestCase::covertSql);
        // 3、生成回滚数据sql
        generateSqlScripts(list, targetRollBackSql, createTargetFilePath("_回滚_%s.sql"), UrmtmfeeBaseDoubleFeeMinFeeTestCase::covertRollBackSql);
        // 4、生成触发更新数据sql
        generateSqlScripts(list, targetBaseUpdateSql, createTargetFilePath("_触发更新_%s.sql"), UrmtmfeeBaseDoubleFeeMinFeeTestCase::covertUpdateSql);
    }

    /**
     * 转换sql
     *
     * @param sqlTemplate sql模板
     * @param data        数据
     * @return sql
     */
    private static String covertSql(String sqlTemplate, String[] data) {
        String mercId = data[0];
        String id = data[2];
        return java.lang.String.format(sqlTemplate, id, mercId);
    }

    /**
     * 转换回滚sql
     *
     * @param sqlTemplate sql模板
     * @param data        拆分的数据
     * @return 回滚sql
     */
    private static String covertRollBackSql(String sqlTemplate, String[] data) {
        String minFeeAmt = data[7];
        String id = data[2];
        return java.lang.String.format(sqlTemplate, minFeeAmt, id);
    }

    /**
     * 转换触发更新的sql
     *
     * @param sqlTemplate sql模板
     * @param data        拆分的数据
     * @return 回滚sql
     */
    private static String covertUpdateSql(String sqlTemplate, String[] data) {
        String rateId = data[1];
        return java.lang.String.format(sqlTemplate, rateId);
    }

    /**
     * 源文件名
     *
     * @return 源文件名
     */
    public String getSourceFileName() {
        return "20241109-双重计费最小金额不为0的计费配置明细.csv";
    }

    /**
     * 文件目录
     *
     * @return 文件目录
     */
    public String getDirectoryPath() {
        return "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据 - 副本\\";
    }

    /**
     * 文件后缀
     *
     * @return 文件后缀
     */
    public String getSourceFileSuffix() {
        return ".csv";
    }
}
