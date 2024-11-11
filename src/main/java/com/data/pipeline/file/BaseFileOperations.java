package com.data.pipeline.file;

import com.data.pipeline.file.business.SqlCovert;
import com.data.pipeline.file.csv.CsvFileReader;
import com.data.pipeline.file.excel.ExcelFileReader;
import com.data.pipeline.file.excel.ExcelFileWriter;
import com.data.pipeline.file.txt.TextFileReader;
import com.data.pipeline.file.txt.TextFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyux
 * @since 2024/8/6 22:55
 */
public class BaseFileOperations {

    public static final Logger logger = LogManager.getLogger(BaseFileOperations.class);

    /**
     * Txt文件读取类
     */
    public AbstractFileReader<String> txtFileReader = new TextFileReader();

    /**
     * Excel文件读取类
     */
    public AbstractFileReader<List<String>> excelFileReader = new ExcelFileReader();

    /**
     * Txt文件写入类
     */
    public TextFileWriter txtFileWriter = new TextFileWriter();

    /**
     * Excel文件写入类
     */
    public ExcelFileWriter excelFileWriter = new ExcelFileWriter();

    /**
     * CSV文件读取类
     */
    public AbstractFileReader<String[]> csvFileReader = new CsvFileReader();

    public void generateScript(List<String[]> list, String sqlTemplate, String targetFile, SqlCovert sqlCovert) throws IOException {

        // 2、生成sql
        List<String> sqlList = list.stream().map(split -> sqlCovert.covertSql(sqlTemplate, split)).collect(Collectors.toList());

        // 3、生成脚本文件（每500条添加一个commit，每5000条生成一个文件）
        generatePagingSqlScript(sqlList, targetFile);
    }

    /**
     * 分页生成sql脚本文件
     *
     * @param sqlList     sql集合
     * @param TARGET_FILE 目标文件
     * @throws IOException IO异常
     */
    public void generatePagingSqlScript(List<String> sqlList, String TARGET_FILE) throws IOException {

        if (null == sqlList) {
            logger.error("sqlList is null");
            return;
        }

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
                txtFileWriter.writeFile(String.format(TARGET_FILE, fileCount), fileDataList, false);
                fileCount++;
                fileDataList = new ArrayList<>();
            }
            count++;
        }

        // 如果fileDataList中还有剩余的数据，写入最后一个文件
        if (!fileDataList.isEmpty()) {
            // 添加最后一次的提交语句
            fileDataList.add("COMMIT;");
            txtFileWriter.writeFile(String.format(TARGET_FILE, fileCount), fileDataList, false);
        }
    }

    public String createTargetFile(String targetSuffix) {
        return getFileDirectory() + getSourceFile().replace(getFileSuffix(), targetSuffix);
    }

    public String getSourceFile() {
        return null;
    }

    public String getFileDirectory() {
        return null;
    }

    public String getFileSuffix() {
        return null;
    }
}
