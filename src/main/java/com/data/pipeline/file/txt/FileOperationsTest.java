package com.data.pipeline.file.txt;

import com.data.pipeline.entity.BusCodeEntity;
import com.data.pipeline.file.BusiCodeParser;
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
            List<String> dataList = FileContentReader.readFileContent("temp/file.txt");
            dataList.forEach(line -> logger.info("解析后的结果：{}", line));
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    @Test
    public void testReadFileAndParseData() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = FileContentReader.readFileContent("temp/file.txt");
            // Parse the data
            List<BusCodeEntity> list = BusiCodeParser.parseData(dataList);
            list.forEach(busCodeEntity -> logger.info("解析后的结果：{}", busCodeEntity));
        } catch (IOException e) {
            logger.error("An error occurred:  {}", e.getMessage());
        }
    }

    @Test
    public void testReadFileAndParseData1() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = FileContentReader.readFileContent("temp/file.txt");
            // Parse the data
            List<BusCodeEntity> list = BusiCodeParser.parseData(dataList);
            list.forEach(busCodeEntity -> {
                logger.info("busCode:{}", busCodeEntity.getBusCode());
                logger.info("tranId：{}", busCodeEntity.getTranId());
            });
        } catch (IOException e) {
            logger.error("An error occurred: " + e.getMessage());
        }
    }
}
