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

        // SELECT đầy đủ 9 cột data
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
        } catch (NumberFormatException e) {
            serviceId = -1;
        }

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
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- 3. CÁC HÀM CRUD CƠ BẢN ---
    public void createDrink(Drink drink) throws SQLException {
        // SQL đã bao gồm đủ 8 cột data
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

    // SỬA LỖI: Thêm cột is_active vào UPDATE
    public void updateDrink(Drink drink) throws SQLException {
        String sql = "UPDATE Drinks SET service_id = ?, drink_name = ?, price = ?, description = ?, volume_ml = ?, is_alcoholic = ?, image_url = ?, is_active = ? WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, drink.getServiceId());
            ps.setString(2, drink.getName());
            ps.setDouble(3, drink.getPrice());
            ps.setString(4, drink.getDescription());
            ps.setInt(5, drink.getVolumeMl());
            ps.setBoolean(6, drink.getIsAlcoholic());
            ps.setString(7, drink.getImageUrl());
            ps.setBoolean(8, drink.getIsActive()); // Đã thêm is_active
            ps.setInt(9, drink.getDrinkId());      // drink_id là tham số cuối
            ps.executeUpdate();
        }
    }

    public Drink getDrinkById(int id) {
        String sql = "SELECT drink_id, service_id, drink_name, price, description, volume_ml, is_alcoholic, image_url, is_active FROM Drinks WHERE drink_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDrink(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        // Giả sử category_id = 3 là Drink Category ID
        String sql = "SELECT service_id, service_name FROM Services WHERE category_id = 3";
        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
                rs.getString("drink_name"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getInt("volume_ml"),
                rs.getBoolean("is_alcoholic"),
                rs.getString("image_url"),
                rs.getBoolean("is_active")
        );
    }

    // --- LẤY DANH SÁCH CHO GIAO DIỆN KHÁCH HÀNG (ĐÃ SỬA LỖI AMBIGUOUS COLUMN) ---
    public List<Drink> getAllActiveDrinks() {
        List<Drink> list = new ArrayList<>();

        // ĐÃ SỬA: SỬ DỤNG ALIAS CHO CÁC CỘT CHUNG (service_id, image_url, is_active)
        // Để đảm bảo tên cột trả về là chính xác.
        String sql = "SELECT d.drink_id, s.service_id AS service_id, d.drink_name, d.price, d.description, d.volume_ml, d.is_alcoholic, s.image_url AS image_url, s.is_active AS is_active "
                + "FROM Drinks d "
                + "JOIN Services s ON d.service_id = s.service_id "
                + "WHERE s.is_active = 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDrink(rs));
            }
        } catch (Exception e) {
            // Vui lòng in lỗi chi tiết ở đây nếu vẫn bị 500
            e.printStackTrace();
        }
        return list;
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
