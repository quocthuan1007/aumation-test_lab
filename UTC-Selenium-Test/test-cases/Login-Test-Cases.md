# Test Cases cho chức năng Đăng nhập Văn phòng điện tử UTC

## Bảng phân tích phân lớp tương đương
| Phân lớp | ID | Mô tả | Dữ liệu thử nghiệm |
| --- | --- | --- | --- |
| **Username** | U1 | Username hợp lệ | Tồn tại trong hệ thống (biến môi trường) |
| | U2 | Username không tồn tại | `username_not_exist` |
| | U3 | Username rỗng | `""` |
| | U4 | Username chỉ chứa khoảng trắng | `"   "` |
| | U5 | Username chứa ký tự đặc biệt | `"@@@###"` |
| **Password** | P1 | Password hợp lệ | Mật khẩu đúng cho Username U1 |
| | P2 | Password sai | `wrong_password` |
| | P3 | Password rỗng | `""` |
| | P4 | Password chỉ chứa khoảng trắng | `"   "` |

## Danh sách Test Cases

| ID | Username | Password | Expected Result |
| --- | --- | --- | --- |
| TC01 | Hợp lệ | Hợp lệ | Đăng nhập thành công |
| TC02 | Không tồn tại | Hợp lệ | Đăng nhập thất bại, hiển thị lỗi "Tài khoản hoặc mật khẩu không đúng." |
| TC03 | Rỗng | Hợp lệ | Hiển thị validation "Bạn chưa nhập tên đăng nhập" |
| TC04 | Khoảng trắng | Hợp lệ | Đăng nhập thất bại / Hiển thị validation |
| TC05 | Ký tự đặc biệt (`@@@###`) | Hợp lệ | Đăng nhập thất bại, hiển thị lỗi |
| TC06 | Hợp lệ | Sai | Đăng nhập thất bại, hiển thị lỗi "Tài khoản hoặc mật khẩu không đúng." |


---

### Chi tiết Test Cases

#### TC01: validLogin
- **Preconditions**: Có kết nối internet, web server hoạt động, trình duyệt mở ở trang Login.
- **Test Steps**:
  1. Nhập username hợp lệ
  2. Nhập password hợp lệ
  3. Click nút "Đăng nhập"
- **Test Data**: Username và Password đúng từ biến môi trường.
- **Expected Result**: Đăng nhập thành công, URL thay đổi, không còn ở trang Login.
- **Actual Result**: (Tự động cập nhật khi test chạy)
- **Status**: TO BE EXECUTED

#### TC02: invalidUsername
- **Preconditions**: Web ở trang Login.
- **Test Steps**:
  1. Nhập username không tồn tại (`username_not_exist`)
  2. Nhập password hợp lệ
  3. Click "Đăng nhập"
- **Expected Result**: Hiển thị thông báo lỗi "Tài khoản hoặc mật khẩu không đúng."

*(Các test cases tiếp theo tương tự theo bảng map bên trên)*
