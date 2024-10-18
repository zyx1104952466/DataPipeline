package com.data.pipeline.file;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ExcelFileOperationsTest extends BaseFileOperations {

    /**
     * .xlsx文件路径
     */
    public static final String SOURCE_FILE = "temp/银联二维码同时有消费二维码的商户.xlsx";

    @Test
    public void testReadFileContent() throws IOException {
        List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE);
        System.out.println("dataList size: " + dataList.size());
        dataList.stream().flatMap(List::stream).filter(line -> !"".equals(line) && null != line).forEach(System.out::println);
    }
}
