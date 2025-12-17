package dao;

import dbContext.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import models.Bicycle;
import models.BikeRentalOption;
import models.BikeServiceOrder;
import models.Service;
import models.SlotAvailability;

public class BikeTransactionDAO extends DBContext {

    public ArrayList<Service> getAllBikeServices() {
        ArrayList<Service> list = new ArrayList<>();
        String sql = "SELECT s.* FROM Services s " +
                     "JOIN ServiceCategories c ON s.category_id = c.category_id " +
                     "WHERE (c.category_name LIKE N'%Xe đạp%' OR c.category_name LIKE '%Bike%' OR c.category_name LIKE '%Rental%') " +
                     "AND s.is_active = 1 AND s.is_deleted = 0";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Service(
                    rs.getInt("service_id"), rs.getString("service_name"), rs.getString("image_url"), 
                    rs.getBoolean("is_active"), rs.getInt("category_id"), rs.getBoolean("is_deleted")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public ArrayList<BikeRentalOption> getBikeOptions(int serviceId) {
        ArrayList<BikeRentalOption> list = new ArrayList<>();
        String sql = "SELECT * FROM BikeRentalOptions WHERE service_id = ? AND is_active = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, serviceId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new BikeRentalOption(
                        rs.getInt("item_id"), rs.getInt("service_id"), rs.getString("option_name"), 
                        rs.getInt("duration_minutes"), rs.getDouble("price"), rs.getBoolean("is_active")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public BikeRentalOption getOptionById(int itemId) {
        String sql = "SELECT * FROM BikeRentalOptions WHERE item_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, itemId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new BikeRentalOption(
                        rs.getInt("item_id"), rs.getInt("service_id"), rs.getString("option_name"), 
                        rs.getInt("duration_minutes"), rs.getDouble("price"), rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public int getTotalBikeCount(int serviceId) {
        String sql = "SELECT COUNT(*) FROM Bicycles WHERE status != 'Maintenance' AND status != 'Deleted' AND service_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, serviceId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getAvailableQuantity(Timestamp start, Timestamp end, int serviceId) {
        int totalBikes = getTotalBikeCount(serviceId);
        int bookedBikes = 0;
        String sqlBooked = "SELECT SUM(d.quantity) FROM ServiceOrders o JOIN OrderDetails d ON o.order_id = d.order_id " +
                           "WHERE o.status IN ('Pending', 'Confirmed') AND d.service_id = ? " +
                           "AND (o.booking_start_date < ? AND o.booking_end_date > ?)";
        try {
            PreparedStatement st2 = connection.prepareStatement(sqlBooked);
            st2.setInt(1, serviceId);
            st2.setTimestamp(2, end);
            st2.setTimestamp(3, start);
            ResultSet rs2 = st2.executeQuery();
            if (rs2.next()) bookedBikes = rs2.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return totalBikes - bookedBikes;
    }

    public ArrayList<SlotAvailability> getHourlyAvailability(Timestamp start, Timestamp end, int serviceId) {
        ArrayList<SlotAvailability> list = new ArrayList<>();
        LocalDateTime checkTime = start.toLocalDateTime();
        LocalDateTime endTime = end.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (checkTime.isBefore(endTime)) {
            LocalDateTime nextHour = checkTime.plusHours(1);
            if (nextHour.isAfter(endTime)) nextHour = endTime;

            Timestamp tStart = Timestamp.valueOf(checkTime);
            Timestamp tEnd = Timestamp.valueOf(nextHour);
            
            int avail = getAvailableQuantity(tStart, tEnd, serviceId);
            String timeStr = checkTime.format(formatter) + " - " + nextHour.format(formatter);
            String status = (avail > 0) ? "Available" : "Full";
            
            list.add(new SlotAvailability(timeStr, avail, status));
            
            checkTime = checkTime.plusHours(1);
        }
        return list;
    }

    public boolean createBikeBooking(int roomId, int serviceId, int optionId, int quantity, String note, Timestamp start, Timestamp end) {
        Connection conn = null;
        try {
            conn = this.connection;
            conn.setAutoCommit(false);

            if (getAvailableQuantity(start, end, serviceId) < quantity) return false;

            BikeRentalOption option = getOptionById(optionId);
            if (option == null) return false;

            String sqlOrder = "INSERT INTO ServiceOrders (room_id, total_amount, status, note, order_date, booking_start_date, booking_end_date) VALUES (?, ?, 'Pending', ?, GETDATE(), ?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, roomId);
            psOrder.setDouble(2, option.getPrice() * quantity);
            psOrder.setString(3, note);
            psOrder.setTimestamp(4, start);
            psOrder.setTimestamp(5, end);
            psOrder.executeUpdate();

            ResultSet rsKey = psOrder.getGeneratedKeys();
            rsKey.next();
            int orderId = rsKey.getInt(1);

            String sqlDetail = "INSERT INTO OrderDetails (order_id, service_id, item_name, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, orderId);
            psDetail.setInt(2, serviceId);
            psDetail.setString(3, option.getOptionName());
            psDetail.setInt(4, quantity);
            psDetail.setDouble(5, option.getPrice());
            psDetail.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
        return false;
    }

    public ArrayList<BikeServiceOrder> getOrdersByStatus(String status) {
        ArrayList<BikeServiceOrder> list = new ArrayList<>();
        String sql = "SELECT o.*, r.room_number, d.item_name, d.quantity " +
                     "FROM ServiceOrders o " +
                     "LEFT JOIN Rooms r ON o.room_id = r.room_id " +
                     "JOIN OrderDetails d ON o.order_id = d.order_id " + 
                     "LEFT JOIN Services s ON d.service_id = s.service_id " +
                     "LEFT JOIN ServiceCategories c ON s.category_id = c.category_id " +
                     "WHERE (c.category_name LIKE N'%Xe đạp%' OR c.category_name LIKE '%Bike%') AND o.status = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new BikeServiceOrder(
                        rs.getInt("order_id"), 
                        rs.getInt("room_id"), 
                        rs.getTimestamp("order_date"), 
                        rs.getDouble("total_amount"),
                        rs.getString("status"), 
                        rs.getString("note"), 
                        rs.getString("room_number"),
                        rs.getTimestamp("booking_start_date"), 
                        rs.getTimestamp("booking_end_date"),
                        rs.getInt("quantity"), 
                        rs.getString("item_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public ArrayList<Bicycle> getPhysicalBikesForHandover(int serviceId) {
        ArrayList<Bicycle> list = new ArrayList<>();
        String sql = "SELECT b.*, s.service_name FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id " +
                     "WHERE b.status = 'Available' AND b.service_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, serviceId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Bicycle(
                    rs.getInt("bike_id"), rs.getString("bike_code"), rs.getInt("service_id"), 
                    rs.getString("status"), rs.getString("condition"), rs.getString("service_name")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public int getServiceIdByOrderId(int orderId) {
        String sql = "SELECT service_id FROM OrderDetails WHERE order_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, orderId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
 
    public void handoverBikes(int orderId, String[] bikeIds) {
        Connection conn = null;
        try {
            conn = this.connection;
            conn.setAutoCommit(false);
            PreparedStatement psOrder = conn.prepareStatement("SELECT total_amount FROM ServiceOrders WHERE order_id = ?");
            psOrder.setInt(1, orderId);
            ResultSet rs = psOrder.executeQuery();
            if (!rs.next()) return;
            double amount = rs.getDouble("total_amount");
            PreparedStatement psRental = conn.prepareStatement("INSERT INTO BikeRentals (order_id, bike_id, start_time, status) VALUES (?, ?, GETDATE(), 'Active')");
            PreparedStatement psBike = conn.prepareStatement("UPDATE Bicycles SET status = 'Rented' WHERE bike_id = ?");
            for (String bid : bikeIds) {
                psRental.setInt(1, orderId);
                psRental.setInt(2, Integer.parseInt(bid));
                psRental.addBatch();
                psBike.setInt(1, Integer.parseInt(bid));
                psBike.addBatch();
            }
            psRental.executeBatch();
            psBike.executeBatch();
            PreparedStatement psInv = conn.prepareStatement("INSERT INTO ServiceInvoices (order_id, created_at, final_amount, status) VALUES (?, GETDATE(), ?, 'Unpaid')");
            psInv.setInt(1, orderId);
            psInv.setDouble(2, amount);
            psInv.executeUpdate();
            PreparedStatement psUpdateOrder = conn.prepareStatement("UPDATE ServiceOrders SET status = 'Confirmed' WHERE order_id = ?");
            psUpdateOrder.setInt(1, orderId);
            psUpdateOrder.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public void returnBikesAndPay(int orderId, String paymentMethod) {
        Connection conn = null;
        try {
            conn = this.connection;
            conn.setAutoCommit(false);
            PreparedStatement psRental = conn.prepareStatement("UPDATE BikeRentals SET end_time = GETDATE(), status = 'Returned' WHERE order_id = ?");
            psRental.setInt(1, orderId);
            psRental.executeUpdate();
            PreparedStatement psBike = conn.prepareStatement("UPDATE Bicycles SET status = 'Available' WHERE bike_id IN (SELECT bike_id FROM BikeRentals WHERE order_id = ?)");
            psBike.setInt(1, orderId);
            psBike.executeUpdate();
            PreparedStatement psInv = conn.prepareStatement("UPDATE ServiceInvoices SET status = 'Paid', payment_method = ? WHERE order_id = ?");
            psInv.setString(1, paymentMethod);
            psInv.setInt(2, orderId);
            psInv.executeUpdate();
            PreparedStatement psOrder = conn.prepareStatement("UPDATE ServiceOrders SET status = 'Completed' WHERE order_id = ?");
            psOrder.setInt(1, orderId);
            psOrder.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
}