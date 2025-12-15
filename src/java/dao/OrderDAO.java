package dao;

import dbContext.DBContext;
import java.sql.*;

public class OrderDAO extends DBContext {

     // 1. Lấy Room ID từ Room Number
     public int getRoomIdByNumber(String roomNumber) throws SQLException {
         String sql = "SELECT room_id FROM Rooms WHERE room_number = ?";
         try (PreparedStatement ps = connection.prepareStatement(sql)) {
             ps.setString(1, roomNumber);
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     return rs.getInt("room_id");
                 }
             }
         }
         return -1;
     }
     
    // 2. Lấy thông tin giá và tên của Service (ĐÃ SỬA LỖI PRICE)
     public ResultSet getServiceDetail(int serviceId) throws SQLException {
         // Lấy service_name từ Services, và giá từ Foods hoặc Drinks (dùng LEFT JOIN và ISNULL)
         String sql = "SELECT s.service_name, ISNULL(f.price, d.price) AS price "
                          + "FROM Services s "
                          + "LEFT JOIN Foods f ON s.service_id = f.service_id "
                          + "LEFT JOIN Drinks d ON s.service_id = d.service_id "
                          + "WHERE s.service_id = ?";

         PreparedStatement ps = connection.prepareStatement(sql);
         ps.setInt(1, serviceId);
         return ps.executeQuery();
     }

    // 3. Chèn Order mới vào ServiceOrders
    public int createOrder(int roomId, String note) throws SQLException {
    int orderId = -1;
    
    // SỬA CÂU LỆNH SQL: Chèn total_amount = 0 và status = 'Pending'
    // Đảm bảo khớp với 4 cột NOT NULL: room_id, order_date, status, (và total_amount nếu NOT NULL)
    String sql = "INSERT INTO ServiceOrders (room_id, order_date, total_amount, status, note) VALUES (?, GETDATE(), 0, ?, ?)";
    
    try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, roomId);
        ps.setString(2, "Pending"); // Tham số 2: status
        ps.setString(3, note);      // Tham số 3: note
        
        int affectedRows = ps.executeUpdate();

        if (affectedRows > 0) {
            // Lấy ID tự động tăng (Identity) vừa được tạo
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
        }
    }
    return orderId;
}

    // 4. Chèn chi tiết món ăn/đồ uống vào OrderDetails
    // Trong OrderDAO.java
// 4. Chèn chi tiết món ăn/đồ uống vào OrderDetails
// Trong OrderDAO.java

// 4. Chèn chi tiết món ăn/đồ uống vào OrderDetails
public boolean createOrderDetail(int orderId, int serviceId, String itemName, int quantity, double unitPrice) throws SQLException {
    // 5 Dấu hỏi: order_id, service_id, item_name, quantity, unit_price
    String sql = "INSERT INTO OrderDetails (order_id, service_id, item_name, quantity, unit_price) VALUES (?, ?, ?, ?, ?)"; 
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, orderId);      
        ps.setInt(2, serviceId);    
        ps.setString(3, itemName);  
        ps.setInt(4, quantity);     
        ps.setDouble(5, unitPrice); 
        
        // --- CHÚ Ý QUAN TRỌNG: KHÔNG CÓ THAM SỐ THỨ 6 Ở ĐÂY ---
        
        return ps.executeUpdate() > 0; // Lỗi xảy ra chính tại đây
    }
}
    
    // 5. Cập nhật Total Amount
     public void updateTotalAmount(int orderId) throws SQLException {
         String sql = "UPDATE ServiceOrders SET total_amount = ("
                 + "SELECT ISNULL(SUM(subtotal), 0) FROM OrderDetails WHERE order_id = ?)"
                 + "WHERE order_id = ?";
         
         // ... (Execute)
     }
}