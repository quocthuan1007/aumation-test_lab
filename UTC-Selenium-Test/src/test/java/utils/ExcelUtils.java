package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelUtils — Tiện ích đọc dữ liệu test từ file Excel (.xlsx).
 *
 * Cấu trúc file Excel:
 *   Hàng 0 (header): TC_ID | Username | Password | ExpectedResult | Description
 *   Hàng 1+        : dữ liệu từng test case
 */
public class ExcelUtils {

    // Đường dẫn tới file Excel test data (tính từ thư mục gốc project)
    public static final String EXCEL_FILE_PATH = "src/test/resources/LoginTestData.xlsx";
    public static final String SHEET_NAME      = "LoginTestCases";

    // Index của từng cột trong Excel
    public static final int COL_TC_ID           = 0;
    public static final int COL_USERNAME        = 1;
    public static final int COL_PASSWORD        = 2;
    public static final int COL_EXPECTED_RESULT = 3;
    public static final int COL_DESCRIPTION     = 4;

    /**
     * Đọc toàn bộ dữ liệu test từ sheet "LoginTestCases".
     * Bỏ qua hàng header (hàng đầu tiên).
     *
     * @return List các mảng String, mỗi mảng là 1 hàng dữ liệu:
     *         [TC_ID, Username, Password, ExpectedResult, Description]
     */
    public static List<String[]> readLoginTestData() {
        List<String[]> testData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(EXCEL_FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                throw new RuntimeException("Không tìm thấy sheet: " + SHEET_NAME);
            }

            // Bắt đầu từ hàng 1 (bỏ qua header ở hàng 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String tcId       = getCellValue(row, COL_TC_ID);
                String username   = getCellValue(row, COL_USERNAME);
                String password   = getCellValue(row, COL_PASSWORD);
                String expected   = getCellValue(row, COL_EXPECTED_RESULT);
                String desc       = getCellValue(row, COL_DESCRIPTION);

                // Bỏ qua hàng trống
                if (tcId.isEmpty() && username.isEmpty()) continue;

                testData.add(new String[]{tcId, username, password, expected, desc});
            }

        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file Excel: " + EXCEL_FILE_PATH, e);
        }

        return testData;
    }

    /**
     * Lấy giá trị ô Excel dưới dạng String (xử lý đủ loại: text, số, boolean, công thức).
     */
    private static String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
