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


---

### Chi tiết Test Cases

*(Các test cases tiếp theo tương tự theo bảng map bên trên)*
