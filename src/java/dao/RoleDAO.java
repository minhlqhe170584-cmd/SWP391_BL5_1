package dao;

import dbContext.DBContext;
import models.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO extends DBContext {

    // --- SQL QUERIES ---
    private static final String GET_ALL_ROLES = "SELECT * FROM StaffRoles ORDER BY role_id";

    private static final String GET_ROLE_BY_ID = "SELECT * FROM StaffRoles WHERE role_id = ?";

    private static final String INSERT_ROLE = "INSERT INTO StaffRoles (role_name) VALUES (?)";

    private static final String UPDATE_ROLE = "UPDATE StaffRoles SET role_name = ? WHERE role_id = ?";

    private static final String DELETE_ROLE = "DELETE FROM StaffRoles WHERE role_id = ?";

    // Query đếm số lượng bản ghi (để tính số trang)
    private static final String COUNT_ROLES_DYNAMIC
            = "SELECT COUNT(*) FROM StaffRoles WHERE role_name LIKE ?";

    // Query lấy dữ liệu kèm Paging
    private static final String GET_ROLES_DYNAMIC
            = "SELECT * FROM StaffRoles WHERE role_name LIKE ? "
            + "ORDER BY role_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    public RoleDAO() {
        super();
    }

    // Helper map ResultSet sang Object Role
    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        return new Role(
                rs.getInt("role_id"),
                rs.getString("role_name")
        );
    }

    // Lấy tất cả (không filter)
    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_ROLES); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToRole(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm theo ID
    public Role getRoleById(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(GET_ROLE_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRole(rs);
                }
            }
        }
        return null;
    }

    // Tạo mới Role
    public boolean createRole(String roleName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ROLE)) {
            ps.setString(1, roleName);
            return ps.executeUpdate() > 0;
        }
    }

    // Cập nhật Role
    public boolean updateRole(int roleId, String roleName) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_ROLE)) {
            ps.setString(1, roleName);
            ps.setInt(2, roleId);
            return ps.executeUpdate() > 0;
        }
    }

    // Xóa Role
    public void deleteRole(int roleId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_ROLE)) {
            ps.setInt(1, roleId);
            ps.executeUpdate();
        }
    }

    // --- PHẦN SEARCH & PAGING DYNAMIC ---
    public int countRoles(String keyword) {
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";
        try (PreparedStatement ps = connection.prepareStatement(COUNT_ROLES_DYNAMIC)) {
            ps.setString(1, searchName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Role> getRoles(String keyword, int pageIndex, int pageSize) {
        List<Role> list = new ArrayList<>();
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "%%" : "%" + keyword.trim() + "%";
        int offset = (pageIndex - 1) * pageSize;

        try (PreparedStatement ps = connection.prepareStatement(GET_ROLES_DYNAMIC)) {
            ps.setString(1, searchName);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRole(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
