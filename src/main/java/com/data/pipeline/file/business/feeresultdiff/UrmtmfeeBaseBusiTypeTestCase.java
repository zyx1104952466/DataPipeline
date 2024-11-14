package com.data.pipeline.file.business.feeresultdiff;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 线下-支付方式不为21的计费配置明细
 *
 * @author zhangyux
 * @since 2024/11/11 上午9:25
 */
public class UrmtmfeeBaseBusiTypeTestCase extends BaseFileOperations {

    List<String> tradeMercList;

    @Before
    public void setUp() throws IOException {
        // 设置待读取的文件编码
        textFileReader.setCHARSET("GB2312");
        // 1、读取文件
        tradeMercList = textFileReader.readFile("temp/有交易的云闪付商户.txt");
    }

    @Test
    public void testGenerateSql() throws IOException {

        // 1、读取文件
        List<String> list = textFileReader.readFile(getDirectoryPath() + this.getSourceFileName());

        // 2、处理数据
        List<String> dataList = list.stream().map(s -> s.split("\\|")).filter(split -> {
            if ("MERC_ID".equals(split[0])) {
                return false;
            }
            String feeCalMthLeft = split[6];
            String feeCalMthRight = split[12];
            return !feeCalMthLeft.equals(feeCalMthRight);
        }).map(split -> {
            String rateId = split[7].trim();
            String mercId = split[0].trim();
            return String.format("UPDATE YSPOS_BOSS.URMTMFEE_BASE t SET t.EXP_DT = t.EFF_DT, t.UPDATE_TIME = SYSDATE WHERE t.RATE_ID = '%s' AND t.MERC_ID = '%s';", rateId, mercId);
        }).distinct().collect(Collectors.toList());

        System.out.println("dataList size: " + dataList.size());
        dataList.forEach(System.out::println);
    }

    @Test
    public void testGenerateSql1() throws IOException {
        // 1、读取文件
        List<String[]> list = csvFileReader.readFile(this.getDirectoryPath() + this.getSourceFileName());
        System.out.println("list size: " + list.size());

        List<List<String>> feeConfigList = readExcelFile();

        List<String> valueList = new ArrayList<>();
        List<String> valueList1 = new ArrayList<>();
        List<String> valueList2 = new ArrayList<>();
        List<String> valueList3 = new ArrayList<>();

        list.forEach(strings -> {
            String rateIdLeft = strings[1];
            String rateIdRight = strings[7];
            List<String> leftFeconfig = null;
            List<String> leftFeconfig1000 = null;
            List<String> rightFeconfig = null;
            List<String> rightFeconfig1000 = null;
            String feeCalMthLeft = strings[6];
            String feeCalMthRight = strings[12];

            String value;
            if (feeCalMthLeft.equals(feeCalMthRight) && "2".equals(feeCalMthLeft)) {
                for (List<String> stringList : feeConfigList) {
                    String feeConfigRateId = stringList.get(15);
                    if (rateIdLeft.equals(feeConfigRateId)) {
                        leftFeconfig = stringList;
                    }
                }
                for (List<String> stringList : feeConfigList) {
                    String feeConfigRateId = stringList.get(15);
                    if (rateIdRight.equals(feeConfigRateId)) {
                        rightFeconfig = stringList;
                    }
                }
                value = extracted("百分比", strings, leftFeconfig, rightFeconfig);
                valueList1.add(value);
            }

            if (feeCalMthLeft.equals(feeCalMthRight) && "3".equals(feeCalMthLeft)) {
                for (List<String> stringList : feeConfigList) {
                    String feeConfigRateId = stringList.get(15);
                    if (rateIdLeft.equals(feeConfigRateId) && ("0.00".equals(stringList.get(17)))) {
                        leftFeconfig = stringList;
                    }
                    if (rateIdLeft.equals(feeConfigRateId) && ("1000.00".equals(stringList.get(17)) || "1000.01".equals(stringList.get(17)))) {
                        leftFeconfig1000 = stringList;
                    }
                }
                for (List<String> stringList : feeConfigList) {
                    String feeConfigRateId = stringList.get(15);
                    if (rateIdRight.equals(feeConfigRateId) && ("0.00".equals(stringList.get(17)))) {
                        rightFeconfig = stringList;
                    }
                    if (rateIdRight.equals(feeConfigRateId) && ("1000.00".equals(stringList.get(17)) || "1000.01".equals(stringList.get(17)))) {
                        rightFeconfig1000 = stringList;
                    }
                }
                value = extracted3("分层套档", strings, leftFeconfig, leftFeconfig1000, rightFeconfig, rightFeconfig1000);
                valueList2.add(value);
            }

            if (!feeCalMthLeft.equals(feeCalMthRight)) {
                value = "【百分比-分层套档】"
                    + "," + strings[0]
                    + "," + strings[1]
                    + "," + covertCardTye(strings[2])
                    + "," + covertFeeCalTyp(strings[6])
                    + "," + strings[7]
                    + "," + covertCardTye(strings[8])
                    + "," + covertFeeCalTyp(strings[12]);
                valueList3.add(value);
            }

        });
        valueList.addAll(valueList1.stream().sorted().collect(Collectors.toCollection(ArrayList::new)));
        valueList.addAll(valueList2.stream().sorted().collect(Collectors.toCollection(ArrayList::new)));
        valueList.addAll(valueList3.stream().sorted().collect(Collectors.toCollection(ArrayList::new)));
        textFileWriter.writeFile("temp/123.txt", valueList, true);

    }

    private String extracted(String flag, String[] strings, List<String> leftFeconfig,
        List<String> rightFeconfig) {
        if (null == leftFeconfig || null == rightFeconfig) {
            return flag
                + "无费率配置"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12]);
        }

        if (leftFeconfig.get(21).equals(rightFeconfig.get(21))
            && leftFeconfig.get(1).equals(rightFeconfig.get(1))
            && leftFeconfig.get(11).equals(rightFeconfig.get(11))) {
            return flag
                + "费率值相同"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + ", " + leftFeconfig.get(21)
                + ", " + leftFeconfig.get(1)
                + ", " + leftFeconfig.get(11)
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12])
                + ", " + rightFeconfig.get(21)
                + ", " + rightFeconfig.get(1)
                + ", " + rightFeconfig.get(11);
        }

        if (!leftFeconfig.get(21).equals(rightFeconfig.get(21))
            || !leftFeconfig.get(1).equals(rightFeconfig.get(1))
            || !leftFeconfig.get(11).equals(rightFeconfig.get(11))) {

            String tradeFlag;
            if (!tradeMercList.contains(strings[0])) {
                tradeFlag = "无交易";
            } else {
                tradeFlag = "有交易";
            }

            return flag
                + "费率值不相同"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + ", " + leftFeconfig.get(21)
                + ", " + leftFeconfig.get(1)
                + ", " + leftFeconfig.get(11)
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12])
                + ", " + rightFeconfig.get(21)
                + ", " + rightFeconfig.get(1)
                + ", " + rightFeconfig.get(11)
                + ", " + tradeFlag;
        }
        return "NULL";
    }

    private String extracted3(String flag, String[] strings, List<String> leftFeconfig, List<String> leftFeconfig1000,
        List<String> rightFeconfig, List<String> rightFeconfig1000) {
        if (null == leftFeconfig || null == rightFeconfig || null == leftFeconfig1000 || null == rightFeconfig1000) {
            return flag
                + "无费率配置"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12]);
        }

        if (leftFeconfig.get(21).equals(rightFeconfig.get(21))
            && leftFeconfig.get(1).equals(rightFeconfig.get(1))
            && leftFeconfig.get(11).equals(rightFeconfig.get(11))
            && leftFeconfig1000.get(21).equals(rightFeconfig1000.get(21))
            && leftFeconfig1000.get(1).equals(rightFeconfig1000.get(1))
            && leftFeconfig1000.get(11).equals(rightFeconfig1000.get(11))) {
            return flag
                + "相同"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + ", " + leftFeconfig.get(21)
                + ", " + leftFeconfig.get(1)
                + ", " + leftFeconfig.get(11)
                + ", " + leftFeconfig1000.get(21)
                + ", " + leftFeconfig1000.get(1)
                + ", " + leftFeconfig1000.get(11)
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12])
                + ", " + rightFeconfig.get(21)
                + ", " + rightFeconfig.get(1)
                + ", " + rightFeconfig.get(11)
                + ", " + rightFeconfig1000.get(21)
                + ", " + rightFeconfig1000.get(1)
                + ", " + rightFeconfig1000.get(11);
        }

        if (!leftFeconfig.get(21).equals(rightFeconfig.get(21))
            || !leftFeconfig.get(1).equals(rightFeconfig.get(1))
            || !leftFeconfig.get(11).equals(rightFeconfig.get(11))
            || !leftFeconfig1000.get(21).equals(rightFeconfig1000.get(21))
            || !leftFeconfig1000.get(1).equals(rightFeconfig1000.get(1))
            || !leftFeconfig1000.get(11).equals(rightFeconfig1000.get(11))) {

            String tradeFlag;
            if (!tradeMercList.contains(strings[0])) {
                tradeFlag = "无交易";
            } else {
                tradeFlag = "有交易";
            }

            return flag
                + "费率值不相同"
                + "," + strings[0]
                + "," + strings[1]
                + "," + covertCardTye(strings[2])
                + "," + covertFeeCalTyp(strings[6])
                + ", " + leftFeconfig.get(21)
                + ", " + leftFeconfig.get(1)
                + ", " + leftFeconfig.get(11)
                + ", " + leftFeconfig1000.get(21)
                + ", " + leftFeconfig1000.get(1)
                + ", " + leftFeconfig1000.get(11)
                + "," + strings[7]
                + "," + covertCardTye(strings[8])
                + "," + covertFeeCalTyp(strings[12])
                + ", " + rightFeconfig.get(21)
                + ", " + rightFeconfig.get(1)
                + ", " + rightFeconfig.get(11)
                + ", " + rightFeconfig1000.get(21)
                + ", " + rightFeconfig1000.get(1)
                + ", " + rightFeconfig1000.get(11)
                + ", " + tradeFlag;
        }
        return "NULL";
    }

    public String covertCardTye(String cardType) {
        if ("**".equals(cardType)) {
            return "全部";
        }
        if ("0".equals(cardType)) {
            return "借记卡";
        }
        if ("1".equals(cardType)) {
            return "贷记卡";
        }
        return "未知";
    }

    public String covertFeeCalTyp(String feeCalTyp) {
        if ("2".equals(feeCalTyp)) {
            return "百分比";
        }
        if ("3".equals(feeCalTyp)) {
            return "分层套档";
        }
        return "未知";
    }

    @Test
    public void test03() throws IOException {
        this.readExcelFile();
    }

    public List<List<String>> readExcelFile() throws IOException {

        List<List<String>> list = excelFileReader.readFile(this.getDirectoryPath() + this.getSourceFileName1());
        System.out.println("list size: " + list.size());
        return list;
    }

    public String getSourceFileName1() {
        return "10.25计费配置异常数据取数字段(new).xlsx";
    }

    /**
     * 源文件名
     *
     * @return 源文件名
     */
    public String getSourceFileName() {
        return "20241109-银联二维码同时维护了消费及二维码的计费配置明细-过滤了账户类型.csv";
    }

    /**
     * 文件目录
     *
     * @return 文件目录
     */
    public String getDirectoryPath() {
        return "E:\\日常迭代\\计费重构二期\\计费配置比对差异需治理的计费配置数据\\";
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
