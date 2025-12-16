package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.Role;
import models.ServiceOrder;
import models.Staff;
import models.Task;

public class TaskDAO extends DBContext {

    private static final String BASE_TASK_SEARCH = "FROM Tasks t "
            + "JOIN Staff s ON t.staff_id = s.staff_id "
            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
            + "JOIN ServiceOrders so ON t.order_id = so.order_id "
            + "JOIN Rooms rm ON so.room_id = rm.room_id "
            + "WHERE 1 = 1";

    public ArrayList<Task> search(String search, String status, String staffId, String sort, int pageIndex, int pageSize) {
        ArrayList<Task> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.task_id, t.task_name, t.description, t.order_id, t.staff_id, t.status, t.created_at, ")
           .append("s.staff_id AS s_staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at AS staff_created_at, ")
           .append("r.role_id, r.role_name, ")
           .append("so.order_id AS so_order_id, so.room_id, so.order_date, so.total_amount, so.status AS order_status, so.note, ")
           .append("rm.room_number ")
           .append(BASE_TASK_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (t.task_name LIKE ? OR t.description LIKE ? OR s.full_name LIKE ? OR rm.room_number LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND t.status = ? ");
        }

        if (staffId != null && !staffId.trim().isEmpty()) {
            sql.append(" AND t.staff_id = ? ");
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY t.task_id ASC ");
        } else {
            switch (sort) {
                case "nameAsc":
                    sql.append(" ORDER BY t.task_name ASC ");
                    break;
                case "nameDesc":
                    sql.append(" ORDER BY t.task_name DESC ");
                    break;
                case "statusAsc":
                    sql.append(" ORDER BY t.status ASC ");
                    break;
                case "statusDesc":
                    sql.append(" ORDER BY t.status DESC ");
                    break;
                case "dateAsc":
                    sql.append(" ORDER BY t.created_at ASC ");
                    break;
                case "dateDesc":
                    sql.append(" ORDER BY t.created_at DESC ");
                    break;
                case "staffAsc":
                    sql.append(" ORDER BY s.full_name ASC ");
                    break;
                case "staffDesc":
                    sql.append(" ORDER BY s.full_name DESC ");
                    break;
                case "roomAsc":
                    sql.append(" ORDER BY rm.room_number ASC ");
                    break;
                case "roomDesc":
                    sql.append(" ORDER BY rm.room_number DESC ");
                    break;
                case "idDesc":
                    sql.append(" ORDER BY t.task_id DESC ");
                    break;
                case "idAsc":
                default:
                    sql.append(" ORDER BY t.task_id ASC ");
                    break;
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                st.setString(idx++, status);
            }

            if (staffId != null && !staffId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(staffId));
            }

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error search task: " + e);
        }

        return list;
    }

    public int countSearch(String search, String status, String staffId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_TASK_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (t.task_name LIKE ? OR t.description LIKE ? OR s.full_name LIKE ? OR rm.room_number LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND t.status = ? ");
        }

        if (staffId != null && !staffId.trim().isEmpty()) {
            sql.append(" AND t.staff_id = ? ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                st.setString(idx++, status);
            }

            if (staffId != null && !staffId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(staffId));
            }

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error count task search: " + e);
        }

        return 0;
    }

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        int taskId = rs.getInt("task_id");
        String taskName = rs.getString("task_name");
        String description = rs.getString("description");
        int orderId = rs.getInt("order_id");
        int staffId = rs.getInt("staff_id");
        String status = rs.getString("status");

        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;

        // Staff
        Staff staff = null;
        int sStaffId = rs.getInt("s_staff_id");
        if (!rs.wasNull()) {
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            boolean isActive = rs.getBoolean("is_active");

            Timestamp staffCreatedAtTimestamp = rs.getTimestamp("staff_created_at");
            LocalDateTime staffCreatedAt = staffCreatedAtTimestamp != null ? staffCreatedAtTimestamp.toLocalDateTime() : null;

            Role role = null;
            int roleId = rs.getInt("role_id");
            if (!rs.wasNull()) {
                String roleName = rs.getString("role_name");
                role = new Role(roleId, roleName);
            }

            staff = new Staff(sStaffId, role, fullName, email, password, isActive, staffCreatedAt);
        }

        // ServiceOrder
        ServiceOrder serviceOrder = null;
        int soOrderId = rs.getInt("so_order_id");
        if (!rs.wasNull()) {
            int roomId = rs.getInt("room_id");
            Timestamp orderDate = rs.getTimestamp("order_date");
            double totalAmount = rs.getDouble("total_amount");
            String orderStatus = rs.getString("order_status");
            String note = rs.getString("note");
            String roomNumber = rs.getString("room_number");

            serviceOrder = new ServiceOrder(soOrderId, roomId, orderDate, totalAmount, orderStatus, note, roomNumber);
        }

        Task task = new Task(taskId, orderId, staffId, taskName, description, status, staff, serviceOrder, createdAt);
        return task;
    }
}
