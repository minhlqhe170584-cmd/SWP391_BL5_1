package dao;

import dbContext.DBContext;
import models.ServiceHistory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrderDAO extends DBContext {

    public List<ServiceHistory> getHistoryByRoomId(int roomId) {
        List<ServiceHistory> list = new ArrayList<>();
        // SQL join để lấy tên dịch vụ, số lượng, giá, trạng thái
        String sql = "SELECT so.order_date, s.service_name, od.quantity, " +
                     "(od.quantity * od.price_at_order) AS item_total_price, so.status " +
                     "FROM ServiceOrders so " +
                     "JOIN OrderDetails od ON so.order_id = od.order_id " +
                     "JOIN Services s ON od.service_id = s.service_id " +
                     "WHERE so.room_id = ? " +
                     "ORDER BY so.order_date DESC";

        try {
            // Dùng connection từ DBContext cũ
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ServiceHistory h = new ServiceHistory();
                h.setOrderDate(rs.getTimestamp("order_date"));
                h.setServiceName(rs.getString("service_name"));
                h.setQuantity(rs.getInt("quantity"));
                h.setTotalPrice(rs.getDouble("item_total_price"));
                h.setStatus(rs.getString("status"));
                list.add(h);
            }
            connection.close(); // Nhớ đóng
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}