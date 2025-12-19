package dao;

import dbContext.DBContext;
import models.Drink;
import models.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO extends DBContext {

    // ... (Các câu lệnh SQL String giữ nguyên như cũ) ...
    // Copy lại các string SQL từ bài trước nếu chưa có
    private static final String GET_DRINKS_PAGING
            = "SELECT d.*, s.service_name FROM Drinks d "
            + "JOIN Services s ON d.service_id = s.service_id "
            + "WHERE d.drink_name LIKE ? AND (? = -1 OR d.service_id = ?) "
            + "ORDER BY d.drink_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    private static final String COUNT_DRINKS
            = "SELECT COUNT(*) FROM Drinks d WHERE d.drink_name LIKE ? AND (? = -1 OR d.service_id = ?)";

    private static final String GET_DRINK_BY_ID
            = "SELECT d.*, s.service_name FROM Drinks d JOIN Services s ON d.service_id = s.service_id WHERE d.drink_id = ?";

    // Sửa dòng này: Chỉ lấy service có tên chứa chữ "Drink" hoặc "Đồ uống"
    private static final String GET_ALL_SERVICES = 
            "SELECT * FROM Services WHERE service_name LIKE '%Drink Service%'";

    private static final String INSERT_DRINK
            = "INSERT INTO Drinks (drink_name, price, image_url, service_id, description, volume_ml, is_alcoholic, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

    private static final String UPDATE_DRINK
            = "UPDATE Drinks SET drink_name = ?, price = ?, image_url = ?, service_id = ?, description = ?, volume_ml = ?, is_alcoholic = ? WHERE drink_id = ?";

    private static final String ACTIVATE = "UPDATE Drinks SET is_active = 1 WHERE drink_id = ?";
    private static final String DEACTIVATE = "UPDATE Drinks SET is_active = 0 WHERE drink_id = ?";

    private Drink mapResultSetToDrink(ResultSet rs) throws SQLException {
        Drink d = new Drink();
        d.setDrinkId(rs.getInt("drink_id"));

        String name = rs.getString("drink_name");
        d.setName(name != null ? name.trim() : "");

        d.setPrice(rs.getDouble("price"));

        String img = rs.getString("image_url");
        d.setImageUrl(img != null ? img.trim() : "default.jpg");

        d.setServiceId(rs.getInt("service_id"));
        d.setDescription(rs.getString("description"));

        // SỬA: Dùng getInt vì Model của bạn là int
        d.setVolumeMl(rs.getInt("volume_ml"));
        d.setIsAlcoholic(rs.getBoolean("is_alcoholic"));

        d.setIsActive(rs.getBoolean("is_active"));
        return d;
    }

    public List<Drink> getDrinks(String keyword, String serviceIdStr, int page, int pageSize) {
        List<Drink> list = new ArrayList<>();
        String search = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        int serviceId = (serviceIdStr == null || serviceIdStr.equals("-1") || serviceIdStr.isEmpty()) ? -1 : Integer.parseInt(serviceIdStr);
        int offset = (page - 1) * pageSize;

        try (PreparedStatement ps = connection.prepareStatement(GET_DRINKS_PAGING)) {
            ps.setString(1, search);
            ps.setInt(2, serviceId);
            ps.setInt(3, serviceId);
            ps.setInt(4, offset);
            ps.setInt(5, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDrink(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countDrinks(String keyword, String serviceIdStr) {
        String search = "%" + (keyword == null ? "" : keyword.trim()) + "%";
        int serviceId = (serviceIdStr == null || serviceIdStr.equals("-1") || serviceIdStr.isEmpty()) ? -1 : Integer.parseInt(serviceIdStr);
        try (PreparedStatement ps = connection.prepareStatement(COUNT_DRINKS)) {
            ps.setString(1, search);
            ps.setInt(2, serviceId);
            ps.setInt(3, serviceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Drink getDrinkById(int id) {
        try (PreparedStatement ps = connection.prepareStatement(GET_DRINK_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToDrink(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_SERVICES); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Service(rs.getInt("service_id"), rs.getString("service_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createDrink(Drink d) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_DRINK)) {
            ps.setString(1, d.getName());
            ps.setDouble(2, d.getPrice());
            ps.setString(3, d.getImageUrl());
            ps.setInt(4, d.getServiceId());
            ps.setString(5, d.getDescription());

            // SỬA: setInt vì volume là int
            ps.setInt(6, d.getVolumeMl());

            // SỬA: Gọi đúng tên getter trong Model của bạn (getIsAlcoholic)
            ps.setBoolean(7, d.getIsAlcoholic());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateDrink(Drink d) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_DRINK)) {
            ps.setString(1, d.getName());
            ps.setDouble(2, d.getPrice());
            ps.setString(3, d.getImageUrl());
            ps.setInt(4, d.getServiceId());
            ps.setString(5, d.getDescription());

            // SỬA: setInt
            ps.setInt(6, d.getVolumeMl());

            // SỬA: Gọi đúng getter
            ps.setBoolean(7, d.getIsAlcoholic());

            ps.setInt(8, d.getDrinkId());
            return ps.executeUpdate() > 0;
        }
    }

    // Các hàm activate/deactivate giữ nguyên
    public boolean activateDrink(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(ACTIVATE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public List<Drink> getAllActiveDrinks() {
        List<Drink> list = new ArrayList<>();
        
        // SQL: Chỉ lấy từ bảng Drinks
        String sql = "SELECT * FROM Drinks WHERE is_active = 1 ORDER BY drink_id DESC"; 

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToDrink(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean deactivateDrink(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DEACTIVATE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
