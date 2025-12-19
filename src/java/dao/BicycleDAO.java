package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Bicycle;

public class BicycleDAO extends DBContext {

    private static final String DYNAMIC_STATUS_SQL = 
            " CASE " +
            "    WHEN b.condition = 'Deleted' THEN 'Deleted' " +
            "    WHEN EXISTS ( " +
            "        SELECT 1 FROM ServiceOrders so " +
            "        JOIN OrderDetails od ON so.order_id = od.order_id " +
            "        JOIN BikeRentals br ON so.order_id = br.order_id " +
            "        WHERE br.bike_id = b.bike_id " +
            "        AND so.status IN ('Confirmed', 'Pending') " +
            "        AND GETDATE() BETWEEN so.booking_start_date AND so.booking_end_date " +
            "    ) THEN 'Rented' " +
            "    ELSE 'Available' " +
            " END as current_status ";

    public ArrayList<Bicycle> search(String search, String statusFilter, String view, String sort, int pageIndex, int pageSize) {
        ArrayList<Bicycle> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT b.bike_id, b.bike_code, b.service_id, b.condition, s.service_name, ");
        sql.append(DYNAMIC_STATUS_SQL);
        sql.append("FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id WHERE 1=1 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (b.bike_code LIKE ? OR s.service_name LIKE ?) ");
        }

        if ("deleted".equals(view)) {
            sql.append(" AND b.condition = 'Deleted' ");
        } else {
            sql.append(" AND (b.condition != 'Deleted' OR b.condition IS NULL) ");
        }

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "codeAsc": sql.append(" ORDER BY b.bike_code ASC "); break;
                case "codeDesc": sql.append(" ORDER BY b.bike_code DESC "); break;
                case "idDesc": sql.append(" ORDER BY b.bike_id DESC "); break;
                default: sql.append(" ORDER BY b.bike_id ASC "); break;
            }
        } else {
            sql.append(" ORDER BY b.bike_id ASC ");
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
                st.setString(idx++, "%" + search.trim() + "%");
            }

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Bicycle b = new Bicycle();
                b.setBikeId(rs.getInt("bike_id"));
                b.setBikeCode(rs.getString("bike_code"));
                b.setServiceId(rs.getInt("service_id"));
                b.setCondition(rs.getString("condition"));
                b.setServiceName(rs.getString("service_name"));
                b.setStatus(rs.getString("current_status"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countSearch(String search, String statusFilter, String view) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id WHERE 1=1 ");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (b.bike_code LIKE ? OR s.service_name LIKE ?) ");
        }

        if ("deleted".equals(view)) {
            sql.append(" AND b.condition = 'Deleted' ");
        } else {
            sql.append(" AND (b.condition != 'Deleted' OR b.condition IS NULL) ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                st.setString(idx++, "%" + search.trim() + "%");
                st.setString(idx++, "%" + search.trim() + "%");
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

    public Bicycle getById(int id) {
        String sql = "SELECT b.bike_id, b.bike_code, b.service_id, b.condition, s.service_name, " +
                     DYNAMIC_STATUS_SQL +
                     "FROM Bicycles b LEFT JOIN Services s ON b.service_id = s.service_id WHERE b.bike_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Bicycle b = new Bicycle();
                b.setBikeId(rs.getInt("bike_id"));
                b.setBikeCode(rs.getString("bike_code"));
                b.setServiceId(rs.getInt("service_id"));
                b.setCondition(rs.getString("condition"));
                b.setServiceName(rs.getString("service_name"));
                b.setStatus(rs.getString("current_status"));
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Bicycle b) throws SQLException {
        String sql = "INSERT INTO Bicycles(bike_code, service_id, condition) VALUES(?,?,?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, b.getBikeCode());
        st.setInt(2, b.getServiceId());
        st.setString(3, b.getCondition());
        st.executeUpdate();
    }

    public void update(Bicycle b) throws SQLException {
        String sql = "UPDATE Bicycles SET bike_code=?, service_id=?, condition=? WHERE bike_id=?";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, b.getBikeCode());
        st.setInt(2, b.getServiceId());
        st.setString(3, b.getCondition());
        st.setInt(4, b.getBikeId());
        st.executeUpdate();
    }

    public void toggleDeleteStatus(int id) throws SQLException {
        String checkSql = "SELECT condition FROM Bicycles WHERE bike_id = ?";
        PreparedStatement checkSt = connection.prepareStatement(checkSql);
        checkSt.setInt(1, id);
        ResultSet rs = checkSt.executeQuery();
        String current = "";
        if (rs.next()) current = rs.getString("condition");

        String newVal = "Deleted".equals(current) ? "Good" : "Deleted";
        
        String updateSql = "UPDATE Bicycles SET condition = ? WHERE bike_id = ?";
        PreparedStatement updateSt = connection.prepareStatement(updateSql);
        updateSt.setString(1, newVal);
        updateSt.setInt(2, id);
        updateSt.executeUpdate();
    }

    public boolean isExistCode(String code, int idToExclude) {
        String sql = "SELECT COUNT(*) FROM Bicycles WHERE bike_code = ? AND bike_id != ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, code);
            st.setInt(2, idToExclude);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}