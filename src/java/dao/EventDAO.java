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

    // 1. Lấy danh sách gói sự kiện (CHỈ LẤY serviceCategoryId = 1)
    public List<Event> getAllEventPackages() {
        List<Event> list = new ArrayList<>();

        // Thêm WHERE e.serviceCategoryId = 1
        String sql = "SELECT e.eventId, e.name, e.price, e.serviceCategoryId, e.roomIds, e.created_date, e.updated_date, e.status, "
                + "sc.category_name "
                + "FROM Events e "
                + "LEFT JOIN ServiceCategories sc ON e.serviceCategoryId = sc.category_id "
                + "WHERE e.serviceCategoryId = 1 " // <--- LỌC CỨNG ID = 1
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
            (eventId, roomIds, check_in_date, check_out_date, message, status, customer_id, created_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())
        """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, er.getEventId());
            st.setString(2, er.getRoomIds());
            st.setString(3, er.getCheckInDate()); // String format 'yyyy-MM-dd HH:mm'
            st.setString(4, er.getCheckOutDate());
            st.setString(5, er.getMessage());
            st.setString(6, er.getStatus());
            st.setInt(7, er.getCustomerId());

            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            c.full_name AS customerName,

            STRING_AGG(r.room_number, ', ') AS roomNames
        FROM EventRequest er
        JOIN Events e ON er.eventId = e.eventId
        JOIN Customers c ON er.customer_id = c.customer_id
        LEFT JOIN Rooms r 
            ON r.room_id IN (
                SELECT value FROM STRING_SPLIT(er.roomIds, ',')
            )
        GROUP BY
            er.requestId, er.status,
            er.check_in_date, er.check_out_date,
            er.message, er.created_date,
            e.name, c.full_name
        ORDER BY er.created_date DESC
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EventRequestView er = new EventRequestView();

                er.setRequestId(rs.getInt("requestId"));
                er.setEventName(rs.getString("name"));
                er.setCustomerName(rs.getString("customerName"));
                er.setRoomNames(rs.getString("roomNames"));
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
        // Chỉ kiểm tra các gói sự kiện đang hoạt động (serviceCategoryId = 1)
        String sql = "SELECT eventId FROM Events WHERE name = ? AND serviceCategoryId = 1";
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

    public static void main(String[] args) {
        EventDAO d = new EventDAO();
        System.out.println(d.getAllEventRequests().get(0).toString());
    }

}
