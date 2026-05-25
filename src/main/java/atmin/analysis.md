### 1. Thiết kế API Endpoints & HTTP Methods
Để đại diện cho 3 "điều ước", chúng ta sẽ sử dụng các phương thức HTTP phù hợp với bản chất của hành động:
*   **Điều ước 1 (Tạo ra vật chất):** Sử dụng `POST /api/genie/wish/create-item`. Phương thức **POST** dùng để tạo mới một tài nguyên (ví dụ: một kho báu).
*   **Điều ước 2 (Thay đổi vận mệnh):** Sử dụng `PUT /api/genie/wish/update-status`. Phương thức **PUT** dùng để cập nhật trạng thái của một tài nguyên hiện có (ví dụ: đổi từ "nghèo" sang "giàu").
*   **Điều ước 3 (Xin lời khuyên):** Sử dụng `GET /api/genie/wish/random-wisdom`. Phương thức **GET** dùng để lấy một thông tin ngẫu nhiên từ thần đèn.

### 2. Lựa chọn mã trạng thái HTTP (Status Codes)
*   **Thành công (200 OK / 201 Created):** Trả về khi điều ước được thực hiện thành công và số lượt ước còn > 0.
*   **Hết lượt ước (403 Forbidden):** Sử dụng 403 để chỉ thị rằng người dùng đã bị cấm thực hiện thêm hành động vì vi phạm giới hạn (hết 3 điều ước).
*   **Dữ liệu không hợp lệ (400 Bad Request):** Trả về khi client gửi thiếu thông tin hoặc dữ liệu sai định dạng (ví dụ: ước một điều ước bị cấm).
*   **Lỗi logic nghiệp vụ (422 Unprocessable Entity):** Có thể dùng cho các trường hợp dữ liệu đúng định dạng nhưng vi phạm quy tắc "đạo đức" của thần đèn.

### 3. Lưu trữ và truy xuất lịch sử
*   **Lưu trữ:** Sử dụng một `List<WishLog>` trong bộ nhớ (In-memory) để ghi lại: nội dung điều ước, thời gian, trạng thái (Thành công/Bị từ chối) và lý do nếu bị từ chối.
*   **Truy xuất:** Cung cấp endpoint `GET /api/genie/history` để trả về danh sách lịch sử này dưới dạng JSON.