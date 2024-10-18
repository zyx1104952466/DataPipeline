package com.data.pipeline.file;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TxtFileOperationsTest extends BaseFileOperations {

    public static final String SOURCE_TEMP_FILE_TXT = "temp/file.txt";

    @Test
    public void testReadFileContent() throws IOException {
        List<String> dataList = txtFileReader.readFile(SOURCE_TEMP_FILE_TXT);
        System.out.println(dataList);
    }

    @Test
    public void testReadFileContentAndWrite() throws IOException {
        List<String> dataList = txtFileReader.readFile(SOURCE_TEMP_FILE_TXT);

        String targetFile = SOURCE_TEMP_FILE_TXT.replace(".txt", "_target.txt");
        txtFileWriter.writeFile(targetFile, dataList, true);
    }
}
