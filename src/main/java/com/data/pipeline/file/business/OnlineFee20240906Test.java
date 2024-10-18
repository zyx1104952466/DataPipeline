package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import com.data.pipeline.utils.HttpRequestExample;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyux
 * @since 2024/9/6 下午4:27
 */
public class OnlineFee20240906Test extends BaseFileOperations {

    private static final String SOURCE_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\";
    private static final String SOURCE_FEE_CONFIG_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\zhangyux (1)\\";

    private static final String FILE_ORDER_NAME = "计费维度统计互联网收单交易(1).xlsx";
    private static final String FEE_CONFIG_FILE_NAME_00_10 = "00_10同时配置了计费配置.xlsx";
    private static final String FEE_CONFIG_FILE_NAME_01_11 = "01_11同时配置了计费配置.xlsx";
    private static final String FEE_CONFIG_FILE_NAME_02_12 = "02_12同时配置了计费配置.xlsx";

    @Test
    public void testReadFileContentOrder01() throws IOException {
        List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);
        System.out.println("dataList.size() = " + dataList.size());
        List<List<String>> newDataList = dataList.stream().filter(row -> "00".equals(row.get(2))).collect(Collectors.toList());
        System.out.println("12交易场景数： " + newDataList.size());
        newDataList.stream().map(row -> {
            String mercId = row.get(0);
            String trantype = row.get(1);
            String payMode = row.get(2);
            String requestBankType = row.get(3);
            String busiCode = row.get(4);
            String requestBankAccountType = row.get(5);
            String count = row.get(6);
            return "商户号: " + mercId + "，交易类型: " + trantype + "，支付方式: " + payMode + "，银行类型: " + requestBankType + "，业务编码: " + busiCode + "，银行账户类型: " + requestBankAccountType + "，交易总笔数: " + count;
        }).collect(Collectors.toSet()).forEach(System.out::println);

//        newDataList.forEach(row -> System.out.println("交易流水参数： " + row));

        List<List<String>> feeConfigDataList = excelFileReader.readFile(SOURCE_FEE_CONFIG_FILE_DIRECTORY + FEE_CONFIG_FILE_NAME_00_10);
        System.out.println("feeConfigDataList.size() = " + feeConfigDataList.size());
        Map<String, List<List<String>>> feeConfigMap = feeConfigDataList.stream().collect(Collectors.groupingBy(row -> row.get(0)));

        newDataList.forEach(row -> {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("===============================");

            String mercId = row.get(0);
            String trantype = row.get(1);

            Map<String, String> map1 = new HashMap<>();
            map1.put("usercode1", mercId);
            map1.put("trantype1", trantype);
            String templateNo = HttpRequestExample.sendPostRequest(map1);

            if (StringUtils.isBlank(templateNo)) {
                System.out.println("商户号: " + mercId + "，交易类型: " + trantype + "，未查询到模板编号");
            } else {
                System.out.println("商户号: " + mercId + "，交易类型: " + trantype + "，模板编号: " + templateNo);
            }

            String requestBankType = row.get(4);
            String requestBankAccountType = row.get(5);
            List<List<String>> feeConfigList = feeConfigMap.get(templateNo);
            if (feeConfigList == null) {
                return;
            }
            feeConfigList = feeConfigList.stream().filter(feeConfigRow -> {
                if (!requestBankType.equals(feeConfigRow.get(2)) && StringUtils.isNotBlank(feeConfigRow.get(2))) {
                    return false;
                }
                if (!requestBankAccountType.equals(feeConfigRow.get(3)) && StringUtils.isNotBlank(feeConfigRow.get(3))) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            if (feeConfigList.size() > 1) {
                System.out.println("交易请求参数 = " + row);
                System.out.println("符合该交易场景的计费配置 = " + feeConfigList);
            }
        });
    }

    @Test
    public void testReadFileContentOrder11() throws IOException {
        List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);
        System.out.println("dataList.size() = " + dataList.size());
        List<List<String>> newDataList = dataList.stream().filter(row -> "11".equals(row.get(2))).collect(Collectors.toList());
        System.out.println("newDataList.size() = " + newDataList.size());
        // 按row.get(0)进行分组
        Map<String, List<List<String>>> map = newDataList.stream().collect(Collectors.groupingBy(row -> row.get(0)));
        System.out.println("map.size() = " + map.size());
        System.out.printf("map = %s%n", map);
    }

    @Test
    public void testReadFileContentOrder02() throws IOException {
        List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);
        System.out.println("dataList.size() = " + dataList.size());
        List<List<String>> newDataList = dataList.stream().filter(row -> "02".equals(row.get(2))).collect(Collectors.toList());
        System.out.println("newDataList.size() = " + newDataList.size());
        // 按row.get(0)进行分组
        Map<String, List<List<String>>> map = newDataList.stream().collect(Collectors.groupingBy(row -> row.get(0)));
        System.out.println("map.size() = " + map.size());
    }

    @Test
    public void testReadFileContentOrder12() throws IOException {
        List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);
        System.out.println("dataList.size() = " + dataList.size());
        List<List<String>> newDataList = dataList.stream().filter(row -> "00".equals(row.get(2))).collect(Collectors.toList());
        System.out.println("newDataList.size() = " + newDataList.size());
        // 按row.get(0)进行分组
        Map<String, List<List<String>>> map = newDataList.stream().collect(Collectors.groupingBy(row -> row.get(0)));
        System.out.println("map.size() = " + map.size());
    }
}
