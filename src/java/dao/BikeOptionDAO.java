package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.BikeRentalOption;
import models.Service;

public class BikeOptionDAO extends DBContext {

    private static final String BASE_SEARCH = "FROM BikeRentalOptions o JOIN Services s ON o.service_id = s.service_id WHERE 1=1 ";

    public ArrayList<BikeRentalOption> search(String search, String serviceId, String status, String sort, int pageIndex, int pageSize) {
        ArrayList<BikeRentalOption> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT o.*, s.service_name " + BASE_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (o.option_name LIKE ? OR s.service_name LIKE ?) ");
        }
        if (serviceId != null && !serviceId.trim().isEmpty()) {
            sql.append(" AND o.service_id = ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            if ("active".equals(status)) sql.append(" AND o.is_active = 1 ");
            else if ("inactive".equals(status)) sql.append(" AND o.is_active = 0 ");
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY o.item_id DESC ");
        } else {
            switch (sort) {
                case "priceAsc": sql.append(" ORDER BY o.price ASC "); break;
                case "priceDesc": sql.append(" ORDER BY o.price DESC "); break;
                case "nameAsc": sql.append(" ORDER BY o.option_name ASC "); break;
                default: sql.append(" ORDER BY o.item_id DESC "); break;
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;
            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
                st.setString(idx++, "%" + search.trim() + "%");
            }
            if (serviceId != null && !serviceId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(serviceId));
            }
            
            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BikeRentalOption o = new BikeRentalOption(
                    rs.getInt("item_id"), rs.getInt("service_id"), 
                    rs.getString("option_name"), rs.getInt("duration_minutes"), 
                    rs.getDouble("price"), rs.getBoolean("is_active")
                );
                o.setServiceName(rs.getString("service_name"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countSearch(String search, String serviceId, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) " + BASE_SEARCH);
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (o.option_name LIKE ? OR s.service_name LIKE ?) ");
        }
        if (serviceId != null && !serviceId.trim().isEmpty()) {
            sql.append(" AND o.service_id = ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            if ("active".equals(status)) sql.append(" AND o.is_active = 1 ");
            else if ("inactive".equals(status)) sql.append(" AND o.is_active = 0 ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;
            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
                st.setString(idx++, "%" + search.trim() + "%");
            }
            if (serviceId != null && !serviceId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(serviceId));
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public BikeRentalOption getById(int id) {
        String sql = "SELECT * FROM BikeRentalOptions WHERE item_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new BikeRentalOption(
                        rs.getInt("item_id"), rs.getInt("service_id"), 
                        rs.getString("option_name"), rs.getInt("duration_minutes"), 
                        rs.getDouble("price"), rs.getBoolean("is_active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(BikeRentalOption o) {
        String sql = "INSERT INTO BikeRentalOptions(service_id, option_name, duration_minutes, price, is_active) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, o.getServiceId());
            st.setString(2, o.getOptionName());
            st.setInt(3, o.getDurationMinutes());
            st.setDouble(4, o.getPrice());
            st.setBoolean(5, o.isActive());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(BikeRentalOption o) {
        String sql = "UPDATE BikeRentalOptions SET service_id=?, option_name=?, duration_minutes=?, price=?, is_active=? WHERE item_id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, o.getServiceId());
            st.setString(2, o.getOptionName());
            st.setInt(3, o.getDurationMinutes());
            st.setDouble(4, o.getPrice());
            st.setBoolean(5, o.isActive());
            st.setInt(6, o.getItemId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void toggleStatus(int id, boolean status) {
        String sql = "UPDATE BikeRentalOptions SET is_active = ? WHERE item_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setBoolean(1, status);
            st.setInt(2, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Service> getBikeServices() {
        ArrayList<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE service_name LIKE N'%Xe đạp%' OR service_name LIKE '%Bike%'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                list.add(new Service(rs.getInt("service_id"), rs.getString("service_name"), "", true, 0, false));
            }
        } catch(SQLException e) {}
        return list;
    }
}