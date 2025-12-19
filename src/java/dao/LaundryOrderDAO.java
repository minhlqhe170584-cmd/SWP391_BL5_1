package dao;

import java.sql.*;
import java.util.ArrayList;
import models.*;
import dbContext.DBContext;
import java.time.LocalDateTime;

public class LaundryOrderDAO extends DBContext {

    // SQL Queries
    private static final String GET_ALL_ORDERS = "SELECT lo.laundry_id, lo.order_id, "
            + "lo.expected_pickup_time, lo.expected_return_time, lo.status, lo.note, "
            + "so.room_id, so.order_date, so.total_amount "
            + "FROM LaundryOrders lo "
            + "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id "
            + "WHERE so.status LIKE N'%Pending%' "
            + "ORDER BY lo.laundry_id DESC";

    private static final String GET_ORDER_BY_ID = "SELECT lo.laundry_id, lo.order_id, "
            + "lo.expected_pickup_time, lo.expected_return_time, lo.status, lo.note, "
            + "so.room_id, so.order_date, so.total_amount, so.status AS service_status, r.room_number "
            + "FROM LaundryOrders lo "
            + "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id "
            + "LEFT JOIN Rooms r ON so.room_id = r.room_id "
            + "WHERE lo.laundry_id = ?";

    private static final String GET_ORDER_DETAILS = "SELECT lod.laundry_id, lod.laundry_item_id, "
            + "lod.quantity, lod.unit_price, lod.subtotal, "
            + "li.item_name, li.description, li.unit, "
            + "s.service_name "
            + "FROM LaundryOrderDetails lod "
            + "LEFT JOIN LaundryItems li ON lod.laundry_item_id = li.laundry_item_id "
            + "LEFT JOIN Services s ON li.service_id = s.service_id "
            + "WHERE lod.laundry_id = ?";

    private static final String INSERT_ORDER = "INSERT INTO LaundryOrders(order_id, "
            + "expected_pickup_time, expected_return_time, status, note) VALUES(?,?,?,?,?)";

    private static final String INSERT_ORDER_DETAIL = "INSERT INTO LaundryOrderDetails(laundry_id, "
            + "laundry_item_id, quantity, unit_price) VALUES(?,?,?,?)";

    private static final String UPDATE_ORDER = "UPDATE LaundryOrders SET "
            + "expected_pickup_time=?, expected_return_time=?, status=?, note=? WHERE laundry_id=?";

    private static final String UPDATE_ORDER_STATUS = "UPDATE LaundryOrders SET status=? WHERE laundry_id=?";

    private static final String DELETE_ORDER_DETAILS = "DELETE FROM LaundryOrderDetails WHERE laundry_id=?";

    private static final String DELETE_ORDER = "DELETE FROM LaundryOrders WHERE laundry_id=?";

    private static final String BASE_ORDER_SEARCH = "FROM LaundryOrders lo "
            + "LEFT JOIN ServiceOrders so ON lo.order_id = so.order_id "
            + "LEFT JOIN Rooms r ON so.room_id = r.room_id WHERE 1=1 ";
    
    private static final String CANCEL_SERVICE_ORDER = 
    "UPDATE ServiceOrders SET status = 'Cancelled' " +
    "WHERE order_id = (SELECT order_id FROM LaundryOrders WHERE laundry_id = ?)";

    private static final String CANCEL_LAUNDRY_ORDER = 
    "UPDATE LaundryOrders SET status = 'Cancelled' WHERE laundry_id = ?";

    // Get all laundry orders
    public ArrayList<LaundryOrder> getAllOrders() {
        ArrayList<LaundryOrder> list = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ALL_ORDERS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                LaundryOrder order = new LaundryOrder();
                order.setLaundryId(rs.getInt("laundry_id"));
                order.setOrderId(rs.getInt("order_id"));

                Timestamp delivery = rs.getTimestamp("expected_pickup_time");
                if (delivery != null) {
                    order.setExpectedPickupTime(delivery.toLocalDateTime());
                }

                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) {
                    order.setExpectedReturnTime(returnTime.toLocalDateTime());
                }

                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));

                // Set ServiceOrder info
                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) {
                    serviceOrder.setOrderDate(orderDate);
                }
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
    public LaundryOrder getOrderById(int laundryId) {
        LaundryOrder order = null;
        try {
            PreparedStatement st = connection.prepareStatement(GET_ORDER_BY_ID);
            st.setInt(1, laundryId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                order = new LaundryOrder();
                order.setLaundryId(rs.getInt("laundry_id"));
                order.setOrderId(rs.getInt("order_id"));

                Timestamp delivery = rs.getTimestamp("expected_pickup_time");
                if (delivery != null) {
                    order.setExpectedPickupTime(delivery.toLocalDateTime());
                }

                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) {
                    order.setExpectedReturnTime(returnTime.toLocalDateTime());
                }

                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));
                order.setRoomNumber(rs.getString("room_number"));

                // Set ServiceOrder info
                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) {
                    serviceOrder.setOrderDate(orderDate);
                }
                serviceOrder.setTotalAmount(rs.getDouble("total_amount"));
                serviceOrder.setStatus(rs.getString("service_status"));
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
    public ArrayList<LaundryOrderDetail> getOrderDetails(int laundryId) {
        ArrayList<LaundryOrderDetail> details = new ArrayList<>();
        try {
            PreparedStatement st = connection.prepareStatement(GET_ORDER_DETAILS);
            st.setInt(1, laundryId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                LaundryOrderDetail detail = new LaundryOrderDetail();
                detail.setLaundryId(rs.getInt("laundry_id"));
                detail.setLaundryItemId(rs.getInt("laundry_item_id"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitPrice(rs.getDouble("unit_price"));
                detail.setSubtotal(rs.getDouble("subtotal"));

                // Set LaundryItem info
                LaundryItem item = new LaundryItem();
                item.setLaundryItemId(rs.getInt("laundry_item_id"));
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
    public int insertOrder(LaundryOrder order, int roomId) {
        int generatedId = 0;
        try {
            connection.setAutoCommit(false);

            double totalAmount = 0.0;
            for (LaundryOrderDetail detail : order.getOrderDetails()) {
                totalAmount += detail.getSubtotal();
            }
            String insertServiceOrder = "INSERT INTO ServiceOrders(room_id, order_date, total_amount, status, note) " +
                                       "VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stServiceOrder = connection.prepareStatement(insertServiceOrder, Statement.RETURN_GENERATED_KEYS);
            stServiceOrder.setInt(1, roomId);
            stServiceOrder.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stServiceOrder.setDouble(3, totalAmount);
            stServiceOrder.setString(4, "Pending");
            stServiceOrder.setString(5, order.getNote());
            
            stServiceOrder.executeUpdate();
            ResultSet rsServiceOrder = stServiceOrder.getGeneratedKeys();
            
            Integer orderId = null;
            if (rsServiceOrder.next()) {
                orderId = rsServiceOrder.getInt(1);
            }
            
            // Insert order
            PreparedStatement st = connection.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, orderId);           
            st.setTimestamp(2, order.getExpectedPickupTime() != null ? Timestamp.valueOf(order.getExpectedPickupTime()) : null);
            st.setTimestamp(3, order.getExpectedReturnTime() != null ? Timestamp.valueOf(order.getExpectedReturnTime()) : null);
            st.setString(4, order.getStatus());
            st.setString(5, order.getNote());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);

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
//    private boolean insertOrderDetail(int laundryId, LaundryOrderDetail detail) {
//        try {
//            PreparedStatement st = connection.prepareStatement(INSERT_ORDER_DETAIL);
//            st.setInt(1, laundryId);
//            st.setInt(2, detail.getLaundryItemId());
//            st.setInt(3, detail.getQuantity());
//            st.setDouble(4, detail.getUnitPrice());
//            st.setDouble(5, detail.getSubtotal());
//            
//            int rowsAffected = st.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            System.out.println("Error inserting order detail: " + e);
//        }
//        return false;
//    }
    // Remove try-catch so the parent method handles the rollback
    private void insertOrderDetail(int laundryId, LaundryOrderDetail detail) throws SQLException {
        PreparedStatement st = connection.prepareStatement(INSERT_ORDER_DETAIL);
        st.setInt(1, laundryId);
        st.setInt(2, detail.getLaundryItemId());
        st.setInt(3, detail.getQuantity());
        st.setDouble(4, detail.getUnitPrice());
        st.executeUpdate();
    }

    // Update order
//    public boolean updateOrder(LaundryOrder order) {
//        try {
//            connection.setAutoCommit(false);
//            
//            // Update order
//            PreparedStatement st = connection.prepareStatement(UPDATE_ORDER);
//            st.setTimestamp(1, order.getPickupTime() != null ? Timestamp.valueOf(order.getPickupTime()) : null);
//            st.setTimestamp(2, order.getExpectedPickupTime()!= null ? Timestamp.valueOf(order.getExpectedPickupTime()) : null);
//            st.setTimestamp(3, order.getExpectedReturnTime() != null ? Timestamp.valueOf(order.getExpectedReturnTime()) : null);
//            st.setString(4, order.getStatus());
//            st.setString(5, order.getNote());
//            st.setInt(6, order.getLaundryId());
//            
//            int rowsAffected = st.executeUpdate();
//            
//            if (rowsAffected > 0) {
//                // Delete old details
//                deleteOrderDetails(order.getLaundryId());
//                
//                // Insert new details
//                if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
//                    for (LaundryOrderDetail detail : order.getOrderDetails()) {
//                        insertOrderDetail(order.getLaundryId(), detail);
//                    }
//                }
//            }
//            
//            connection.commit();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                System.out.println("Error rollback: " + ex);
//            }
//            System.out.println("Error updating order: " + e);
//        } finally {
//            try {
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.out.println("Error setting auto commit: " + e);
//            }
//        }
//        return false;
//    }
    public boolean updateOrder(LaundryOrder order) {
        PreparedStatement st = null;
        try {
            connection.setAutoCommit(false);

            // 1. Update Main Order
            st = connection.prepareStatement(UPDATE_ORDER);         
            st.setTimestamp(1, order.getExpectedPickupTime() != null ? Timestamp.valueOf(order.getExpectedPickupTime()) : null);
            st.setTimestamp(2, order.getExpectedReturnTime() != null ? Timestamp.valueOf(order.getExpectedReturnTime()) : null);
            st.setString(3, order.getStatus());
            st.setString(4, order.getNote());
            st.setInt(5, order.getLaundryId());

            int rowsAffected = st.executeUpdate();

            deleteOrderDetails(order.getLaundryId());

            if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                for (LaundryOrderDetail detail : order.getOrderDetails()) {      
                    insertOrderDetail(order.getLaundryId(), detail);
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Error rollback: " + ex);
            }
            System.out.println("Error updating order: " + e);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing/resetting: " + e);
            }
        }
    }

    // Update order status
    public boolean updateOrderStatus(int laundryId, String status) {
        Connection conn = null;
        PreparedStatement stLaundry = null;
        PreparedStatement stService = null;
        PreparedStatement stOrderDetail = null;
        
        try {
            conn = connection;
            conn.setAutoCommit(false);
                      
            String getCurrentInfo = "SELECT lo.status AS current_status, lo.order_id " +
                                   "FROM LaundryOrders lo WHERE lo.laundry_id = ?";
            PreparedStatement stGetInfo = conn.prepareStatement(getCurrentInfo);
            stGetInfo.setInt(1, laundryId);
            ResultSet rsInfo = stGetInfo.executeQuery();
            
            if (!rsInfo.next()) {
                rsInfo.close();
                stGetInfo.close();
                return false;
            }
            
            String currentStatus = rsInfo.getString("current_status");
            int orderId = rsInfo.getInt("order_id");
            rsInfo.close();
            stGetInfo.close();
            
       
            stLaundry = conn.prepareStatement(UPDATE_ORDER_STATUS);
            stLaundry.setString(1, status);
            stLaundry.setInt(2, laundryId);
            stLaundry.executeUpdate();
            
           
            String serviceOrderStatus = "Pending"; 
            
            if ("COMPLETED".equalsIgnoreCase(status)) {
                serviceOrderStatus = "Completed";
            } else if ("CANCELLED".equalsIgnoreCase(status)) {
                serviceOrderStatus = "Cancelled";
            } else if ("PENDING".equalsIgnoreCase(currentStatus) && !"PENDING".equalsIgnoreCase(status)) {              
                serviceOrderStatus = "Confirmed";
            } else {              
                String getServiceStatus = "SELECT status FROM ServiceOrders WHERE order_id = ?";
                PreparedStatement stGetServiceStatus = conn.prepareStatement(getServiceStatus);
                stGetServiceStatus.setInt(1, orderId);
                ResultSet rsServiceStatus = stGetServiceStatus.executeQuery();
                if (rsServiceStatus.next()) {
                    serviceOrderStatus = rsServiceStatus.getString("status");
                }
                rsServiceStatus.close();
                stGetServiceStatus.close();
            }
            
            String updateServiceStatus = "UPDATE ServiceOrders SET status = ? WHERE order_id = ?";
            stService = conn.prepareStatement(updateServiceStatus);
            stService.setString(1, serviceOrderStatus);
            stService.setInt(2, orderId);
            stService.executeUpdate();
            
            if ("Confirmed".equalsIgnoreCase(serviceOrderStatus)) {       
                String checkInvoice = "SELECT COUNT(*) FROM ServiceInvoices WHERE order_id = ?";
                PreparedStatement stCheckInvoice = conn.prepareStatement(checkInvoice);
                stCheckInvoice.setInt(1, orderId);
                ResultSet rsCheckInvoice = stCheckInvoice.executeQuery();
                boolean hasInvoice = false;
                if (rsCheckInvoice.next()) {
                    hasInvoice = rsCheckInvoice.getInt(1) > 0;
                }
                rsCheckInvoice.close();
                stCheckInvoice.close();
                            
                if (!hasInvoice) {  
                    String getTotalAmount = "SELECT total_amount FROM ServiceOrders WHERE order_id = ?";
                    PreparedStatement stGetAmount = conn.prepareStatement(getTotalAmount);
                    stGetAmount.setInt(1, orderId);
                    ResultSet rsAmount = stGetAmount.executeQuery();
                    double finalAmount = 0.0;
                    if (rsAmount.next()) 
                    {
                        finalAmount = rsAmount.getDouble("total_amount");
                    }
                    rsAmount.close();
                    stGetAmount.close();
                    
                    String insertInvoice = "INSERT INTO ServiceInvoices (order_id, created_at, final_amount, status) VALUES (?, GETDATE(), ?, 'Unpaid')";
                    PreparedStatement stInvoice = conn.prepareStatement(insertInvoice);
                    stInvoice.setInt(1, orderId);
                    stInvoice.setDouble(2, finalAmount);
                    stInvoice.executeUpdate();
                    stInvoice.close();
                }
            }
            
            //Create OrderDetails records
            if ("PENDING".equalsIgnoreCase(currentStatus) && !"PENDING".equalsIgnoreCase(status)) {
                String getLaundryDetails = "SELECT lod.laundry_item_id, lod.quantity, lod.unit_price, " +
                                         "li.item_name, li.service_id " +
                                         "FROM LaundryOrderDetails lod " +
                                         "LEFT JOIN LaundryItems li ON lod.laundry_item_id = li.laundry_item_id " +
                                         "WHERE lod.laundry_id = ?";
                PreparedStatement stGetDetails = conn.prepareStatement(getLaundryDetails);
                stGetDetails.setInt(1, laundryId);
                ResultSet rsDetails = stGetDetails.executeQuery();
                
                String checkExisting = "SELECT COUNT(*) FROM OrderDetails WHERE order_id = ?";
                PreparedStatement stCheck = conn.prepareStatement(checkExisting);
                stCheck.setInt(1, orderId);
                ResultSet rsCheck = stCheck.executeQuery();
                boolean hasExisting = false;
                if (rsCheck.next()) {
                    hasExisting = rsCheck.getInt(1) > 0;
                }
                rsCheck.close();
                stCheck.close();
                
                // Only insert if OrderDetails don't exist yet
                if (!hasExisting) {
                    String insertOrderDetail = "INSERT INTO OrderDetails (order_id, service_id, item_name, quantity, unit_price) " +
                                             "VALUES (?, ?, ?, ?, ?)";
                    stOrderDetail = conn.prepareStatement(insertOrderDetail);
                    
                    while (rsDetails.next()) {
                        stOrderDetail.setInt(1, orderId);
                        stOrderDetail.setInt(2, rsDetails.getInt("service_id"));
                        stOrderDetail.setString(3, rsDetails.getString("item_name"));
                        stOrderDetail.setInt(4, rsDetails.getInt("quantity"));
                        stOrderDetail.setDouble(5, rsDetails.getDouble("unit_price"));
                        stOrderDetail.addBatch();
                    }
                    
                    if (stOrderDetail != null) {
                        stOrderDetail.executeBatch();
                    }
                }
                
                if (rsDetails != null) {
                    rsDetails.close();
                }
                if (stGetDetails != null) {
                    stGetDetails.close();
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error rolling back: " + ex);
            }
            System.out.println("Error updating order status: " + e);
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (stLaundry != null) stLaundry.close();
                if (stService != null) stService.close();
                if (stOrderDetail != null) stOrderDetail.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e);
            }
        }
    }

  
    private boolean deleteOrderDetails(int laundryId) {
        try {
            PreparedStatement st = connection.prepareStatement(DELETE_ORDER_DETAILS);
            st.setInt(1, laundryId);
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting order details: " + e);
        }
        return false;
    }

   
    public boolean deleteOrder(int laundryId) {
        try {
            connection.setAutoCommit(false);

            // Delete details first
            deleteOrderDetails(laundryId);

            // Delete order
            PreparedStatement st = connection.prepareStatement(DELETE_ORDER);
            st.setInt(1, laundryId);
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
    
    public boolean cancelOrder(int laundryId) {
    Connection conn = null;
    PreparedStatement stService = null;
    PreparedStatement stLaundry = null;

    try {
        conn = connection; 
        conn.setAutoCommit(false); 

        stService = conn.prepareStatement(CANCEL_SERVICE_ORDER);
        stService.setInt(1, laundryId);
        int rowsService = stService.executeUpdate();

        stLaundry = conn.prepareStatement(CANCEL_LAUNDRY_ORDER);
        stLaundry.setInt(1, laundryId);
        stLaundry.executeUpdate();

        if (rowsService > 0) {
            conn.commit();
            return true;
        } else {
            conn.rollback();
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error canceling order: " + e);
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
            System.out.println("Error rolling back: " + ex);
        }
        return false;
    } finally {
        try {
            if (conn != null) conn.setAutoCommit(true);
            if (stService != null) stService.close();
            if (stLaundry != null) stLaundry.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e);
        }
    }
}

    // Search with filters, sorting, and pagination
    public ArrayList<LaundryOrder> search(String search, String status, String filter, String sort, int pageIndex, int pageSize) {
        ArrayList<LaundryOrder> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lo.laundry_id, lo.order_id, lo.expected_pickup_time, ")
                .append("lo.expected_return_time, lo.status, lo.note, so.room_id, so.order_date, so.total_amount, so.status, r.room_number ")
                .append(BASE_ORDER_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(lo.laundry_id AS VARCHAR) LIKE ? OR CAST(lo.order_id AS VARCHAR) LIKE ? ")
                    .append("OR CAST(so.room_id AS VARCHAR) LIKE ? OR lo.note LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND lo.status = ? ");
        }
        
//        if ("deleted".equals(deleteFilter)) {
//            sql.append(" AND is_deleted = 1 ");
//        } else {
//            sql.append(" AND is_deleted = 0 ");
        
        if(filter == null || filter.isEmpty()){
            sql.append(" AND so.status LIKE N'%Pending%' OR so.status LIKE N'%PENDING%'");
        }else{
            switch (filter){
                case "Canceled":
                    sql.append(" AND so.status LIKE N'%Canceled%' OR so.status LIKE N'%Cancelled%' OR so.status LIKE N'%CANCELLED%' ");
                    break;
                case "Completed":
                    sql.append(" AND so.status LIKE N'%Completed%' OR so.status LIKE N'%Paid%' ");
                    break;
                case "Confirmed":
                    sql.append(" AND so.status LIKE N'%Confirmed%' ");
                    break;
                default:
                    sql.append(" AND so.status LIKE N'%Pending%' OR so.status LIKE N'%PENDING%' ");
                    break;
            }
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
                order.setLaundryId(rs.getInt("laundry_id"));
                order.setOrderId(rs.getInt("order_id"));
                order.setRoomNumber(rs.getString("room_number"));

                Timestamp delivery = rs.getTimestamp("expected_pickup_time");
                if (delivery != null) {
                    order.setExpectedPickupTime(delivery.toLocalDateTime());
                }

                Timestamp returnTime = rs.getTimestamp("expected_return_time");
                if (returnTime != null) {
                    order.setExpectedReturnTime(returnTime.toLocalDateTime());
                }

                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));

                ServiceOrder serviceOrder = new ServiceOrder();
                serviceOrder.setOrderId(rs.getInt("order_id"));
                serviceOrder.setRoomId(rs.getInt("room_id"));
                serviceOrder.setStatus(rs.getString("status"));
                Timestamp orderDate = rs.getTimestamp("order_date");
                if (orderDate != null) {
                    serviceOrder.setOrderDate(orderDate);
                }
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
    public int countSearch(String search, String status, String filter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) ").append(BASE_ORDER_SEARCH);

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (CAST(lo.laundry_id AS VARCHAR) LIKE ? OR CAST(lo.order_id AS VARCHAR) LIKE ? ")
                    .append("OR CAST(so.room_id AS VARCHAR) LIKE ? OR lo.note LIKE ?) ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND lo.status = ? ");
        }

        if(filter == null || filter.isEmpty()){
            sql.append(" AND so.status LIKE N'%Pending%' OR so.status LIKE N'%PENDING%'");
        }else{
            switch (filter){
                case "Canceled":
                    sql.append(" AND so.status LIKE N'%Canceled%' OR so.status LIKE N'%Cancelled%' OR so.status LIKE N'%CANCELLED%' ");
                    break;
                case "Completed":
                    sql.append(" AND so.status LIKE N'%Completed%' OR so.status LIKE N'%Paid%' ");
                    break;
                case "Confirmed":
                    sql.append(" AND so.status LIKE N'%Confirmed%' ");
                    break;
                default:
                    sql.append(" AND so.status LIKE N'%Pending%' OR so.status LIKE N'%PENDING%' ");
                    break;
            }
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
