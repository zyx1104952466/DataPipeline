package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineFeeConfigPayMode01And11Test extends BaseFileOperations {

    /**
     * 文件目录
     */
    public static final String SOURCE_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\zhangyux (1)\\";

    /**
     * 文件名
     */
    private static final String FILE_NAME = "01_11同时配置了计费配置.xlsx";

    /**
     * 网银
     */
    @Test
    public void testReadFileContentOrder3() {
        try {
            List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_NAME);

            List<List<String>> list01 = dataList.stream().filter(row -> "01".equals(row.get(1))).collect(Collectors.toList());
            List<List<String>> list11 = dataList.stream().filter(row -> "11".equals(row.get(1))).collect(Collectors.toList());

            List<List<String>>  newDataList = new ArrayList<>();

            list01.forEach(row -> {

                String oldKey = row.get(0) + "-" + row.get(2)+ "-" + row.get(3);
                String oldValue = row.get(4) + "-" + row.get(5);

                list11.forEach(rowNew -> {
                    String newKey = rowNew.get(0) + "-" + rowNew.get(2) + "-" + rowNew.get(3);
                    String newValue = rowNew.get(4) + "-" + rowNew.get(5);
                    if (oldKey.equals(newKey) && !oldValue.equals(newValue)) {
                        logger.info("模板编号: {}, 01-网银PC费率: {}, 11-网银手机费率: {}", newKey, oldValue, newValue);
                        List<String> row1 = new ArrayList<>();
                        row1.add(rowNew.get(0));
                        row1.add(rowNew.get(2));
                        row1.add(rowNew.get(3));
                        row1.add(oldValue);
                        row1.add(newValue);
                        newDataList.add(row1);
                    }
                });
            });

            excelFileWriter.writeExcelFile("temp/存在网银PC以及手机区别的计费配置（模板编号-行别-银行账户类型）.xlsx", newDataList, true);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage());
        }
    }
}
