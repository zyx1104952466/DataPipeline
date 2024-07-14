package com.data.pipeline.file;

import com.data.pipeline.entity.BusiCodeEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class FileOperationsTest {


    @Test
    public void testReadFileContent() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = FileContentReader.readFileContent("file.txt");
            dataList.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileAndParseData() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = FileContentReader.readFileContent("file.txt");
            // Parse the data
            List<BusiCodeEntity> list = BusiCodeParser.parseData(dataList);
            list.forEach(busiCodeEntity -> System.out.println(busiCodeEntity.toString()));
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFileAndParseData1() {
        try {
            // Example of reading from an absolute file path
            List<String> dataList = FileContentReader.readFileContent("file.txt");
            // Parse the data
            List<BusiCodeEntity> list = BusiCodeParser.parseData(dataList);
            list.forEach(busiCodeEntity -> {
                System.out.println(busiCodeEntity.getBusiCode());
                System.out.println(busiCodeEntity.getTran());
            });
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
