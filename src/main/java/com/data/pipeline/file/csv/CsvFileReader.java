package com.data.pipeline.file.csv;

import com.data.pipeline.file.AbstractFileReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvFileReader extends AbstractFileReader<String[]> {

    public static final Logger LOGGER = LogManager.getLogger(CsvFileReader.class);

    @Override
    public List<String[]> parseFile(InputStream inputStream, String filePath) {
        List<String[]> sqlList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] header = reader.readNext(); // 读取表头
            LOGGER.info("表头: {}", Arrays.toString(header));
            String[] line;
            while ((line = reader.readNext()) != null) {
                sqlList.add(line);
            }
        } catch (CsvValidationException e) {
            System.out.println("读取文件失败: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO异常: " + e.getMessage());
        }
        LOGGER.info("Read {} rows from file: {}", sqlList.size(), filePath);
        return sqlList;
    }
}
