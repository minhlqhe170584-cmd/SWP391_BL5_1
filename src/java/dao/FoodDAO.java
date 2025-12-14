/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import models.Food;
import models.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SWP391_Project
 */
public class FoodDAO extends DBContext {

    // --- 1. LẤY DANH SÁCH (Hỗ trợ Tìm kiếm + Lọc Service + Phân trang) ---
    public List<Food> getFoods(String keyword, String serviceIdStr, int pageIndex, int pageSize) {
        List<Food> list = new ArrayList<>();

        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim();
        int serviceId = -1;
        try {
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }
        } catch (NumberFormatException e) {
            serviceId = -1;
        }

        int offset = (pageIndex - 1) * pageSize;

        // Xây dựng SQL động (ĐÃ BAO GỒM is_vegetarian)
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT food_id, service_id, food_name, price, description, image_url, is_active, is_vegetarian FROM Foods WHERE 1=1 ");

        if (!searchName.isEmpty()) {
            sql.append(" AND food_name LIKE ? ");
        }
        if (serviceId != -1) {
            sql.append(" AND service_id = ? ");
        }

        sql.append(" ORDER BY food_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (!searchName.isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (serviceId != -1) {
                ps.setInt(index++, serviceId);
            }

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
        } catch (NumberFormatException e) {
            serviceId = -1;
        }

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
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- 3. CÁC HÀM CRUD CƠ BẢN ---
    
    // SỬA: Thêm is_vegetarian vào INSERT
    public void createFood(Food food) throws SQLException {
        String sql = "INSERT INTO Foods (service_id, food_name, price, description, image_url, is_active, is_vegetarian) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, food.getServiceId());
            ps.setString(2, food.getName());
            ps.setDouble(3, food.getPrice());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImageUrl());
            ps.setBoolean(6, food.getIsActive());
            ps.setBoolean(7, food.isIsVegetarian()); // THÊM THAM SỐ 7
            ps.executeUpdate();
        }
    }

    // SỬA: Thêm is_vegetarian vào UPDATE
    public void updateFood(Food food) throws SQLException {
        String sql = "UPDATE Foods SET service_id = ?, food_name = ?, price = ?, description = ?, image_url = ?, is_active = ?, is_vegetarian = ? WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, food.getServiceId());
            ps.setString(2, food.getName());
            ps.setDouble(3, food.getPrice());
            ps.setString(4, food.getDescription());
            ps.setString(5, food.getImageUrl());
            ps.setBoolean(6, food.getIsActive()); // Đảm bảo is_active cũng được update
            ps.setBoolean(7, food.isIsVegetarian()); // THÊM THAM SỐ 7
            ps.setInt(8, food.getFoodId());
            ps.executeUpdate();
        }
    }

    // SỬA: Liệt kê rõ các cột để đảm bảo lấy is_vegetarian
    public Food getFoodById(int id) {
        String sql = "SELECT food_id, service_id, food_name, price, description, image_url, is_active, is_vegetarian FROM Foods WHERE food_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFood(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Các hàm deactivate/activate giữ nguyên
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
        String sql = "SELECT service_id, service_name FROM Services WHERE category_id = 2";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Service(rs.getInt("service_id"), rs.getString("service_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- HÀM MAP RESULTSET CHUẨN (8 tham số) ---
    private Food mapResultSetToFood(ResultSet rs) throws SQLException {
        return new Food(
                rs.getInt("food_id"),
                rs.getInt("service_id"),
                rs.getString("food_name"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getBoolean("is_active"),
                rs.getBoolean("is_vegetarian")
        );
    }

    // --- LẤY DANH SÁCH CHO GIAO DIỆN KHÁCH HÀNG ---
    public List<Food> getAllActiveFoods() {
        List<Food> list = new ArrayList<>();
        
        // SỬA SELECT: THÊM f.is_vegetarian VÀO SELECT LIST VÀ DÙNG ALIAS
        String sql = "SELECT f.food_id, s.service_id AS service_id, f.food_name, f.price, f.description, s.image_url AS image_url, s.is_active AS is_active, f.is_vegetarian "
                + "FROM Foods f "
                + "JOIN Services s ON f.service_id = s.service_id "
                + "WHERE s.is_active = 1"; 

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToFood(rs)); 
            }
        } catch (Exception e) {
            e.printStackTrace(); // Giữ lại để debug
        }
        return list;
    }
    
    // Test Main (Optional)
    public static void main(String[] args) {
        FoodDAO dao = new FoodDAO();
        System.out.println("Test getFoods:");
        List<Food> list = dao.getFoods("", "", 1, 10);
        System.out.println("Size: " + list.size());
    }
}