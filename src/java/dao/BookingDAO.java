package dao;

import dbContext.DBContext;
import models.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {

    // 1. Lấy toàn bộ danh sách (Dùng cho Admin/Lễ tân xem lịch sử)
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, c.full_name, r.room_number " +
                     "FROM Bookings b " +
                     "LEFT JOIN Customers c ON b.customer_id = c.customer_id " +
                     "LEFT JOIN Rooms r ON b.room_id = r.room_id " +
                     "ORDER BY b.booking_id DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. Lấy 1 Booking theo ID (Dùng khi cần xem chi tiết hoặc sửa)
    public Booking getBookingById(int id) {
        String sql = "SELECT b.*, c.full_name, r.room_number " +
                     "FROM Bookings b " +
                     "LEFT JOIN Customers c ON b.customer_id = c.customer_id " +
                     "LEFT JOIN Rooms r ON b.room_id = r.room_id " +
                     "WHERE b.booking_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 3. THÊM MỚI (Khách đặt phòng)
    // Logic: Lưu Booking -> Đổi trạng thái phòng thành 'Reserved' (Giữ chỗ)
    public boolean addBooking(Booking b) {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdateRoom = null;

        // Bỏ cột 'note', bỏ 'created_at' (để DB tự sinh hoặc null)
        String sqlInsert = "INSERT INTO Bookings "
                + "(customer_id, room_id, check_in_date, check_out_date, status, booking_code, total_amount, real_check_in, real_check_out) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdateRoomStatus = "UPDATE Rooms SET room_status = 'Reserved' WHERE room_id = ?";

        try {
            conn = connection;
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // BƯỚC 1: Insert Booking
            psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setInt(1, b.getCustomerId());
            psInsert.setInt(2, b.getRoomId());
            psInsert.setDate(3, b.getCheckInDate());
            psInsert.setDate(4, b.getCheckOutDate());
            psInsert.setString(5, b.getStatus()); // Thường là 'Pending'
            psInsert.setString(6, b.getBookingCode());
            psInsert.setDouble(7, b.getTotalAmount());
            psInsert.setTimestamp(8, null); // real_check_in (chưa có)
            psInsert.setTimestamp(9, null); // real_check_out (chưa có)
            
            psInsert.executeUpdate();

            // BƯỚC 2: Update Room Status -> Reserved
            psUpdateRoom = conn.prepareStatement(sqlUpdateRoomStatus);
            psUpdateRoom.setInt(1, b.getRoomId());
            psUpdateRoom.executeUpdate();

            // --- CHỐT GIAO DỊCH ---
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (psInsert != null) psInsert.close();
                if (psUpdateRoom != null) psUpdateRoom.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 4. CHECK-IN (Lễ tân làm thủ tục)
    // Logic: Đổi status đơn -> 'CheckedIn', Đổi phòng -> 'Occupied' (Đang ở)
    public boolean checkInBooking(int bookingId, int roomId) {
        String sqlUpdateBooking = "UPDATE Bookings SET status = 'CheckedIn', real_check_in = GETDATE() WHERE booking_id = ?";
        String sqlUpdateRoom = "UPDATE Rooms SET room_status = 'Occupied' WHERE room_id = ?";
        
        return executeTransaction(bookingId, roomId, sqlUpdateBooking, sqlUpdateRoom);
    }

    // 5. CHECK-OUT (Lễ tân trả phòng)
    // Logic: Đổi status đơn -> 'CheckedOut', Đổi phòng -> 'Available' (Trống)
    public boolean checkOutBooking(int bookingId, int roomId) {
        String sqlUpdateBooking = "UPDATE Bookings SET status = 'CheckedOut', real_check_out = GETDATE() WHERE booking_id = ?";
        String sqlUpdateRoom = "UPDATE Rooms SET room_status = 'Available' WHERE room_id = ?";
        
        return executeTransaction(bookingId, roomId, sqlUpdateBooking, sqlUpdateRoom);
    }

    // 6. HỦY ĐƠN (Admin/Lễ tân hủy khi khách không đến)
    // Logic: Đổi status đơn -> 'Cancelled', Đổi phòng -> 'Available' (Trống)
    public boolean cancelBooking(int bookingId, int roomId) {
        String sqlUpdateBooking = "UPDATE Bookings SET status = 'Cancelled' WHERE booking_id = ?";
        String sqlUpdateRoom = "UPDATE Rooms SET room_status = 'Available' WHERE room_id = ?";
        
        return executeTransaction(bookingId, roomId, sqlUpdateBooking, sqlUpdateRoom);
    }

    // 7. Cập nhật thông tin đơn (Ít dùng, nhưng giữ lại cho đầy đủ CRUD)
    public boolean updateBooking(Booking b) {
        String sql = "UPDATE Bookings SET room_id=?, check_in_date=?, check_out_date=?, status=?, real_check_in=?, real_check_out=?, total_amount=? " +
                     "WHERE booking_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, b.getRoomId());
            ps.setDate(2, b.getCheckInDate());
            ps.setDate(3, b.getCheckOutDate());
            ps.setString(4, b.getStatus());
            ps.setTimestamp(5, b.getRealCheckIn());
            ps.setTimestamp(6, b.getRealCheckOut());
            ps.setDouble(7, b.getTotalAmount());
            ps.setInt(8, b.getBookingId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 8. Xóa hẳn đơn (Cẩn thận khi dùng)
    public boolean deleteBooking(int id) {
        String sql = "DELETE FROM Bookings WHERE booking_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ==================================================================
    // 9. [MỚI] KIỂM TRA TRÙNG LỊCH (Check Availability)
    // Logic: Đếm xem có đơn nào KHÁC 'Cancelled'/'Rejected' 
    //        mà thời gian đè lên khoảng khách chọn không.
    // ==================================================================
    public boolean checkAvailability(int roomId, Date checkIn, Date checkOut) {
        String sql = "SELECT COUNT(*) FROM Bookings "
                   + "WHERE room_id = ? "
                   + "AND status NOT IN ('Cancelled', 'Rejected') " // Bỏ qua đơn đã hủy
                   + "AND (check_in_date < ? AND check_out_date > ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setDate(2, checkOut); // Logic: Start cũ < End mới
            ps.setDate(3, checkIn);  // Logic: End cũ > Start mới
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                // Nếu count == 0 nghĩa là KHÔNG có đơn nào trùng -> Phòng trống (Available)
                return count == 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mặc định trả về false (coi như bận) nếu lỗi DB để an toàn
    }

    // --- HÀM PHỤ TRỢ ---

    // Hàm chạy Transaction chung cho Check-in/Check-out/Cancel (Tránh lặp code)
    private boolean executeTransaction(int bookingId, int roomId, String sqlBooking, String sqlRoom) {
        Connection conn = null;
        try {
            conn = connection;
            conn.setAutoCommit(false); // Bắt đầu

            // 1. Update trạng thái đơn hàng
            try (PreparedStatement ps1 = conn.prepareStatement(sqlBooking)) {
                ps1.setInt(1, bookingId);
                ps1.executeUpdate();
            }

            // 2. Update trạng thái phòng
            try (PreparedStatement ps2 = conn.prepareStatement(sqlRoom)) {
                ps2.setInt(1, roomId);
                ps2.executeUpdate();
            }

            conn.commit(); // Thành công
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    // Hàm map dữ liệu từ ResultSet vào Object Booking
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setCheckInDate(rs.getDate("check_in_date"));
        b.setCheckOutDate(rs.getDate("check_out_date"));
        b.setStatus(rs.getString("status"));
        b.setRealCheckIn(rs.getTimestamp("real_check_in"));
        b.setRealCheckOut(rs.getTimestamp("real_check_out"));
        b.setBookingCode(rs.getString("booking_code"));
        b.setTotalAmount(rs.getDouble("total_amount"));
        
        // Lấy thêm tên khách và số phòng (từ lệnh JOIN)
        // Dùng try-catch để tránh lỗi nếu câu query không join
        try { b.setCustomerName(rs.getString("full_name")); } catch(Exception e){}
        try { b.setRoomNumber(rs.getString("room_number")); } catch(Exception e){}
        
        return b;
    }
    
}