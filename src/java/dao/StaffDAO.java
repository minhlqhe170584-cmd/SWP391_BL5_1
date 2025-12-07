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
 * @author Lân
 */
public class StaffDAO extends DBContext {
    
    // CRUD Cơ bản
    private static final String GET_ALL_STAFFS = 
            "SELECT s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at, r.role_id, r.role_name " +
            "FROM Staff s LEFT JOIN StaffRoles r ON s.role_id = r.role_id ORDER BY s.staff_id"; // Dùng LEFT JOIN
            
    private static final String GET_STAFF_BY_ID = 
            "SELECT s.*, r.role_name FROM Staff s LEFT JOIN StaffRoles r ON s.role_id = r.role_id WHERE s.staff_id = ?";
            
    private static final String INSERT_STAFF = 
            "INSERT INTO Staff (role_id, full_name, email, password, is_active) VALUES (?, ?, ?, ?, 1)";
            
    private static final String UPDATE_STAFF = 
            "UPDATE Staff SET role_id = ?, full_name = ?, email = ?, password = ? WHERE staff_id = ?";
            
    private static final String DEACTIVATE_STAFF = "UPDATE Staff SET is_active = 0 WHERE staff_id = ?";
    private static final String ACTIVATE_STAFF = "UPDATE Staff SET is_active = 1 WHERE staff_id = ?";

    private static final String COUNT_STAFFS_DYNAMIC = 
            "SELECT COUNT(*) FROM Staff s LEFT JOIN StaffRoles r ON s.role_id = r.role_id " +
            "WHERE s.full_name LIKE ? AND (? = -1 OR s.role_id = ?)";

    private static final String GET_STAFFS_DYNAMIC = 
            "SELECT s.*, r.role_name FROM Staff s LEFT JOIN StaffRoles r ON s.role_id = r.role_id " +
            "WHERE s.full_name LIKE ? AND (? = -1 OR s.role_id = ?) " +
            "ORDER BY s.staff_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    public StaffDAO() {
        super();
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        int roleId = rs.getInt("role_id");
        String roleName = rs.getString("role_name");
        if (roleName == null) roleName = "Unknown Role"; 

        Role role = new Role(roleId, roleName);
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;
        
        return new Staff(
                rs.getInt("staff_id"),
                role,
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"), 
                rs.getBoolean("is_active"),
                createdAt
        );
    }

    public List<Staff> getAllStaffs() {
        List<Staff> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_STAFFS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createStaff(Staff staff) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_STAFF)) {
            ps.setInt(1, staff.getRole().getRoleId());
            ps.setString(2, staff.getFullName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, staff.getPassWordHash());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStaff(Staff staff) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_STAFF)) {
            ps.setInt(1, staff.getRole().getRoleId());
            ps.setString(2, staff.getFullName());
            ps.setString(3, staff.getEmail());
            ps.setString(4, staff.getPassWordHash());
            ps.setInt(5, staff.getStaffId());
            return ps.executeUpdate() > 0;
        }
    }

    public Staff getStaffById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_STAFF_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToStaff(rs);
            }
        }
        return null;
    }

    public boolean deactivateStaff(int staffId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DEACTIVATE_STAFF)) {
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean activateStaff(int staffId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(ACTIVATE_STAFF)) {
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        }
    }

    public Staff checkLogin(String email, String password) {
        String sql = "SELECT s.*, r.role_name FROM Staff s " +
                     "JOIN StaffRoles r ON s.role_id = r.role_id " +
                     "WHERE s.email = ? AND s.password = ? AND s.is_active = 1";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, password);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapResultSetToStaff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countStaffs(String keyword, String roleIdStr) {
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";
        int roleId = (roleIdStr == null || roleIdStr.isEmpty()) ? -1 : Integer.parseInt(roleIdStr);

        try (PreparedStatement ps = connection.prepareStatement(COUNT_STAFFS_DYNAMIC)) {
            ps.setString(1, searchName);
            ps.setInt(2, roleId);
            ps.setInt(3, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Staff> getStaffs(String keyword, String roleIdStr, int pageIndex, int pageSize) {
        List<Staff> list = new ArrayList<>();
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";
        int roleId = (roleIdStr == null || roleIdStr.isEmpty()) ? -1 : Integer.parseInt(roleIdStr);
        int offset = (pageIndex - 1) * pageSize;

        try (PreparedStatement ps = connection.prepareStatement(GET_STAFFS_DYNAMIC)) {
            ps.setString(1, searchName);
            ps.setInt(2, roleId);
            ps.setInt(3, roleId);
            ps.setInt(4, offset);
            ps.setInt(5, pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToStaff(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}