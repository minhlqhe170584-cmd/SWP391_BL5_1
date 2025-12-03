// File: RoomDAO.java

package dao; 

import dbContext.DBContext;
import models.Room;
import models.RoomType;
import models.RoomStatus; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Đảm bảo import BigDecimal

public class RoomDAO extends DBContext { 
    // DBContext được sử dụng để lấy Connection
    /**
     * Lấy toàn bộ danh sách phòng (Phiên bản gọn gàng, sử dụng try-with-resources).
     * @return Danh sách các đối tượng Room.
     */
    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        
        // Sử dụng try-with-resources để tự động đóng Connection, Statement, ResultSet.
        try (Connection connection = getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT R.room_id, R.room_number, R.status, R.room_password, R.is_active_login, R.type_id, " +
        "RT.type_name, RT.capacity, RT.base_price_weekday, RT.base_price_weekend " +
        "FROM Rooms R " +
        "JOIN RoomTypes RT ON R.type_id = RT.type_id " +
        "ORDER BY R.room_number ASC");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                
                // 1. Ánh xạ RoomType Detail
                RoomType typeDetail = new RoomType();
                typeDetail.setTypeId(rs.getInt("type_id"));
                typeDetail.setTypeName(rs.getString("type_name"));
                typeDetail.setCapacity(rs.getInt("capacity"));
                typeDetail.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                typeDetail.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend"));

                // 2. Ánh xạ Room
                Room room = new Room();
                
                // Ánh xạ thuộc tính Room
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setTypeId(rs.getInt("type_id"));
                room.setRoomPassword(rs.getString("room_password"));
                room.setIsActiveLogin(rs.getBoolean("is_active_login"));
                
                // Chuyển đổi và ánh xạ Status (String -> Enum)
                String statusString = rs.getString("status");
                room.setStatus(RoomStatus.fromString(statusString)); 
                
                // Gán chi tiết loại phòng
                room.setRoomTypeDetail(typeDetail);
                
                roomList.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn SQL: " + e.getMessage());
            // Có thể re-throw RuntimeException ở đây nếu không muốn xử lý lỗi tại chỗ
        } 
        return roomList;
    }
}