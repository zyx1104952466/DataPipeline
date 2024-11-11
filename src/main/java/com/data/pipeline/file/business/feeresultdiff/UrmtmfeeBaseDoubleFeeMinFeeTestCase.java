package com.data.pipeline.file.business.feeresultdiff;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyux
 * @since 2024/11/11 上午9:25
 */
public class UrmtmfeeBaseDoubleFeeMinFeeTestCase extends BaseFileOperations {

    public static final String SOURCE_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-双重计费最小金额不为0的计费配置明细.csv";
    public static final String TARGET_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-双重计费最小金额不为0的计费配置明细NEW_%s.sql";
    public static final String TARGET_ROLLBACK_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-双重计费最小金额不为0的计费配置明细NEW_回滚_%s.sql";
    public static final String TARGET_BASE_UPDATE_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-双重计费最小金额不为0的计费配置明细_修改base表更新时间NEW_回滚_%s.sql";

    private final String targetSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_INFO t SET t.MIN_FEE_AMT = 0, t.UPDATE_TIME = SYSDATE WHERE t.ID = '%s' AND t.MERC_ID = '%s' AND t.FEE_RAT1 = 0 AND t.FIX_FEE_AMT = 0;";
    private final String targetRollBackSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_INFO t SET t.MIN_FEE_AMT = %s, t.UPDATE_TIME = SYSDATE WHERE t.ID = '%s';";
    private final String targetBaseUpdateSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s';";

    @Before
    public void setUp() {
        // 设置待读取的文件编码
        txtFileReader.setCHARSET("GB2312");
    }

    @Test
    public void testGenerateSql() throws IOException {

        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(SOURCE_FILE);

        // 2、生成回滚数据sql
        List<String> sqlList = list.stream().map(split -> {
            String mercId = split[0];
            String id = split[2];
            return String.format(targetSql, id, mercId);
        }).collect(Collectors.toList());

        // 3、生成脚本文件（每500条添加一个commit，每5000条生成一个文件）
        generatePagingSqlScript(sqlList, TARGET_FILE);
    }

    @Test
    public void testGenerateRollbackSql() throws IOException {

        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(SOURCE_FILE);

        // 2、生成回滚数据sql
        List<String> sqlList = list.stream().map(split -> {
            String minFeeAmt = split[7];
            String id = split[2];
            return String.format(targetRollBackSql, minFeeAmt, id);
        }).collect(Collectors.toList());

        // 3、生成脚本文件（每500条添加一个commit，每5000条生成一个文件）
        generatePagingSqlScript(sqlList, TARGET_ROLLBACK_FILE);
    }

    @Test
    public void testGenerateBaseUpdateSql() throws IOException {

        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(SOURCE_FILE);

        // 2、生成回滚数据sql
        List<String> sqlList = list.stream().map(split -> {
            String rateId = split[1];
            return String.format(targetBaseUpdateSql, rateId);
        }).collect(Collectors.toList());

        // 3、生成脚本文件（每500条添加一个commit，每5000条生成一个文件）
        generatePagingSqlScript(sqlList, TARGET_BASE_UPDATE_FILE);
    }
}
