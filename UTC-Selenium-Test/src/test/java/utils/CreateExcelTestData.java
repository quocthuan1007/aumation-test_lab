package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Script tạo file Excel LoginTestData.xlsx.
 * Chạy 1 lần để tạo file test data.
 * Sau đó chỉ cần sửa file Excel trực tiếp để thêm/sửa test case.
 */
public class CreateExcelTestData {

    public static void main(String[] args) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("LoginTestCases");

        // ===== Style cho header =====
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // ===== Hàng 0: Header =====
        Row header = sheet.createRow(0);
        String[] headers = {"TC_ID", "Username", "Password", "ExpectedResult", "Description"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== Dữ liệu test cases =====
        // Format: TC_ID | Username | Password | ExpectedResult | Description
        // ExpectedResult: "SUCCESS" = đăng nhập thành công, "ERROR" = hiển thị lỗi
        String[][] data = {
            // ===== TC01–TC10: Test cases gốc =====
            {"TC01", "<<VALID_USERNAME>>", "<<VALID_PASSWORD>>", "SUCCESS",  "Đăng nhập với tài khoản hợp lệ"},
            {"TC02", "username_not_exist", "<<VALID_PASSWORD>>", "ERROR",    "Username không tồn tại"},
            {"TC03", "",                  "<<VALID_PASSWORD>>", "ERROR",    "Username rỗng"},
            {"TC04", "   ",               "<<VALID_PASSWORD>>", "ERROR",    "Username chỉ có khoảng trắng"},
            {"TC05", "@@@###",            "<<VALID_PASSWORD>>", "ERROR",    "Username chứa ký tự đặc biệt"},
            {"TC06", "<<VALID_USERNAME>>", "wrong_password",    "ERROR",    "Password sai"},
            {"TC07", "<<VALID_USERNAME>>", "",                  "ERROR",    "Password rỗng"},
            {"TC08", "<<VALID_USERNAME>>", "   ",               "ERROR",    "Password chỉ có khoảng trắng"},
            {"TC09", "",                  "",                  "ERROR",    "Username và Password đều rỗng"},
            {"TC10", "username_not_exist","wrong_password",     "ERROR",    "Cả Username và Password đều sai"},

            // ===== TC11–TC20: Test cases mở rộng =====
            {"TC11", "' OR '1'='1",        "<<VALID_PASSWORD>>", "ERROR",    "SQL Injection trong Username"},
            {"TC12", "<<VALID_USERNAME>>",  "' OR '1'='1",       "ERROR",    "SQL Injection trong Password"},
            {"TC13", "<script>alert(1)</script>", "<<VALID_PASSWORD>>", "ERROR", "XSS attack trong Username"},
            {"TC14", "a".repeat(1) ,       "<<VALID_PASSWORD>>", "ERROR",    "Username 1 ký tự (quá ngắn)"},
            {"TC15", "a".repeat(256),      "<<VALID_PASSWORD>>", "ERROR",    "Username quá dài (256 ký tự)"},
            {"TC16", "<<VALID_USERNAME>>", "a".repeat(256),      "ERROR",    "Password quá dài (256 ký tự)"},
            {"TC17", "test@gmail.com",     "<<VALID_PASSWORD>>", "ERROR",    "Username dạng email"},
            {"TC18", "nguyenvanan",        "<<VALID_PASSWORD>>", "ERROR",    "Username tiếng Việt không dấu"},
            {"TC19", "12345678",           "<<VALID_PASSWORD>>", "ERROR",    "Username toàn số"},
            {"TC20", "<<VALID_USERNAME>>", "pass word 123",      "ERROR",    "Password có khoảng trắng ở giữa"},
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                row.createCell(j).setCellValue(data[i][j]);
            }
        }

        // Auto-size tất cả cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi ra file
        String outputPath = "src/test/resources/LoginTestData.xlsx";
        new java.io.File("src/test/resources").mkdirs();
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        }
        workbook.close();

        System.out.println("✅ Đã tạo file: " + outputPath);
        System.out.println("📌 Thay <<VALID_USERNAME>> và <<VALID_PASSWORD>> bằng tài khoản thật trong file Excel.");
    }
}
