package dao;

import dbContext.DBContext;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.Customer;
import models.Room;
import models.RoomType;

/**
 * DAO chuyên phục vụ nghiệp vụ Check-in tại lễ tân.
 *
 * - Tra cứu Booking theo Mã booking
 * - Tra cứu Booking theo Tên khách + ngày nhận phòng
 * - Xác thực Booking hợp lệ trước khi cho Check-in
 * - Xử lý khách không đến (No-show): hủy booking + trả trạng thái phòng
 *
 * Lưu ý: Chỉ thao tác trên các Booking có status hợp lệ:
 *  - Chặn phòng (không trống): Confirmed, CheckedIn
 *  - Không chặn phòng: Cancelled, CheckedOut
 */
public class BookingCheckInDAO extends DBContext {

    // --- STATIC QUERIES ---

    // Lấy chi tiết Booking (kèm Room, RoomType, Customer) theo mã booking
    private static final String GET_BOOKING_BY_CODE =
            "SELECT TOP 1 b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, " +
            "       b.status, b.booking_code, b.total_amount, " +
            "       c.full_name AS customer_name, c.email, c.phone, " +
            "       r.room_number, r.room_status, r.type_id, r.is_active_login, " +
            "       rt.type_name, rt.capacity, rt.description, rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active " +
            "FROM Bookings b " +
            "JOIN Customers c ON b.customer_id = c.customer_id " +
            "JOIN Rooms r ON b.room_id = r.room_id " +
            "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
            "WHERE b.booking_code = ?";

    // Lấy chi tiết Booking theo ID
    private static final String GET_BOOKING_BY_ID =
            "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, " +
            "       b.status, b.booking_code, b.total_amount, " +
            "       c.full_name AS customer_name, c.email, c.phone, " +
            "       r.room_number, r.room_status, r.type_id, r.is_active_login, " +
            "       rt.type_name, rt.capacity, rt.description, rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active " +
            "FROM Bookings b " +
            "JOIN Customers c ON b.customer_id = c.customer_id " +
            "JOIN Rooms r ON b.room_id = r.room_id " +
            "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
            "WHERE b.booking_id = ?";

    // Tra cứu danh sách Booking theo tên khách + ngày nhận phòng (Dùng cho lễ tân tìm nhanh)
    private static final String SEARCH_BOOKING_BY_NAME_AND_DATE =
            "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, " +
            "       b.status, b.booking_code, b.total_amount, " +
            "       c.full_name AS customer_name, c.email, c.phone, " +
            "       r.room_number, r.room_status, r.type_id, r.is_active_login, " +
            "       rt.type_name, rt.capacity, rt.description, rt.image_url, rt.base_price_weekday, rt.base_price_weekend, rt.is_active " +
            "FROM Bookings b " +
            "JOIN Customers c ON b.customer_id = c.customer_id " +
            "JOIN Rooms r ON b.room_id = r.room_id " +
            "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
            "WHERE c.full_name LIKE ? " +
            "  AND CAST(b.check_in_date AS DATE) = ? " +
            "ORDER BY b.booking_id DESC";

    // Cập nhật trạng thái Booking
    private static final String UPDATE_BOOKING_STATUS =
            "UPDATE Bookings SET status = ? WHERE booking_id = ?";

    // Lấy Room_id & Status hiện tại của Booking (để xử lý No-show)
    private static final String GET_ROOM_INFO_BY_BOOKING =
            "SELECT room_id, status FROM Bookings WHERE booking_id = ?";

    // Đổi trạng thái phòng
    private static final String UPDATE_ROOM_STATUS =
            "UPDATE Rooms SET room_status = ? WHERE room_id = ?";

    // --- HELPER MAP FUNCTION ---
    private Booking mapBookingWithRelations(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setRoomId(rs.getInt("room_id"));

        Timestamp in = rs.getTimestamp("check_in_date");
        Timestamp out = rs.getTimestamp("check_out_date");
        b.setCheckInDate(in);
        b.setCheckOutDate(out);

        b.setStatus(rs.getString("status"));
        try {
            b.setBookingCode(rs.getString("booking_code"));
        } catch (SQLException e) {
            // cột có thể null / không dùng
        }
        try {
            b.setTotalAmount(rs.getDouble("total_amount"));
        } catch (SQLException e) {
            // có thể chưa dùng total_amount
        }

        // Map Customer đơn giản
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setFullName(rs.getString("customer_name"));
        c.setEmail(rs.getString("email"));
        c.setPhone(rs.getString("phone"));
        b.setCustomer(c);

        // Map Room + RoomType
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setTypeId(rs.getInt("type_id"));
        r.setStatus(rs.getString("room_status"));
        r.setActiveLogin(rs.getBoolean("is_active_login"));

        RoomType rt = new RoomType();
        rt.setTypeId(rs.getInt("type_id"));
        rt.setTypeName(rs.getString("type_name"));
        try {
            rt.setCapacity(rs.getInt("capacity"));
        } catch (SQLException e) {
        }
        try {
            rt.setDescription(rs.getString("description"));
        } catch (SQLException e) {
        }
        try {
            rt.setImageUrl(rs.getString("image_url"));
        } catch (SQLException e) {
        }
        try {
            rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
        } catch (SQLException e) {
        }
        try {
            rt.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend"));
        } catch (SQLException e) {
        }
        r.setRoomType(rt);
        b.setRoom(r);

        return b;
    }

    // --- PUBLIC API ---

    /**
     * Tìm Booking theo Mã booking (dùng khi lễ tân nhập mã để xác nhận).
     * Không filter status ở đây, để Servlet quyết định hợp lệ hay không
     * (Confirmed / CheckedIn / CheckedOut / Cancelled...).
     */
    public Booking findByBookingCode(String bookingCode) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_BOOKING_BY_CODE);
            st.setString(1, bookingCode);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return mapBookingWithRelations(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error findByBookingCode: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy Booking theo ID (dùng cho màn Checkout / chi tiết).
     */
    public Booking findById(int bookingId) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_BOOKING_BY_ID);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return mapBookingWithRelations(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error findById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tra cứu danh sách Booking theo Tên khách + Ngày nhận phòng.
     * Ví dụ: nhập \"Nguyen Van A\" + 2025-12-20 để tìm.
     */
    public List<Booking> searchByCustomerNameAndCheckInDate(String customerNameKeyword, Date checkInDate) {
        List<Booking> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(SEARCH_BOOKING_BY_NAME_AND_DATE);
            st.setString(1, "%" + customerNameKeyword.trim() + "%");
            st.setDate(2, checkInDate);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapBookingWithRelations(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searchByCustomerNameAndCheckInDate: " + e.getMessage());
        }
        return list;
    }

    /**
     * Danh sách Booking cần check-in trong ngày (theo ngày dự kiến).
     * - Dùng cho màn Dashboard lễ tân.
     * - Lọc theo: CAST(check_in_date AS DATE) = ? AND status = 'Confirmed'
     */
    public List<Booking> getCheckInBookingsByDate(Date date) {
        List<Booking> list = new ArrayList<>();
        String sql =
                "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, " +
                "       b.status, b.booking_code, b.total_amount, " +
                "       c.full_name AS customer_name, c.email, c.phone, " +
                "       r.room_number, r.room_status, r.type_id, r.is_active_login, " +
                "       rt.type_name, rt.capacity, rt.description, rt.image_url, " +
                "       rt.base_price_weekday, rt.base_price_weekend, rt.is_active " +
                "FROM Bookings b " +
                "JOIN Customers c ON b.customer_id = c.customer_id " +
                "JOIN Rooms r ON b.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE CAST(b.check_in_date AS DATE) = ? " +
                "  AND b.status = 'Confirmed' " +
                "ORDER BY r.room_number ASC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, date);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBookingWithRelations(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getCheckInBookingsByDate: " + e.getMessage());
        }
        return list;
    }

    public List<Booking> getTodayCheckInBookings() {
        return getCheckInBookingsByDate(new Date(System.currentTimeMillis()));
    }

    /**
     * Đổi trạng thái Booking (VD: Confirmed -> CheckedIn, CheckedIn -> CheckedOut, ...).
     */
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_BOOKING_STATUS);
            st.setString(1, newStatus);
            st.setInt(2, bookingId);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updateBookingStatus: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xử lý khách đến làm thủ tục Check-in:
     *  - YÊU CẦU: booking.status hiện tại phải là 'Confirmed'
     *  - Sau khi thành công: cập nhật booking.status = 'CheckedIn'
     *
     * Lưu ý: phòng đã được chặn từ lúc đặt (status phòng có thể là 'Occupied'),
     * nên ở đây chỉ đổi trạng thái Booking.
     */
    public boolean doCheckIn(int bookingId) {
        try {
            // Chỉ cho phép từ Confirmed -> CheckedIn
            String sql = "UPDATE Bookings SET status = 'CheckedIn' WHERE booking_id = ? AND status = 'Confirmed'";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, bookingId);
            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error doCheckIn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check-in và cập nhật luôn thời gian thực tế (check_in_date = GETDATE()).
     */
    public boolean doCheckInWithNow(int bookingId) {
        try {
            String sql = "UPDATE Bookings "
                    + "SET status = 'CheckedIn', real_check_in = GETDATE() "
                    + "WHERE booking_id = ? AND status = 'Confirmed'";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, bookingId);
            int rows = st.executeUpdate();
            
            String sql2 = "UPDATE r SET r.is_active_login = 1 " +
                        "FROM Rooms r " +
                        "JOIN Bookings b ON b.room_id = r.room_id " +
                        "WHERE b.booking_id = ? " +
                        "AND b.status = 'Checked-in' ";
            PreparedStatement st2 = connection.prepareStatement(sql);
            st2.setInt(1, bookingId);
            int rows2 = st2.executeUpdate();
            
            return rows > 0 && rows2 > 0;
        } catch (SQLException e) {
            System.out.println("Error doCheckInWithNow: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xử lý khách không đến (No-show):
     *  - Đổi Booking: Confirmed -> Cancelled
     *  - Trả phòng về trạng thái 'Available'
     */
    public boolean handleNoShowAndReleaseRoom(int bookingId) {
        try {
            // 1. Lấy room_id + status hiện tại của Booking
            int roomId = -1;
            String currentStatus = null;

            PreparedStatement stInfo = connection.prepareStatement(GET_ROOM_INFO_BY_BOOKING);
            stInfo.setInt(1, bookingId);
            ResultSet rs = stInfo.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("room_id");
                currentStatus = rs.getString("status");
            }

            if (roomId == -1) {
                return false; // Không tìm thấy booking
            }

            // Chỉ xử lý No-show cho booking đang Confirmed
            if (!"Confirmed".equalsIgnoreCase(currentStatus)) {
                return false;
            }

            // 2. Hủy booking
            PreparedStatement stCancel = connection.prepareStatement(UPDATE_BOOKING_STATUS);
            stCancel.setString(1, "Cancelled");
            stCancel.setInt(2, bookingId);
            int rowsBooking = stCancel.executeUpdate();

            // 3. Trả phòng về trạng thái Available
            PreparedStatement stRoom = connection.prepareStatement(UPDATE_ROOM_STATUS);
            stRoom.setString(1, "Available");
            stRoom.setInt(2, roomId);
            int rowsRoom = stRoom.executeUpdate();

            return rowsBooking > 0 && rowsRoom > 0;
        } catch (SQLException e) {
            System.out.println("Error handleNoShowAndReleaseRoom: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checkout khỏi phòng:
     *  - YÊU CẦU: booking đang ở trạng thái 'CheckedIn'
     *  - Cập nhật: status = 'CheckedOut', check_out_date = GETDATE()
     *  - Trả phòng về trạng thái 'Available'
     */
    public boolean doCheckOutAndReleaseRoom(int bookingId) {
        try {
            // 1. Lấy room_id + status hiện tại
            int roomId = -1;
            String currentStatus = null;

            PreparedStatement stInfo = connection.prepareStatement(GET_ROOM_INFO_BY_BOOKING);
            stInfo.setInt(1, bookingId);
            ResultSet rs = stInfo.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("room_id");
                currentStatus = rs.getString("status");
            }

            if (roomId == -1) {
                return false;
            }

            // Chỉ cho phép checkout khi đang CheckedIn
            if (!"CheckedIn".equalsIgnoreCase(currentStatus)) {
                return false;
            }

            // 2. Cập nhật Booking -> CheckedOut + thời gian thực tế
            String sqlUpdateBooking = "UPDATE Bookings "
                    + "SET status = 'CheckedOut', real_check_out = GETDATE() "
                    + "WHERE booking_id = ?";
            PreparedStatement stB = connection.prepareStatement(sqlUpdateBooking);
            stB.setInt(1, bookingId);
            int rowsBooking = stB.executeUpdate();

            // 3. Trả phòng về Available
            PreparedStatement stRoom = connection.prepareStatement(UPDATE_ROOM_STATUS);
            stRoom.setString(1, "Available");
            stRoom.setInt(2, roomId);
            int rowsRoom = stRoom.executeUpdate();

            return rowsBooking > 0 && rowsRoom > 0;
        } catch (SQLException e) {
            System.out.println("Error doCheckOutAndReleaseRoom: " + e.getMessage());
        }
        return false;
    }

    /**
     * Danh sách Booking cần check-out trong ngày (theo ngày dự kiến).
     * - Lọc theo: CAST(check_out_date AS DATE) = ? AND status = 'CheckedIn'
     */
    public List<Booking> getCheckOutBookingsByDate(Date date) {
        List<Booking> list = new ArrayList<>();
        String sql =
                "SELECT b.booking_id, b.customer_id, b.room_id, b.check_in_date, b.check_out_date, " +
                "       b.status, b.booking_code, b.total_amount, " +
                "       c.full_name AS customer_name, c.email, c.phone, " +
                "       r.room_number, r.room_status, r.type_id, r.is_active_login, " +
                "       rt.type_name, rt.capacity, rt.description, rt.image_url, " +
                "       rt.base_price_weekday, rt.base_price_weekend, rt.is_active " +
                "FROM Bookings b " +
                "JOIN Customers c ON b.customer_id = c.customer_id " +
                "JOIN Rooms r ON b.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE CAST(b.check_out_date AS DATE) = ? " +
                "  AND b.status = 'CheckedIn' " +
                "ORDER BY r.room_number ASC";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, date);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBookingWithRelations(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getCheckOutBookingsByDate: " + e.getMessage());
        }
        return list;
    }

    public List<Booking> getTodayCheckOutBookings() {
        return getCheckOutBookingsByDate(new Date(System.currentTimeMillis()));
    }
}


