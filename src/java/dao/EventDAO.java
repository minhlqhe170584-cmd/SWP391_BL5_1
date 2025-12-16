package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Event;
import models.EventCategory;
import models.EventRequest;
import models.EventRequestView;

public class EventDAO extends DBContext {

    // 1. Lấy danh sách gói sự kiện (CHỈ LẤY serviceCategoryId = 5)
    public List<Event> getAllEventPackages() {
        List<Event> list = new ArrayList<>();

        // Thêm WHERE e.serviceCategoryId = 5
        String sql = "SELECT e.eventId, e.name, e.price, e.serviceCategoryId, e.roomIds, e.created_date, e.updated_date, e.status, "
                + "sc.category_name "
                + "FROM Events e "
                + "LEFT JOIN ServiceCategories sc ON e.serviceCategoryId = sc.category_id "
                + "WHERE e.serviceCategoryId = 5 AND e.status = 'Active' " // <--- LỌC CỨNG ID = 5
                + "ORDER BY e.created_date DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getAllEventPackages: " + e.getMessage());
        }
        return list;
    }

    // 2. Lấy chi tiết (Thêm e.status vào SELECT)
    public Event getEventById(int id) {
        String sql = "SELECT e.eventId, e.name, e.price, e.serviceCategoryId, e.roomIds, e.created_date, e.updated_date, e.status, " // <--- Thêm e.status
                + "sc.category_name "
                + "FROM Events e "
                + "LEFT JOIN ServiceCategories sc ON e.serviceCategoryId = sc.category_id "
                + "WHERE e.eventId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return mapEvent(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getEventById: " + e.getMessage());
        }
        return null;
    }

    // 3. Thêm mới (Thêm status vào INSERT)
    public void insertEvent(Event event) {
        String sql = "INSERT INTO Events (name, price, serviceCategoryId, roomIds, status, created_date) VALUES (?, ?, ?, ?, ?, GETDATE())";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, event.getEventName());
            st.setBigDecimal(2, event.getPricePerTable());
            st.setInt(3, event.getEventCatId());
            st.setString(4, event.getLocation());
            st.setString(5, event.getStatus()); // <--- Set Status
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error insertEvent: " + e.getMessage());
        }
    }

    // 4. Cập nhật (Thêm status vào UPDATE)
    public void updateEvent(Event event) {
        String sql = "UPDATE Events SET name = ?, price = ?, serviceCategoryId = ?, roomIds = ?, status = ?, updated_date = GETDATE() WHERE eventId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, event.getEventName());
            st.setBigDecimal(2, event.getPricePerTable());
            st.setInt(3, event.getEventCatId());
            st.setString(4, event.getLocation());
            st.setString(5, event.getStatus()); // <--- Set Status
            st.setInt(6, event.getEventId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updateEvent: " + e.getMessage());
        }
    }

    // ... (Hàm delete và getAllEventCategories giữ nguyên) ...
    public void deleteEvent(int id) {
        /* Giữ nguyên */ }

    // 5. Lấy danh sách Event Category (Triển khai logic SQL)
    public List<EventCategory> getAllEventCategories() {
        List<EventCategory> list = new ArrayList<>();
        // Giả định ServiceCategories có category_id, category_name, description
        String sql = "SELECT category_id, category_name, description FROM ServiceCategories";

        // Sử dụng try-with-resources để quản lý connection/statement/resultset
        try (java.sql.PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                EventCategory cat = new EventCategory();
                cat.setEventCatId(rs.getInt("category_id"));
                cat.setCategoryName(rs.getString("category_name"));
                cat.setDescription(rs.getString("description"));
                list.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("Error getAllEventCategories: " + e.getMessage());
        }
        return list;
    }

    // --- Helper Map (Thêm map status) ---
    private Event mapEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("eventId"));
        event.setEventName(rs.getString("name"));
        event.setPricePerTable(rs.getBigDecimal("price"));
        event.setEventCatId(rs.getInt("serviceCategoryId"));
        event.setLocation(rs.getString("roomIds"));
        event.setCreatedDate(rs.getTimestamp("created_date"));
        event.setUpdatedDate(rs.getTimestamp("updated_date"));

        // --- [MỚI] Map Status ---
        event.setStatus(rs.getString("status"));
        // -----------------------

        try {
            EventCategory cat = new EventCategory();
            cat.setEventCatId(rs.getInt("serviceCategoryId"));
            cat.setCategoryName(rs.getString("category_name"));
            event.setEventCategory(cat);
        } catch (Exception e) {
        }

        return event;
    }

// Hàm Insert (Giữ nguyên logic của bạn, SQL Server tự parse String thành DateTime)
    public boolean insertEventRequest(EventRequest er) {
        String sql = """
            INSERT INTO EventRequest
            (eventId, roomIds, check_in_date, check_out_date, message, status, room_id, created_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())
        """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, er.getEventId());
            st.setString(2, er.getRoomIds());
            st.setString(3, er.getCheckInDate()); // String format 'yyyy-MM-dd HH:mm'
            st.setString(4, er.getCheckOutDate());
            st.setString(5, er.getMessage());
            st.setString(6, er.getStatus());
            st.setInt(7, er.getRoomId());

            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Trong EventDAO.java, phương thức getAllEventRequests()
    public List<EventRequestView> getAllEventRequests() {
        List<EventRequestView> list = new ArrayList<>();

        String sql = """
    SELECT 
        er.requestId,
        er.status,
        er.check_in_date,
        er.check_out_date,
        er.message,
        er.created_date,

        e.name,
        -- Tên phòng chính/đơn (Room Booked)
        r_booked.room_number AS bookedRoomName, 
        
        -- Danh sách phòng mở rộng (Rooms in roomIds)
        STRING_AGG(r_list.room_number, ', ') AS roomNames
        
    FROM EventRequest er
    JOIN Events e ON er.eventId = e.eventId
    
    -- LEFT JOIN 1: Dùng để lấy tên phòng từ cột room_id (Phòng chính/đơn)
    LEFT JOIN Rooms r_booked ON er.room_id = r_booked.room_id
    
    -- LEFT JOIN 2: Dùng để lấy tên phòng từ cột roomIds (Danh sách phòng)
    LEFT JOIN Rooms r_list 
        ON r_list.room_id IN (
            SELECT value FROM STRING_SPLIT(er.roomIds, ',')
        )
        
    GROUP BY
        er.requestId, er.status,
        er.check_in_date, er.check_out_date,
        er.message, er.created_date,
        e.name, r_booked.room_number -- Thêm tên phòng chính vào GROUP BY
    ORDER BY er.created_date DESC
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EventRequestView er = new EventRequestView();

                er.setRequestId(rs.getInt("requestId"));
                er.setEventName(rs.getString("name"));

                // Lấy tên phòng chính/đơn mới
                er.setBookedRoomName(rs.getString("bookedRoomName"));

                // Giữ lại tên phòng từ roomIds
                er.setRoomNames(rs.getString("roomNames"));

                // Cài đặt Customer Name là mặc định (đã sửa ở phần trước)
                er.setCustomerName("[Guest/N/A]");

                er.setStatus(rs.getString("status"));
                er.setCheckInDate(rs.getDate("check_in_date"));
                er.setCheckOutDate(rs.getDate("check_out_date"));
                er.setMessage(rs.getString("message"));
                er.setCreatedDate(rs.getTimestamp("created_date"));

                list.add(er);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void updateEventRequestStatus(int requestId, String newStatus) {
        String sql = "UPDATE EventRequest SET status = ?, updated_date = GETDATE() WHERE requestId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, requestId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách thời gian đã được đặt (Status = ACCEPT) của các phòng trong danh sách
    // Input: "1,2" (chuỗi id phòng của gói sự kiện hiện tại)
    public List<String> getBookedRanges(String roomIdsStr) {
        List<String> ranges = new ArrayList<>();

        if (roomIdsStr == null || roomIdsStr.isEmpty()) {
            return ranges;
        }

        // Logic: 
        // 1. Tìm tất cả đơn đã ACCEPT.
        // 2. Kiểm tra xem đơn đó có dính dáng đến phòng nào trong `roomIdsStr` không.
        // (Ở đây dùng query đơn giản: Lấy hết đơn ACCEPT, sau đó lọc xem có trùng phòng không)
        String sql = "SELECT roomIds, check_in_date, check_out_date FROM EventRequest WHERE status = 'ACCEPT'";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            // Chuyển chuỗi input "1,2" thành mảng ["1", "2"] để so sánh
            String[] targetRooms = roomIdsStr.split(",");

            while (rs.next()) {
                String bookedRooms = rs.getString("roomIds"); // VD: "2,3"
                boolean isConflict = false;

                // Kiểm tra giao thoa phòng
                for (String tRoom : targetRooms) {
                    if (bookedRooms.contains(tRoom.trim())) {
                        isConflict = true;
                        break;
                    }
                }

                if (isConflict) {
                    // Nếu trùng phòng -> Thêm khoảng thời gian vào danh sách chặn
                    // Format JSON cho Flatpickr: { from: "...", to: "..." }
                    String from = rs.getTimestamp("check_in_date").toString();
                    String to = rs.getTimestamp("check_out_date").toString();
                    // Loại bỏ phần nano giây thừa nếu có, giữ format yyyy-MM-dd HH:mm:ss
                    ranges.add("{ from: '" + from + "', to: '" + to + "' }");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ranges;
    }

// Phương thức kiểm tra tên gói sự kiện có tồn tại không
    public boolean checkEventNameExists(String name) {
        // Chỉ kiểm tra các gói sự kiện đang hoạt động (serviceCategoryId = 5)
        String sql = "SELECT eventId FROM Events WHERE name = ? AND serviceCategoryId = 5";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next(); // True nếu tìm thấy
            }
        } catch (SQLException e) {
            System.err.println("Error checkEventNameExists: " + e.getMessage());
            return true; // Xử lý an toàn: nếu có lỗi DB thì coi như trùng
        }
    }

    /**
     * Cập nhật trạng thái (status) của Gói Sự kiện.
     *
     * @param eventId ID của Gói Sự kiện.
     * @param newStatus Trạng thái mới ('Active' hoặc 'Inactive').
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateEventStatus(int eventId, String newStatus) {
        // Sử dụng chung cho cả Activate và Deactivate
        String sql = "UPDATE Events SET status = ?, updated_date = GETDATE() WHERE eventId = ?";

        try (java.sql.PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, newStatus);
            st.setInt(2, eventId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updateEventStatus: " + e.getMessage());
            return false;
        }
    }

// Thay thế hàm searchEventRequests cũ bằng hàm này:
    public List<EventRequestView> searchEventRequests(String keyword, String status) {
        List<EventRequestView> list = new ArrayList<>();

        // --- CÁC LỖI ĐÃ SỬA ---
        // 1. Tên bảng: 'EventRequest' (số ít) thay vì 'EventRequests'
        // 2. Tên cột: 'check_in_date', 'check_out_date', 'room_id' (snake_case) thay vì camelCase
        // 3. Customer: Tạm thời set cứng tên khách vì bảng EventRequest của bạn chưa có cột customerId
        StringBuilder sql = new StringBuilder(
                "SELECT r.requestId, e.name AS eventName, "
                + "r.roomIds, r.status, r.check_in_date, r.check_out_date, r.message, r.created_date, "
                + "rm.room_number AS bookedRoomName "
                + "FROM EventRequest r "
                + // <--- Đã sửa tên bảng
                "JOIN Events e ON r.eventId = e.eventId "
                + "LEFT JOIN Rooms rm ON r.room_id = rm.room_id "
                + // <--- Đã sửa r.room_id
                "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // 1. Filter theo Keyword (Chỉ tìm theo Tên sự kiện, vì chưa có thông tin Khách hàng)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND e.name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }

        // 2. Filter theo Status
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND r.status = ? ");
            params.add(status);
        }

        // Sắp xếp: Mới nhất lên đầu
        sql.append(" ORDER BY r.created_date DESC");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                EventRequestView v = new EventRequestView();
                v.setRequestId(rs.getInt("requestId"));
                v.setEventName(rs.getString("eventName"));

                // Set tên khách mặc định (Do DB chưa có cột customerId)
                v.setCustomerName("[Guest]");

                // Xử lý hiển thị danh sách phòng (nếu cần hiển thị tên thay vì ID, cần query phức tạp hơn như getAll)
                v.setRoomNames(rs.getString("roomIds"));

                v.setStatus(rs.getString("status"));
                v.setCheckInDate(rs.getTimestamp("check_in_date"));   // <--- Đã sửa tên cột
                v.setCheckOutDate(rs.getTimestamp("check_out_date")); // <--- Đã sửa tên cột
                v.setMessage(rs.getString("message"));
                v.setCreatedDate(rs.getTimestamp("created_date"));
                v.setBookedRoomName(rs.getString("bookedRoomName"));

                list.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error searchEventRequests: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Event> searchEventPackages(String keyword, String status) {
        List<Event> list = new ArrayList<>();

        // 1. Base Query: Chỉ lấy serviceCategoryId = 5 (Gói sự kiện)
        StringBuilder sql = new StringBuilder(
                "SELECT e.eventId, e.name, e.price, e.serviceCategoryId, e.roomIds, e.created_date, e.updated_date, e.status, "
                + "sc.category_name "
                + "FROM Events e "
                + "LEFT JOIN ServiceCategories sc ON e.serviceCategoryId = sc.category_id "
                + "WHERE e.serviceCategoryId = 5 "); // Luôn giữ điều kiện này

        List<Object> params = new ArrayList<>();

        // 2. Filter theo Keyword (Tên gói)
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND e.name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }

        // 3. Filter theo Status (Active/Inactive)
        if (status != null && !status.isEmpty()) {
            sql.append(" AND e.status = ? ");
            params.add(status);
        }

        // Sắp xếp: Mới nhất lên đầu
        sql.append(" ORDER BY e.created_date DESC");

        try (PreparedStatement st = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                // Tận dụng lại hàm mapEvent cũ nếu có, hoặc map thủ công
                Event event = new Event();
                event.setEventId(rs.getInt("eventId"));
                event.setEventName(rs.getString("name"));
                event.setPricePerTable(rs.getBigDecimal("price"));
                event.setEventCatId(rs.getInt("serviceCategoryId"));
                event.setLocation(rs.getString("roomIds"));
                event.setCreatedDate(rs.getTimestamp("created_date"));
                event.setUpdatedDate(rs.getTimestamp("updated_date"));
                event.setStatus(rs.getString("status"));

                // Map Category Name
                EventCategory cat = new EventCategory();
                cat.setCategoryName(rs.getString("category_name"));
                event.setEventCategory(cat);

                list.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error searchEventPackages: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        EventDAO d = new EventDAO();
        System.out.println(d.getAllEventRequests().get(0).toString());
    }

}
