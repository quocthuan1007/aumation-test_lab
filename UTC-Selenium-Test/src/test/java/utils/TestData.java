package utils;

public class TestData {
    public static final String VALID_USERNAME = System.getenv("UTC_TEST_USERNAME") != null ? System.getenv("UTC_TEST_USERNAME") : "student123";
    public static final String VALID_PASSWORD = System.getenv("UTC_TEST_PASSWORD") != null ? System.getenv("UTC_TEST_PASSWORD") : "student_password";
    
    public static final String INVALID_USERNAME = "username_not_exist";
    public static final String INVALID_PASSWORD = "wrong_password";
    
    public static final String EMPTY_VALUE = "";
    public static final String WHITESPACE_VALUE = "   ";
    public static final String SPECIAL_CHARACTER_USERNAME = "@@@###";

    // Expected error messages
    public static final String ERROR_INVALID_CREDENTIALS = "Tài khoản hoặc mật khẩu không đúng.";
    public static final String ERROR_EMPTY_USERNAME = "Bạn chưa nhập tên đăng nhập";
    public static final String ERROR_EMPTY_PASSWORD = "Bạn chưa nhập mật khẩu";
}
