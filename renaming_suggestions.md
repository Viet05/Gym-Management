# Ý kiến đặt tên lại cho dự án Gym Management

Dưới đây là các đề xuất đổi tên để làm cho cấu trúc dự án ngắn gọn, dễ hiểu và chuyên nghiệp hơn.

## 1. Cấu trúc thư mục gốc (Root Directory)

*   **Hiện tại:** `titanium-gym-manager`
*   **Đề xuất:** `frontend`
*   **Lý do:** Tên thư mục nên phản ánh vai trò của nó trong dự án (frontend code) thay vì tên sản phẩm cụ thể, giúp dễ dàng nhận biết cấu trúc full-stack (backend/frontend).

## 2. Backend (Java/Spring Boot)

### 2.1. Entities (`com.group2.gymmanagement.entities`)

Các entity hiện tại có tên khá dài và lặp lại từ khóa.

| Tên hiện tại | Tên đề xuất | Lý do |
| :--- | :--- | :--- |
| `MemberPackageAssigment.java` | `Subscription.java` | "Subscription" (Đăng ký) mô tả chính xác mối quan hệ giữa Member và Package, ngắn gọn hơn nhiều so với "Assignment". |
| `MemberTrainerAssignment.java` | `TrainingSession.java` hoặc `TrainerBooking.java` | "TrainingSession" hoặc "Booking" rõ nghĩa hơn cho việc đặt lịch với PT. |
| `MembershipPackage.java` | `GymPackage.java` hoặc `Plan.java` | "MembershipPackage" hơi lặp. "Plan" hoặc "GymPackage" ngắn gọn hơn. (Lưu ý: tránh dùng `Package` vì trùng từ khóa Java). |
| `TrainerProfile.java` | `Trainer.java` | Nếu entity này đại diện cho Trainer, chỉ cần `Trainer` là đủ. Thông tin profile là thuộc tính của Trainer. |
| `TrainerSchedule.java` | `Schedule.java` | Nếu ngữ cảnh đã rõ ràng trong package hoặc quan hệ, `Schedule` là đủ. Hoặc `WorkShift` nếu là ca làm việc. |
| `GymmanagementApplication.java` | `GymApp.java` | Ngắn gọn, chuẩn convention thường thấy. |

### 2.2. Controllers (`com.group2.gymmanagement.controller`)

| Tên hiện tại | Tên đề xuất | Lý do |
| :--- | :--- | :--- |
| `GymmanagementApplication.java` | `GymApp.java` | Tên class chính nên ngắn gọn. |

### 2.3. DTOs

Nên đồng bộ tên DTO với tên Entity mới. Ví dụ:
*   `MemberPackageAssignmentRequest` -> `SubscriptionRequest`
*   `TrainerProfileResponse` -> `TrainerResponse`

## 3. Frontend (React/TypeScript)

### 3.1. Components & Views

Tên file hiện tại khá ổn, tuy nhiên có thể cân nhắc:

*   `titanium-gym-manager/views/Members.tsx` -> `MemberList.tsx` (nếu chỉ hiển thị danh sách) hoặc giữ nguyên nếu là trang quản lý chung.
*   `titanium-gym-manager/views/Packages.tsx` -> `Plans.tsx` (nếu đổi entity thành Plan).

## 4. Tổng kết

Việc đổi tên này sẽ giúp:
1.  **Code dễ đọc hơn:** Giảm bớt độ dài dòng của các khai báo biến và tên class.
2.  **Dễ gõ code hơn:** Tên ngắn gọn giúp tăng tốc độ coding.
3.  **Thể hiện đúng ngữ nghĩa:** Các từ như `Subscription`, `Session` mang ý nghĩa nghiệp vụ rõ ràng hơn là `Assignment`.

Bạn có muốn tôi thực hiện việc đổi tên này cho toàn bộ dự án không? (Bao gồm cả việc refactor code để cập nhật các tham chiếu).
