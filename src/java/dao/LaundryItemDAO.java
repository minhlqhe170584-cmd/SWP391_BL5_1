package dao;

import java.sql.*;
import java.util.ArrayList;
import models.LaundryItem;
import models.Service;
import dbContext.DBContext;

public class LaundryItemDAO extends DBContext {
    
    private static final String GET_ALL_ITEMS = "SELECT li.laundry_item_id, li.service_id, li.item_name, " +
            "li.description, li.default_price, li.unit, li.is_active, s.service_name " +
            "FROM LaundryItems li " +
            "LEFT JOIN Services s ON li.service_id = s.service_id " +
            "ORDER BY li.item_name";
    
    private static final String GET_ACTIVE_ITEMS = "SELECT li.laundry_item_id, li.service_id, li.item_name, " +
            "li.description, li.default_price, li.unit, li.is_active, s.service_name " +
            "FROM LaundryItems li " +
            "LEFT JOIN Services s ON li.service_id = s.service_id " +
            "WHERE li.is_active = 1 " +
            "ORDER BY li.item_name";
    
    private static final String GET_ITEM_BY_ID = "SELECT li.laundry_item_id, li.service_id, li.item_name, " +
            "li.description, li.default_price, li.unit, li.is_active, s.service_name " +
            "FROM LaundryItems li " +
            "LEFT JOIN Services s ON li.service_id = s.service_id " +
            "WHERE li.laundry_item_id = ?";
    
    private static final String GET_ITEMS_BY_SERVICE = "SELECT li.laundry_item_id, li.service_id, li.item_name, " +
            "li.description, li.default_price, li.unit, li.is_active " +
            "FROM LaundryItems li " +
            "WHERE li.service_id = ? AND li.is_active = 1 " +
            "ORDER BY li.item_name";
    
    // Get all items
    public ArrayList<LaundryItem> getAllItems() {
        ArrayList<LaundryItem> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_ITEMS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getInt("laundry_item_id"));
                item.setServiceId(rs.getInt("service_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setDefaultPrice(rs.getDouble("default_price"));
                item.setUnit(rs.getString("unit"));
                item.setIsActive(rs.getBoolean("is_active"));
                
                // Set service info
                Service service = new Service();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                item.setService(service);
                
                list.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all items: " + e);
        }
        return list;
    }
    
    // Get active items only
    public ArrayList<LaundryItem> getAllActiveItems() {
        ArrayList<LaundryItem> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ACTIVE_ITEMS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getInt("laundry_item_id"));
                item.setServiceId(rs.getInt("service_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setDefaultPrice(rs.getDouble("default_price"));
                item.setUnit(rs.getString("unit"));
                item.setIsActive(rs.getBoolean("is_active"));
                
                // Set service info
                Service service = new Service();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                item.setService(service);
                
                list.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting active items: " + e);
        }
        return list;
    }
    
    // Get item by ID
    public LaundryItem getItemById(int itemId) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_ITEM_BY_ID);
            st.setLong(1, itemId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getInt("laundry_item_id"));
                item.setServiceId(rs.getInt("service_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setDefaultPrice(rs.getDouble("default_price"));
                item.setUnit(rs.getString("unit"));
                item.setIsActive(rs.getBoolean("is_active"));
                
                // Set service info
                Service service = new Service();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                item.setService(service);
                
                return item;
            }
        } catch (SQLException e) {
            System.out.println("Error getting item by ID: " + e);
        }
        return null;
    }
    
    // Get items by service
    public ArrayList<LaundryItem> getItemsByService(int serviceId) {
        ArrayList<LaundryItem> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ITEMS_BY_SERVICE);
            st.setInt(1, serviceId);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getInt("laundry_item_id"));
                item.setServiceId(rs.getInt("service_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setDefaultPrice(rs.getDouble("default_price"));
                item.setUnit(rs.getString("unit"));
                item.setIsActive(rs.getBoolean("is_active"));
                
                list.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting items by service: " + e);
        }
        return list;
    }
}