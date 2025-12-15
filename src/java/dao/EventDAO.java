package dao;

import com.sun.jdi.connect.spi.Connection;
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

    public List<EventCategory> getAllEventCategories() {
        /* Giữ nguyên */ return new ArrayList<>();
    } // (Code cũ của bạn)

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

    public boolean insertEventRequest(EventRequest er) {
        String sql = """
        INSERT INTO EventRequest
        (eventId, roomIds, check_in_date, check_out_date, message, status, customer_id, created_date)
        VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, er.getEventId());
            st.setString(2, er.getRoomIds());
            st.setString(3, er.getCheckInDate());
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
    
    public static void main(String[] args) {
        EventDAO d = new EventDAO();
        System.out.println(d.getAllEventRequests().get(0).toString());
    }

}
