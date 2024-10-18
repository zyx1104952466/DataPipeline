package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import com.data.pipeline.file.generate.SqlGenerate;
import com.data.pipeline.file.txt.TextFileWriter;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成脚本文件（修复配错了业务代码的收单消费计费配置，将业务代码从理财POS调整为消费）
 *
 * @author zhangyux
 * @since 2024/8/6 23:15
 */
public class SqlGenerateTest extends BaseFileOperations {

    /**
     * .xlsx文件路径
     */
    public static final String TEMP_20240802_XLSX = "temp/配错业务代码_理财POS的计费配置 - 需云商服确认的数据.xlsx";

    @Test
    public void testReadFileContent3() {
        try {
            // Example of reading from an absolute file path
            List<List<String>> dataList = excelFileReader.readFile(TEMP_20240802_XLSX);

            List<List<String>> writeDataList = new ArrayList<>();
            dataList.forEach(list -> {
                List<String> writeData = new ArrayList<>();
                writeData.add(list.get(0).replace(".00", ""));
                writeData.add(list.get(1));
                writeData.add(list.get(2));
                writeDataList.add(writeData);
            });

            List<String> sqlList = new ArrayList<>();
            // count计数器
            AtomicInteger count = new AtomicInteger(0);

            writeDataList.forEach(list -> {
                String targetSql = SqlGenerate.generateRollbackSql(list.get(0), list.get(1));
                sqlList.add(targetSql);
                logger.info("生成的sql语句：{}", targetSql);
                if (count.addAndGet(1) % 500 == 0) {
                    sqlList.add("COMMIT;");
                }
            });
            sqlList.add("COMMIT;");

            TextFileWriter textFileWriter = new TextFileWriter();
            textFileWriter.writeFile("temp/配错业务代码_理财POS的计费配置-回滚.txt", sqlList, true);
        } catch (IOException e) {
            logger.info("An error occurred: " + e.getMessage());
        }
    }
}
