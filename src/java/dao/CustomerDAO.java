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
}
