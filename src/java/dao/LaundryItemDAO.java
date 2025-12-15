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
            "WHERE li.is_active = 1 " +
            "AND li.laundry_item_id = ?";
    
    private static final String GET_ITEMS_BY_SERVICE = "SELECT li.laundry_item_id, li.service_id, li.item_name, " +
            "li.description, li.default_price, li.unit, li.is_active " +
            "FROM LaundryItems li " +
            "WHERE li.service_id = ? AND li.is_active = 1 " +
            "ORDER BY li.item_name";
    
    private static final String INSERT_ITEM = "INSERT INTO LaundryItems(service_id, item_name, description, " +
            "default_price, unit, is_active) VALUES(?,?,?,?,?,?)";
    
    private static final String UPDATE_ITEM = "UPDATE LaundryItems SET service_id=?, item_name=?, description=?, " +
            "default_price=?, unit=?, is_active=? WHERE laundry_item_id=?";
    
    private static final String ACTIVATE_ITEM = "UPDATE LaundryItems SET is_active=1 WHERE laundry_item_id=?";
    
    private static final String DEACTIVATE_ITEM = "UPDATE LaundryItems SET is_active=0 WHERE laundry_item_id=?";
    
    private static final String BASE_SEARCH = "FROM LaundryItems li " +
            "LEFT JOIN Services s ON li.service_id = s.service_id WHERE 1=1";
    
    private static final String GET_ACTIVE_SERVICES = "SELECT s.* FROM Services s JOIN ServiceCategories sc ON s.category_id = sc.category_id "+
            "WHERE (sc.category_name LIKE N'%Laundry%' OR sc.category_name LIKE N'%Giặt là%') AND s.is_active = 1 AND s.is_deleted = 0 " +
            "ORDER BY s.service_name";
    private static final String GET_ACTIVE_SERVICES_ID = "SELECT s.* FROM Services s JOIN ServiceCategories sc ON s.category_id = sc.category_id "+
            "WHERE (sc.category_name LIKE N'%Laundry%' OR sc.category_name LIKE N'%Giặt là%') AND s.is_active = 1 AND s.is_deleted = 0 " +
            "AND service_id = ?";
    
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
    
    // Insert new item
    public boolean insertItem(LaundryItem item) {
        try {
            PreparedStatement st = connection.prepareStatement(INSERT_ITEM);
            st.setInt(1, item.getServiceId());
            st.setString(2, item.getItemName());
            st.setString(3, item.getDescription());
            st.setDouble(4, item.getDefaultPrice());
            st.setString(5, item.getUnit());
            st.setBoolean(6, item.getIsActive());
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting item: " + e);
        }
        return false;
    }
    
    // Update item
    public boolean updateItem(LaundryItem item) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_ITEM);
            st.setInt(1, item.getServiceId());
            st.setString(2, item.getItemName());
            st.setString(3, item.getDescription());
            st.setDouble(4, item.getDefaultPrice());
            st.setString(5, item.getUnit());
            st.setBoolean(6, item.getIsActive());
            st.setLong(7, item.getLaundryItemId());
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e);
        }
        return false;
    }
    
    // Activate item
    public boolean activateItem(int itemId) {
        try {
            PreparedStatement st = connection.prepareStatement(ACTIVATE_ITEM);
            st.setLong(1, itemId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error activating item: " + e);
        }
        return false;
    }
    
    // Deactivate item
    public boolean deactivateItem(int itemId) {
        try {
            PreparedStatement st = connection.prepareStatement(DEACTIVATE_ITEM);
            st.setLong(1, itemId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deactivating item: " + e);
        }
        return false;
    }
    
    // Search with filters
    public ArrayList<LaundryItem> search(String search, Integer serviceId, String status, String sort, int pageIndex, int pageSize) {
        ArrayList<LaundryItem> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT li.laundry_item_id, li.service_id, li.item_name, li.description, ")
           .append("li.default_price, li.unit, li.is_active, s.service_name ")
           .append(BASE_SEARCH);
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (li.item_name LIKE ? OR li.description LIKE ?) ");
        }
        
        if (serviceId != null) {
            sql.append(" AND li.service_id = ? ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND li.is_active = ? ");
        }
        
        // Sorting
        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY li.item_name ASC ");
        } else {
            switch (sort) {
                case "nameDesc":
                    sql.append(" ORDER BY li.item_name DESC ");
                    break;
                case "priceAsc":
                    sql.append(" ORDER BY li.default_price ASC ");
                    break;
                case "priceDesc":
                    sql.append(" ORDER BY li.default_price DESC ");
                    break;
                case "serviceAsc":
                    sql.append(" ORDER BY s.service_name ASC ");
                    break;
                case "serviceDesc":
                    sql.append(" ORDER BY s.service_name DESC ");
                    break;
                default:
                    sql.append(" ORDER BY li.item_name ASC ");
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
            }
            
            if (serviceId != null) {
                st.setLong(idx++, serviceId);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                st.setInt(idx++, "true".equals(status) ? 1 : 0);
            }
            
            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);
            
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
            System.out.println("Error searching items: " + e);
        }
        
        return list;
    }
    
    // Count search results
    public int countSearch(String search, Integer serviceId, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_SEARCH);
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (li.item_name LIKE ? OR li.description LIKE ?) ");
        }
        
        if (serviceId != null) {
            sql.append(" AND li.service_id = ? ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND li.is_active = ? ");
        }
        
        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;
            
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }
            
            if (serviceId != null) {
                st.setLong(idx++, serviceId);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                st.setInt(idx++, "true".equals(status) ? 1 : 0);
            }
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting items: " + e);
        }
        
        return 0;
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
    
        // Get active services only
    public ArrayList<Service> getActiveServices() {
        ArrayList<Service> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ACTIVE_SERVICES);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Service service = new Service();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setCategoryId(rs.getInt("category_id"));
                service.setImageUrl(rs.getString("image_url"));
                service.setIsActive(rs.getBoolean("is_active"));
                service.setIsDeleted(rs.getBoolean("is_deleted"));
                list.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Error getting active services: " + e);
        }
        return list;
    }
    
    // Get service by ID
    public Service getServiceById(int serviceId) {
        try {
            PreparedStatement st = connection.prepareStatement(GET_ACTIVE_SERVICES_ID);
            st.setLong(1, serviceId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                Service service = new Service();
                service.setServiceId(rs.getInt("service_id"));
                service.setServiceName(rs.getString("service_name"));
                service.setCategoryId(rs.getInt("category_id"));
                service.setImageUrl(rs.getString("image_url"));
                service.setIsActive(rs.getBoolean("is_active"));
                service.setIsDeleted(rs.getBoolean("is_deleted"));
                return service;
            }
        } catch (SQLException e) {
            System.out.println("Error getting service by ID: " + e);
        }
        return null;
    }
}

