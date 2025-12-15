/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.LaundryOrderDetail;
import models.Service;

/**
 *
 * @author Acer
 */
public class LaundryHandleDAO extends DBContext {
    public Integer createLaundryOrder(Integer roomId, LocalDateTime expectedPickupTime, LocalDateTime expectedReturnTime, String note, 
                                   ArrayList<LaundryOrderDetail> orderDetails) {
        Connection conn = null;
        Integer laundryId = null;
        
        try {
            conn = connection;
            conn.setAutoCommit(false);
            
            // Calculate total amount
            double totalAmount = 0.0;
            for (LaundryOrderDetail detail : orderDetails) {
                totalAmount += detail.getSubtotal();
            }
            
            // Step 1: Create ServiceOrder
            String insertServiceOrder = "INSERT INTO ServiceOrders(room_id, order_date, total_amount, status, note) " +
                                       "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stServiceOrder = conn.prepareStatement(insertServiceOrder, Statement.RETURN_GENERATED_KEYS);
            stServiceOrder.setInt(1, roomId);
            stServiceOrder.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stServiceOrder.setDouble(3, totalAmount);
            stServiceOrder.setString(4, "PENDING");
            stServiceOrder.setString(5, note);
            
            stServiceOrder.executeUpdate();
            ResultSet rsServiceOrder = stServiceOrder.getGeneratedKeys();
            
            Integer orderId = null;
            if (rsServiceOrder.next()) {
                orderId = rsServiceOrder.getInt(1);
            }
            
            if (orderId == null) {
                throw new SQLException("Failed to create service order");
            }
            
            // Step 2: Create LaundryOrder
            String insertLaundryOrder = "INSERT INTO LaundryOrders(order_id, expected_pickup_time, expected_return_time, status, note) " +
                                       "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stLaundryOrder = conn.prepareStatement(insertLaundryOrder, Statement.RETURN_GENERATED_KEYS);
            stLaundryOrder.setInt(1, orderId);
            
            if (expectedPickupTime != null) {
                stLaundryOrder.setTimestamp(2, Timestamp.valueOf(expectedPickupTime));
            } else {
                stLaundryOrder.setTimestamp(2, null);
            }
            
            if (expectedReturnTime != null) {
                stLaundryOrder.setTimestamp(3, Timestamp.valueOf(expectedReturnTime));
            } else {
                stLaundryOrder.setTimestamp(3, null);
            }
            
            stLaundryOrder.setString(4, "Pending");
            stLaundryOrder.setString(5, note);
            
            stLaundryOrder.executeUpdate();
            ResultSet rsLaundryOrder = stLaundryOrder.getGeneratedKeys();
            
            if (rsLaundryOrder.next()) {
                laundryId = rsLaundryOrder.getInt(1);
            }
            
            if (laundryId == null) {
                throw new SQLException("Failed to create laundry order");
            }
            
            // Step 3: Insert LaundryOrderDetails
            String insertDetail = "INSERT INTO LaundryOrderDetails(laundry_id, laundry_item_id, quantity, unit_price) " +
                                 "VALUES(?, ?, ?, ?)";
            PreparedStatement stDetail = conn.prepareStatement(insertDetail);
            
            for (LaundryOrderDetail detail : orderDetails) {
                stDetail.setInt(1, laundryId);
                stDetail.setInt(2, detail.getLaundryItemId());
                stDetail.setInt(3, detail.getQuantity());
                stDetail.setDouble(4, detail.getUnitPrice());              
                stDetail.executeUpdate();
            }
            
            conn.commit();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback error: " + ex);
                }
            }
            System.out.println("Error creating laundry order: " + e);
            laundryId = null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.out.println("Error setting auto commit: " + e);
                }
            }
        }
        
        return laundryId;
    }
    
    public ArrayList<Service> getAllLaundryServices() {
        ArrayList<Service> list = new ArrayList<>();
        String sql = "SELECT s.* FROM Services s " +
                     "JOIN ServiceCategories c ON s.category_id = c.category_id " +
                     "WHERE (c.category_name LIKE N'%Giặt Là%' OR c.category_name LIKE '%Laundry%' OR c.category_name LIKE '%LAUNDRY%') " +
                     "AND s.is_active = 1 AND s.is_deleted = 0";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Service(
                    rs.getInt("service_id"), 
                    rs.getString("service_name"), 
                    rs.getString("image_url"), 
                    rs.getBoolean("is_active"), 
                    rs.getInt("category_id"), 
                    rs.getBoolean("is_deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
