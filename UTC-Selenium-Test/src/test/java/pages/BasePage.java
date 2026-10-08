package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage — Class cha cho tất cả Page Object.
 * Chứa các thao tác chung: wait, click, type, getText, isDisplayed.
 * Tất cả Page Object đều phải kế thừa class này.
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Timeout mặc định cho explicit wait
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * Chờ element xuất hiện rồi trả về.
     */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Chờ element clickable rồi trả về.
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Xóa text cũ và nhập text mới vào input field.
     */
    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Click vào element.
     */
    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    /**
     * Lấy text của element (đã trim khoảng trắng).
     */
    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    /**
     * Kiểm tra element có đang hiển thị không (có xử lý exception nếu không tìm thấy).
     */
    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy URL hiện tại của trang.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Lấy title của trang.
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
}
