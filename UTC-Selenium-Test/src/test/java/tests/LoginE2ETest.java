package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.LoginPage;
import utils.ExcelUtils;
import utils.ScreenshotWatcher;

import java.util.List;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * LoginE2ETest — Automation E2E Test cho chức năng đăng nhập UTC.
 *
 * @ExtendWith(ScreenshotWatcher.class): tự động chụp màn hình khi test FAIL
 * (đúng chuẩn slide buổi 8 trang 61 — dùng JUnit 5 Extension)
 *
 * Test data được đọc từ file Excel: src/test/resources/LoginTestData.xlsx
 */
@ExtendWith(ScreenshotWatcher.class)
@Feature("Đăng nhập — Văn phòng điện tử UTC")
public class LoginE2ETest extends BaseTest {

    private LoginPage loginPage;

    // Tài khoản lấy từ biến môi trường — không bao giờ hardcode vào code
    private static final String VALID_USERNAME = System.getenv("UTC_TEST_USERNAME") != null
            ? System.getenv("UTC_TEST_USERNAME") : "student123";
    private static final String VALID_PASSWORD = System.getenv("UTC_TEST_PASSWORD") != null
            ? System.getenv("UTC_TEST_PASSWORD") : "student_password";

    // Placeholder trong Excel
    private static final String PLACEHOLDER_USERNAME = "<<VALID_USERNAME>>";
    private static final String PLACEHOLDER_PASSWORD = "<<VALID_PASSWORD>>";

    /**
     * Kiểm tra có tài khoản UTC thật không (biến môi trường được set).
     * Nếu chưa set → test SUCCESS sẽ SKIP thay vì FAIL.
     */
    private static boolean hasRealCredentials() {
        return System.getenv("UTC_TEST_USERNAME") != null
                && System.getenv("UTC_TEST_PASSWORD") != null;
    }

    @BeforeEach
    public void setupTest() {
        loginPage = new LoginPage(driver);
    }

    // =====================================================================
    // TC01 — TC10: Đọc từ file Excel
    // =====================================================================

    @Test
    @DisplayName("E2E Login Tests — đọc từ file Excel (TC01–TC10)")
    public void runAllLoginTestsFromExcel() {
        List<String[]> testData = ExcelUtils.readLoginTestData();
        Assertions.assertFalse(testData.isEmpty(), "File Excel không có dữ liệu test!");

        int passed = 0, failed = 0;
        StringBuilder report = new StringBuilder("\n===== KẾT QUẢ AUTOMATION TEST =====\n");

        for (String[] row : testData) {
            String tcId       = row[ExcelUtils.COL_TC_ID];
            String username   = resolveCredential(row[ExcelUtils.COL_USERNAME]);
            String password   = resolveCredential(row[ExcelUtils.COL_PASSWORD]);
            String expected   = row[ExcelUtils.COL_EXPECTED_RESULT].trim().toUpperCase();
            String desc       = row[ExcelUtils.COL_DESCRIPTION];

            // Bỏ qua các test SUCCESS nếu chưa có credentials thật
            if ("SUCCESS".equals(expected) && !hasRealCredentials()) {
                report.append(String.format("⏭️  [%s] SKIP — %s | Chưa set biến môi trường UTC_TEST_USERNAME/PASSWORD%n", tcId, desc));
                continue;
            }

            driver.get("https://vanphongdientu.utc.edu.vn/Login");
            String urlBefore = driver.getCurrentUrl();

            try {
                loginPage.login(username, password);

                if ("SUCCESS".equals(expected)) {
                    String urlAfter = driver.getCurrentUrl();
                    boolean success = !urlAfter.equals(urlBefore) && !urlAfter.contains("/Login");
                    if (success) {
                        report.append(String.format("✅ [%s] PASS — %s%n", tcId, desc));
                        passed++;
                    } else {
                        utils.ScreenshotUtils.takeScreenshot(driver, tcId + "_FAIL");
                        report.append(String.format("❌ [%s] FAIL — %s | URL không đổi: %s%n", tcId, desc, urlAfter));
                        failed++;
                    }
                } else { // "ERROR"
                    boolean hasError = loginPage.isErrorMessageDisplayed();
                    if (hasError) {
                        String errorText = loginPage.getErrorMessage();
                        report.append(String.format("✅ [%s] PASS — %s | Lỗi: \"%s\"%n", tcId, desc, errorText));
                        passed++;
                    } else {
                        utils.ScreenshotUtils.takeScreenshot(driver, tcId + "_FAIL");
                        report.append(String.format("❌ [%s] FAIL — %s | Không tìm thấy thông báo lỗi%n", tcId, desc));
                        failed++;
                    }
                }

            } catch (Exception e) {
                utils.ScreenshotUtils.takeScreenshot(driver, tcId + "_ERROR");
                report.append(String.format("💥 [%s] ERROR — %s | Exception: %s%n", tcId, desc, e.getMessage()));
                failed++;
            }
        }

        report.append(String.format("%n===== TỔNG KẾT: %d PASS / %d FAIL / %d TỔNG =====%n",
                passed, failed, passed + failed));
        System.out.println(report);

        // Fail test nếu có bất kỳ test case nào failed
        Assertions.assertEquals(0, failed,
                String.format("Có %d test case bị FAIL! Xem log và screenshots để biết chi tiết.", failed));
    }

    // =====================================================================
    // Các test case riêng lẻ (không cần Excel, dùng để debug nhanh)
    // =====================================================================

    // =====================================================================
    // Helper: thay placeholder trong Excel bằng credential thật
    // =====================================================================

    private String resolveCredential(String value) {
        if (PLACEHOLDER_USERNAME.equals(value)) return VALID_USERNAME;
        if (PLACEHOLDER_PASSWORD.equals(value)) return VALID_PASSWORD;
        return value;
    }
}
