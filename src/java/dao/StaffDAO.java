/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import models.Staff;
import models.Role;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StaffDAO extends DBContext {

    private static final String GET_ALL_STAFFS = "SELECT s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at, "
            + "r.role_id, r.role_name FROM Staff s JOIN StaffRoles r ON s.role_id = r.role_id ORDER BY s.staff_id";
    private static final String GET_STAFF_BY_ID = "SELECT s.*, r.role_name FROM Staff s JOIN StaffRoles r ON s.role_id = r.role_id WHERE s.staff_id = ?";
    private static final String INSERT_STAFF = "INSERT INTO Staff (role_id, full_name, email, password, is_active) VALUES (?, ?, ?, ?, 1)";
    private static final String UPDATE_STAFF = "UPDATE Staff SET role_id = ?, full_name = ?, email = ?, password = ? WHERE staff_id = ?";
    private static final String DEACTIVATE_STAFF = "UPDATE Staff SET is_active = 0 WHERE staff_id = ?";
    private static final String ACTIVATE_STAFF = "UPDATE Staff SET is_active = 1 WHERE staff_id = ?";
    public StaffDAO() {
        super();
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;
        return new Staff(
                rs.getInt("staff_id"),
                role,
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"), // Đọc password_hash từ cột 'password'
                rs.getBoolean("is_active"),
                createdAt
        );
    }

    public boolean createStaff(Staff staff) throws SQLException {
        PreparedStatement ps = null;
        try {
            String rawPassword = staff.getPassWordHash();
            ps = connection.prepareStatement(INSERT_STAFF);
            ps.setInt(1, staff.getRole().getRoleId());
            ps.setString(2, staff.getFullName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, rawPassword);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<Staff> getAllStaffs() {
        List<Staff> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(GET_ALL_STAFFS);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Staff getStaffById(int id) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Staff staff = null;
        try {
            ps = connection.prepareStatement(GET_STAFF_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                staff = mapResultSetToStaff(rs);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return staff;
    }

    public boolean updateStaff(Staff staff) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(UPDATE_STAFF);
            String rawPassword = staff.getPassWordHash();
            ps.setInt(1, staff.getRole().getRoleId());
            ps.setString(2, staff.getFullName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, rawPassword);
            ps.setInt(5, staff.getStaffId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean deactivateStaff(int staffId) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(DEACTIVATE_STAFF);
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
    public boolean activateStaff(int staffId) throws SQLException {
    PreparedStatement ps = null;
    try {
        ps = connection.prepareStatement(ACTIVATE_STAFF);
        ps.setInt(1, staffId);
        return ps.executeUpdate() > 0;
    } finally {
        if (ps != null) ps.close();
    }
}
    public Staff checkLogin(String email, String password) {
        // SQL: Phải JOIN bảng StaffRoles để biết ông nhân viên này giữ chức vụ gì
        String sql = "SELECT s.*, r.role_name " +
                     "FROM Staff s " +
                     "JOIN StaffRoles r ON s.role_id = r.role_id " +
                     "WHERE s.email = ? AND s.password = ? AND s.is_active = 1";
                     
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Staff s = new Staff();
                // 1. Map thông tin nhân viên
                s.setStaffId(rs.getInt("staff_id"));
                s.setFullName(rs.getString("full_name"));
                s.setEmail(rs.getString("email"));
                // s.setPassWordHash(rs.getString("password")); 
                s.setIsActive(rs.getBoolean("is_active"));
                
                // 2. Map thông tin Role (Quan trọng để phân quyền)
                Role r = new Role();
                r.setRoleId(rs.getInt("role_id"));     // Lấy từ bảng Staff (khóa ngoại)
                r.setRoleName(rs.getString("role_name")); // Lấy từ bảng StaffRoles (do lệnh JOIN)
                
                // Gán đối tượng Role vào Staff
                s.setRole(r);
                
                return s;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi StaffDAO: " + e.getMessage());
        }
        return null;
    }
}
