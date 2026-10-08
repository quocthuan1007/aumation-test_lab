package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage — Page Object cho trang đăng nhập UTC.
 * Kế thừa BasePage để tái sử dụng các thao tác wait, click, type, getText.
 */
public class LoginPage extends BasePage {

    // ===== LOCATORS =====
    private final By usernameInput       = By.name("username");
    private final By passwordInput       = By.name("userpwd");
    private final By loginButton         = By.cssSelector("input.submit_login");
    private final By rememberMeCheckbox  = By.id("persistent");         // Để isSelected()
    private final By rememberMeLabel     = By.cssSelector("label.check"); // Để click
    private final By forgotPasswordLink  = By.cssSelector("a[href='/Login/GetPass']");
    private final By emailLoginLink      = By.xpath("//a[contains(text(),'e-mail UTC')]");
    private final By errorMessage        = By.className("error");

    // ===== CONSTRUCTOR =====
    public LoginPage(WebDriver driver) {
        super(driver); // Gọi constructor BasePage
    }

    // ===== NAVIGATION =====

    /**
     * Mở trang Login và trả về chính nó (để dùng method chaining).
     */
    public LoginPage open() {
        driver.get("https://vanphongdientu.utc.edu.vn/Login");
        return this;
    }

    /**
     * Kiểm tra hiện tại đang ở trang Login không.
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("/Login");
    }

    /**
     * Nhập username vào ô input.
     */
    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    /**
     * Nhập password vào ô input.
     */
    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    /**
     * Click nút Đăng nhập.
     */
    public void clickLogin() {
        click(loginButton);
    }

    /**
     * Click vào label của checkbox "Ghi nhớ đăng nhập".
     */
    public void clickRememberMe() {
        click(rememberMeLabel);
    }

    /**
     * Kiểm tra checkbox "Ghi nhớ đăng nhập" có được chọn không.
     */
    public boolean isRememberMeSelected() {
        return driver.findElement(rememberMeCheckbox).isSelected();
    }

    /**
     * Click vào link "Quên mật khẩu".
     */
    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }

    /**
     * Click vào link đăng nhập bằng e-mail UTC.
     */
    public void clickEmailLogin() {
        click(emailLoginLink);
    }

    /**
     * Thực hiện đăng nhập: nhập username, password rồi click Đăng nhập.
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ===== ASSERTIONS / GETTERS =====

    /**
     * Kiểm tra thông báo lỗi có xuất hiện không.
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Lấy nội dung thông báo lỗi.
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }
}

