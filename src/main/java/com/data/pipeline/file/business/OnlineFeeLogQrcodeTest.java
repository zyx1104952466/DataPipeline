package com.data.pipeline.file.business;

import com.data.pipeline.entity.FeeEntity;
import com.data.pipeline.file.BaseFileOperations;
import com.data.pipeline.file.parser.RegexParser;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnlineFeeLogQrcodeTest extends BaseFileOperations {

    private static final String SOURCE_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\生产日志数据\\fee_server_qrcode\\";

    private static final String FILE_QRCODE_NAME = "info.20240726.0.log";

    /**
     * Test reading the content of a file
     */
    @Test
    public void testReadFileContent() {
        try {
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_QRCODE_NAME);

            // 过滤掉不符合条件的数据
            dataList = dataList.stream().filter(line -> line.contains("计费参数：")).collect(Collectors.toList());

            List<FeeEntity> list = RegexParser.parseData(dataList);

            List<List<String>> rowData = new ArrayList<>();
            for (FeeEntity fee : list) {

                List<String> header = new ArrayList<>();
                header.add(fee.getTranType());
                header.add(fee.getPayMode());
                header.add(fee.getBusiCode());
                header.add(fee.getBankAccountType());
                header.add(fee.getPayeeCustId());
                header.add(fee.getPayerCustId());
                header.add(fee.getAreacode());
                header.add(fee.getSrcCustId());
                rowData.add(header);
            }

            excelFileWriter.writeExcelFile("temp/online_fee_log.xlsx", rowData, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * 读取日志文件内容，将计费请求参数信息过滤出来，并转换成参数对象，再识别出部分维度信息的所有枚举值组合。
     */
    @Test
    public void testReadFileContent1() {
        try {
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_QRCODE_NAME);

            // 过滤掉不符合条件的数据
            dataList = dataList.stream().filter(line -> line.contains("计费参数：")).collect(Collectors.toList());

            List<FeeEntity> list = RegexParser.parseData(dataList);

            Map<String, Integer> map = new HashMap<>();
            list.forEach(feeEntity -> {
                String key =
                    feeEntity.getTranType() + "-" + feeEntity.getPayMode() + "-" + feeEntity.getBusiCode() + "-" + feeEntity.getBankType() + "-" + feeEntity.getBankAccountType() + "-" + feeEntity.getAreacode();
                map.put(key, map.getOrDefault(key, 0) + 1);
            });

            // 按照次数排序，从大到小，并且每个key单独打印一行
            map.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .forEach(entry -> logger.info("key: {}, value: {}", entry.getKey(), entry.getValue()));

            List<List<String>> rowData = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String[] keys = entry.getKey().split("-");
                List<String> header = new ArrayList<>();
                header.add(keys[0]);
                header.add(keys[1]);
                header.add(keys[2]);
                header.add(keys[3]);
                header.add(keys[4]);
                header.add(keys[5]);
                header.add(entry.getValue().toString());
                rowData.add(header);
            }

            excelFileWriter.writeExcelFile("temp/online_fee_qrcode_log_enum.xlsx", rowData, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Test reading the content of a file and writing it to another file
     */
    @Test
    public void testReadFileContentOrderToTxt() {
        try {
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_QRCODE_NAME);

            // 过滤掉不符合条件的数据
            dataList = dataList.stream().filter(line -> line.contains("计费参数：")).collect(Collectors.toList());

            List<String> list = RegexParser.parseDataByString(dataList);

            txtFileWriter.writeFile("temp/online_fee_qrcode_log.txt", list, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }
}
