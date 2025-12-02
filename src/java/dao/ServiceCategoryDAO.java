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
import models.ServiceCategory;

public class ServiceCategoryDAO extends DBContext {

    public ArrayList<ServiceCategory> getAll() {
        ArrayList<ServiceCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM ServiceCategories";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
        String sql = "SELECT * FROM ServiceCategories WHERE category_id = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
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
        String sql = "INSERT INTO ServiceCategories(category_name, description) VALUES(?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getCategoryName());
            st.setString(2, c.getDescription());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error insert Category: " + e);
        }
    }

    public void update(ServiceCategory c) {
        String sql = "UPDATE ServiceCategories SET category_name=?, description=? WHERE category_id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, c.getCategoryName());
            st.setString(2, c.getDescription());
            st.setInt(3, c.getCategoryId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error update Category: " + e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM ServiceCategories WHERE category_id=?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error delete Category: " + e);
        }
    }
}
