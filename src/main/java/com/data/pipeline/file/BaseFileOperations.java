package com.data.pipeline.file;

import com.data.pipeline.file.business.SqlConverter;
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

    public static final Logger LOGGER = LogManager.getLogger(BaseFileOperations.class);

    /** 文本文件读取器 */
    public final AbstractFileReader<String> textFileReader = new TextFileReader();

    /** Excel文件读取器 */
    public final AbstractFileReader<List<String>> excelFileReader = new ExcelFileReader();

    /** 文本文件写入器 */
    public final TextFileWriter textFileWriter = new TextFileWriter();

    /** Excel文件写入器 */
    public final ExcelFileWriter excelFileWriter = new ExcelFileWriter();

    /** CSV文件读取器 */
    public final AbstractFileReader<String[]> csvFileReader = new CsvFileReader();

    /**
     * 根据给定的模板和数据生成SQL脚本，并写入到目标文件中
     *
     * @param dataRows 数据行列表
     * @param template SQL模板
     * @param targetFile 目标文件路径格式化字符串
     * @param sqlConverter SQL转换器
     * @throws IOException 如果发生I/O错误
     */
    public void generateSqlScripts(List<String[]> dataRows, String template, String targetFile, SqlConverter sqlConverter) throws IOException {

        LOGGER.info("Generating SQL scripts...");
        LOGGER.info("template: {}", template);
        LOGGER.info("targetFile: {}", targetFile);

        List<String> sqlStatements = dataRows.stream()
            .map(row -> sqlConverter.convertToSql(template, row))
            .collect(Collectors.toList());
        this.writePaginatedSqlScripts(sqlStatements, targetFile);
    }

    /**
     * 将SQL语句以分页形式写入到文件中
     *
     * @param sqlStatements SQL语句列表
     * @param targetFilePathFormat 目标文件路径格式化字符串
     * @throws IOException 如果发生I/O错误
     */
    private void writePaginatedSqlScripts(List<String> sqlStatements, String targetFilePathFormat) throws IOException {

        LOGGER.info("Writing paginated SQL scripts...");

        if (sqlStatements == null) {
            LOGGER.error("SQL statements list is null.");
            return;
        }

        int statementIndex = 1;
        int fileIndex = 1;
        List<String> currentFileContent = new ArrayList<>();

        for (String sql : sqlStatements) {
            currentFileContent.add(sql);
            if (statementIndex % 500 == 0) {
                currentFileContent.add("COMMIT;");
            }
            if (statementIndex % 5000 == 0) {
                LOGGER.info("Writing SQL scripts to file: {}", fileIndex);
                textFileWriter.writeFile(String.format(targetFilePathFormat, fileIndex), currentFileContent, false);
                fileIndex++;
                currentFileContent.clear();
            }
            statementIndex++;
        }

        if (!currentFileContent.isEmpty()) {
            currentFileContent.add("COMMIT;");
            LOGGER.info("Writing SQL scripts to file: {}", fileIndex);
            textFileWriter.writeFile(String.format(targetFilePathFormat, fileIndex), currentFileContent, false);
        }
        LOGGER.info("SQL scripts generation completed.");
    }

    /**
     * 创建目标文件路径
     *
     * @param targetSuffix 目标文件后缀
     * @return 完整的目标文件路径
     */
    protected String createTargetFilePath(String targetSuffix) {
        return getDirectoryPath() + getSourceFileName().replace(getSourceFileSuffix(), targetSuffix);
    }

    protected String getSourceFileName() {
        throw new UnsupportedOperationException("Method getSourceFileName() should be implemented by subclass.");
    }

    protected String getDirectoryPath() {
        throw new UnsupportedOperationException("Method getSourceFileName() should be implemented by subclass.");
    }

    protected String getSourceFileSuffix() {
        throw new UnsupportedOperationException("Method getSourceFileName() should be implemented by subclass.");
    }
}
