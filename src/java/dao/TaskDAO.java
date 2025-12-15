package dao;

import dbContext.DBContext;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import models.Role;
import models.Room;
import models.ServiceOrder;
import models.Staff;
import models.Task;

/**
 *
 * @author Acer
 */
public class TaskDAO extends DBContext {
//
//    private static final String GET_ALL_TASKS = "SELECT t.task_id, t.task_name, t.description, t.staff_id, "
//            + "t.related_room_id, t.status, t.created_at, t.finished_at, "
//            + "s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, "
//            + "r.role_id, r.role_name, "
//            + "rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, "
//            + "rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//            + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active "
//            + "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "ORDER BY t.task_id";
//
//    private static final String GET_TASK_BY_ID = "SELECT t.task_id, t.task_name, t.description, t.staff_id, "
//            + "t.related_room_id, t.status, t.created_at, t.finished_at, "
//            + "s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, "
//            + "r.role_id, r.role_name, "
//            + "rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, "
//            + "rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//            + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active "
//            + "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "WHERE t.task_id = ?";
//
//    private static final String GET_TASKS_BY_STAFF = "SELECT t.task_id, t.task_name, t.description, t.staff_id, "
//            + "t.related_room_id, t.status, t.created_at, t.finished_at, "
//            + "s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, "
//            + "r.role_id, r.role_name, "
//            + "rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, "
//            + "rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//            + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active "
//            + "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "WHERE t.staff_id = ?";
//
//    private static final String GET_TASKS_BY_ROOM = "SELECT t.task_id, t.task_name, t.description, t.staff_id, "
//            + "t.related_room_id, t.status, t.created_at, t.finished_at, "
//            + "s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, "
//            + "r.role_id, r.role_name, "
//            + "rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, "
//            + "rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//            + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active "
//            + "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "WHERE t.related_room_id = ?";
//
//    private static final String GET_TASKS_BY_STATUS = "SELECT t.task_id, t.task_name, t.description, t.staff_id, "
//            + "t.related_room_id, t.status, t.created_at, t.finished_at, "
//            + "s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, "
//            + "r.role_id, r.role_name, "
//            + "rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, "
//            + "rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//            + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active "
//            + "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "WHERE t.status = ?";
//
//    private static final String INSERT_TASK = "INSERT INTO Tasks (task_name, description, staff_id, related_room_id, status, created_at) "
//            + "VALUES (?, ?, ?, ?, ?, ?)";
//
//    private static final String UPDATE_TASK = "UPDATE Tasks SET task_name = ?, description = ?, staff_id = ?, related_room_id = ?, status = ?, finished_at = ? WHERE task_id = ?";
//
//    private static final String UPDATE_TASK_STATUS = "UPDATE Tasks SET status = ?, finished_at = ? WHERE task_id = ?";
//
//    private static final String DELETE_TASK = "DELETE FROM Tasks WHERE task_id = ?";
//
//    private static final String ASSIGN_TASK = "UPDATE Tasks SET staff_id = ? WHERE task_id = ?";
//
//    private static final String ASSIGN_ROOM = "UPDATE Tasks SET related_room_id = ? WHERE task_id = ?";
//
//    private static final String BASE_TASK_SEARCH = "FROM Tasks t "
//            + "LEFT JOIN Staff s ON t.staff_id = s.staff_id "
//            + "LEFT JOIN StaffRoles r ON s.role_id = r.role_id "
//            + "LEFT JOIN Rooms rm ON t.related_room_id = rm.room_id "
//            + "LEFT JOIN RoomTypes rt ON rm.type_id = rt.type_id "
//            + "WHERE 1=1";
//
//    public ArrayList<Task> getAllTasks() {
//        ArrayList<Task> list = new ArrayList<>();
//        try {
//            PreparedStatement st = connection.prepareStatement(GET_ALL_TASKS);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {
//                list.add(extractTaskFromResultSet(rs));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting all tasks: " + e);
//        }
//        return list;
//    }
//
//    public Task getTaskById(int taskId) {
//        try {
//            PreparedStatement st = connection.prepareStatement(GET_TASK_BY_ID);
//            st.setInt(1, taskId);
//            ResultSet rs = st.executeQuery();
//            if (rs.next()) {
//                return extractTaskFromResultSet(rs);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting task by ID: " + e);
//        }
//        return null;
//    }
//
//    public ArrayList<Task> getTasksByStaffId(int staffId) {
//        ArrayList<Task> list = new ArrayList<>();
//        try {
//            PreparedStatement st = connection.prepareStatement(GET_TASKS_BY_STAFF);
//            st.setInt(1, staffId);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {
//                list.add(extractTaskFromResultSet(rs));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting tasks by staff ID: " + e);
//        }
//        return list;
//    }
//
//    public ArrayList<Task> getTasksByRoomId(int roomId) {
//        ArrayList<Task> list = new ArrayList<>();
//        try {
//            PreparedStatement st = connection.prepareStatement(GET_TASKS_BY_ROOM);
//            st.setInt(1, roomId);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {
//                list.add(extractTaskFromResultSet(rs));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting tasks by room ID: " + e);
//        }
//        return list;
//    }
//
//    public ArrayList<Task> getTasksByStatus(String status) {
//        ArrayList<Task> list = new ArrayList<>();
//        try {
//            PreparedStatement st = connection.prepareStatement(GET_TASKS_BY_STATUS);
//            st.setString(1, status);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {
//                list.add(extractTaskFromResultSet(rs));
//            }
//        } catch (SQLException e) {
//            System.out.println("Error getting tasks by status: " + e);
//        }
//        return list;
//    }
//
//    public boolean insertTask(Task task) {
//        try {
//            PreparedStatement st = connection.prepareStatement(INSERT_TASK);
//            st.setString(1, task.getTaskName());
//            st.setString(2, task.getDescription());
//
//            if (task.getStaff() != null) {
//                st.setInt(3, task.getStaff().getStaffId());
//            } else {
//                st.setNull(3, Types.INTEGER);
//            }
//
//            // Handle related_room_id
//            if (task.getRoom() != null) {
//                st.setInt(4, task.getRoom().getRoomId());
//            } else {
//                st.setNull(4, Types.INTEGER);
//            }
//
//            st.setString(5, task.getStatus());
//            st.setTimestamp(6, task.getCreatedAt() != null ? Timestamp.valueOf(task.getCreatedAt()) : Timestamp.valueOf(LocalDateTime.now()));
//
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error inserting task: " + e);
//        }
//        return false;
//    }
//
//    public boolean updateTask(Task task) {
//        try {
//            PreparedStatement st = connection.prepareStatement(UPDATE_TASK);
//            st.setString(1, task.getTaskName());
//            st.setString(2, task.getDescription());
//
//            if (task.getStaff() != null) {
//                st.setInt(3, task.getStaff().getStaffId());
//            } else {
//                st.setNull(3, Types.INTEGER);
//            }
//
//            // Handle related_room_id
//            if (task.getRoom() != null) {
//                st.setInt(4, task.getRoom().getRoomId());
//            } else {
//                st.setNull(4, Types.INTEGER);
//            }
//
//            st.setString(5, task.getStatus());
//            st.setTimestamp(6, task.getFinishedAt() != null ? Timestamp.valueOf(task.getFinishedAt()) : null);
//            st.setInt(7, task.getTaskId());
//
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error updating task: " + e);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean updateTaskStatus(int taskId, String status) {
//        try {
//            PreparedStatement st = connection.prepareStatement(UPDATE_TASK_STATUS);
//            st.setString(1, status);
//
//            // If status is completed, set finished_at to current time
//            if ("Completed".equalsIgnoreCase(status)) {
//                st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
//            } else {
//                st.setNull(2, Types.TIMESTAMP);
//            }
//
//            st.setInt(3, taskId);
//
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error updating task status: " + e);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean deleteTask(int taskId) {
//        try {
//            PreparedStatement st = connection.prepareStatement(DELETE_TASK);
//            st.setInt(1, taskId);
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error deleting task: " + e);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean assignTaskToStaff(int taskId, int staffId) {
//        try {
//            PreparedStatement st = connection.prepareStatement(ASSIGN_TASK);
//            st.setInt(1, staffId);
//            st.setInt(2, taskId);
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error assigning task to staff: " + e);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean assignTaskToRoom(int taskId, int roomId) {
//        try {
//            PreparedStatement st = connection.prepareStatement(ASSIGN_ROOM);
//            st.setInt(1, roomId);
//            st.setInt(2, taskId);
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error assigning task to room: " + e);
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public ArrayList<Task> search(String search, String status, String roomId, String sort, int pageIndex, int pageSize) {
//        ArrayList<Task> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT t.task_id, t.task_name, t.description, t.staff_id, ")
//                .append("t.related_room_id, t.status, t.created_at, t.finished_at, ")
//                .append("s.staff_id, s.full_name, s.email, s.password, s.is_active, s.created_at as staff_created_at, ")
//                .append("r.role_id, r.role_name, ")
//                .append("rm.room_id, rm.room_number, rm.type_id, rm.status as room_status, rm.room_password, rm.is_active_login, ")
//                .append("rt.type_id as room_type_id, rt.type_name, rt.capacity, rt.description as room_type_description, "
//                        + "rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active as room_type_is_active ")
//                .append(BASE_TASK_SEARCH);
//
//        if (search != null && !search.trim().isEmpty()) {
//            sql.append(" AND (t.task_name LIKE ? OR t.description LIKE ?) ");
//        }
//
//        if (status != null && !status.trim().isEmpty()) {
//            sql.append(" AND t.status = ? ");
//        }
//
//        if (roomId != null && !roomId.trim().isEmpty()) {
//            sql.append(" AND t.related_room_id = ? ");
//        }
//
//        if (sort == null || sort.isEmpty()) {
//            sql.append(" ORDER BY t.task_id ASC ");
//        } else {
//            switch (sort) {
//                case "nameAsc":
//                    sql.append(" ORDER BY t.task_name ASC ");
//                    break;
//                case "nameDesc":
//                    sql.append(" ORDER BY t.task_name DESC ");
//                    break;
//                case "statusAsc":
//                    sql.append(" ORDER BY t.status ASC ");
//                    break;
//                case "statusDesc":
//                    sql.append(" ORDER BY t.status DESC ");
//                    break;
//                case "dateAsc":
//                    sql.append(" ORDER BY t.created_at ASC ");
//                    break;
//                case "dateDesc":
//                    sql.append(" ORDER BY t.created_at DESC ");
//                    break;
//                case "roomAsc":
//                    sql.append(" ORDER BY rm.room_number ASC ");
//                    break;
//                case "roomDesc":
//                    sql.append(" ORDER BY rm.room_number DESC ");
//                    break;
//                case "idDesc":
//                    sql.append(" ORDER BY t.task_id DESC ");
//                    break;
//                case "idAsc":
//                default:
//                    sql.append(" ORDER BY t.task_id ASC ");
//                    break;
//            }
//        }
//
//        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
//
//        try {
//            PreparedStatement st = connection.prepareStatement(sql.toString());
//            int idx = 1;
//
//            if (search != null && !search.trim().isEmpty()) {
//                String searchPattern = "%" + search.trim() + "%";
//                st.setString(idx++, searchPattern);
//                st.setString(idx++, searchPattern);
//            }
//
//            if (status != null && !status.trim().isEmpty()) {
//                st.setString(idx++, status);
//            }
//
//            if (roomId != null && !roomId.trim().isEmpty()) {
//                st.setInt(idx++, Integer.parseInt(roomId));
//            }
//
//            int offset = (pageIndex - 1) * pageSize;
//            st.setInt(idx++, offset);
//            st.setInt(idx, pageSize);
//
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {
//                list.add(extractTaskFromResultSet(rs));
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error search task: " + e);
//        }
//
//        return list;
//    }
//
//    public int countSearch(String search, String status, String roomId) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT COUNT(*) ").append(BASE_TASK_SEARCH);
//
//        if (search != null && !search.trim().isEmpty()) {
//            sql.append(" AND (t.task_name LIKE ? OR t.description LIKE ?) ");
//        }
//
//        if (status != null && !status.trim().isEmpty()) {
//            sql.append(" AND t.status = ? ");
//        }
//
//        if (roomId != null && !roomId.trim().isEmpty()) {
//            sql.append(" AND t.related_room_id = ? ");
//        }
//
//        try {
//            PreparedStatement st = connection.prepareStatement(sql.toString());
//            int idx = 1;
//
//            if (search != null && !search.trim().isEmpty()) {
//                String searchPattern = "%" + search.trim() + "%";
//                st.setString(idx++, searchPattern);
//                st.setString(idx++, searchPattern);
//            }
//
//            if (status != null && !status.trim().isEmpty()) {
//                st.setString(idx++, status);
//            }
//
//            if (roomId != null && !roomId.trim().isEmpty()) {
//                st.setInt(idx++, Integer.parseInt(roomId));
//            }
//
//            ResultSet rs = st.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error count task search: " + e);
//        }
//
//        return 0;
//    }
//
//    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
//        int taskId = rs.getInt("task_id");
//        String taskName = rs.getString("task_name");
//        String description = rs.getString("description");
//        String status = rs.getString("status");
//
//        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
//        LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;
//
//        Timestamp finishedAtTimestamp = rs.getTimestamp("finished_at");
//        LocalDateTime finishedAt = finishedAtTimestamp != null ? finishedAtTimestamp.toLocalDateTime() : null;
//
//        // Extract Staff object if exists
//        Staff staff = null;
//        int staffId = rs.getInt("staff_id");
//        if (!rs.wasNull()) {
//            String fullName = rs.getString("full_name");
//            String email = rs.getString("email");
//            String password = rs.getString("password");
//            boolean isActive = rs.getBoolean("is_active");
//
//            Timestamp staffCreatedAtTimestamp = rs.getTimestamp("staff_created_at");
//            LocalDateTime staffCreatedAt = staffCreatedAtTimestamp != null ? staffCreatedAtTimestamp.toLocalDateTime() : null;
//
//            // Extract Role object if exists
//            Role role = null;
//            int roleId = rs.getInt("role_id");
//            if (!rs.wasNull()) {
//                String roleName = rs.getString("role_name");
//                role = new Role(roleId, roleName);
//            }
//
//            staff = new Staff(staffId, role, fullName, email, password, isActive, staffCreatedAt);
//        }
//
//        // Extract Room object if exists
//        Room room = null;
//        int roomId = rs.getInt("room_id");
//        if (!rs.wasNull()) {
//            String roomNumber = rs.getString("room_number");
//            int typeId = rs.getInt("type_id");
//            String roomStatus = rs.getString("room_status");
//            String roomPassword = rs.getString("room_password");
//            boolean isActiveLogin = rs.getBoolean("is_active_login");
//
//            // --- [SỬA ĐOẠN NÀY] Lấy thêm isEventRoom ---
//            boolean isEventRoom = false;
//            try {
//                // Cố gắng lấy cột isEventRoom. 
//                // Dùng try-catch để nếu câu SQL của bạn chưa select cột này thì code không bị chết, nó sẽ lấy mặc định là false.
//                isEventRoom = rs.getBoolean("isEventRoom");
//            } catch (SQLException e) {
//                // Bỏ qua nếu không tìm thấy cột
//            }
//            // ------------------------------------------
//
//            // Extract RoomType object if exists
//            models.RoomType roomType = null;
//            int roomTypeId = rs.getInt("room_type_id");
//            if (!rs.wasNull()) {
//                String typeName = rs.getString("type_name");
//                int capacity = rs.getInt("capacity");
//                String roomTypeDescription = rs.getString("room_type_description");
//                String imageUrl = rs.getString("image_url");
//                double basePriceWeekday = rs.getDouble("base_price_weekday");
//                double basePriceWeekend = rs.getDouble("base_price_weekend");
//                boolean roomTypeIsActive = rs.getBoolean("room_type_is_active");
//                roomType = new models.RoomType(roomTypeId, typeName, capacity, roomTypeDescription,
//                        imageUrl, java.math.BigDecimal.valueOf(basePriceWeekday), java.math.BigDecimal.valueOf(basePriceWeekend), roomTypeIsActive);
//            }
//            
//            // --- [SỬA ĐOẠN NÀY] Gọi Constructor mới (có thêm isEventRoom ở cuối) ---
//            room = new models.Room(roomId, roomNumber, typeId, roomType, roomStatus, roomPassword, isActiveLogin, isEventRoom);
//        }
//
//        Task task = new Task(taskId, taskName, description, status, createdAt, finishedAt, staff);
//        task.setRoom(room);
//        return task;
//    }
//
//    public static void main(String[] args) {
//        try {
//            System.out.println("Creating TaskDAO...");
//            TaskDAO dao = new TaskDAO();
//
//            System.out.println("Fetching tasks...");
//            List<Task> tasks = dao.search("", "", "", "", 1, 5);
//
//            System.out.println("Found " + tasks.size() + " tasks");
//
//            for (Task t : tasks) {
//                System.out.println("Task ID: " + t.getTaskId());
//                System.out.println("Task Name: " + t.getTaskName());
//                System.out.println("Status: " + t.getStatus());
//                System.out.println("Staff: " + (t.getStaff() != null ? t.getStaff().getFullName() : "None"));
//                System.out.println("Room: " + (t.getRoom() != null ? t.getRoom().getRoomNumber() : "None"));
//                System.out.println("---");
//            }
//        } catch (Exception ex) {
//            System.out.println("Lỗi: " + ex);
//            ex.printStackTrace();  // This will show you the exact line causing the error
//        }
//
//    }
}
