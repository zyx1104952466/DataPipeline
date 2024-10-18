package com.data.pipeline.file;

import com.data.pipeline.file.excel.ExcelFileReader;
import com.data.pipeline.file.excel.ExcelFileWriter;
import com.data.pipeline.file.txt.TextFileReader;
import com.data.pipeline.file.txt.TextFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
}
