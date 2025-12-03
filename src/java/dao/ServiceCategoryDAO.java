package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.ServiceCategory;

public class ServiceCategoryDAO extends DBContext {

    private static final String GET_ALL_CATEGORIES = "SELECT category_id, category_name, description FROM ServiceCategories ORDER BY category_id";
    private static final String GET_CATEGORY_BY_ID = "SELECT category_id, category_name, description FROM ServiceCategories WHERE category_id = ?";
    private static final String INSERT_CATEGORY = "INSERT INTO ServiceCategories(category_name, description) VALUES(?, ?)";
    private static final String UPDATE_CATEGORY = "UPDATE ServiceCategories SET category_name=?, description=? WHERE category_id=?";
    private static final String DELETE_CATEGORY = "DELETE FROM ServiceCategories WHERE category_id=?";
    private static final String BASE_CATEGORY_SEARCH = "FROM ServiceCategories WHERE 1=1";

    public ArrayList<ServiceCategory> getAll() {
        ArrayList<ServiceCategory> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_CATEGORIES);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new ServiceCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error getAll Categories: " + e);
        }
        return list;
    }

    public ServiceCategory getById(int id) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_CATEGORY_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new ServiceCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error getById Category: " + e);
        }
        return null;
    }

    public void insert(ServiceCategory c) {
        try {
            PreparedStatement st = connection.prepareStatement(INSERT_CATEGORY);
            st.setString(1, c.getCategoryName());
            st.setString(2, c.getDescription());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error insert Category: " + e);
        }
    }

    public void update(ServiceCategory c) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_CATEGORY);
            st.setString(1, c.getCategoryName());
            st.setString(2, c.getDescription());
            st.setInt(3, c.getCategoryId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error update Category: " + e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement st = connection.prepareStatement(DELETE_CATEGORY);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error delete Category: " + e);
        }
    }

    public ArrayList<ServiceCategory> search(String search, String sort, int pageIndex, int pageSize) {
        ArrayList<ServiceCategory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT category_id, category_name, description ").append(BASE_CATEGORY_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND category_name LIKE ? ");
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY category_id ASC ");
        } else {
            switch (sort) {
                case "nameAsc":
                    sql.append(" ORDER BY category_name ASC ");
                    break;
                case "nameDesc":
                    sql.append(" ORDER BY category_name DESC ");
                    break;
                case "idDesc":
                    sql.append(" ORDER BY category_id DESC ");
                    break;
                case "idAsc":
                default:
                    sql.append(" ORDER BY category_id ASC ");
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

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new ServiceCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error search category: " + e);
        }
        return list;
    }

    public int countSearch(String search) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_CATEGORY_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND category_name LIKE ? ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            if (search != null && !search.trim().isEmpty()) {
                st.setString(1, "%" + search.trim() + "%");
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error count category search: " + e);
        }
        return 0;
    }
}
