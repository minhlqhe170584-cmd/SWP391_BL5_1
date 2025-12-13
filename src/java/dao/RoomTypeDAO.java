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

public class RoomTypeDAO extends DBContext {

    // 1. LẤY TẤT CẢ LOẠI PHÒNG (Cho Dropdown & List)
    public List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        // THÊM: WHERE isEventRoom = 0
        // (Để dropdown tạo phòng mới chỉ hiện loại phòng ngủ, không hiện sảnh tiệc)
        String sql = "SELECT * FROM RoomTypes WHERE isEventRoom = 0 ORDER BY type_id ASC";
        
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

    // 2. PHÂN TRANG (Cho màn hình Room Type List)
    public List<RoomType> pagingRoomTypes(int index) {
        List<RoomType> list = new ArrayList<>();
        // THÊM: WHERE isEventRoom = 0
        String sql = "SELECT * FROM RoomTypes WHERE isEventRoom = 0 "
                   + "ORDER BY type_id ASC "
                   + "OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (index - 1) * 5);
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
            System.out.println("Error pagingRoomTypes: " + e.getMessage());
        }
        return list;
    }

    // 3. ĐẾM TỔNG SỐ (Để tính trang)
    public int getTotalRoomTypes() {
        // THÊM: WHERE isEventRoom = 0
        String sql = "SELECT COUNT(*) FROM RoomTypes WHERE isEventRoom = 0";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getTotalRoomTypes: " + e.getMessage());
        }
        return 0;
    }
    
    // 4. INSERT (Cần set mặc định isEventRoom = 0)
    public void insertRoomType(RoomType rt) {
        // Mặc định isEventRoom = 0 (Vì đây là quản lý phòng ngủ)
        String sql = "INSERT INTO RoomTypes (type_name, capacity, description, image_url, base_price_weekday, base_price_weekend, is_active, isEventRoom) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, rt.getTypeName());
            st.setInt(2, rt.getCapacity());
            st.setString(3, rt.getDescription());
            st.setString(4, rt.getImageUrl());
            st.setBigDecimal(5, rt.getBasePriceWeekday());
            st.setBigDecimal(6, rt.getBasePriceWeekend());
            st.setBoolean(7, rt.isIsActive());
            
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error insertRoomType: " + e.getMessage());
        }
    }

    // 5. UPDATE
    public void updateRoomType(RoomType rt) {
        String sql = "UPDATE RoomTypes SET type_name=?, capacity=?, description=?, image_url=?, base_price_weekday=?, base_price_weekend=?, is_active=? WHERE type_id=?";
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
    
    // 6. XÓA MỀM (Chuyển is_active = 0)
    public void deleteRoomType(int id) {
        String sql = "UPDATE RoomTypes SET is_active = 0 WHERE type_id = ?";
        try {
             PreparedStatement st = connection.prepareStatement(sql);
             st.setInt(1, id);
             st.executeUpdate();
        } catch (SQLException e) {
             System.out.println("Error deleteRoomType: " + e.getMessage());
        }
    }

    // 7. KHÔI PHỤC
    public void restoreRoomType(int id) {
        String sql = "UPDATE RoomTypes SET is_active = 1 WHERE type_id = ?";
        try {
             PreparedStatement st = connection.prepareStatement(sql);
             st.setInt(1, id);
             st.executeUpdate();
        } catch (SQLException e) {
             System.out.println("Error restoreRoomType: " + e.getMessage());
        }
    }

    // 8. GET BY ID
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

    // 9. CHECK TRÙNG TÊN
    public boolean checkTypeNameExists(String typeName, int currentId) {
        // Chỉ check trùng trong nhóm isEventRoom = 0 (Tránh trùng với sảnh tiệc nếu muốn, hoặc check toàn bộ cũng được)
        String sql = "SELECT COUNT(*) FROM RoomTypes WHERE type_name = ? AND type_id != ? AND isEventRoom = 0";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, typeName);
            st.setInt(2, currentId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checkTypeNameExists: " + e.getMessage());
        }
        return false;
    }
}