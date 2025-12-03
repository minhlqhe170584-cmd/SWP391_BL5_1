package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Service;

public class ServiceDAO extends DBContext {

    private static final String GET_ALL_SERVICES = "SELECT service_id, service_name, price, unit, image_url, is_active, category_id FROM Services ORDER BY service_id";
    private static final String GET_SERVICE_BY_ID = "SELECT service_id, service_name, price, unit, image_url, is_active, category_id FROM Services WHERE service_id = ?";
    private static final String INSERT_SERVICE = "INSERT INTO Services(service_name, price, unit, image_url, is_active, category_id) VALUES(?,?,?,?,?,?)";
    private static final String UPDATE_SERVICE = "UPDATE Services SET service_name=?, price=?, unit=?, image_url=?, is_active=?, category_id=? WHERE service_id=?";
    private static final String DELETE_SERVICE = "DELETE FROM Services WHERE service_id=?";
    private static final String SEARCH_FILTER = "SELECT * FROM Services WHERE 1=1";
    private static final String BASE_SERVICE_SEARCH = "FROM Services WHERE 1=1";

    public ArrayList<Service> getAll() {
        ArrayList<Service> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_SERVICES);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_active"),
                        rs.getInt("category_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error getAll Services: " + e);
        }
        return list;
    }

    public Service getById(int id) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_SERVICE_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_active"),
                        rs.getInt("category_id")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error getById Service: " + e);
        }
        return null;
    }

    public void insert(Service s) {
        try {
            PreparedStatement st = connection.prepareStatement(INSERT_SERVICE);
            st.setString(1, s.getServiceName());
            st.setDouble(2, s.getPrice());
            st.setString(3, s.getUnit());
            st.setString(4, s.getImageUrl());
            st.setBoolean(5, s.isIsActive());
            st.setInt(6, s.getCategoryId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error insert Service: " + e);
        }
    }

    public void update(Service s) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_SERVICE);
            st.setString(1, s.getServiceName());
            st.setDouble(2, s.getPrice());
            st.setString(3, s.getUnit());
            st.setString(4, s.getImageUrl());
            st.setBoolean(5, s.isIsActive());
            st.setInt(6, s.getCategoryId());
            st.setInt(7, s.getServiceId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error update Service: " + e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement st = connection.prepareStatement(DELETE_SERVICE);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error delete Service: " + e);
        }
    }

    public ArrayList<Service> searchFilter(String search, String categoryId) {
        ArrayList<Service> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SEARCH_FILTER);

        try {
            if (search != null && !search.isEmpty()) {
                sql.append(" AND service_name LIKE ? ");
            }

            if (categoryId != null && !categoryId.isEmpty()) {
                sql.append(" AND category_id = ? ");
            }

            PreparedStatement st = connection.prepareStatement(sql.toString());

            int idx = 1;
            if (search != null && !search.isEmpty()) {
                st.setString(idx++, "%" + search + "%");
            }

            if (categoryId != null && !categoryId.isEmpty()) {
                st.setInt(idx++, Integer.parseInt(categoryId));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_active"),
                        rs.getInt("category_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error filter search: " + e);
        }

        return list;
    }

    public ArrayList<Service> search(String search, String categoryId, String sort, int pageIndex, int pageSize) {
        ArrayList<Service> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT service_id, service_name, price, unit, image_url, is_active, category_id ").append(BASE_SERVICE_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND service_name LIKE ? ");
        }

        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND category_id = ? ");
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY service_id ASC ");
        } else {
            switch (sort) {
                case "nameAsc":
                    sql.append(" ORDER BY service_name ASC ");
                    break;
                case "nameDesc":
                    sql.append(" ORDER BY service_name DESC ");
                    break;
                case "priceAsc":
                    sql.append(" ORDER BY price ASC ");
                    break;
                case "priceDesc":
                    sql.append(" ORDER BY price DESC ");
                    break;
                case "idDesc":
                    sql.append(" ORDER BY service_id DESC ");
                    break;
                case "idAsc":
                default:
                    sql.append(" ORDER BY service_id ASC ");
                    break;
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
            }

            if (categoryId != null && !categoryId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(categoryId));
            }

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("price"),
                        rs.getString("unit"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_active"),
                        rs.getInt("category_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error search service: " + e);
        }

        return list;
    }

    public int countSearch(String search, String categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_SERVICE_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND service_name LIKE ? ");
        }

        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND category_id = ? ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
            }

            if (categoryId != null && !categoryId.trim().isEmpty()) {
                st.setInt(idx++, Integer.parseInt(categoryId));
            }

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error count service search: " + e);
        }

        return 0;
    }
}
