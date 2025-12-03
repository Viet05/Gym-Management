# Gym Management System - Refactoring Changelog

## Ngày 03/12/2025 - Sửa lỗi JPA Mapping

### 1. Trainer.java
**Vấn đề**: `@JoinColumn` tham chiếu sai tên cột khi kết nối với entity `User`

**Thay đổi**: Dòng 21
```diff
 @OneToOne(fetch = FetchType.LAZY)
-@JoinColumn(name = "user_id", referencedColumnName = "id")
+@JoinColumn(name = "user_id", referencedColumnName = "user_id")
 private User user;
```

**Lý do**: Entity `User` có `@Column(name = "user_id")` cho trường `id`, nên `referencedColumnName` phải là `"user_id"` chứ không phải `"id"`

---

### 2. Payment.java
**Vấn đề**: `@JoinColumn` sử dụng sai tên cột khi tham chiếu đến `User`

**Thay đổi**: Dòng 29
```diff
 @ManyToOne
-@JoinColumn(name = "id")
+@JoinColumn(name = "user_id", referencedColumnName = "user_id")
 private User member;
```

**Lý do**: 
- Tên cột trong bảng `payment` phải là `user_id` (không phải `id`)
- Cần chỉ rõ `referencedColumnName = "user_id"` để khớp với tên cột trong bảng `user`

---

## Tổng kết

### Entities đã sửa
- ✅ `Trainer.java` - Sửa foreign key mapping
- ✅ `Payment.java` - Sửa foreign key mapping

### Entities đã kiểm tra (không có lỗi)
- ✅ `User.java` - Primary key mapping đúng
- ✅ `Subscription.java` - Join column đúng
- ✅ `TrainerBooking.java` - Join columns đúng
- ✅ `Schedule.java` - Join column đúng
- ✅ `GymPlan.java`
- ✅ `Attendance.java`

### Kết quả
- ✅ Backend compile thành công
- ✅ Application khởi động không lỗi
- ✅ Hibernate mapping hoạt động chính xác

---

## Lưu ý kỹ thuật

### Nguyên tắc JPA Mapping với User Entity
Khi tạo relationship với `User` entity, luôn sử dụng:
```java
@JoinColumn(name = "user_id", referencedColumnName = "user_id")
```

**Giải thích**:
- `name = "user_id"`: Tên cột foreign key trong bảng hiện tại
- `referencedColumnName = "user_id"`: Tên cột trong bảng `user` (không phải tên thuộc tính Java `id`)

### Cấu trúc User Entity
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "user_id")  // ← Database column name
private Long id;           // ← Java property name
```
