package com.data.pipeline.file;

import com.data.pipeline.file.excel.ExcelFileReader;
import com.data.pipeline.file.excel.ExcelFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFileOperationsTest {

    private static final Logger logger = LogManager.getLogger(ExcelFileOperationsTest.class);

    /**
     * .xlsx文件路径
     */
    public static final String TEMP_20240706_XLSX = "temp/20240706.xlsx";

    /**
     * .xls文件路径
     */
    public static final String TEMP_XLS = "temp/20240706.xls";

    /**
     * Excel文件读取器
     */
    AbstractFileReader<List<String>> fileReader = new ExcelFileReader();

    /**
     * Excel文件写入器
     */
    ExcelFileWriter excelFileWriter = new ExcelFileWriter();

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
            List<List<String>> dataList = fileReader.readFile(TEMP_XLS);
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

            List<List<String>> writeDataList = new ArrayList<>();
            dataList.forEach(list ->{
                List<String> writeData = new ArrayList<>();
                writeData.add(list.get(0));
                writeData.add(list.get(1));
                writeData.add(list.get(2));
                writeData.add(list.get(3));
                writeData.add(list.get(4));
                writeData.add(list.get(5));
                writeData.add(list.get(6));
                writeData.add(list.get(7));
                writeData.add(list.get(8));
                writeData.add(list.get(9));
                writeDataList.add(writeData);
            });

            excelFileWriter.writeExcelFile("temp/江西分公司 (1)_new.xls", writeDataList, true);
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }
}
