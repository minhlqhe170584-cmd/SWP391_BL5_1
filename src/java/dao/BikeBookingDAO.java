package dao;

import dbContext.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import models.BikeRentalOption;

public class BikeBookingDAO extends DBContext {

    public ArrayList<BikeRentalOption> getBikeOptions() {
        ArrayList<BikeRentalOption> list = new ArrayList<>();
        // Giả sử Service ID của xe đạp là 1. Nếu muốn linh động có thể truyền tham số.
        String sql = "SELECT * FROM BikeRentalOptions WHERE service_id = 1"; 
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new BikeRentalOption(
                        rs.getInt("item_id"),
                        rs.getInt("service_id"),
                        rs.getString("option_name"),
                        rs.getInt("duration_minutes"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        rs.getInt("item_id"),
                        rs.getInt("service_id"),
                        rs.getString("option_name"),
                        rs.getInt("duration_minutes"),
                        rs.getDouble("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createBikeOrder(int roomId, int optionId, int quantity, String note) {
        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psDetail = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            conn = this.connection; 
            conn.setAutoCommit(false);

            BikeRentalOption option = getOptionById(optionId);
            if (option == null) return false;
            
            double totalAmount = option.getPrice() * quantity;

            // 1. Tạo ServiceOrder chung
            String sqlOrder = "INSERT INTO ServiceOrders (room_id, total_amount, status, note, order_date) VALUES (?, ?, 'Pending', ?, GETDATE())";
            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, roomId);
            psOrder.setDouble(2, totalAmount);
            psOrder.setString(3, note);
            
            if (psOrder.executeUpdate() == 0) {
                throw new SQLException("Creating bike order failed.");
            }

            int newOrderId = 0;
            rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                newOrderId = rs.getInt(1);
            }

            // 2. Tạo OrderDetail (Lưu cụ thể là thuê gói nào của xe đạp)
            String sqlDetail = "INSERT INTO OrderDetails (order_id, service_id, item_name, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";
            psDetail = conn.prepareStatement(sqlDetail);
            psDetail.setInt(1, newOrderId);
            psDetail.setInt(2, 1); // HARDCODE: 1 là Service ID của Bike Rental
            psDetail.setString(3, option.getOptionName());
            psDetail.setInt(4, quantity);
            psDetail.setDouble(5, option.getPrice());
            
            psDetail.executeUpdate();

            conn.commit();
            result = true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {}
        }
        return result;
    }
}