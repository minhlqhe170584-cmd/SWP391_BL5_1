package models;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private int roleId;
    private boolean isActive;
    
    // Thuộc tính phụ (Không có trong bảng Users, nhưng lấy từ bảng Roles khi JOIN)
    // Để tiện cho việc check quyền: "ADMIN", "RECEPTIONIST", "CUSTOMER"
    private String roleName; 

    public User() {
    }

    public User(int userId, String fullName, String email, String password, String phone, String address, int roleId, boolean isActive) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
        this.isActive = isActive;
    }

    // --- GETTERS & SETTERS ---

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", fullName=" + fullName + ", roleId=" + roleId + '}';
    }
}
//1. Nhóm Lấy Dữ Liệu (SELECT) - Dùng nhiều nhất
//Thầy cô thường bảo: "Em mở bảng Customer lên xem nào" hoặc "Tìm cho tôi ông nào tên là A".
//
//Lấy tất cả dữ liệu bảng:
//
//SQL
//
//SELECT * FROM Customers;
//SELECT * FROM Bookings;
//Lấy dữ liệu có điều kiện (WHERE):
//
//SQL
//
//-- Tìm khách hàng có ID là 5
//SELECT * FROM Customers WHERE customer_id = 5;
//
//-- Tìm các đơn đặt phòng đang "Pending"
//SELECT * FROM Bookings WHERE status = 'Pending';
//
//-- Tìm phòng có giá dưới 1 triệu
//SELECT * FROM Rooms WHERE price < 1000000;
//Tìm kiếm gần đúng (LIKE) - Dùng khi tìm tên:
//
//SQL
//
//-- Tìm khách hàng có tên chứa chữ "Tuan"
//SELECT * FROM Customers WHERE full_name LIKE '%Tuan%';
//Sắp xếp dữ liệu (ORDER BY):
//
//SQL
//
//-- Sắp xếp phòng từ giá cao xuống thấp
//SELECT * FROM Rooms ORDER BY price DESC;
//2. Nhóm Chỉnh Sửa Dữ Liệu (INSERT, UPDATE, DELETE)
//Dùng khi thầy bảo: "Thêm thử 1 phòng mới xem", "Sửa lại số điện thoại ông này", "Xóa cái đơn rác kia đi".
//
//Thêm mới (INSERT):
//
//SQL
//
//-- Thêm một loại phòng mới (Ví dụ)
//INSERT INTO Rooms (room_name, price, status) 
//VALUES ('P505', 500000, 1);
//Cập nhật (UPDATE) - ⚠️ QUAN TRỌNG: Luôn nhớ phải có WHERE. Không có WHERE là nó sửa toàn bộ database đấy!
//
//SQL
//
//-- Đổi số điện thoại của khách hàng có ID = 3
//UPDATE Customers 
//SET phone = '0999888777' 
//WHERE customer_id = 3;
//Xóa (DELETE) - ⚠️ QUAN TRỌNG: Cũng phải có WHERE.
//
//SQL
//
//-- Xóa đơn đặt phòng số 10
//DELETE FROM Bookings WHERE booking_id = 10;
//3. Nhóm Thống Kê (COUNT, SUM)
//Dùng khi thầy hỏi: "Có bao nhiêu khách hàng tất cả?" hoặc "Tổng tiền đơn này là bao nhiêu?".
//
//Đếm số lượng (COUNT):
//
//SQL
//
//-- Đếm tổng số khách hàng
//SELECT COUNT(*) FROM Customers;
//Tính tổng (SUM):
//
//SQL
//
//-- Tính tổng tiền của tất cả các booking
//SELECT SUM(total_price) FROM Bookings;
//4. Nhóm Nối Bảng (JOIN) - Câu hỏi lấy điểm cao
//Thầy hỏi: "Booking này là của khách hàng nào? Em query ra tên khách hàng xem?". Lúc này bảng Bookings chỉ có customer_id, bạn phải nối sang bảng Customers.
//
//SQL
//
//SELECT 
//    b.booking_id, 
//    b.check_in_date, 
//    c.full_name,  -- Lấy tên từ bảng Customer
//    c.phone       -- Lấy sđt từ bảng Customer
//FROM Bookings b
//JOIN Customers c ON b.customer_id = c.customer_id;