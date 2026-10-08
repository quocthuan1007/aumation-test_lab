# UTC Selenium Automation Test — Văn phòng điện tử UTC

## 📁 Cấu trúc project

```
UTC-Selenium-Test/
├── src/test/java/
│   ├── base/
│   │   └── BaseTest.java          # Khởi tạo WebDriver + HEADLESS mode, timeout
│   ├── pages/                     # PAGE OBJECTS
│   │   ├── BasePage.java          # Thao tác chung: wait, click, type, getText
│   │   └── LoginPage.java         # Form đăng nhập — kế thừa BasePage
│   ├── tests/                     # AUTOMATION E2E TEST SCRIPTS
│   │   └── LoginE2ETest.java      # 10 test cases đăng nhập (TC01–TC10)
│   └── utils/
│       ├── ExcelUtils.java        # Đọc test data từ file Excel (.xlsx)
│       ├── ScreenshotUtils.java   # Chụp màn hình khi test FAIL
│       ├── TestData.java          # Hằng số test data
│       └── CreateExcelTestData.java  # Script tạo file Excel (chạy 1 lần)
├── src/test/resources/
│   └── LoginTestData.xlsx         # File Excel chứa test data (TC01–TC10)
├── screenshots/                   # Screenshot khi test FAIL (tự động tạo)
├── test-cases/
│   └── Login-Test-Cases.md        # Tài liệu test case
└── pom.xml                        # Maven config + dependencies
```

## ⚙️ Cài đặt & Chạy test

### Bước 1: Tạo file Excel test data
```bash
# Chạy class CreateExcelTestData.java để tạo file Excel
# Trong IntelliJ: chuột phải → Run 'CreateExcelTestData.main()'
# Sau đó mở file src/test/resources/LoginTestData.xlsx và điền tài khoản thật vào TC01
```

### Bước 2: Cài biến môi trường (quan trọng!)
```bash
# Windows PowerShell
$env:UTC_TEST_USERNAME = "your_student_id"
$env:UTC_TEST_PASSWORD = "your_password"

# Windows CMD
set UTC_TEST_USERNAME=your_student_id
set UTC_TEST_PASSWORD=your_password
```

### Bước 3: Chạy automation test
```bash
mvn test
# hoặc
.\mvnw.cmd test
```

### Chạy test đơn lẻ
```bash
# Chạy chỉ file LoginE2ETest
mvn test -Dtest=LoginE2ETest

# Chạy 1 method cụ thể
mvn test -Dtest="LoginE2ETest#TC01_validLogin"

# chạy report
.\mvnw.cmd allure:serve

# Chạy test với mode không headless (xem trực tiếp trên màn hình)
# → Sửa HEADLESS = false trong BaseTest.java
```

## 🖥️ Headless Mode

Trong [`BaseTest.java`](src/test/java/base/BaseTest.java), thay đổi dòng:

```java
private static final boolean HEADLESS = true;  // true = không mở cửa sổ Chrome
                                                 // false = mở Chrome thật (để debug)
```

## 📊 File Excel Test Data

File: `src/test/resources/LoginTestData.xlsx`  
Sheet: `LoginTestCases`

| Cột | Nội dung |
|---|---|
| TC_ID | Mã test case (TC01–TC10) |
| Username | Tên đăng nhập (dùng `<<VALID_USERNAME>>` cho tài khoản hợp lệ) |
| Password | Mật khẩu (dùng `<<VALID_PASSWORD>>` cho mật khẩu hợp lệ) |
| ExpectedResult | `SUCCESS` = đăng nhập thành công, `ERROR` = phải có thông báo lỗi |
| Description | Mô tả test case |

## 🧪 Test Cases

| ID | Username | Password | Kết quả mong đợi |
|---|---|---|---|
| TC01 | Hợp lệ | Hợp lệ | Đăng nhập thành công |
| TC02 | Không tồn tại | Hợp lệ | Lỗi: "Tài khoản hoặc mật khẩu không đúng" |
| TC03 | Rỗng | Hợp lệ | Lỗi: "Bạn chưa nhập tên đăng nhập" |
| TC04 | Khoảng trắng | Hợp lệ | Lỗi (validation) |
| TC05 | Ký tự đặc biệt | Hợp lệ | Lỗi |
| TC06 | Hợp lệ | Sai | Lỗi: "Tài khoản hoặc mật khẩu không đúng" |
| TC07 | Hợp lệ | Rỗng | Lỗi: "Bạn chưa nhập mật khẩu" |
| TC08 | Hợp lệ | Khoảng trắng | Lỗi |
| TC09 | Rỗng | Rỗng | Lỗi: "Bạn chưa nhập tên đăng nhập" |
| TC10 | Không tồn tại | Sai | Lỗi: "Tài khoản hoặc mật khẩu không đúng" |

## 📦 Dependencies

- **Selenium Java** `4.25.0` — Automation WebDriver
- **JUnit 5** `5.11.0` — Test runner
- **Apache POI** `5.2.5` — Đọc file Excel (.xlsx)
- **ChromeDriver** — Tự động quản lý bởi Selenium Manager (không cần cài thêm)

## 📸 Screenshots

Khi test FAIL, screenshot tự động được lưu vào thư mục `screenshots/` với format:
```
screenshots/TC02_FAIL_2026-10-08_15-30-00.png
```
