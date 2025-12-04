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

/**
 *
 * @author Admin
 */
public class RoomDAO extends DBContext {

    /**
     * Hàm kiểm tra đăng nhập cho Room Account
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
    
    // (Sau này bạn có thể viết thêm các hàm getListRoom, updateStatus... ở đây)
}