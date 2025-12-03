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

/**
 *
 * @author Acer
 */
public class CustomerDAO extends DBContext{
    
    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        
        try{
            PreparedStatement st = connection.prepareCall(sql);
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                list.add(new Customer(
                        rs.getInt("customer_id"), 
                        rs.getString("customer_fullName"), 
                        rs.getString("customer_email"), 
                        rs.getString("customer_password"), 
                        rs.getString("customer_phone"), 
                        rs.getBoolean("is_active"), 
                        rs.getObject("create_at", LocalDateTime.class)
                ));
            }
            
        } catch (SQLException ex){
            System.out.println(ex);
        }
        return list;
    }
    
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM Customers WHERE customer_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("customer_fullName"),
                    rs.getString("customer_email"),
                    rs.getString("customer_password"),
                    rs.getString("customer_phone"),
                    rs.getBoolean("is_active"),
                    // Handling LocalDateTime
                    rs.getObject("create_at", LocalDateTime.class)
                );
            }

        } catch (SQLException e) {
            System.out.println("Error getCustomerById: " + e);
        }
        return null;
    }

    public void insertCustomer(Customer c) {
        // Assuming customer_id is Auto-Increment, so we don't insert it
        String sql = "INSERT INTO Customers(customer_fullName, customer_email, customer_password, customer_phone, is_active, create_at) "
                   + "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setBoolean(5, c.isIsActive());
            // Use setObject for LocalDateTime
            st.setObject(6, c.getCreateAt()); 
            
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error insert Customer: " + e);
        }
    }

    public void updateCustomer(Customer c) {
        // Usually we do NOT update 'create_at'
        String sql = "UPDATE Customers SET customer_fullName=?, customer_email=?, customer_password=?, customer_phone=?, is_active=? "
                   + "WHERE customer_id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setBoolean(5, c.isIsActive());
            // The ID is the last parameter for the WHERE clause
            st.setInt(6, c.getCustomerId()); 
            
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error update Customer: " + e);
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
        // Bỏ cột identity_card trong câu lệnh INSERT
        String sql = "INSERT INTO Customers (full_name, email, password, phone, is_active, created_at) VALUES (?, ?, ?, ?, ?, GETDATE())";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getFullName());
            st.setString(2, c.getEmail());
            st.setString(3, c.getPassword());
            st.setString(4, c.getPhone());
            st.setBoolean(5, true); // is_active
            
            st.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error register: " + e.getMessage());
        }
    }
}
