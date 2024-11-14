package com.data.pipeline.file.business;

import com.data.pipeline.file.BaseFileOperations;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 双重计费费率、固定金额为0，最小收费金额不为0的数据
 *
 * @author zhangyux
 * @since 2024/9/24 上午11:59
 */
public class SqlGenerateIsDoubleMinTest extends BaseFileOperations {

    public static final String SOURCE_FILE = "E:\\日常迭代\\计费重构二期\\20241023\\双重计费-费率固定金额为0-最小金额不为0.xlsx";
    public static final String TARGET_FILE = "E:\\日常迭代\\计费重构二期\\20241023\\双重计费-费率固定金额为0-最小金额不为0_%s.sql";

    public String TARGET_SQL = "update yspos_boss.urmtmfee_info t set t.min_fee_amt = 0 where t.id = '%s' and t.min_fee_amt = %s and t.fee_rat1 = 0 and t.fix_fee_amt = 0;";

    @Test
    public void testGenerateSql() throws IOException {

        List<List<String>> list = excelFileReader.readFile(SOURCE_FILE);

        if (list != null) {
            List<String> sqlList = list.stream().map(s -> {
                String id = s.get(0).replace(".00", "");
                String minFeeAmt = s.get(3);
                return String.format(TARGET_SQL, id, minFeeAmt);
            }).collect(Collectors.toList());
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
