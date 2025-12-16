package dao;

import dbContext.DBContext;
import models.LaundryOrderDetail;
import models.ServiceHistory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDAO extends DBContext {

    // ==================================================================
    // 1. LẤY LỊCH SỬ DỊCH VỤ (Theo phòng - Chỉ lấy đơn chưa thanh toán)
    // ==================================================================
    public List<ServiceHistory> getHistoryByRoomId(int roomId) {
        List<ServiceHistory> list = new ArrayList<>();
        
        /* LOGIC SQL SỬA ĐỔI:
         - Lấy trực tiếp item_name, unit_price, subtotal từ OrderDetails.
         - Điều kiện: room_id khớp VÀ status chưa thanh toán ('Paid').
        */
        String sql = "SELECT " +
                     "    od.detail_id, " +
                     "    od.order_id, " +
                     "    od.service_id, " +
                     "    od.item_name, " +
                     "    od.quantity, " +
                     "    od.unit_price, " +
                     "    od.subtotal, " +
                     "    so.order_date, " +
                     "    so.status " +
                     "FROM OrderDetails od " +
                     "JOIN ServiceOrders so ON od.order_id = so.order_id " +
                     "WHERE so.room_id = ? " +
                     "AND so.status != 'Paid' " + // Lọc đơn chưa thanh toán
                     "ORDER BY so.order_date DESC";

        // Sử dụng try-with-resources để tự động đóng PS và RS (nhưng giữ Connection)
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setInt(1, roomId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServiceHistory h = new ServiceHistory();
                    
                    // Map dữ liệu (Khớp với ServiceHistory.java mới)
                    h.setDetailId(rs.getInt("detail_id"));
                    h.setOrderId(rs.getInt("order_id"));
                    h.setServiceId(rs.getInt("service_id"));
                    
                    // Quan trọng: Lấy item_name từ bảng chi tiết
                    h.setItemName(rs.getString("item_name")); 
                    
                    h.setQuantity(rs.getInt("quantity"));
                    h.setUnitPrice(rs.getDouble("unit_price"));
                    h.setSubtotal(rs.getDouble("subtotal"));
                    
                    h.setOrderDate(rs.getTimestamp("order_date"));
                    h.setStatus(rs.getString("status"));
                    
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==================================================================
    // 2. TẠO ĐƠN GIẶT LÀ (Transaction: ServiceOrder -> LaundryOrder -> Details)
    // ==================================================================
    public Integer createLaundryOrder(Integer roomId, LocalDateTime expectedPickupTime, 
                                      LocalDateTime expectedReturnTime, String note, 
                                      ArrayList<LaundryOrderDetail> orderDetails) {
        Connection conn = connection; // Lấy kết nối từ DBContext
        Integer laundryId = null;
        PreparedStatement stService = null;
        PreparedStatement stLaundry = null;
        PreparedStatement stDetail = null;
        ResultSet rs = null;

        try {
            // 1. Bắt đầu Transaction
            conn.setAutoCommit(false);

            // Tính tổng tiền
            double totalAmount = 0;
            for (LaundryOrderDetail d : orderDetails) {
                totalAmount += d.getSubtotal();
            }

            // ---------------------------------------------------
            // BƯỚC A: Tạo ServiceOrder (Bảng cha tổng)
            // ---------------------------------------------------
            String sqlService = "INSERT INTO ServiceOrders(room_id, order_date, total_amount, status, note) VALUES(?, GETDATE(), ?, 'Pending', ?)";
            stService = conn.prepareStatement(sqlService, Statement.RETURN_GENERATED_KEYS);
            stService.setInt(1, roomId);
            stService.setDouble(2, totalAmount);
            stService.setString(3, note); // Ghi chú chung
            stService.executeUpdate();

            rs = stService.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) orderId = rs.getInt(1);

            // ---------------------------------------------------
            // BƯỚC B: Tạo LaundryOrders (Bảng cha của Giặt là)
            // ---------------------------------------------------
            String sqlLaundry = "INSERT INTO LaundryOrders(order_id, pickup_time, expected_pickup_time, expected_return_time, status, note) " +
                                "VALUES(?, NULL, ?, ?, 'Pending', ?)";
            stLaundry = conn.prepareStatement(sqlLaundry, Statement.RETURN_GENERATED_KEYS);
            stLaundry.setInt(1, orderId);
            
            // Xử lý Null cho ngày tháng
            if (expectedPickupTime != null) stLaundry.setTimestamp(2, Timestamp.valueOf(expectedPickupTime));
            else stLaundry.setNull(2, Types.TIMESTAMP);

            if (expectedReturnTime != null) stLaundry.setTimestamp(3, Timestamp.valueOf(expectedReturnTime));
            else stLaundry.setNull(3, Types.TIMESTAMP);

            stLaundry.setString(4, note);
            stLaundry.executeUpdate();

            rs = stLaundry.getGeneratedKeys();
            if (rs.next()) laundryId = rs.getInt(1);

            // ---------------------------------------------------
            // BƯỚC C: Tạo LaundryOrderDetails (Chi tiết từng món)
            // ---------------------------------------------------
            String sqlDetail = "INSERT INTO LaundryOrderDetails(laundry_id, laundry_item_id, quantity, unit_price) VALUES(?, ?, ?, ?)";
            stDetail = conn.prepareStatement(sqlDetail);

            // Insert vào bảng Service OrderDetails (Bảng con tổng quát - Để hiện thị lịch sử chung)
            // Lưu ý: Cần thêm logic insert vào bảng OrderDetails gốc nữa nếu muốn query ở hàm getHistoryByRoomId ra dữ liệu
            String sqlCommonDetail = "INSERT INTO OrderDetails(order_id, service_id, item_name, quantity, unit_price) " +
                                     "SELECT ?, service_id, item_name, ?, ? FROM LaundryItems WHERE laundry_item_id = ?";
            PreparedStatement stCommonDetail = conn.prepareStatement(sqlCommonDetail);

            for (LaundryOrderDetail d : orderDetails) {
                // 1. Insert vào bảng chuyên biệt LaundryOrderDetails
                stDetail.setInt(1, laundryId);
                stDetail.setInt(2, d.getLaundryItemId());
                stDetail.setInt(3, d.getQuantity());
                stDetail.setDouble(4, d.getUnitPrice());
                stDetail.addBatch(); // Gom lệnh lại chạy 1 lần

                // 2. Insert vào bảng chung OrderDetails (Để hàm getHistory lấy được)
                stCommonDetail.setInt(1, orderId);
                stCommonDetail.setInt(2, d.getQuantity());
                stCommonDetail.setDouble(3, d.getUnitPrice());
                stCommonDetail.setInt(4, d.getLaundryItemId());
                stCommonDetail.addBatch();
            }
            
            stDetail.executeBatch();
            stCommonDetail.executeBatch(); // Chạy batch

            // 2. Commit Transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return null; // Trả về null nếu lỗi
        } finally {
            // 3. Đóng Resources & Reset AutoCommit
            try {
                if (rs != null) rs.close();
                if (stService != null) stService.close();
                if (stLaundry != null) stLaundry.close();
                if (stDetail != null) stDetail.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) { e.printStackTrace(); }
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