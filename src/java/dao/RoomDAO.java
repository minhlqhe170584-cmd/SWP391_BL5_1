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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Đảm bảo import BigDecimal

/**
 *
 * @author Admin
 */
public class RoomDAO extends DBContext {

    /**
     * Hàm kiểm tra đăng nhập cho Room Account
     *
     * @param roomNumber Số phòng (User nhập vào)
     * @param password Mật khẩu phòng (User nhập vào)
     * @return Object Room nếu đúng, null nếu sai
     */
    public Room checkRoomLogin(String roomNumber, String password) {
        // SQL Logic:
        // 1. Đúng số phòng (room_number)
        // 2. Đúng mật khẩu (room_password)
        // 3. Phòng đang được kích hoạt cho phép đăng nhập (is_active_login = 1)
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

        String sql = "SELECT r.room_id, r.room_number, r.status, r.room_password, r.is_active_login, r.type_id, "
                + "t.type_name "
                + // Lấy thêm cột tên loại phòng
                "FROM Rooms r "
                + "INNER JOIN RoomTypes t ON r.type_id = t.type_id "
                + // Kết nối 2 bảng qua type_id
                "ORDER BY r.room_number ASC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Room room = new Room();

                // Set các thuộc tính cơ bản của Room
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("status"));
                room.setRoomPassword(rs.getString("room_password"));
                room.setActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));

                // --- PHẦN MỚI THÊM ---
                // Tạo đối tượng RoomType và set tên lấy từ DB
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name")); // Lấy type_name từ kết quả join

                // Gán RoomType vào Room
                room.setRoomType(rt);
                // ---------------------

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
        // SQL Server: Dùng OFFSET và FETCH NEXT để phân trang
        // index: số trang hiện tại (1, 2, 3...)
        // (index - 1) * 5: Số dòng cần bỏ qua
        String sql = "SELECT r.room_id, r.room_number, r.status, r.room_password, r.is_active_login, r.type_id, t.type_name " +
                     "FROM Rooms r " +
                     "INNER JOIN RoomTypes t ON r.type_id = t.type_id " +
                     "ORDER BY r.room_number ASC " +
                     "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            // Tính toán vị trí bắt đầu lấy
            st.setInt(1, (index - 1) * 5); 
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("status"));
                room.setActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));
                
                // Set Room Type Name
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
    //Kết thúc phần xử lý phân trang
    
    
    //Phần xử lý tìm kiếm phòng theo roomNumber
    // Hàm tìm kiếm theo số phòng (Search by Room Number)
    public List<Room> searchRoomsByNumber(String keyword) {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT r.room_id, r.room_number, r.status, r.room_password, r.is_active_login, r.type_id, t.type_name " +
                     "FROM Rooms r " +
                     "INNER JOIN RoomTypes t ON r.type_id = t.type_id " +
                     "WHERE r.room_number LIKE ? " +  // Điều kiện tìm kiếm
                     "ORDER BY r.room_number ASC";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            // Thêm dấu % để tìm gần đúng (VD: nhập "10" tìm ra "101", "102", "210"...)
            st.setString(1, "%" + keyword + "%"); 
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("status"));
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

}
