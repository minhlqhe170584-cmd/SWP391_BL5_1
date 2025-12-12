package dao;

import dbContext.DBContext;
import models.Booking;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BookingDAO extends DBContext {

    // 1. Kiểm tra phòng có trống trong khoảng thời gian khách chọn không?
    // Logic: Nếu tồn tại một đơn đặt phòng nào mà (Ngày yêu cầu đến < Ngày người ta đi) VÀ (Ngày yêu cầu đi > Ngày người ta đến)
    // => Tức là bị trùng lịch.
    public boolean isRoomAvailable(int roomId, Timestamp checkIn, Timestamp checkOut) {
        String sql = "SELECT COUNT(*) FROM Bookings " +
                     "WHERE room_id = ? " +
                     "AND status != 'Cancelled' " + // Không tính đơn đã hủy
                     "AND (check_in_date < ? AND check_out_date > ?)";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roomId);
            st.setTimestamp(2, checkOut); // Chú ý thứ tự logic SQL
            st.setTimestamp(3, checkIn);
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Nếu count = 0 là trống (Available)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. Thêm đơn đặt phòng mới
    public boolean insertBooking(Booking b) {
        String sql = "INSERT INTO Bookings(customer_id, room_id, check_in_date, check_out_date, status) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, b.getCustomerId());
            st.setInt(2, b.getRoomId());
            st.setTimestamp(3, b.getCheckInDate());
            st.setTimestamp(4, b.getCheckOutDate());
            st.setString(5, "Confirmed"); // Mặc định là đã xác nhận
            
            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}