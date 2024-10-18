package com.data.pipeline.file.excel;

import com.data.pipeline.enums.FileTypeEnum;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExcelFileWriter {

    private static final String RESOURCE_BASE_PATH = "src/main/resources/";

    /**
     * Writes data to an Excel file (.xls or .xlsx). If the file exists, it will be overwritten. The path can be an
     * absolute file path or a relative resource path.
     *
     * @param filePath       the path of the file to write
     * @param rowData        the list of lists representing rows and cells to write to the file
     * @param isResourcePath true if the file path is a relative resource path, false if it's an absolute file path
     * @throws IOException if an I/O error occurs writing to the file
     */
    public void writeExcelFile(String filePath, List<List<String>> rowData, boolean isResourcePath) throws IOException {
        Workbook workbook = createWorkbook(filePath);
        Sheet sheet = workbook.createSheet("Sheet1");

        for (int i = 0; i < rowData.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> rowValues = rowData.get(i);
            for (int j = 0; j < rowValues.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowValues.get(j));
            }
        }

        Path path = resolvePath(filePath, isResourcePath);
        try (FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }

    private Workbook createWorkbook(String filePath) {
        if (filePath.endsWith(FileTypeEnum.XLSX.getType())) {
            return new XSSFWorkbook();
        } else if (filePath.endsWith(FileTypeEnum.XLS.getType())) {
            return new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + filePath);
        }
    }

    private Path resolvePath(String filePath, boolean isResourcePath) throws IOException {
        Path path;
        if (isResourcePath) {
            path = Paths.get(RESOURCE_BASE_PATH, filePath);
        } else {
            path = Paths.get(filePath);
        }

        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        return path;
    }
}
