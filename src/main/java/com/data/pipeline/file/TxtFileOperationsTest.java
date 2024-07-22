package com.data.pipeline.file;

import com.data.pipeline.entity.BusCodeEntity;
import com.data.pipeline.file.parser.RegexBusCodeParser;
import com.data.pipeline.file.txt.TextFileReader;
import com.data.pipeline.file.txt.TextFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TxtFileOperationsTest {

    private static final Logger logger = LogManager.getLogger(TxtFileOperationsTest.class);

    public static final String SOURCE_TEMP_FILE_TXT = "temp/file.txt";
    public static final String TARGET_TEMP_FILE_1_TXT = "temp/file1.txt";
    public static final String TARGET_TEMP_FILE_2_TXT = "temp/file2.txt";

    AbstractFileReader<String> fileReader = new TextFileReader();
    TextFileWriter textFileWriter = new TextFileWriter();

    /**
     * Test reading the content of a file
     */
    @Test
    public void testReadFileContent() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = fileReader.readFile(SOURCE_TEMP_FILE_TXT);
            logger.info("解析后的结果：{}", dataList);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Test reading the content of a file and writing it to another file
     */
    @Test
    public void testReadFileContentAndWrite() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = fileReader.readFile(SOURCE_TEMP_FILE_TXT);
            logger.info("解析后的结果：{}", dataList);

            textFileWriter.writeFile(TARGET_TEMP_FILE_1_TXT, dataList, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Test reading the content of a file, parsing it, and writing the parsed data to another file
     */
    @Test
    public void testReadAndParseBusCodeData() {
        try {
            // Read data from an absolute file path
            List<String> rawData = fileReader.readFile(SOURCE_TEMP_FILE_TXT);
            logger.info("解析后rawData的结果：{}", rawData);

            // Parse the raw data into BusCodeEntity objects
            List<BusCodeEntity> parsedData = RegexBusCodeParser.parseData(rawData);
            logger.info("解析后的parsedData结果：{}", parsedData);

            // Convert the parsed data to strings
            List<String> parsedDataStrings = parsedData.stream()
                    .map(BusCodeEntity::toString)
                    .collect(Collectors.toList());

            // Write the parsed data to a file in the resources directory
            textFileWriter.writeFile(TARGET_TEMP_FILE_2_TXT, parsedDataStrings, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }

    /**
     * Test reading the content of a file and parsing it
     */
    @Test
    public void testReadFileAndParseData1() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = fileReader.readFile(SOURCE_TEMP_FILE_TXT);
            // Parse the data
            List<BusCodeEntity> list = RegexBusCodeParser.parseData(dataList);
            list.forEach(busCodeEntity -> {
                logger.info("busCode：{}", busCodeEntity.getBusCode());
                logger.info("tranId：{}", busCodeEntity.getTranId());
            });
        } catch (IOException e) {
            logger.error("An error occurred: " + e.getMessage());
        }
    }
}
