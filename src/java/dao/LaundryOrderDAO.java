package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.*;
import dbContext.DBContext;

public class LaundryOrderDAO extends DBContext {
    
    // SQL Queries
    private static final String GET_ALL_ORDERS = "SELECT lo.laundry_id, lo.order_id, lo.pickup_time, " +
            "lo.expected_delivery_time, lo.expected_return_time, lo.status, lo.note, " +
            "so.room_id, so.order_date, so.total_amount " +
            "FROM LaundryOrders lo " +
            "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id " +
            "ORDER BY lo.laundry_id DESC";
    
    private static final String GET_ORDER_BY_ID = "SELECT lo.laundry_id, lo.order_id, lo.pickup_time, " +
            "lo.expected_delivery_time, lo.expected_return_time, lo.status, lo.note, " +
            "so.room_id, so.order_date, so.total_amount " +
            "FROM LaundryOrders lo " +
            "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id " +
            "WHERE lo.laundry_id = ?";
    
    private static final String GET_ORDER_DETAILS = "SELECT lod.laundry_id, lod.laundry_item_id, " +
            "lod.quantity, lod.unit_price, lod.subtotal, " +
            "li.item_name, li.description, li.unit, " +
            "s.service_name " +
            "FROM LaundryOrderDetails lod " +
            "LEFT JOIN LaundryItems li ON lod.laundry_item_id = li.laundry_item_id " +
            "LEFT JOIN Services s ON li.service_id = s.service_id " +
            "WHERE lod.laundry_id = ?";
    
    private static final String INSERT_ORDER = "INSERT INTO LaundryOrders(order_id, pickup_time, " +
            "expected_delivery_time, expected_return_time, status, note) VALUES(?,?,?,?,?,?)";
    
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO LaundryOrderDetails(laundry_id, " +
            "laundry_item_id, quantity, unit_price, subtotal) VALUES(?,?,?,?,?)";
    
    private static final String UPDATE_ORDER = "UPDATE LaundryOrders SET pickup_time=?, " +
            "expected_delivery_time=?, expected_return_time=?, status=?, note=? WHERE laundry_id=?";
    
    private static final String UPDATE_ORDER_STATUS = "UPDATE LaundryOrders SET status=? WHERE laundry_id=?";
    
    private static final String DELETE_ORDER_DETAILS = "DELETE FROM LaundryOrderDetails WHERE laundry_id=?";
    
    private static final String DELETE_ORDER = "DELETE FROM LaundryOrders WHERE laundry_id=?";
    
    private static final String BASE_ORDER_SEARCH = "FROM LaundryOrders lo " +
            "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id WHERE 1=1";
    
    // Get all laundry orders
    public ArrayList<LaundryOrder> getAllOrders() {
        ArrayList<LaundryOrder> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_ORDERS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LaundryOrder order = new LaundryOrder();
                order.setLaundryId(rs.getLong("laundry_id"));
                order.setOrderId(rs.getLong("order_id"));
                
                Timestamp pickup = rs.getTimestamp("pickup_time");
                if (pickup != null) order.setPickupTime(pickup.toLocalDateTime());
                
                Timestamp delivery = rs.getTimestamp("expected_delivery_time");
                if (delivery != null) order.setExpectedDeliveryTime(delivery.toLocalDateTime());
                
                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) order.setExpectedReturnTime(returnTime.toLocalDateTime());
                
                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));
                
                // Set ServiceOrder info
                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) serviceOrder.setOrderDate(orderDate);
                serviceOrder.setTotalAmount(rs.getDouble("total_amount"));
                order.setServiceOrder(serviceOrder);
                
                list.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e);
        }
        return list;
    }
    
    // Get order by ID with details
    public LaundryOrder getOrderById(Long laundryId) {
        LaundryOrder order = null;
        try {
            PreparedStatement st = connection.prepareStatement(GET_ORDER_BY_ID);
            st.setLong(1, laundryId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                order = new LaundryOrder();
                order.setLaundryId(rs.getLong("laundry_id"));
                order.setOrderId(rs.getLong("order_id"));
                
                Timestamp pickup = rs.getTimestamp("pickup_time");
                if (pickup != null) order.setPickupTime(pickup.toLocalDateTime());
                
                Timestamp delivery = rs.getTimestamp("expected_delivery_time");
                if (delivery != null) order.setExpectedDeliveryTime(delivery.toLocalDateTime());
                
                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) order.setExpectedReturnTime(returnTime.toLocalDateTime());
                
                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));
                
                // Set ServiceOrder info
                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) serviceOrder.setOrderDate(orderDate);
                serviceOrder.setTotalAmount(rs.getDouble("total_amount"));
                order.setServiceOrder(serviceOrder);
                
                // Get order details
                order.setOrderDetails(getOrderDetails(laundryId));
            }
        } catch (SQLException e) {
            System.out.println("Error getting order by ID: " + e);
        }
        return order;
    }
    
    // Get order details
    public ArrayList<LaundryOrderDetail> getOrderDetails(Long laundryId) {
        ArrayList<LaundryOrderDetail> details = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ORDER_DETAILS);
            st.setLong(1, laundryId);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                LaundryOrderDetail detail = new LaundryOrderDetail();
                detail.setLaundryId(rs.getLong("laundry_id"));
                detail.setLaundryItemId(rs.getLong("laundry_item_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitPrice(rs.getDouble("unit_price"));
                detail.setSubtotal(rs.getDouble("subtotal"));
                
                // Set LaundryItem info
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getLong("laundry_item_id"));
                item.setItemName(rs.getString("item_name"));
                item.setDescription(rs.getString("description"));
                item.setUnit(rs.getString("unit"));
                detail.setLaundryItem(item);
                
                details.add(detail);
            }
        } catch (SQLException e) {
            System.out.println("Error getting order details: " + e);
        }
        return details;
    }
    
    // Insert new order with details
    public Long insertOrder(LaundryOrder order) {
        Long generatedId = null;
        try {
            connection.setAutoCommit(false);
            
            // Insert order
            PreparedStatement st = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            st.setLong(1, order.getOrderId());
            st.setTimestamp(2, order.getPickupTime() != null ? Timestamp.valueOf(order.getPickupTime()) : null);
            st.setTimestamp(3, order.getExpectedDeliveryTime() != null ? Timestamp.valueOf(order.getExpectedDeliveryTime()) : null);
            st.setTimestamp(4, order.getExpectedReturnTime() != null ? Timestamp.valueOf(order.getExpectedReturnTime()) : null);
            st.setString(5, order.getStatus());
            st.setString(6, order.getNote());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getLong(1);
                    
                    // Insert order details
                    if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                        for (LaundryOrderDetail detail : order.getOrderDetails()) {
                            insertOrderDetail(generatedId, detail);
                        }
                    }
                }
            }
            
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error rollback: " + ex);
            }
            System.out.println("Error inserting order: " + e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error setting auto commit: " + e);
            }
        }
        return generatedId;
    }
    
    // Insert order detail
    private boolean insertOrderDetail(Long laundryId, LaundryOrderDetail detail) {
        try {
            PreparedStatement st = connection.prepareStatement(INSERT_ORDER_DETAIL);
            st.setLong(1, laundryId);
            st.setLong(2, detail.getLaundryItemId());
            st.setInt(3, detail.getQuantity());
            st.setDouble(4, detail.getUnitPrice());
            st.setDouble(5, detail.getSubtotal());
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting order detail: " + e);
        }
        return false;
    }
    
    // Update order
    public boolean updateOrder(LaundryOrder order) {
        try {
            connection.setAutoCommit(false);
            
            // Update order
            PreparedStatement st = connection.prepareStatement(UPDATE_ORDER);
            st.setTimestamp(1, order.getPickupTime() != null ? Timestamp.valueOf(order.getPickupTime()) : null);
            st.setTimestamp(2, order.getExpectedDeliveryTime() != null ? Timestamp.valueOf(order.getExpectedDeliveryTime()) : null);
            st.setTimestamp(3, order.getExpectedReturnTime() != null ? Timestamp.valueOf(order.getExpectedReturnTime()) : null);
            st.setString(4, order.getStatus());
            st.setString(5, order.getNote());
            st.setLong(6, order.getLaundryId());
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                // Delete old details
                deleteOrderDetails(order.getLaundryId());
                
                // Insert new details
                if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                    for (LaundryOrderDetail detail : order.getOrderDetails()) {
                        insertOrderDetail(order.getLaundryId(), detail);
                    }
                }
            }
            
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error rollback: " + ex);
            }
            System.out.println("Error updating order: " + e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error setting auto commit: " + e);
            }
        }
        return false;
    }
    
    // Update order status
    public boolean updateOrderStatus(Long laundryId, String status) {
        try {
            PreparedStatement st = connection.prepareStatement(UPDATE_ORDER_STATUS);
            st.setString(1, status);
            st.setLong(2, laundryId);
            
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e);
        }
        return false;
    }
    
    // Delete order details
    private boolean deleteOrderDetails(Long laundryId) {
        try {
            PreparedStatement st = connection.prepareStatement(DELETE_ORDER_DETAILS);
            st.setLong(1, laundryId);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting order details: " + e);
        }
        return false;
    }
    
    // Delete order
    public boolean deleteOrder(Long laundryId) {
        try {
            connection.setAutoCommit(false);
            
            // Delete details first
            deleteOrderDetails(laundryId);
            
            // Delete order
            PreparedStatement st = connection.prepareStatement(DELETE_ORDER);
            st.setLong(1, laundryId);
            int rowsAffected = st.executeUpdate();
            
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error rollback: " + ex);
            }
            System.out.println("Error deleting order: " + e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error setting auto commit: " + e);
            }
        }
        return false;
    }
    
    // Search with filters, sorting, and pagination
    public ArrayList<LaundryOrder> search(String search, String status, String sort, int pageIndex, int pageSize) {
        ArrayList<LaundryOrder> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lo.laundry_id, lo.order_id, lo.pickup_time, lo.expected_delivery_time, ")
           .append("lo.expected_return_time, lo.status, lo.note, so.room_id, so.order_date, so.total_amount ")
           .append(BASE_ORDER_SEARCH);
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(lo.laundry_id AS VARCHAR) LIKE ? OR CAST(lo.order_id AS VARCHAR) LIKE ? ")
               .append("OR CAST(so.room_id AS VARCHAR) LIKE ? OR lo.note LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND lo.status = ? ");
        }
        
        // Sorting
        if (sort == null || sort.isEmpty()) {
            sql.append(" ORDER BY lo.laundry_id DESC ");
        } else {
            switch (sort) {
                case "idAsc":
                    sql.append(" ORDER BY lo.laundry_id ASC ");
                    break;
                case "idDesc":
                    sql.append(" ORDER BY lo.laundry_id DESC ");
                    break;
                case "orderIdAsc":
                    sql.append(" ORDER BY lo.order_id ASC ");
                    break;
                case "orderIdDesc":
                    sql.append(" ORDER BY lo.order_id DESC ");
                    break;
                case "pickupAsc":
                    sql.append(" ORDER BY lo.pickup_time ASC ");
                    break;
                case "pickupDesc":
                    sql.append(" ORDER BY lo.pickup_time DESC ");
                    break;
                case "statusAsc":
                    sql.append(" ORDER BY lo.status ASC ");
                    break;
                case "statusDesc":
                    sql.append(" ORDER BY lo.status DESC ");
                    break;
                default:
                    sql.append(" ORDER BY lo.laundry_id DESC ");
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
                st.setString(idx++, searchPattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                st.setString(idx++, status);
            }
            
            int offset = (pageIndex - 1) * pageSize;
            st.setInt(idx++, offset);
            st.setInt(idx, pageSize);
            
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LaundryOrder order = new LaundryOrder();
                order.setLaundryId(rs.getLong("laundry_id"));
                order.setOrderId(rs.getLong("order_id"));
                
                Timestamp pickup = rs.getTimestamp("pickup_time");
                if (pickup != null) order.setPickupTime(pickup.toLocalDateTime());
                
                Timestamp delivery = rs.getTimestamp("expected_delivery_time");
                if (delivery != null) order.setExpectedDeliveryTime(delivery.toLocalDateTime());
                
                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) order.setExpectedReturnTime(returnTime.toLocalDateTime());
                
                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));
                
                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) serviceOrder.setOrderDate(orderDate);
                serviceOrder.setTotalAmount(rs.getDouble("total_amount"));
                order.setServiceOrder(serviceOrder);
                
                list.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error search orders: " + e);
        }
        
        return list;
    }
    
    // Count search results
    public int countSearch(String search, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_ORDER_SEARCH);
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(lo.laundry_id AS VARCHAR) LIKE ? OR CAST(lo.order_id AS VARCHAR) LIKE ? ")
               .append("OR CAST(so.room_id AS VARCHAR) LIKE ? OR lo.note LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND lo.status = ? ");
        }
        
        try {
            PreparedStatement st = connection.prepareStatement(sql.toString());
            int idx = 1;
            
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
                st.setString(idx++, searchPattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                st.setString(idx++, status);
            }
            
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error count orders: " + e);
        }
        
        return 0;
    }
}