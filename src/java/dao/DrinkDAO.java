/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import models.Drink;
import models.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SWP391_Project
 */
public class DrinkDAO extends DBContext {

    // --- 1. LẤY DANH SÁCH (Hỗ trợ Tìm kiếm + Lọc Service + Phân trang) ---
    public List<Drink> getDrinks(String keyword, String serviceIdStr, int pageIndex, int pageSize) {
        List<Drink> list = new ArrayList<>();
        
        // Xử lý tham số đầu vào
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim();
        int serviceId = -1;
        try {
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }
        } catch (NumberFormatException e) { serviceId = -1; }
        
        int offset = (pageIndex - 1) * pageSize;

        // SQL Server 2012+ Paging
        // SỬA: Tên bảng 'Drinks' và cột 'drink_name'
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT drink_id, service_id, drink_name, price, description, volume_ml, is_alcoholic, image_url, is_active FROM Drinks WHERE 1=1 ");

        if (!searchName.isEmpty()) {
            sql.append(" AND drink_name LIKE ? ");
        }
        if (serviceId != -1) {
            sql.append(" AND service_id = ? ");
        }

        sql.append(" ORDER BY drink_id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

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
                list.add(mapResultSetToDrink(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- 2. ĐẾM TỔNG SỐ BẢN GHI (Cho Phân trang) ---
    public int countDrinks(String keyword, String serviceIdStr) {
        String searchName = (keyword == null || keyword.trim().isEmpty()) ? "" : keyword.trim();
        int serviceId = -1;
        try {
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }
        } catch (NumberFormatException e) { serviceId = -1; }

        // SỬA: Tên bảng 'Drinks'
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Drinks WHERE 1=1 ");
        
        if (!searchName.isEmpty()) {
            sql.append(" AND drink_name LIKE ? ");
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

    public void createDrink(Drink drink) throws SQLException {
        // SỬA: Tên bảng 'Drinks' và cột 'drink_name'
        String sql = "INSERT INTO Drinks (service_id, drink_name, price, description, volume_ml, is_alcoholic, image_url, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drink.getServiceId());
            ps.setString(2, drink.getName());
            ps.setDouble(3, drink.getPrice());
            ps.setString(4, drink.getDescription());
            ps.setInt(5, drink.getVolumeMl());
            ps.setBoolean(6, drink.getIsAlcoholic());
            ps.setString(7, drink.getImageUrl());
            ps.setBoolean(8, drink.getIsActive());
            ps.executeUpdate();
        }
    }

    public void updateDrink(Drink drink) throws SQLException {
        // SỬA: Tên bảng 'Drinks' và cột 'drink_name'
        String sql = "UPDATE Drinks SET service_id = ?, drink_name = ?, price = ?, description = ?, volume_ml = ?, is_alcoholic = ?, image_url = ? WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drink.getServiceId());
            ps.setString(2, drink.getName());
            ps.setDouble(3, drink.getPrice());
            ps.setString(4, drink.getDescription());
            ps.setInt(5, drink.getVolumeMl());
            ps.setBoolean(6, drink.getIsAlcoholic());
            ps.setString(7, drink.getImageUrl());
            ps.setInt(8, drink.getDrinkId());
            ps.executeUpdate();
        }
    }

    public Drink getDrinkById(int id) {
        String sql = "SELECT * FROM Drinks WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToDrink(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void deactivateDrink(int drinkId) throws SQLException {
        String sql = "UPDATE Drinks SET is_active = 0 WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drinkId);
            ps.executeUpdate();
        }
    }

    public void activateDrink(int drinkId) throws SQLException {
        String sql = "UPDATE Drinks SET is_active = 1 WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drinkId);
            ps.executeUpdate();
        }
    }

    // Lấy danh sách Service cho Dropdown
    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        // LƯU Ý: Nếu Category ID của Drink không phải là 3, hãy sửa số 3 này lại
        String sql = "SELECT service_id, service_name FROM Services WHERE category_id = 3"; 
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

    // --- HÀM MAP RESULTSET ---
    private Drink mapResultSetToDrink(ResultSet rs) throws SQLException {
        return new Drink(
                rs.getInt("drink_id"),
                rs.getInt("service_id"),
                rs.getString("drink_name"), // Đã sửa thành drink_name
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getInt("volume_ml"),
                rs.getBoolean("is_alcoholic"),
                rs.getString("image_url"),
                rs.getBoolean("is_active")
        );
    }
    
    // Test Main
    public static void main(String[] args) {
        DrinkDAO dao = new DrinkDAO();
        System.out.println("Test getDrinks:");
        List<Drink> list = dao.getDrinks("", "", 1, 10);
        System.out.println("Size: " + list.size());
        for (Drink d : list) {
            System.out.println("ID: " + d.getDrinkId() + " | Name: " + d.getName());
        }
    }
}