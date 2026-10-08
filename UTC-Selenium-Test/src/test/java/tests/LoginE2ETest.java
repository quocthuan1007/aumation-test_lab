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

    @Test
    @Story("TC01 — Đăng nhập hợp lệ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Kiểm tra đăng nhập thành công với tài khoản hợp lệ — URL phải thay đổi")
    @DisplayName("TC01 — Đăng nhập với tài khoản hợp lệ")
    public void TC01_validLogin() {
        // SKIP nếu chưa set biến môi trường — không FAIL
        assumeTrue(hasRealCredentials(),
                "[SKIP] TC01 cần tài khoản UTC thật. " +
                "Set biến môi trường: UTC_TEST_USERNAME và UTC_TEST_PASSWORD");

        String urlBefore = driver.getCurrentUrl();
        loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        String urlAfter = driver.getCurrentUrl();
        Assertions.assertNotEquals(urlBefore, urlAfter, "URL phải thay đổi sau khi đăng nhập thành công");
        Assertions.assertFalse(urlAfter.contains("/Login"), "Phải thoát khỏi trang /Login sau khi đăng nhập");
    }

    @Test
    @Story("TC02 — Username không tồn tại")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC02 — Username không tồn tại")
    public void TC02_invalidUsername() {
        loginPage.login("username_not_exist", VALID_PASSWORD);

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Phải hiển thị thông báo lỗi");
        Assertions.assertTrue(
                loginPage.getErrorMessage().contains("Tài khoản hoặc mật khẩu không đúng"),
                "Thông báo lỗi không đúng: " + loginPage.getErrorMessage()
        );
    }

    @Test
    @Story("TC03 — Username rỗng")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC03 — Username rỗng")
    public void TC03_emptyUsername() {
        loginPage.login("", VALID_PASSWORD);

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Phải hiển thị validation");
        Assertions.assertTrue(
                loginPage.getErrorMessage().contains("Bạn chưa nhập tên đăng nhập"),
                "Thông báo lỗi không đúng: " + loginPage.getErrorMessage()
        );
    }

    @Test
    @Story("TC04 — Username khoảng trắng")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC04 — Username chỉ có khoảng trắng")
    public void TC04_usernameWhitespace() {
        loginPage.login("   ", VALID_PASSWORD);

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Phải hiển thị lỗi khi username chỉ có khoảng trắng");
    }

    @Test
    @Story("TC05 — Username ký tự đặc biệt")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("TC05 — Username chứa ký tự đặc biệt")
    public void TC05_specialCharsUsername() {
        loginPage.login("@@@###", VALID_PASSWORD);

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Phải hiển thị lỗi khi username có ký tự đặc biệt");
    }

    @Test
    @Story("TC06 — Password sai")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC06 — Password sai")
    public void TC06_wrongPassword() {
        loginPage.login(VALID_USERNAME, "wrong_password");

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Phải hiển thị thông báo lỗi");
        Assertions.assertTrue(
                loginPage.getErrorMessage().contains("Tài khoản hoặc mật khẩu không đúng"),
                "Thông báo lỗi không đúng: " + loginPage.getErrorMessage()
        );
    }

    @Test
    @Story("TC07 — Password rỗng")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("TC07 — Password rỗng")
    public void TC07_emptyPassword() {
        loginPage.login(VALID_USERNAME, "");

        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Phải hiển thị validation");
        Assertions.assertTrue(
                loginPage.getErrorMessage().contains("Bạn chưa nhập mật khẩu"),
                "Thông báo lỗi không đúng: " + loginPage.getErrorMessage()
        );
    }

    // =====================================================================
    // Helper: thay placeholder trong Excel bằng credential thật
    // =====================================================================

    private String resolveCredential(String value) {
        if (PLACEHOLDER_USERNAME.equals(value)) return VALID_USERNAME;
        if (PLACEHOLDER_PASSWORD.equals(value)) return VALID_PASSWORD;
        return value;
    }
}
