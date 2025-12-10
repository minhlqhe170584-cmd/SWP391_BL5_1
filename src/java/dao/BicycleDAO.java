package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Bicycle;

public class BicycleDAO extends DBContext {

    private static final String GET_BY_ID = "SELECT b.bike_id, b.bike_code, b.service_id, b.status, b.condition, s.service_name FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id WHERE b.bike_id = ?";
    private static final String INSERT_BICYCLE = "INSERT INTO Bicycles(bike_code, service_id, status, condition) VALUES(?,?,?,?)";
    private static final String UPDATE_BICYCLE = "UPDATE Bicycles SET bike_code=?, service_id=?, status=?, condition=? WHERE bike_id=?";
    private static final String CHECK_STATUS = "SELECT status FROM Bicycles WHERE bike_id = ?";
    private static final String SET_STATUS = "UPDATE Bicycles SET status = ? WHERE bike_id = ?";
    private static final String BASE_BICYCLE_SEARCH = "FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id WHERE 1=1";
    private static final String CHECK_DUPLICATE_CODE = "SELECT COUNT(*) FROM Bicycles WHERE bike_code = ? AND bike_id != ?";

    public Bicycle getById(int id) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Bicycle(
                        rs.getInt("bike_id"),
                        rs.getString("bike_code"),
                        rs.getInt("service_id"),
                        rs.getString("status"),
                        rs.getString("condition"),
                        rs.getString("service_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Bicycle b) throws SQLException {
        PreparedStatement st = connection.prepareStatement(INSERT_BICYCLE);
        st.setString(1, b.getBikeCode());
        st.setInt(2, b.getServiceId());
        st.setString(3, b.getStatus());
        st.setString(4, b.getCondition());
        st.executeUpdate();
    }

    public void update(Bicycle b) throws SQLException {
        PreparedStatement st = connection.prepareStatement(UPDATE_BICYCLE);
        st.setString(1, b.getBikeCode());
        st.setInt(2, b.getServiceId());
        st.setString(3, b.getStatus());
        st.setString(4, b.getCondition());
        st.setInt(5, b.getBikeId());
        st.executeUpdate();
    }

    public void toggleDeleteStatus(int id) throws SQLException {
        String currentStatus = "";
        PreparedStatement checkSt = connection.prepareStatement(CHECK_STATUS);
        checkSt.setInt(1, id);
        ResultSet rs = checkSt.executeQuery();
        if (rs.next()) {
            currentStatus = rs.getString("status");
        }

        String newStatus;
        if ("Deleted".equalsIgnoreCase(currentStatus)) {
            newStatus = "Available";
        } else {
            newStatus = "Deleted";
        }

        PreparedStatement updateSt = connection.prepareStatement(SET_STATUS);
        updateSt.setString(1, newStatus);
        updateSt.setInt(2, id);
        updateSt.executeUpdate();
    }

    public ArrayList<Bicycle> search(String search, String statusFilter, String view, String sort, int pageIndex, int pageSize) {
        ArrayList<Bicycle> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.bike_id, b.bike_code, b.service_id, b.status, b.condition, s.service_name ").append(BASE_BICYCLE_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (b.bike_code LIKE ? OR s.service_name LIKE ?) ");
        }

        if ("deleted".equals(view)) {
            sql.append(" AND b.status = 'Deleted' ");
        } else {
            sql.append(" AND b.status != 'Deleted' ");
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
                sql.append(" AND b.status = ? ");
            }
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY b.bike_id DESC ");
        } else {
            switch (sort) {
                case "codeAsc": sql.append(" ORDER BY b.bike_code ASC "); break;
                case "codeDesc": sql.append(" ORDER BY b.bike_code DESC "); break;
                case "statusAsc": sql.append(" ORDER BY b.status ASC "); break;
                case "idAsc": sql.append(" ORDER BY b.bike_id ASC "); break;
                default: sql.append(" ORDER BY b.bike_id DESC "); break;
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

            if (!"deleted".equals(view)) {
                if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
                    st.setString(idx++, statusFilter);
                }
            }

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Bicycle(
                        rs.getInt("bike_id"),
                        rs.getString("bike_code"),
                        rs.getInt("service_id"),
                        rs.getString("status"),
                        rs.getString("condition"),
                        rs.getString("service_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countSearch(String search, String statusFilter, String view) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_BICYCLE_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (b.bike_code LIKE ? OR s.service_name LIKE ?) ");
        }

        if ("deleted".equals(view)) {
            sql.append(" AND b.status = 'Deleted' ");
        } else {
            sql.append(" AND b.status != 'Deleted' ");
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
                sql.append(" AND b.status = ? ");
            }
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
                st.setString(idx++, "%" + search.trim() + "%");
            }
            
            if (!"deleted".equals(view)) {
                if (statusFilter != null && !statusFilter.trim().isEmpty() && !statusFilter.equals("All")) {
                    st.setString(idx++, statusFilter);
                }
            }

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean isExistCode(String code, int idToExclude) {
        try {
            PreparedStatement st = connection.prepareStatement(CHECK_DUPLICATE_CODE);
            st.setString(1, code);
            st.setInt(2, idToExclude);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}