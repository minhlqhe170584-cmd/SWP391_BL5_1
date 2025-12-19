package dao;

import dbContext.DBContext;
import models.Room;
import models.RoomType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRoomDAO extends DBContext {

    // Hàm này CHUYÊN DÙNG cho trang Booking Form
    // Nó JOIN bảng RoomTypes để lấy Giá tiền và Tên loại phòng
    public Room getRoomForBooking(int roomId) {
        String sql = "SELECT r.room_id, r.room_number, r.room_status, " +
                     "       t.type_id, t.type_name, t.base_price_weekday, t.capacity " +
                     "FROM Rooms r " +
                     "JOIN RoomTypes t ON r.type_id = t.type_id " +
                     "WHERE r.room_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Room r = new Room();
                r.setRoomId(rs.getInt("room_id"));
                r.setRoomNumber(rs.getString("room_number"));
                r.setStatus(rs.getString("room_status")); 
                
                // Map thông tin loại phòng (để lấy giá hiển thị)
                RoomType t = new RoomType();
                t.setTypeId(rs.getInt("type_id"));
                t.setTypeName(rs.getString("type_name"));
                
                // Lưu ý: Đảm bảo DB bạn có cột base_price_weekday hoặc sửa tên cột cho đúng
                try {
                    t.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                } catch (Exception e) {
                    // Nếu lỗi map giá, để mặc định 0 hoặc log lỗi
                    System.out.println("Lỗi map giá: " + e.getMessage());
                }
                
                t.setCapacity(rs.getInt("capacity"));
                
                r.setRoomType(t);
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}