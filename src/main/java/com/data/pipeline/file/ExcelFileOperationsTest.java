package com.data.pipeline.file;

import com.data.pipeline.entity.FeeEntity;
import com.data.pipeline.file.excel.ExcelFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFileOperationsTest {

    private static final Logger logger = LogManager.getLogger(ExcelFileOperationsTest.class);
    public static final String TEMP_20240706_XLSX = "temp/20240706.xlsx";
    public static final String TEMP_20240706_XLS = "temp/20240706.xls";

    AbstractFileReader<List<String>> fileReader = new ExcelFileReader();

    /**
     * Test reading the content of a file by .xlsx
     */
    @Test
    public void testReadFileContent() {
        try {
            // Example of reading from an absolute file path
            List<List<String>> dataList = fileReader.readFile(TEMP_20240706_XLSX);
            // 遍历dataList并打印内容
            dataList.forEach(list -> logger.info("解析后的结果{}", list));
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Test reading the content of a file by .xls
     */
    @Test
    public void testReadFileContent1() {
        try {
            // Example of reading from an absolute file path
            List<List<String>> dataList = fileReader.readFile(TEMP_20240706_XLS);
            // 遍历dataList并打印内容
            dataList.forEach(list -> logger.info("解析后的结果{}", list));
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Test reading the content of a file by .xls
     */
    @Test
    public void testReadFileContent2() {
        try {
            // Example of reading from an absolute file path
            List<List<String>> dataList = fileReader.readFile("temp/江西分公司 (1).xls");

            List<FeeEntity> feeEntities = new ArrayList<>();
            dataList.forEach(list ->{
                FeeEntity feeEntity = new FeeEntity();
                feeEntity.setMercId(list.get(0));
                feeEntity.setBusiType(list.get(1));
                feeEntity.setCardType(list.get(2));
                feeEntity.setFeeMode(list.get(3));
                feeEntity.setCarryMode(list.get(4));
                feeEntity.setFeeCalType(list.get(5));
                feeEntity.setMinFeeAmt(list.get(6));
                feeEntity.setMaxFeeAmt(list.get(7));
                feeEntity.setFeeRate(list.get(8));
                feeEntity.setFixFeeAmt(list.get(9));
                feeEntities.add(feeEntity);
            });
            logger.info("【江西分公司 (1).xls】解析后的结果{}", feeEntities);
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }
}
