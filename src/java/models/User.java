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