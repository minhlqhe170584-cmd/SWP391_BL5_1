package dao;

import dbContext.DBContext;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import models.*;
import models.Booking;

public class CheckInDAO extends DBContext {
    
    public ArrayList<Booking> searchPendingBookings(String search) {
        ArrayList<Booking> bookings = new ArrayList<>();
        
        try {
            String sql = "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, " +
                        "b.check_out_date, b.status, b.real_check_in, b.real_check_out, " +
                        "c.full_name, c.email, c.phone, " +
                        "r.room_number, r.room_status, r.is_active_login, rt.type_id " +
                        "FROM Bookings b " +
                        "JOIN Customers c ON b.customer_id = c.customer_id " +
                        "JOIN Rooms r ON b.room_id = r.room_id " +
                        "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                        "WHERE b.status = 'Confirmed' " +
                        "AND (CAST(b.booking_id AS VARCHAR) LIKE ? " +
                        "OR r.room_number LIKE ? " +
                        "OR c.full_name LIKE ? " +
                        "OR c.phone LIKE ?) " +
                        "ORDER BY b.check_in_date";
            
            PreparedStatement st = connection.prepareStatement(sql);
            String searchPattern = "%" + search + "%";
            st.setString(1, searchPattern);
            st.setString(2, searchPattern);
            st.setString(3, searchPattern);
            st.setString(4, searchPattern);
            
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setRoomId(rs.getInt("room_id"));
                
                Timestamp checkIn = rs.getTimestamp("check_in_date");
                if (checkIn != null) booking.setCheckInDate(checkIn.toLocalDateTime());
                
                Timestamp checkOut = rs.getTimestamp("check_out_date");
                if (checkOut != null) booking.setCheckOutDate(checkOut.toLocalDateTime());
                
                booking.setStatus(rs.getString("status"));
                
                // Customer info
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFullName(rs.getString("full_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                booking.setCustomer(customer);
                
                // Room info
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("room_status"));
                room.setIsActiveLogin(rs.getBoolean("is_active_login"));
                room.setTypeId(rs.getInt("type_id"));
                booking.setRoom(room);
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.out.println("Error searching bookings: " + e);
        }
        
        return bookings;
    }
    
    
    public Booking getBookingById(Integer bookingId) {
        try {
            String sql = "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, " +
                        "b.check_out_date, b.status, b.real_check_in, b.real_check_out, " +
                        "c.full_name, c.email, c.phone, " +
                        "r.room_number, r.room_status, r.is_active_login, r.room_password, " +
                        "rt.type_name, rt.capacity " +
                        "FROM Bookings b " +
                        "JOIN Customers c ON b.customer_id = c.customer_id " +
                        "JOIN Rooms r ON b.room_id = r.room_id " +
                        "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                        "WHERE b.booking_id = ?";
            
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setRoomId(rs.getInt("room_id"));
                
                Timestamp checkIn = rs.getTimestamp("check_in_date");
                if (checkIn != null) booking.setCheckInDate(checkIn.toLocalDateTime());
                
                Timestamp checkOut = rs.getTimestamp("check_out_date");
                if (checkOut != null) booking.setCheckOutDate(checkOut.toLocalDateTime());
                
                Timestamp realCheckIn = rs.getTimestamp("real_check_in");
                if (realCheckIn != null) booking.setRealcheckInDate(realCheckIn.toLocalDateTime());
                
                booking.setStatus(rs.getString("status"));
                
                // Customer info
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFullName(rs.getString("full_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                booking.setCustomer(customer);
                
                // Room info
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getString("room_number"));
                room.setStatus(rs.getString("room_status"));
                room.setIsActiveLogin(rs.getBoolean("is_active_login"));
                room.setRoomPassword(rs.getString("room_password"));
                room.setTypeName(rs.getString("type_nam"));
                room.setCapacity(rs.getInt("capacity"));
                booking.setRoom(room);
                
                return booking;
            }
        } catch (SQLException e) {
            System.out.println("Error getting booking: " + e);
        }
        
        return null;
    }
    
    /**
     * Generate random room password
     */
    private String generateRoomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
    
    /**
     * Process check-in
     */
    public CheckInResult processCheckIn(Integer bookingId, Integer staffId) {
        CheckInResult result = new CheckInResult();
        Connection conn = null;
        
        try {
            conn = connection;
            conn.setAutoCommit(false);
            
            // 1. Get booking details
            Booking booking = getBookingById(bookingId);
            
            if (booking == null) {
                result.setSuccess(false);
                result.setMessage("Không tìm thấy booking");
                return result;
            }
            
            // 2. Check room status
            if (!"Available".equals(booking.getRoom().getRoomStatus()) && 
                !"Reserved".equals(booking.getRoom().getRoomStatus())) {
                result.setSuccess(false);
                result.setMessage("Phòng không sẵn sàng. Trạng thái: " + booking.getRoom().getRoomStatus());
                return result;
            }
            
            // 3. Check if login is active
            if (!booking.getRoom().getIsActiveLogin()) {
                result.setSuccess(false);
                result.setMessage("Phòng chưa được kích hoạt đăng nhập. Vui lòng liên hệ IT.");
                return result;
            }
            
            // 4. Generate room password
            String roomPassword = generateRoomPassword();
            
            // 5. Update Room - set password, status to Occupied
            String updateRoom = "UPDATE Rooms SET room_password = ?, room_status = 'Occupied' WHERE room_id = ?";
            PreparedStatement stRoom = conn.prepareStatement(updateRoom);
            stRoom.setString(1, roomPassword);
            stRoom.setInt(2, booking.getRoomId());
            stRoom.executeUpdate();
            
            // 6. Update Booking - set status to Checked-In, real_check_in
            String updateBooking = "UPDATE Bookings SET status = 'Checked-In', real_check_in = ? WHERE booking_id = ?";
            PreparedStatement stBooking = conn.prepareStatement(updateBooking);
            stBooking.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stBooking.setInt(2, bookingId);
            stBooking.executeUpdate();
            
            // 7. Create RoomHistory record
            String insertHistory = "INSERT INTO RoomHistory(room_id, customer_id, booking_id, action, description, created_at) " +
                                  "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement stHistory = conn.prepareStatement(insertHistory);
            stHistory.setInt(1, booking.getRoomId());
            stHistory.setInt(2, booking.getCustomerId());
            stHistory.setInt(3, bookingId);
            stHistory.setString(4, "Check-In");
            stHistory.setString(5, "Customer checked in. Staff ID: " + staffId);
            stHistory.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stHistory.executeUpdate();
            
            conn.commit();
            
            // 8. Set result
            result.setSuccess(true);
            result.setMessage("Check-in thành công!");
            result.setRoomNumber(booking.getRoom().getRoomNumber());
            result.setRoomPassword(roomPassword);
            result.setCustomerName(booking.getCustomer().getFullName());
            result.setCheckInTime(LocalDateTime.now());
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback error: " + ex);
                }
            }
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
            System.out.println("Error processing check-in: " + e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error setting auto commit: " + e);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Result class for check-in process
     */
    public static class CheckInResult {
        private boolean success;
        private String message;
        private String roomNumber;
        private String roomPassword;
        private String customerName;
        private LocalDateTime checkInTime;
        
        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getRoomNumber() { return roomNumber; }
        public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
        
        public String getRoomPassword() { return roomPassword; }
        public void setRoomPassword(String roomPassword) { this.roomPassword = roomPassword; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        
        public LocalDateTime getCheckInTime() { return checkInTime; }
        public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    }
}