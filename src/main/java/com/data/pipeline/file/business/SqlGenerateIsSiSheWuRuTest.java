package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhangyux
 * @since 2024/9/24 上午11:59
 */
public class SqlGenerateIsSiSheWuRuTest extends BaseFileOperations {

    public static final String SOURCE_FILE = "E:\\日常迭代\\计费重构二期\\需要修复小数进位规则标识的数据.csv";
    public static final String TARGET_FILE = "E:\\日常迭代\\计费重构二期\\需要修复小数进位规则标识的数据_%s.sql";

    public String TARGET_SQL = "update yspos_boss.urmtmfee_base t set t.is_si_wu_ru = 'N', t.update_time = sysdate where rate_id = '%s' and t.is_si_wu_ru is null;";

    @Test
    public void testGenerateSql() throws IOException {

        List<String> list = textFileReader.readFile(SOURCE_FILE);

        if (list != null) {
            List<String> sqlList = list.stream().map(s -> String.format(TARGET_SQL, s)).collect(Collectors.toList());
            // 每500条添加一个commit，每5000条生成一个文件
            int count = 1;
            int fileCount = 1;

            List<String> fileDataList = new ArrayList<>();

            for (String s : sqlList) {
                fileDataList.add(s);
                if (count % 500 == 0) {
                    fileDataList.add("COMMIT;");
                }
                if (count % 5000 == 0) {
                    Optional<String> fileName = Optional.of(String.format(TARGET_FILE, fileCount));
                    textFileWriter.writeFile(fileName.orElse("temp/需要修复小数进位规则标识的数据.sql"), fileDataList,
                        false);
                    fileCount++;
                    fileDataList = new ArrayList<>();
                }
                count++;
            }

            // 如果fileDataList中还有剩余的数据，写入最后一个文件
            if (!fileDataList.isEmpty()) {
                // 添加最后一次的提交语句
                fileDataList.add("COMMIT;");
                Optional<String> fileName = Optional.of(String.format(TARGET_FILE, fileCount));
                textFileWriter.writeFile(fileName.orElse("temp/需要修复小数进位规则标识的数据.sql"), fileDataList, false);
            }
        }
    }
}
