# UTC Selenium Automation Test Lab

Dự án Automation Testing sử dụng Selenium WebDriver, JUnit 5, Apache POI và Allure Report cho hệ thống Văn phòng điện tử UTC.

## 📁 Thư mục dự án

Chi tiết dự án nằm trong thư mục [`UTC-Selenium-Test/`](UTC-Selenium-Test/):
- **Source code test**: [`UTC-Selenium-Test/src/test/java/`](UTC-Selenium-Test/src/test/java/)
- **Test data (Excel)**: [`UTC-Selenium-Test/src/test/resources/LoginTestData.xlsx`](UTC-Selenium-Test/src/test/resources/LoginTestData.xlsx)
- **Tài liệu test cases**: [`UTC-Selenium-Test/test-cases/Login-Test-Cases.md`](UTC-Selenium-Test/test-cases/Login-Test-Cases.md)
- **Cấu hình Maven**: [`UTC-Selenium-Test/pom.xml`](UTC-Selenium-Test/pom.xml)
- **Hướng dẫn chi tiết**: [`UTC-Selenium-Test/README.md`](UTC-Selenium-Test/README.md)

## 🚀 Hướng dẫn chạy nhanh

```bash
cd UTC-Selenium-Test

# Chạy toàn bộ test
mvn test
# hoặc
.\mvnw.cmd test

# Xem báo cáo Allure
.\mvnw.cmd allure:serve
```
