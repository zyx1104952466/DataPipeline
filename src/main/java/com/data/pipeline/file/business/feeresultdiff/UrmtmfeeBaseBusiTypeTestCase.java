package com.data.pipeline.file.business.feeresultdiff;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Before;
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
public class UrmtmfeeBaseBusiTypeTestCase extends BaseFileOperations {

    /**
     * 源端文件路径
     */
    public static final String SOURCE_FILE =
        "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\20241109-银联二维码同时维护了消费及二维码的计费配置明细-过滤了账户类型.csv";

    @Before
    public void setUp() {
        // 设置待读取的文件编码
        txtFileReader.setCHARSET("GB2312");
    }

    @Test
    public void testReadFile() throws IOException {

        // 1、读取文件
        List<String> list = txtFileReader.readFile(SOURCE_FILE);
        if (null == list) {
            logger.error("list is null");
            return;
        }
        System.out.println("list size: " + list.size());

        // 1、读取文件
        List<String> tradeMercList = txtFileReader.readFile("temp/有交易的云闪付商户.txt");

        List<String> dataList = list.stream().filter(s -> {
            String[] split = s.split("\\|");
            if ("MERC_ID".equals(split[0])) {
                return false;
            }
            String mercId = split[0].trim();
            if (!tradeMercList.contains(mercId)) {
                return false;
            }

            String feeCalMthLeft = split[6];
            String feeCalMthRight = split[12];
            return feeCalMthLeft.equals(feeCalMthRight);
        }).collect(Collectors.toList());

        System.out.println("dataList size: " + dataList.size());
        dataList.forEach(System.out::println);
    }

    @Test
    public void testGenerateSql() throws IOException {

        // 1、读取文件
        List<String> list = txtFileReader.readFile(SOURCE_FILE);
        if (null == list) {
            logger.error("list is null");
            return;
        }
        System.out.println("list size: " + list.size());

        // 1、读取文件
        List<String> tradeMercList = txtFileReader.readFile("temp/有交易的云闪付商户.txt");

        List<String> dataList = list.stream().map(s -> s.split("\\|")).filter(split -> {
            if ("MERC_ID".equals(split[0])) {
                return false;
            }
            String mercId = split[0].trim();
            if (!tradeMercList.contains(mercId)) {
                return false;
            }

            String feeCalMthLeft = split[6];
            String feeCalMthRight = split[12];
            return !feeCalMthLeft.equals(feeCalMthRight);
        }).map(split -> {
            String rateId = split[7].trim();
            String mercId = split[0].trim();
            return String.format("UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.EXP_DT = t.EFF_DT, t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s' AND t.MERC_ID = '%s';", rateId, mercId);
        }).collect(Collectors.toList());

        System.out.println("dataList size: " + dataList.size());
        dataList.forEach(System.out::println);
    }
}
