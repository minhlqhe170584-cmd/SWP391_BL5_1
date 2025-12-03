/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.Customer;
import dbContext.DBContext;
import java.util.List;

/**
 *
 * @author Acer
 */
public class CustomerDAO extends DBContext{
    
    private static final String GET_ALL_CUSTOMER = "SELECT customer_id, full_name, email, password, phone, identity_card, is_active, create_at FROM Customers ORDER BY customer_id";
    private static final String GET_CUSTOMER_BY_ID = "SELECT customer_id, full_name, email, password, phone, identity_card, is_active, create_at FROM Customers WHERE customer_id = ?";
    private static final String INSERT_NEW_CUSTOMER = "INSERT INTO Customers(full_name, email, password, phone, identity_card, is_active, create_at) VALUES(?,?,?,?,?,?,?)";
    private static final String UPDATE_CUSTOMER = "UPDATE Customers SET full_name=?, email=?, password=?, phone=?, identity_card=?, is_active=? WHERE customer_id=?";
    private static final String DEACTIVE_CUSTOMER = "UPDATE Customers SET is_active=? WHERE customer_id=?";
    private static final String SEARCH_FILTER ="SELECT * FROM Customers WHERE 1=1";
    
    public List<Customer> getAllCustomer() {
        List<Customer> list = new ArrayList<>();
        
        // Use try-with-resources to ensure PreparedStatement and ResultSet close automatically
        try (PreparedStatement st = connection.prepareStatement(GET_ALL_CUSTOMER);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),       // Fixed column name to match SQL
                    rs.getString("email"),           // Fixed column name to match SQL
                    rs.getString("password"),        // Fixed column name to match SQL
                    rs.getString("phone"),           // Fixed column name to match SQL
                    rs.getString("identity_card"),   // Added missing field found in SQL
                    rs.getBoolean("is_active"),
                    rs.getObject("create_at", LocalDateTime.class)
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error getAllCustomer: " + ex.getMessage());
        }
        return list;
    }

    public Customer getCustomerById(int id) {
        try (PreparedStatement st = connection.prepareStatement(GET_CUSTOMER_BY_ID)) {
            st.setInt(1, id);
            
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("identity_card"),
                        rs.getBoolean("is_active"),
                        rs.getObject("create_at", LocalDateTime.class)
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getCustomerById: " + e.getMessage());
        }
        return null;
    }

    public void insertCustomer(Customer c) {
        // SQL requires 7 parameters
        try (PreparedStatement st = connection.prepareStatement(INSERT_NEW_CUSTOMER)) {
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setString(5, c.getIdentityCard()); // Added based on SQL string
            st.setBoolean(6, c.isIsActive());
            st.setObject(7, c.getCreateAt());

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error insert Customer: " + e.getMessage());
        }
    }

    public void updateCustomer(Customer c) {
        // SQL requires 7 parameters (6 setters + 1 ID for Where clause)
        try (PreparedStatement st = connection.prepareStatement(UPDATE_CUSTOMER)) {
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setString(5, c.getIdentityCard()); // Added based on SQL string
            st.setBoolean(6, c.isIsActive());
            // The ID is the last parameter for the WHERE clause
            st.setInt(7, c.getCustomerId());

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error update Customer: " + e.getMessage());
        }
    }
    
    // Added this method since you had the Constant defined for it
    public void deactivateCustomer(int customerId, boolean isActive) {
        try (PreparedStatement st = connection.prepareStatement(DEACTIVE_CUSTOMER)) {
            st.setBoolean(1, isActive);
            st.setInt(2, customerId);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deactivate Customer: " + e.getMessage());
        }
    }
    public Customer checkLogin(String email, String password) {
        // SQL: Tìm khách hàng có email, pass đúng và đang hoạt động (is_active = 1)
        String sql = "SELECT * FROM Customers WHERE email = ? AND password = ? AND is_active = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Customer c = new Customer();
                // Map đúng tên cột trong Database sang Model
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFullName(rs.getString("full_name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhone(rs.getString("phone"));
                // Các trường khác nếu cần
                // c.setIsActive(rs.getBoolean("is_active"));
                
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi CustomerDAO: " + e.getMessage());
        }
        return null;
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
        return false; // Chưa tồn tại
    }
    
       public void register(Customer c) {
        // SQL: Thêm identity_card (CCCD) vào câu lệnh INSERT
        String sql = "INSERT INTO Customers (full_name, email, password, phone, identity_card, is_active, created_at) VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
                   
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setString(5, c.getIdentityCard()); // Tham số CCCD/CMND
            st.setBoolean(6, true); // is_active
            
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error register: " + e.getMessage());
        }
    }
}
