package com.data.pipeline.file.business.feeresultdiff;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线下-支付方式不为21的计费配置明细
 *
 * @author zhangyux
 * @since 2024/11/11 上午9:25
 */
public class UrmtmfeeBasePayModeTestCase extends BaseFileOperations {

    /**
     * 源端文件路径
     */
    public static final String SOURCE_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-支付方式不为21的计费配置明细.csv";

    /**
     * 目标文件路径（修改数据的脚本文件）
     */
    public static final String TARGET_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-支付方式不为21的计费配置明细NEW_%s.sql";

    /**
     * 回滚文件路径（回滚数据的脚本文件）
     */
    public static final String ROLLBACK_TARGET_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-支付方式不为21的计费配置明细NEW_回滚_%s.sql";

    /**
     * 目标sql（修改数据的sql）
     */
    private final String targetSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.EXP_DT = t.EFF_DT, t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s' AND t.MERC_ID = '%s' AND t.PAY_MODE = '%s';";

    /**
     * 回滚sql（回滚数据的sql）
     */
    private final String targetRollBackSql =
        "UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.EXP_DT = '%s', t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s';";

    @Test
    public void testGenerateSql() throws IOException {

        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(SOURCE_FILE);

        // 2、生成回滚数据sql
        List<String> sqlList = list.stream().map(split -> {
            String mercId = split[0];
            String rateId = split[4];
            String payMode = split[8];
            return String.format(targetSql, rateId, mercId, payMode);
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
            String expDt = split[2].replace("\"", "");
            String rateId = split[4].replace("\"", "");
            return String.format(targetRollBackSql, expDt, rateId);
        }).collect(Collectors.toList());

        // 3、生成脚本文件（每500条添加一个commit，每5000条生成一个文件）
        generatePagingSqlScript(sqlList, ROLLBACK_TARGET_FILE);
    }
}
