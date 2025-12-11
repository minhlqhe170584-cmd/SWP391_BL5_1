/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import models.Food;
import models.Service; // Nhớ import model Service
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SWP391_Project
 */
public class FoodDAO extends DBContext {

    // --- 1. LẤY DANH SÁCH (Hỗ trợ Tìm kiếm + Lọc Service + Phân trang) ---
    // Đã sửa sang Dynamic SQL để tránh lỗi tham số ? = -1
    public List<Food> getFoods(String keyword, String serviceIdStr, int pageIndex, int pageSize) {
        List<Food> list = new ArrayList<>();
        
        // Xử lý tham số đầu vào
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim();
        int serviceId = -1;
        try {
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }
        } catch (NumberFormatException e) { serviceId = -1; }
        
        int offset = (pageIndex - 1) * pageSize;

        // Xây dựng SQL động
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT food_id, service_id, food_name, price, description, image_url, is_active FROM Foods WHERE 1=1 ");

        if (!searchName.isEmpty()) {
            sql.append(" AND food_name LIKE ? ");
        }
        if (serviceId != -1) {
            sql.append(" AND service_id = ? ");
        }

        // Sắp xếp và phân trang (SQL Server 2012+)
        sql.append(" ORDER BY food_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            
            // Set tham số theo thứ tự động
            if (!searchName.isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (serviceId != -1) {
                ps.setInt(index++, serviceId);
            }
            
            // Set tham số phân trang
            ps.setInt(index++, offset);
            ps.setInt(index++, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToFood(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- 2. ĐẾM TỔNG SỐ BẢN GHI (Để tính Total Pages) ---
    public int countFoods(String keyword, String serviceIdStr) {
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim();
        int serviceId = -1;
        try {
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }
        } catch (NumberFormatException e) { serviceId = -1; }

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Foods WHERE 1=1 ");
        
        if (!searchName.isEmpty()) {
            sql.append(" AND food_name LIKE ? ");
        }
        if (serviceId != -1) {
            sql.append(" AND service_id = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (!searchName.isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (serviceId != -1) {
                ps.setInt(index++, serviceId);
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- 3. CÁC HÀM CRUD CƠ BẢN ---
    // (Đã giữ nguyên logic ánh xạ của bạn nhưng sửa tên hàm createFood cho chuẩn)

    public void createFood(Food food) throws SQLException {
        String sql = "INSERT INTO Foods (service_id, food_name, price, description, image_url, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, food.getServiceId());
            ps.setString(2, food.getName());
            ps.setDouble(3, food.getPrice());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImageUrl());
            ps.setBoolean(6, food.getIsActive());
            ps.executeUpdate();
        }
    }

    public void updateFood(Food food) throws SQLException {
        String sql = "UPDATE Foods SET service_id = ?, food_name = ?, price = ?, description = ?, image_url = ? WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, food.getServiceId());
            ps.setString(2, food.getName());
            ps.setDouble(3, food.getPrice());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImageUrl());
            ps.setInt(6, food.getFoodId());
            ps.executeUpdate();
        }
    }

    public Food getFoodById(int id) {
        String sql = "SELECT * FROM Foods WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToFood(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void deactivateFood(int foodId) throws SQLException {
        String sql = "UPDATE Foods SET is_active = 0 WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            ps.executeUpdate();
        }
    }

    public void activateFood(int foodId) throws SQLException {
        String sql = "UPDATE Foods SET is_active = 1 WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            ps.executeUpdate();
        }
    }

    // Lấy danh sách Service cho Dropdown
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        // Lưu ý: Tôi bỏ điều kiện WHERE category_id = 2 để đảm bảo dropdown luôn có dữ liệu.
        // Nếu bạn bắt buộc chỉ lấy category_id = 2 thì hãy sửa lại câu SQL.
        String sql = "SELECT service_id, service_name FROM Services WHERE category_id = 2"; 
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Service(rs.getInt("service_id"), rs.getString("service_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- HÀM MAP RESULTSET GIỮ NGUYÊN ---
    private Food mapResultSetToFood(ResultSet rs) throws SQLException {
        return new Food(
                rs.getInt("food_id"),
                rs.getInt("service_id"),
                rs.getString("food_name"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getBoolean("is_active")
        );
    }
    
    // Test Main (Optional)
    public static void main(String[] args) {
        FoodDAO dao = new FoodDAO();
        System.out.println("Test getFoods:");
        List<Food> list = dao.getFoods("", "", 1, 10);
        System.out.println("Size: " + list.size());
    }
}