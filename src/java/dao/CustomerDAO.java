/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Customer;
import dbContext.DBContext;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Acer
 */
public class CustomerDAO extends DBContext{
    
    private static final String GET_ALL_CUSTOMER = "SELECT customer_id, full_name, email, password, phone, is_active, created_at FROM Customers ORDER BY customer_id";
    private static final String GET_CUSTOMER_BY_ID = "SELECT customer_id, full_name, email, password, phone, is_active, created_at FROM Customers WHERE customer_id = ?";
    private static final String INSERT_NEW_CUSTOMER = "INSERT INTO Customers(full_name, email, password, phone, is_active, created_at) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_CUSTOMER = "UPDATE Customers SET full_name=?, email=?, password=?, phone=? WHERE customer_id=?";
    private static final String DEACTIVE_CUSTOMER = "UPDATE Customers SET is_active= 0 WHERE customer_id=?";
    private static final String ACTIVE_CUSTOMER = "UPDATE Customers SET is_active= 1 WHERE customer_id=?";
    private static final String SEARCH_FILTER = "SELECT * FROM Customers WHERE 1=1";
    private static final String BASE_CUSTOMER_SEARCH = "FROM Customers WHERE 1=1";
    
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_CUSTOMER);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all customers: " + e);
        }
        return list;
    }
    
    public Customer getCustomerById(int customerId) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_CUSTOMER_BY_ID);
            st.setInt(1, customerId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer by ID: " + e);
        }
        return null;
    }
    
    public boolean insertCustomer(Customer customer) {
        try {
            PreparedStatement st = connection.prepareStatement(INSERT_NEW_CUSTOMER);
            st.setString(1, customer.getFullName());
            st.setString(2, customer.getEmail());
            st.setString(3, customer.getPassword());
            st.setString(4, customer.getPhone());
            st.setBoolean(5, customer.isIsActive());
            st.setTimestamp(6, Timestamp.valueOf(customer.getCreateAt()));
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting customer: " + e);
        }
        return false;
    }
    
    public boolean updateCustomer(Customer customer) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_CUSTOMER);
            st.setString(1, customer.getFullName());
            st.setString(2, customer.getEmail());
            st.setString(3, customer.getPassword());
            st.setString(4, customer.getPhone());
            st.setInt(5, customer.getCustomerId());
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e);
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deactivateCustomer(int customerId) {
        try {
            PreparedStatement st = connection.prepareStatement(DEACTIVE_CUSTOMER);
            st.setInt(1, customerId);
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deactivating customer: " + e);
            e.printStackTrace();
        }
        return false;
    }
    
        public boolean activateCustomer(int customerId) {
        try {
            PreparedStatement st = connection.prepareStatement(ACTIVE_CUSTOMER);
            st.setInt(1, customerId);
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error activating customer: " + e);
            e.printStackTrace();
        }
        return false;
    }
    
    public ArrayList<Customer> searchFilter(String search, String status) {
        ArrayList<Customer> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SEARCH_FILTER);

        try {
            if (search != null && !search.isEmpty()) {
                sql.append(" AND (full_name LIKE ? OR email LIKE ? OR phone LIKE ?) ");
            }

            if (status != null && !status.isEmpty()) {
                sql.append(" AND is_active = ? ");
            }

            PreparedStatement st = connection.prepareStatement(sql.toString());

            int idx = 1;
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }

            if (status != null && !status.isEmpty()) {
                st.setBoolean(idx++, Boolean.parseBoolean(status));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error filter search: " + e);
        }

        return list;
    }
    
    public ArrayList<Customer> search(String search, String status, String sort, int pageIndex, int pageSize) {
        ArrayList<Customer> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT customer_id, full_name, email, password, phone, is_active, created_at ").append(BASE_CUSTOMER_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (full_name LIKE ? OR email LIKE ? OR phone LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND is_active = ? ");
        }

        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY customer_id ASC ");
        } else {
            switch (sort) {
                case "nameAsc":
                    sql.append(" ORDER BY full_name ASC ");
                    break;
                case "nameDesc":
                    sql.append(" ORDER BY full_name DESC ");
                    break;
                case "emailAsc":
                    sql.append(" ORDER BY email ASC ");
                    break;
                case "emailDesc":
                    sql.append(" ORDER BY email DESC ");
                    break;
                case "dateAsc":
                    sql.append(" ORDER BY created_at ASC ");
                    break;
                case "dateDesc":
                    sql.append(" ORDER BY created_at DESC ");
                    break;
                case "idDesc":
                    sql.append(" ORDER BY customer_id DESC ");
                    break;
                case "idAsc":
                default:
                    sql.append(" ORDER BY customer_id ASC ");
                    break;
            }
        }

        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                if("true".equals(status))st.setInt(idx++, 1);   
                if("false".equals(status)) st.setInt(idx++, 0);
            }

            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error search customer: " + e);
        }

        return list;
    }
    
    public int countSearch(String search, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_CUSTOMER_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (full_name LIKE ? OR email LIKE ? OR phone LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND is_active = ? ");
        }

        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                st.setBoolean(idx++, Boolean.parseBoolean(status));
            }

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error count customer search: " + e);
        }

        return 0;
    }
    /**
     * Kiểm tra xem Email đã tồn tại trong Database chưa
     * @param email Email cần kiểm tra
     * @return true nếu đã có, false nếu chưa có
     */
    public boolean checkEmailExist(String email) {
        String sql = "SELECT email FROM Customers WHERE email = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            
            // Nếu result set có dữ liệu -> Email đã tồn tại
            if (rs.next()) {
                return true; 
            }
        } catch (SQLException e) {
            System.out.println("Error checkEmailExist: " + e.getMessage());
        }
        return false; 
    }
    
    public void register(Customer c) {
        // SQL: Xóa cột identity_card khỏi câu lệnh INSERT
        String sql = "INSERT INTO Customers (full_name, email, password, phone, is_active, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, GETDATE())";
                   
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            // Bỏ dòng setString cho CCCD
            st.setBoolean(5, true); // is_active bây giờ là tham số thứ 5
            
            st.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error register: " + e.getMessage());
        }
    }
    // Bổ sung vào CustomerDAO.java
    public void updatePassword(String email, String newPassword) {
        String sql = "UPDATE Customers SET password = ? WHERE email = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, newPassword);
            st.setString(2, email);
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updatePassword: " + e.getMessage());
        }
    }// --- 1. HÀM LOGIN (Trả về đối tượng Customer để lưu Session) ---
    public Customer login(String email, String password) {
        String sql = "SELECT * FROM Customers WHERE email = ? AND password = ?";
        try {
            // Dùng biến 'connection' có sẵn từ DBContext cũ
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                
                c.setIsActive(rs.getBoolean("is_active"));
                // Quan trọng: Đóng kết nối sau khi lấy xong
                connection.close();
                return c;
            }
            
            // Nếu không tìm thấy, cũng phải đóng kết nối
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- 2. HÀM UPDATE PROFILE (Cho chức năng cập nhật) ---
    public boolean updateProfile(Customer c) {
        String sql = "UPDATE Customers SET full_name = ?, phone = ?, password = ? WHERE customer_id = ?";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            
            // Dùng setNString cho tiếng Việt
            ps.setNString(1, c.getFullName()); 
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getPassword());
            ps.setInt(4, c.getCustomerId());
            
            int rowsAffected = ps.executeUpdate();
            
            // Đóng kết nối
            connection.close();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Nếu bạn có hàm checkLogin cũ trả về boolean, bạn có thể xóa nó đi 
    // và dùng hàm login() ở trên thay thế là tốt nhất.
}
