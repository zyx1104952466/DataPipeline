package com.data.pipeline.file.business;

import com.data.pipeline.entity.FeeEntity;
import com.data.pipeline.file.BaseFileOperations;
import com.data.pipeline.file.parser.RegexParser;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineFeeLogOrderTest extends BaseFileOperations {

    private static final String SOURCE_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\生产日志数据\\fee_server_order\\";

    private static final String FILE_ORDER_NAME = "info.20240726.0.log";

    /**
     * Test reading the content of a file
     */
    @Test
    public void testReadFileContentOrder() {
        try {
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);

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

            excelFileWriter.writeExcelFile("temp/online_fee_order_log.xlsx", rowData, true);
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
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_ORDER_NAME);

            // 过滤掉不符合条件的数据
            dataList = dataList.stream().filter(line -> line.contains("计费参数：")).collect(Collectors.toList());

            List<String> list = RegexParser.parseDataByString(dataList);

            txtFileWriter.writeFile("temp/online_fee_order_log.txt", list, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Test reading the content of a file
     */
    @Test
    public void testReadFileContentOrder2() {
        try {
            List<String> dataList = txtFileReader.readFile(SOURCE_FILE_DIRECTORY);
            // 过滤掉不符合条件的数据
            dataList = dataList.stream().filter(line -> line.contains("计费参数：")).collect(Collectors.toList());
            List<FeeEntity> list = RegexParser.parseData(dataList);

            List<List<String>> rowData = new ArrayList<>();
            for (FeeEntity fee : list) {

                if ("01".equals(fee.getPayMode()) || "11".equals(fee.getPayMode())) {
                    //                if ("02".equals(fee.getPayMode()) || "12".equals(fee.getPayMode())) {
                    //                if ("00".equals(fee.getPayMode()) || "10".equals(fee.getPayMode())) {
                } else {
                    continue;
                }

                List<String> header = new ArrayList<>();
                header.add(fee.getTranType());
                header.add(fee.getPayeeCustId());
                header.add(fee.getPayerCustId());
                header.add(fee.getPayMode());
                header.add(fee.getBankType());
                header.add(fee.getBusiCode());
                header.add(fee.getBankAccountType());
                rowData.add(header);
            }

            excelFileWriter.writeExcelFile("temp/online_fee_log_paymode_01_11.xlsx", rowData, true);
            //            excelFileWriter.writeExcelFile("temp/online_fee_log_paymode_02_12.xlsx", rowData, true);
            //            excelFileWriter.writeExcelFile("temp/online_fee_log_paymode_00_10.xlsx", rowData, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }
}
