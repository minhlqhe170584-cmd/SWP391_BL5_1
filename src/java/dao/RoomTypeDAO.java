/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * RoomTypeDAO.java
 */
package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.RoomType;
import java.math.BigDecimal;

/**
 * Lớp này chịu trách nhiệm tương tác với bảng [RoomTypes] trong DB
 */
public class RoomTypeDAO extends DBContext { // Giả sử bạn kế thừa DBContext để lấy connection

    // 1. LẤY TẤT CẢ LOẠI PHÒNG (Cho trang List)
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomTypes ORDER BY type_id ASC"; // Lấy mới nhất lên đầu
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                rt.setCapacity(rs.getInt("capacity"));
                rt.setDescription(rs.getString("description"));
                rt.setImageUrl(rs.getString("image_url"));
                rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                rt.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend"));
                rt.setIsActive(rs.getBoolean("is_active"));
                
                list.add(rt);
            }
        } catch (SQLException e) {
            System.out.println("Error getAllRoomTypes: " + e.getMessage());
        }
        return list;
    }

    // 2. LẤY LOẠI PHÒNG THEO ID (Cho chức năng Edit - Load dữ liệu cũ lên form)
    public RoomType getRoomTypeById(int id) {
        String sql = "SELECT * FROM RoomTypes WHERE type_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                RoomType rt = new RoomType();
                rt.setTypeId(rs.getInt("type_id"));
                rt.setTypeName(rs.getString("type_name"));
                rt.setCapacity(rs.getInt("capacity"));
                rt.setDescription(rs.getString("description"));
                rt.setImageUrl(rs.getString("image_url"));
                rt.setBasePriceWeekday(rs.getBigDecimal("base_price_weekday"));
                rt.setBasePriceWeekend(rs.getBigDecimal("base_price_weekend"));
                rt.setIsActive(rs.getBoolean("is_active"));
                return rt;
            }
        } catch (SQLException e) {
            System.out.println("Error getRoomTypeById: " + e.getMessage());
        }
        return null;
    }

    // 3. THÊM MỚI LOẠI PHÒNG (Cho chức năng Add)
    public void insertRoomType(RoomType rt) {
        String sql = "INSERT INTO RoomTypes (type_name, capacity, description, image_url, base_price_weekday, base_price_weekend, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, rt.getTypeName());
            st.setInt(2, rt.getCapacity());
            st.setString(3, rt.getDescription());
            st.setString(4, rt.getImageUrl());
            st.setBigDecimal(5, rt.getBasePriceWeekday());
            st.setBigDecimal(6, rt.getBasePriceWeekend());
            st.setBoolean(7, rt.isIsActive()); // Lưu ý getter của bạn là isIsActive
            
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error insertRoomType: " + e.getMessage());
        }
    }

    // 4. CẬP NHẬT LOẠI PHÒNG (Cho chức năng Update)
    public void updateRoomType(RoomType rt) {
        String sql = "UPDATE RoomTypes SET type_name=?, capacity=?, description=?, image_url=?, "
                   + "base_price_weekday=?, base_price_weekend=?, is_active=? WHERE type_id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, rt.getTypeName());
            st.setInt(2, rt.getCapacity());
            st.setString(3, rt.getDescription());
            st.setString(4, rt.getImageUrl());
            st.setBigDecimal(5, rt.getBasePriceWeekday());
            st.setBigDecimal(6, rt.getBasePriceWeekend());
            st.setBoolean(7, rt.isIsActive());
            st.setInt(8, rt.getTypeId());
            
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updateRoomType: " + e.getMessage());
        }
    }
    
    // 5. XÓA (Hoặc ẩn) LOẠI PHÒNG
    // Tùy logic, ở đây mình viết hàm Delete cứng, nếu bạn muốn Soft Delete (đổi active=0) thì bảo mình sửa nhé.
    public void deleteRoomType(int id) {
        String sql = "DELETE FROM RoomTypes WHERE type_id = ?";
        try {
             PreparedStatement st = connection.prepareStatement(sql);
             st.setInt(1, id);
             st.executeUpdate();
        } catch (SQLException e) {
             System.out.println("Error deleteRoomType: " + e.getMessage());
        }
    }
}
