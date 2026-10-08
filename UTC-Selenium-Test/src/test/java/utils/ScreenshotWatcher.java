package utils;

import base.BaseTest;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotWatcher — JUnit 5 Extension tự động chụp màn hình khi test FAIL.
 * Đăng ký trên test class bằng: @ExtendWith(ScreenshotWatcher.class)
 *
 * Theo chuẩn slide buổi 8 trang 61: dùng AfterTestExecutionCallback.
 */
public class ScreenshotWatcher implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext ctx) throws Exception {
        // Chỉ chụp khi test FAIL (có exception)
        if (ctx.getExecutionException().isPresent()) {
            try {
                var driver = BaseTest.getDriver();
                if (driver instanceof TakesScreenshot ts) {
                    File screenshotFile = ts.getScreenshotAs(OutputType.FILE);

                    String testName = ctx.getDisplayName()
                            .replaceAll("[^a-zA-Z0-9_\\-]", "_");
                    String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                    String fileName = testName + "_" + timestamp + ".png";

                    Path dest = Path.of("screenshots/" + fileName);
                    Files.createDirectories(dest.getParent());
                    Files.copy(screenshotFile.toPath(), dest,
                            StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("📸 Screenshot saved: " + dest.toAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("⚠️ Failed to save screenshot: " + e.getMessage());
            }
        }
    }
}
