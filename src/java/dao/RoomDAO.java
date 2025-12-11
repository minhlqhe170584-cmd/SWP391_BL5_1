/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Room;
import models.RoomType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class RoomDAO extends DBContext {

    /**
     * Hàm kiểm tra đăng nhập cho Room Account
     */
    public Room checkRoomLogin(String roomNumber, String password) {
        String sql = "SELECT * FROM Rooms WHERE room_number = ? AND room_password = ? AND is_active_login = 1";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, roomNumber);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Room r = new Room();
                r.setRoomId(rs.getInt("room_id"));
                r.setRoomNumber(rs.getString("room_number"));
                r.setTypeId(rs.getInt("type_id"));
                r.setStatus(rs.getString("status"));
                r.setRoomPassword(rs.getString("room_password"));
                r.setActiveLogin(rs.getBoolean("is_active_login"));

                return r;
            }
        } catch (SQLException e) {
            System.out.println("Error checkRoomLogin: " + e.getMessage());
        }
        return null;
    }

    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();

        String sql = "SELECT r.room_id, r.room_number, r.room_status, r.room_password, r.is_active_login, r.type_id, "
                + "t.type_name "
                + "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + "ORDER BY r.room_number ASC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("room_status"));
                room.setRoomPassword(rs.getString("room_password"));
                room.setActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));

                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name")); 
                room.setRoomType(rt);

                roomList.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn SQL getAllRooms: " + e.getMessage());
        }
        return roomList;
    }

    //Phần xử lý phân trang màn hình List Room
    // 1. Hàm đếm tổng số lượng phòng
    public int getTotalRooms() {
        String sql = "SELECT COUNT(*) FROM Rooms";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getTotalRooms: " + e.getMessage());
        }
        return 0;
    }

    // 2. Hàm lấy danh sách phòng theo trang (Mỗi trang 5 phòng)
    public List<Room> pagingRooms(int index) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.room_id, r.room_number, r.room_status, r.room_password, r.is_active_login, r.type_id, t.type_name "
                + "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + "ORDER BY r.room_number ASC "
                + "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (index - 1) * 5);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("room_status"));
                room.setActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));

                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                room.setRoomType(rt);

                list.add(room);
            }
        } catch (SQLException e) {
            System.out.println("Error pagingRooms: " + e.getMessage());
        }
        return list;
    }

    // Hàm tìm kiếm theo số phòng
    public List<Room> searchRoomsByNumber(String keyword) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.room_id, r.room_number, r.room_status, r.room_password, r.is_active_login, r.type_id, t.type_name "
                + "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + "WHERE r.room_number LIKE ? "
                + "ORDER BY r.room_number ASC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("room_status"));
                room.setActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));

                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                room.setRoomType(rt);

                list.add(room);
            }
        } catch (SQLException e) {
            System.out.println("Error searchRoomsByNumber: " + e.getMessage());
        }
        return list;
    }

    // === TÌM KIẾM CÓ BỘ LỌC ===
    public List<Room> findRooms(String keyword, String typeId, String status, String active, String floor) {
        List<Room> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT r.room_id, r.room_number, r.room_status, r.is_active_login, r.type_id, t.type_name "
                + "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // 1. Keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND r.room_number LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        // 2. Type
        if (typeId != null && !typeId.isEmpty()) {
            sql.append(" AND r.type_id = ? ");
            params.add(Integer.parseInt(typeId));
        }
        // 3. Status
        if (status != null && !status.isEmpty()) {
            sql.append(" AND r.room_status = ? ");
            params.add(status);
        }
        // 4. Active
        if (active != null && !active.isEmpty()) {
            sql.append(" AND r.is_active_login = ? ");
            params.add(Boolean.parseBoolean(active));
        }
        
        // 5. Logic lọc tầng (ĐÃ SỬA: Dùng SUBSTRING thay vì LIKE để tránh lỗi 1 -> 10, 11)
        if (floor != null && !floor.isEmpty()) {
            // Logic: Cắt bỏ 2 ký tự cuối để lấy số tầng, so sánh chính xác
            sql.append(" AND SUBSTRING(r.room_number, 1, LEN(r.room_number)-2) = ? ");
            params.add(floor);
        }

        sql.append(" ORDER BY r.room_number ASC");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            // Gán tham số
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setStatus(rs.getString("room_status"));
                    room.setActiveLogin(rs.getBoolean("is_active_login"));
                    room.setTypeId(rs.getInt("type_id"));

                    RoomType rt = new RoomType();
                    rt.setTypeId(rs.getInt("type_id"));
                    rt.setTypeName(rs.getString("type_name"));
                    room.setRoomType(rt);

                    list.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findRooms: " + e.getMessage());
        }
        return list;
    }

    // === HÀM LẤY DANH SÁCH TẦNG TỪ DB (CẦN CHO DROPDOWN) ===
    public List<Integer> getExistingFloors() {
        List<Integer> floors = new ArrayList<>();
        // Logic: Lấy các số tầng tồn tại, loại bỏ trùng lặp, sắp xếp tăng dần
        String sql = "SELECT DISTINCT CAST(SUBSTRING(room_number, 1, LEN(room_number)-2) AS INT) as floor_num "
                + "FROM Rooms "
                + "WHERE LEN(room_number) > 2 " // Chỉ lấy số phòng hợp lệ (> 2 ký tự)
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

    public List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomTypes";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                list.add(rt);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getAllRoomTypes: " + e.getMessage());
        }
        return list;
    }

    //Phần xử lý Room Details
    public Room getRoomById(int id) {
        String sql = "SELECT r.room_id, r.room_number, r.room_status, r.room_password, r.is_active_login, r.type_id, "
                + "t.type_name, t.capacity, t.base_price_weekday, t.base_price_weekend, t.description "
                + "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + "WHERE r.room_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("room_id"));
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setStatus(rs.getString("room_status"));
                    room.setRoomPassword(rs.getString("room_password"));
                    room.setActiveLogin(rs.getBoolean("is_active_login"));
                    room.setTypeId(rs.getInt("type_id"));

                    models.RoomType rt = new models.RoomType();
                    rt.setTypeId(rs.getInt("type_id"));
                    rt.setTypeName(rs.getString("type_name"));
                    rt.setCapacity(rs.getInt("capacity"));
                    rt.setDescription(rs.getString("description"));
                    rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                    rt.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend"));

                    room.setRoomType(rt);

                    return room;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getRoomById: " + e.getMessage());
        }
        return null;
    }

    //Phần xử lý Update Room
    public void updateRoom(Room room) {
        String sql = "UPDATE Rooms SET room_number = ?, type_id = ?, room_status = ?, room_password = ?, is_active_login = ? WHERE room_id = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getTypeId());
            st.setString(3, room.getStatus());
            st.setString(4, room.getRoomPassword());
            st.setBoolean(5, room.isActiveLogin());
            st.setInt(6, room.getRoomId());

            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateRoom: " + e.getMessage());
        }
    }

    //Kiểm tra số phòng đã tồn tại chưa
    public boolean checkRoomNumberExists(String roomNumber, int currentId) {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE room_number = ? AND room_id != ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, roomNumber);
            st.setInt(2, currentId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checkRoomNumberExists: " + e.getMessage());
        }
        return false;
    }

    //Phần Ban Room cấm phòng
    public void updateRoomStatus(int roomId, String newStatus) {
        String sql = "UPDATE Rooms SET room_status = ? WHERE room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, newStatus);
            st.setInt(2, roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateRoomStatus: " + e.getMessage());
        }
    }

    //Phần xử lý Insert phòng(Thêm phòng mới)
    public void insertRoom(Room room) {
        String sql = "INSERT INTO Rooms (room_number, type_id, room_status, room_password, is_active_login) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, room.getRoomNumber());
            st.setInt(2, room.getTypeId());
            st.setString(3, room.getStatus());
            st.setString(4, room.getRoomPassword());
            st.setBoolean(5, room.isActiveLogin());

            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error insertRoom: " + e.getMessage());
        }
    }

    //Phần xử lý delete phòng
    public void deleteRoom(int roomId) {
        String sql = "DELETE FROM Rooms WHERE room_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, roomId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleteRoom: " + e.getMessage());
        }
    }
}