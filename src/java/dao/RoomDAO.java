package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Room;
import models.RoomType;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO extends DBContext {

    // 1. Check Login (Cho phép cả phòng ngủ & phòng sự kiện đăng nhập để order dịch vụ)
    public Room checkRoomLogin(String roomNumber, String password) {
        String sql = "SELECT * FROM Rooms WHERE room_number = ? AND room_password = ? AND is_active_login = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, roomNumber);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // Lưu ý: Hàm này trả về Room basic, chưa join RoomType
                Room r = new Room();
                r.setRoomId(rs.getInt("room_id"));
                r.setRoomNumber(rs.getString("room_number"));
                r.setTypeId(rs.getInt("type_id"));
                r.setStatus(rs.getString("room_status"));
                r.setRoomPassword(rs.getString("room_password"));
                r.setActiveLogin(rs.getBoolean("is_active_login"));
                // Lấy thêm cột isEventRoom nếu cần phân biệt sau khi login
                try { r.setIsEventRoom(rs.getInt("isEventRoom") == 1); } catch (Exception e) {} 
                return r;
            }
        } catch (SQLException e) {
            System.out.println("Error checkRoomLogin: " + e.getMessage());
        }
        return null;
    }

    // 2. Lấy tất cả (Chỉ lấy phòng thường, không lấy phòng sự kiện)
    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        // THÊM: WHERE r.isEventRoom = 0
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r "
                   + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.isEventRoom = 0 " 
                   + "ORDER BY r.room_number ASC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                roomList.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllRooms: " + e.getMessage());
        }
        return roomList;
    }
    
    // 2.2 Lấy tất cả (Chỉ lấy phòng sự kiện)
    public List<Room> getAllEventRooms() {
        List<Room> roomList = new ArrayList<>();
        // THÊM: WHERE r.isEventRoom = 0
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r "
                   + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.isEventRoom = 1 " 
                   + "ORDER BY r.room_number ASC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                roomList.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllRooms: " + e.getMessage());
        }
        return roomList;
    }

    // 3. Phân trang (CHỈ LẤY PHÒNG THƯỜNG)
    // pageSize mặc định là 5 trong Servlet, hoặc truyền vào đây
    public List<Room> pagingRooms(int index) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r "
                   + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.isEventRoom = 0 " // <-- CHỈ LẤY PHÒNG NGỦ
                   + "ORDER BY r.room_number ASC "
                   + "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY"; // Hardcode 5 row
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (index - 1) * 5);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error pagingRooms: " + e.getMessage());
        }
        return list;
    }

    // 4. Tìm kiếm cơ bản
    public List<Room> searchRoomsByNumber(String keyword) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.room_number LIKE ? ORDER BY r.room_number ASC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searchRoomsByNumber: " + e.getMessage());
        }
        return list;
    }

    // 5. Tìm kiếm nâng cao (Filter)
    public List<Room> findRooms(String keyword, String typeId, String status, String active, String floor) {
        List<Room> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
              + "FROM Rooms r "
              + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
              + "WHERE 1=1 AND r.isEventRoom = 0 "); // <-- CHỈ LẤY PHÒNG NGỦ

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND r.room_number LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (typeId != null && !typeId.isEmpty()) {
            sql.append(" AND r.type_id = ? ");
            params.add(Integer.parseInt(typeId));
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.room_status = ? ");
            params.add(status);
        }
        if (active != null && !active.isEmpty()) {
            sql.append(" AND r.is_active_login = ? ");
            params.add(Boolean.parseBoolean(active));
        }
        if (floor != null && !floor.isEmpty()) {
            sql.append(" AND SUBSTRING(r.room_number, 1, LEN(r.room_number)-2) = ? ");
            params.add(floor);
        }
        
        sql.append(" ORDER BY r.room_number ASC");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findRooms: " + e.getMessage());
        }
        return list;
    }

    // 6. Lấy chi tiết
    public Room getRoomById(int id) {
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r INNER JOIN RoomTypes t ON r.type_id = t.type_id WHERE r.room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapRoom(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getRoomById: " + e.getMessage());
        }
        return null;
    }

    // 7. Update
    public void updateRoom(Room room) {
        String sql = "UPDATE Rooms SET room_number = ?, type_id = ?, room_status = ?, room_password = ?, is_active_login = ? WHERE room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getTypeId());
            st.setString(3, room.getStatus());
            st.setString(4, room.getRoomPassword());
            st.setBoolean(5, room.isActiveLogin()); // SỬA: Gọi hàm chuẩn
            st.setInt(6, room.getRoomId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateRoom: " + e.getMessage());
        }
    }

    // 8. Insert
    public void insertRoom(Room room) {
        String sql = "INSERT INTO Rooms (room_number, type_id, room_status, room_password, is_active_login) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getTypeId());
            st.setString(3, room.getStatus());
            st.setString(4, room.getRoomPassword());
            st.setBoolean(5, room.isActiveLogin()); // SỬA: Gọi hàm chuẩn
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error insertRoom: " + e.getMessage());
        }
    }

    // 9. Update Status (Ban/Unban)
    public void updateRoomStatus(int roomId, String newStatus) {
        String sql = "UPDATE Rooms SET room_status = ? WHERE room_id = ?"; // SỬA: room_status
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, newStatus);
            st.setInt(2, roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateRoomStatus: " + e.getMessage());
        }
    }

    // 10. Helper: Map ResultSet to Object (Tránh lặp code)
    private Room mapRoom(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setStatus(rs.getString("room_status")); // SỬA: room_status
        r.setRoomPassword(rs.getString("room_password"));
        r.setActiveLogin(rs.getBoolean("is_active_login")); // SỬA: setActiveLogin
        r.setTypeId(rs.getInt("type_id"));
        
        try { r.setLastCleanedAt(rs.getTimestamp("last_cleaned_at")); } catch (Exception e) {}

        // Map RoomType (nếu có join)
        try {
            RoomType rt = new RoomType();
            rt.setTypeId(rs.getInt("type_id"));
            rt.setTypeName(rs.getString("type_name"));
            // Các trường phụ có thể null tùy câu query
            try { rt.setCapacity(rs.getInt("capacity")); } catch(Exception e){}
            try { rt.setDescription(rs.getString("description")); } catch(Exception e){}
            try { rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday")); } catch(Exception e){}
            try { rt.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend")); } catch(Exception e){}
            r.setRoomType(rt);
        } catch (Exception e) { /* Bỏ qua nếu query không join */ }
        
        return r;
    }

    // Các hàm phụ khác
    // Đếm tổng số phòng (Chỉ đếm phòng thường)
    public int getTotalRooms() {
        // THÊM: WHERE isEventRoom = 0
        String sql = "SELECT COUNT(*) FROM Rooms WHERE isEventRoom = 0";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }

    public List<Integer> getExistingFloors() {
        List<Integer> floors = new ArrayList<>();
        // CẬP NHẬT: Thêm điều kiện 'AND isEventRoom = 0'
        // Mục đích: Chỉ lấy tầng của phòng khách (vd: 101, 205). 
        // Tránh lỗi khi gặp phòng sự kiện tên chữ (vd: HALL-A) không thể ép kiểu sang số (INT) được.
        String sql = "SELECT DISTINCT CAST(SUBSTRING(room_number, 1, LEN(room_number)-2) AS INT) as floor_num "
                   + "FROM Rooms "
                   + "WHERE LEN(room_number) > 2 AND isEventRoom = 0 " // <-- QUAN TRỌNG
                   + "ORDER BY floor_num";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                floors.add(rs.getInt("floor_num"));
            }
        } catch (SQLException e) {
            System.err.println("Error getExistingFloors: " + e.getMessage());
        }
        return floors;
    }

    public boolean checkRoomNumberExists(String roomNumber, int currentId) {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE room_number = ? AND room_id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, roomNumber);
            st.setInt(2, currentId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {}
        return false;
    }

    public void deleteRoom(int roomId) {
        String sql = "DELETE FROM Rooms WHERE room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.executeUpdate();
        } catch (SQLException e) {}
    }

    // Lấy danh sách Loại phòng (CHỈ LẤY LOẠI PHÒNG NGỦ - isEventRoom = 0)
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        // SỬA: Thêm điều kiện WHERE isEventRoom = 0
        String sql = "SELECT * FROM RoomTypes WHERE isEventRoom = 0"; 
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                rt.setCapacity(rs.getInt("capacity"));
                // ... (các set khác nếu cần)
                list.add(rt);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getAllRoomTypes: " + e.getMessage());
        }
        return list;
    }
    // 1. Hàm đếm tổng số phòng (Để tính tổng số trang)
    public int countTotalRooms() {
        String sql = "SELECT COUNT(*) FROM Rooms";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Hàm lấy phòng theo trang (Mỗi trang lấy pageSize phòng)
    public List<Room> getRoomsByPage(int pageIndex, int pageSize) {
        List<Room> list = new ArrayList<>();
        
        // Cập nhật SQL: Thêm WHERE r.isEventRoom = 0 trước ORDER BY
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.isEventRoom = 0 " // <-- ĐIỀU KIỆN QUAN TRỌNG
                   + "ORDER BY r.room_number ASC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
                   
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (pageIndex - 1) * pageSize); // Vị trí bắt đầu
            st.setInt(2, pageSize);                   // Số lượng lấy
            
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                // Nếu bạn đã có hàm mapRoom(rs) thì dùng cho gọn: list.add(mapRoom(rs));
                // Còn không thì giữ nguyên code map thủ công dưới đây:
                Room r = new Room();
                r.setRoomId(rs.getInt("room_id"));
                r.setRoomNumber(rs.getString("room_number"));
                r.setStatus(rs.getString("room_status"));
                r.setActiveLogin(rs.getBoolean("is_active_login"));
                
                models.RoomType rt = new models.RoomType();
                rt.setTypeName(rs.getString("type_name"));
                rt.setCapacity(rs.getInt("capacity"));
                rt.setDescription(rs.getString("description"));
                rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                
                r.setRoomType(rt);
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    // --- [NEW] CÁC HÀM DÀNH CHO EVENT ROOM (isEventRoom = 1) ---

    // 11. Đếm tổng số phòng SỰ KIỆN
    public int getTotalEventRooms() {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE isEventRoom = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {}
        return 0;
    }

    // 12. Phân trang phòng SỰ KIỆN
    public List<Room> pagingEventRooms(int index) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.isEventRoom = 1 " // <-- CHỈ LẤY PHÒNG SỰ KIỆN
                   + "ORDER BY r.room_number ASC "
                   + "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (index - 1) * 5);
            ResultSet rs = st.executeQuery();
            while (rs.next()) list.add(mapRoom(rs));
        } catch (SQLException e) {
            System.out.println("Error pagingEventRooms: " + e.getMessage());
        }
        return list;
    }

    // 13. Tìm kiếm nâng cao phòng SỰ KIỆN (Thêm tham số keyword)
    // Lưu ý: Đã sửa lại signature của hàm để nhận thêm keyword
    public List<Room> findEventRooms(String keyword, String typeId, String minCap, String maxCap) {
        List<Room> list = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
              + "FROM Rooms r "
              + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
              + "WHERE r.isEventRoom = 1 "); 

        List<Object> params = new ArrayList<>();

        // 1. [MỚI] Lọc theo Tên Sảnh (keyword)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND r.room_number LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }

        // 2. Lọc theo Loại phòng
        if (typeId != null && !typeId.trim().isEmpty()) {
            sql.append(" AND r.type_id = ? ");
            params.add(Integer.parseInt(typeId));
        }

        // 3. Lọc theo Sức chứa (Min)
        if (minCap != null && !minCap.trim().isEmpty()) {
            sql.append(" AND t.capacity >= ? ");
            params.add(Integer.parseInt(minCap));
        }

        // 4. Lọc theo Sức chứa (Max)
        if (maxCap != null && !maxCap.trim().isEmpty()) {
            sql.append(" AND t.capacity <= ? ");
            params.add(Integer.parseInt(maxCap));
        }
        
        sql.append(" ORDER BY t.capacity ASC, r.room_number ASC");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRoom(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findEventRooms: " + e.getMessage());
        }
        return list;
    }
    
    // 14. Lấy danh sách Loại phòng SỰ KIỆN (Cho Dropdown Filter bên Event Room)
    public List<RoomType> getAllEventRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomTypes WHERE isEventRoom = 1"; // Chỉ lấy loại sảnh tiệc
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                list.add(rt);
            }
        } catch (SQLException e) {}
        return list;
    }
    
    // 15. Lấy chi tiết phòng SỰ KIỆN theo ID (Dùng cho chức năng Lock/Edit bên EventRoomServlet)
    public Room getEventRoomById(int id) {
        String sql = "SELECT r.*, t.type_name, t.capacity, t.description, t.base_price_weekday, t.base_price_weekend "
                   + "FROM Rooms r INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                   + "WHERE r.room_id = ? AND r.isEventRoom = 1"; // <-- Quan trọng: isEventRoom = 1
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return mapRoom(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getEventRoomById: " + e.getMessage());
        }
        return null;
    }
}