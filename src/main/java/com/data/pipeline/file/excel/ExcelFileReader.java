package com.data.pipeline.file.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExcelFileReader {

    /**
     * Reads the content of an Excel file from the specified path.
     * The path can be a resource path within the project's resources directory or an absolute file path.
     *
     * @param filePath the path of the Excel file to read
     * @return a List of List of strings, each representing a row of the Excel file
     * @throws IOException if an I/O error occurs reading from the file
     */
    public static List<List<String>> readExcelFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            // If the path is an absolute file path
            try (InputStream fileInputStream = Files.newInputStream(path)) {
                return parseExcelFile(fileInputStream);
            }
        } else {
            // If the path is a resource path
            try (InputStream resourceStream = ExcelFileReader.class.getClassLoader().getResourceAsStream(filePath)) {
                if (resourceStream == null) {
                    throw new IOException("Resource not found: " + filePath);
                }
                return parseExcelFile(resourceStream);
            }
        }
    }

    private static List<List<String>> parseExcelFile(InputStream inputStream) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Reading the first sheet
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValueAsString(cell));
                }
                data.add(rowData);
            }
        }
        return data;
    }

    private static String getCellValueAsString(Cell cell) {
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
