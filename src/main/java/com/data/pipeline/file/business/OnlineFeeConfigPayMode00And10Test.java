package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineFeeConfigPayMode00And10Test extends BaseFileOperations {

    /**
     * 文件目录
     */
    public static final String SOURCE_FILE_DIRECTORY = "E:\\日常迭代\\计费重构二期\\zhangyux (1)\\";

    /**
     * 文件名
     */
    private static final String FILE_NAME = "00_10同时配置了计费配置.xlsx";

    /**
     * 平台账户
     */
    @Test
    public void testReadFileContentOrder5() {
        try {
            List<List<String>> dataList = excelFileReader.readFile(SOURCE_FILE_DIRECTORY + FILE_NAME);

            List<List<String>> list00 =
                dataList.stream().filter(row -> "00".equals(row.get(1))).collect(Collectors.toList());

            LOGGER.info("list00: {}", list00);
            List<List<String>> list10 =
                dataList.stream().filter(row -> "10".equals(row.get(1))).collect(Collectors.toList());
            LOGGER.info("list10: {}", list10);

            List<List<String>> newDataList = new ArrayList<>();

            list00.forEach(row -> {

                String oldKey = row.get(0) + "-" + row.get(2) + "-" + row.get(3);
                String oldValue = row.get(4) + "-" + row.get(5);

                list10.forEach(rowNew -> {
                    String newKey = rowNew.get(0) + "-" + rowNew.get(2) + "-" + rowNew.get(3);
                    String newValue = rowNew.get(4) + "-" + rowNew.get(5);
                    if (oldKey.equals(newKey) && !oldValue.equals(newValue)) {
                        LOGGER.info("模板编号: {}, 00-平台账户PC费率: {}, 10-平台账户手机费率: {}", newKey, oldValue,
                            newValue);
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
/*
            excelFileWriter.writeExcelFile("temp/存在平台账户PC以及手机区别的计费配置（模板编号-行别-银行账户类型）.xlsx",
                newDataList, true);*/
        } catch (IOException e) {
            LOGGER.error("An error occurred: {}", e.getMessage());
        }
    }
}
