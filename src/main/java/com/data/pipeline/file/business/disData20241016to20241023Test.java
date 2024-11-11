package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import com.data.pipeline.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 同时配置有银联二维码和消费的商户处理类
 *
 * @author zhangyux
 * @since 2024/9/2 下午2:21
 */
public class disData20241016to20241023Test extends BaseFileOperations {

    /**
     * .xlsx文件路径
     */
    public static final String SOURCE_FILE = "E:\\日常迭代\\计费重构二期\\20241023\\20241015-20241023差异数据.xlsx";
    public static final String SOURCE_FILE1 = "E:\\日常迭代\\计费重构二期\\20241023\\双重计费-费率固定金额为0-最小金额不为0.xlsx";

    @Test
    public void test() throws IOException {
        List<List<String>> records = excelFileReader.readFile(SOURCE_FILE);

        System.out.println("records size: " + records.size());

        if (records == null || records.isEmpty()) {
            return; // 如果文件为空或无法读取，则直接返回
        }

        Set set = new HashSet();
        Set set1 = getSet();
        Set methodSet = new HashSet();

        int orderTradeFeeAmtV1Count = 0;
        int orderTradeFeeAmtV1NonMinCount = 0;
        int negativeTradeFee2Count = 0;
        int onlineTradeCalcFeeCount = 0;
        int positiveTradeFeeCount = 0;
        BigDecimal orderTradeFeeAmtV1Sum = BigDecimal.ZERO;
        BigDecimal positiveTradeFeeSum = BigDecimal.ZERO;
        for (int i = 1; i < records.size(); i++) { // 跳过标题行
            List<String> record = records.get(i);

            try {
                String logNo = record.get(2);
                String userCode = record.get(5);
                String newFeeResultInfoSnapshot = record.get(11);
                String paramsJson = record.get(15);
                String oldFeeRspSnapshot = record.get(16);

                Map<String, Object> paramsMap = JsonUtil.objectMapper.readValue(paramsJson, new TypeReference<Map<String, Object>>(){});
                String feeMethodName = (String) paramsMap.get("name");
                methodSet.add(feeMethodName);
                Map<String, Object> param1 = (Map<String, Object>) paramsMap.get("param1");

                Map newFeeResultInfoSnapshotMap = JsonUtil.objectMapper.readValue(newFeeResultInfoSnapshot, Map.class);
                Map oldFeeRspSnapshotMap = JsonUtil.objectMapper.readValue(oldFeeRspSnapshot, Map.class);

                if ("orderTradeFeeAmtV1".equals(feeMethodName)) {
                    if (!set1.contains(userCode)) {
                        orderTradeFeeAmtV1NonMinCount++;
                        set.add(userCode+"-"+param1.get("busiCode"));

                        String newpayeeFee = newFeeResultInfoSnapshotMap.get("payeeFee").toString();
                        String oldpayeeFee = oldFeeRspSnapshotMap.get("payeeFee").toString();
                        BigDecimal result = new BigDecimal(newpayeeFee).subtract(new BigDecimal(oldpayeeFee));
                        System.out.println("订单计费-非最小金额原因: " + userCode+", " + logNo+", "+param1.get("busiCode") + "差异金额：" + result);

                    } else {
                        String newsFee = newFeeResultInfoSnapshotMap.get("sFee").toString();
                        String oldsFee = oldFeeRspSnapshotMap.get("sFee").toString();
                        String newdFee = newFeeResultInfoSnapshotMap.get("dFee").toString();
                        String olddFee = oldFeeRspSnapshotMap.get("dFee").toString();
                        if (!newsFee.equals(oldsFee)) {
                            BigDecimal result = new BigDecimal(newsFee).subtract(new BigDecimal(oldsFee));
                            orderTradeFeeAmtV1Sum = orderTradeFeeAmtV1Sum.add(result);
                        }
                        if (!newdFee.equals(olddFee)) {
                            BigDecimal result = new BigDecimal(newdFee).subtract(new BigDecimal(olddFee));
                            orderTradeFeeAmtV1Sum = orderTradeFeeAmtV1Sum.add(result);
                        }
                        orderTradeFeeAmtV1Count++;
                    }
                }

                if ("negativeTradeFee2".equals(feeMethodName)) {
                    negativeTradeFee2Count++;
                }

                if ("onlineTradeCalcFee".equals(feeMethodName)) {
                    onlineTradeCalcFeeCount++;
                }

                if ("positiveTradeFee".equals(feeMethodName)) {
                    positiveTradeFeeCount++;
                    String newFee = newFeeResultInfoSnapshotMap.get("fee").toString();
                    String oldFee = oldFeeRspSnapshotMap.get("fee").toString();
                    BigDecimal result = new BigDecimal(newFee).subtract(new BigDecimal(oldFee));
                    System.out.println("mercId:" + userCode + ", logNo:" + logNo + ", positiveTradeFee差异金额：" + result);
                    System.out.println();
                    positiveTradeFeeSum = positiveTradeFeeSum.add(result);
                }
            } catch (IOException e) {
                System.err.println("Failed to parse JSON for record: " + record);
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Record is missing required fields: " + record);
                e.printStackTrace();
            }
        }

        System.out.println("=====================================");
        System.out.println("Set size: " + set.size());
        set.forEach(System.out::println);
        System.out.println("=====================================");
        System.out.println("Method Set size: " + methodSet.size());
        System.out.println(methodSet);
        System.out.println("=====================================");
        System.out.println("orderTradeFeeAmtV1Count: " + orderTradeFeeAmtV1Count);
        System.out.println("orderTradeFeeAmtV1Sum: " + orderTradeFeeAmtV1Sum);
        System.out.println("=====================================");
        System.out.println("orderTradeFeeAmtV1NonMinCount: " + orderTradeFeeAmtV1NonMinCount);
        System.out.println("=====================================");
        System.out.println("negativeTradeFee2Count: " + negativeTradeFee2Count);
        System.out.println("=====================================");
        System.out.println("onlineTradeCalcFeeCount: " + onlineTradeCalcFeeCount);
        System.out.println("=====================================");
        System.out.println("positiveTradeFeeCount: " + positiveTradeFeeCount);
        System.out.println("positiveTradeFeeSum: " + positiveTradeFeeSum);

        System.out.println("total:" + (orderTradeFeeAmtV1Count + orderTradeFeeAmtV1NonMinCount + negativeTradeFee2Count + onlineTradeCalcFeeCount + positiveTradeFeeCount));

    }

    private Set getSet() throws IOException {
        Set set1 = new HashSet();
        List<List<String>> records1 = excelFileReader.readFile(SOURCE_FILE1);
        records1.forEach(record -> set1.add(record.get(2)));
        return set1;
    }
}
