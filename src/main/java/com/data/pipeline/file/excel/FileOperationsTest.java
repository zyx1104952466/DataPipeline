package com.data.pipeline.file.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class FileOperationsTest {

    private static final Logger logger = LogManager.getLogger(FileOperationsTest.class);

    @Test
    public void testReadFileContent() {
        try {
            // Example of reading from an absolute file path
            List<List<String>> dataList = ExcelFileReader.readExcelFile("temp/20240706.xlsx");
            // 遍历dataList并打印内容
            dataList.forEach(list -> logger.info("解析后的结果{}", list));
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }
}
