/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dbContext.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Service;

public class ServiceDAO extends DBContext {
    
    public ArrayList<Service> getAll() {
        ArrayList<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Services";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
        String sql = "SELECT * FROM Services WHERE service_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
            System.out.println("Error getById: " + e);
        }
        return null;
    }

    public void insert(Service s) {
        String sql = "INSERT INTO Services(service_name, price, unit, image_url, is_active, category_id) "
                + "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
        String sql = "UPDATE Services SET service_name=?, price=?, unit=?, image_url=?, is_active=?, category_id=? "
                + "WHERE service_id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
        String sql = "DELETE FROM Services WHERE service_id=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error delete Service: " + e);
        }
    }
    
    public ArrayList<Service> searchFilter(String search, String categoryId) {
        ArrayList<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE 1=1 ";

        try {

            if (search != null && !search.isEmpty()) {
                sql += " AND service_name LIKE ? ";
            }

            if (categoryId != null && !categoryId.isEmpty()) {
                sql += " AND category_id = ? ";
            }

            PreparedStatement st = connection.prepareStatement(sql);

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
}
