package com.data.pipeline.file.excel;

import com.data.pipeline.file.AbstractFileReader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelFileReader extends AbstractFileReader<List<String>> {

    private static final int MAX_COLUMNS = 10;

    @Override
    protected List<List<String>> parseFile(InputStream inputStream, String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        Workbook workbook = null;

        try {
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                throw new IllegalArgumentException("The specified file is not Excel file");
            }

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < MAX_COLUMNS; i++) {
                    rowData.add(getCellValueAsString(row.getCell(i)));
                }
                data.add(rowData);
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return data;
    }

    private String getCellValueAsString(Cell cell) {

        if (null == cell) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown Cell Type";
        }
    }
}
