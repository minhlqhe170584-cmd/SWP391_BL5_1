package dao;

import dbContext.DBContext;
import models.ServiceHistory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}