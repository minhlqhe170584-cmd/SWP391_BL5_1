package dao;

import dbContext.DBContext;
import models.ServiceHistory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import models.LaundryOrderDetail;

public class ServiceOrderDAO extends DBContext {

    // Hàm lấy lịch sử dịch vụ (Chỉ lấy các đơn chưa thanh toán - Cho khách đang ở)
    public List<ServiceHistory> getHistoryByRoomId(int roomId) {
        List<ServiceHistory> list = new ArrayList<>();
        
        /* LOGIC SQL:
           1. JOIN 3 bảng: ServiceOrders (Đơn), OrderDetails (Chi tiết), Services (Tên món)
           2. Điều kiện WHERE: 
              - room_id = ?: Của phòng đang đăng nhập
              - is_paid = 0: CHỈ hiển thị đơn chưa thanh toán (Khách hiện tại). 
                             Đơn cũ đã thanh toán (của khách trước) sẽ bị ẩn.
        */
        String sql = "SELECT " +
                     "    so.order_date, " +
                     "    s.service_name, " +
                     "    od.quantity, " +
                     "    (od.quantity * od.price_at_order) AS item_total_price, " +
                     "    so.status " +
                     "FROM ServiceOrders so " +
                     "JOIN OrderDetails od ON so.order_id = od.order_id " +
                     "JOIN Services s ON od.service_id = s.service_id " +
                     "WHERE so.room_id = ? AND so.is_paid = 0 " + 
                     "ORDER BY so.order_date DESC";

        try {
            // Sử dụng biến 'connection' được kế thừa từ DBContext cũ
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, roomId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ServiceHistory h = new ServiceHistory();
                
                // Map dữ liệu từ SQL vào Object ServiceHistory
                h.setOrderDate(rs.getTimestamp("order_date"));
                h.setServiceName(rs.getString("service_name"));
                h.setQuantity(rs.getInt("quantity"));
                h.setTotalPrice(rs.getDouble("item_total_price"));
                h.setStatus(rs.getString("status"));
                
                list.add(h);
            }
            
            // Quan trọng: Đóng kết nối sau khi lấy xong dữ liệu (theo style DBContext cũ)
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Integer createLaundryOrder(Integer roomId, LocalDateTime expectedPickupTime, LocalDateTime expectedReturnTime, String note, 
                                   ArrayList<LaundryOrderDetail> orderDetails) {
        Connection conn = null;
        Integer laundryId = null;
        
        try {
            conn = connection;
            conn.setAutoCommit(false);
            
            // Calculate total amount
            double totalAmount = 0.0;
            for (LaundryOrderDetail detail : orderDetails) {
                totalAmount += detail.getSubtotal();
            }
            
            // Step 1: Create ServiceOrder
            String insertServiceOrder = "INSERT INTO ServiceOrders(room_id, order_date, total_amount, status, note) " +
                                       "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stServiceOrder = conn.prepareStatement(insertServiceOrder, Statement.RETURN_GENERATED_KEYS);
            stServiceOrder.setInt(1, roomId);
            stServiceOrder.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stServiceOrder.setDouble(3, totalAmount);
            stServiceOrder.setString(4, "Pending");
            stServiceOrder.setString(5, note);
            
            stServiceOrder.executeUpdate();
            ResultSet rsServiceOrder = stServiceOrder.getGeneratedKeys();
            
            Integer orderId = null;
            if (rsServiceOrder.next()) {
                orderId = rsServiceOrder.getInt(1);
            }
            
            if (orderId == null) {
                throw new SQLException("Failed to create service order");
            }
            
            // Step 2: Create LaundryOrder
            String insertLaundryOrder = "INSERT INTO LaundryOrders(order_id, expected_pickup_time, expected_return_time, status, note) " +
                                       "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stLaundryOrder = conn.prepareStatement(insertLaundryOrder, Statement.RETURN_GENERATED_KEYS);
            stLaundryOrder.setInt(1, orderId);
            
            if (expectedPickupTime != null) {
                stLaundryOrder.setTimestamp(2, Timestamp.valueOf(expectedPickupTime));
            } else {
                stLaundryOrder.setTimestamp(2, null);
            }
            
            if (expectedReturnTime != null) {
                stLaundryOrder.setTimestamp(3, Timestamp.valueOf(expectedReturnTime));
            } else {
                stLaundryOrder.setTimestamp(3, null);
            }
            
            stLaundryOrder.setString(4, "Pending");
            stLaundryOrder.setString(5, note);
            
            stLaundryOrder.executeUpdate();
            ResultSet rsLaundryOrder = stLaundryOrder.getGeneratedKeys();
            
            if (rsLaundryOrder.next()) {
                laundryId = rsLaundryOrder.getInt(1);
            }
            
            if (laundryId == null) {
                throw new SQLException("Failed to create laundry order");
            }
            
            // Step 3: Insert LaundryOrderDetails
            String insertDetail = "INSERT INTO LaundryOrderDetails(laundry_id, laundry_item_id, quantity, unit_price) " +
                                 "VALUES(?, ?, ?, ?)";
            PreparedStatement stDetail = conn.prepareStatement(insertDetail);
            
            for (LaundryOrderDetail detail : orderDetails) {
                stDetail.setInt(1, laundryId);
                stDetail.setInt(2, detail.getLaundryItemId());
                stDetail.setInt(3, detail.getQuantity());
                stDetail.setDouble(4, detail.getUnitPrice());              
                stDetail.executeUpdate();
            }
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback error: " + ex);
                }
            }
            System.out.println("Error creating laundry order: " + e);
            laundryId = null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error setting auto commit: " + e);
                }
            }
        }
        
        return laundryId;
    }
    
    
    public int createServiceOrder(int roomId, String note) {
        int orderId = 0;
        String sql = """
                        INSERT INTO ServiceOrders(room_id, order_date, total_amount, status, note) " +
                                                           "VALUES(?,  GETDATE(), 0, ?, ?)
                    """;
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, roomId);
            st.setString(2, "PENDING");
            st.setString(3, note);
            int affectedRows = st.executeUpdate();

        if (affectedRows > 0) {
            // Lấy ID tự động tăng (Identity) vừa được tạo
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }
    
    public static void main(String[] args) {
        int res = 0;
        ServiceOrderDAO dao = new ServiceOrderDAO();
        LaundryOrderDetail detail = new LaundryOrderDetail();
                    detail.setLaundryItemId(1);
                    detail.setQuantity(1);
                    detail.setUnitPrice(20000.0);
                    detail.setSubtotal(20000.0);
        ArrayList<LaundryOrderDetail> list = new ArrayList<>();
        list.add(detail);
//        res = dao.createLaundryOrder(1, LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 30)), "abc", list);
//        System.out.println(res);
    }
}