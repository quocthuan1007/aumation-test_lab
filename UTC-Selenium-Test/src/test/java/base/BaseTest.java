package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class BaseTest {

    // ThreadLocal để mỗi test thread có driver riêng (an toàn khi chạy song song)
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    protected WebDriver driver;

    /** Cho phép ScreenshotWatcher truy cập driver từ bên ngoài */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    // Đặt HEADLESS = true để chạy ẩn (không mở cửa sổ trình duyệt)
    // Đặt false nếu muốn xem trực tiếp trên màn hình khi debug
    private static final boolean HEADLESS = true;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        if (HEADLESS) {
            // Chạy headless — không mở cửa sổ Chrome thật
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // Các options tối ưu cho automation
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--lang=vi");

        // Selenium Manager tự động quản lý ChromeDriver từ phiên bản 4.6+
        driver = new ChromeDriver(options);
        driverThread.set(driver); // Để ScreenshotWatcher dùng qua getDriver()

        if (!HEADLESS) {
            driver.manage().window().maximize();
        }

        // Implicit wait toàn cục 5 giây
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://vanphongdientu.utc.edu.vn/Login");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        driverThread.remove(); // Xoá khỏi ThreadLocal tránh memory leak
    }
}

