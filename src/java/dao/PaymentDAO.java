package dao;

import dbContext.DBContext;
import models.PaymentDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DBContext {

    /**
     * 1. Tìm Booking ID đang hoạt động dựa trên Số Phòng
     * Logic: Tìm booking của phòng này mà trạng thái chưa Check-out hoặc Cancelled
     */
    public int getActiveBookingIdByRoom(String roomNumber) {
        String sql = "SELECT TOP 1 b.booking_id " +
                     "FROM Bookings b " +
                     "JOIN Rooms r ON b.room_id = r.room_id " +
                     "WHERE r.room_number = ? " +
                     "AND b.status NOT IN ('CheckedOut', 'Cancelled') " + 
                     "ORDER BY b.booking_id DESC"; 
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, roomNumber);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("booking_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Không tìm thấy
    }

    /**
     * 2. Lấy toàn bộ thông tin thanh toán (Room + Dịch vụ chi tiết)
     * - Tiền phòng: Lấy từ cột total_amount trong bảng Bookings (Giá gốc).
     * - Tiền dịch vụ: Join bảng OrderDetails để lấy tên món, số lượng, đơn giá.
     */
    public PaymentDTO getPaymentInfo(int bookingId) {
        PaymentDTO dto = new PaymentDTO();
        double totalService = 0;
        double totalRoom = 0;

        // A. Lấy Tiền Phòng (Từ bảng Bookings)
        String sqlRoom = "SELECT total_amount FROM Bookings WHERE booking_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sqlRoom);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // Lấy giá trị từ cột total_amount trong bảng Bookings
                totalRoom = rs.getDouble("total_amount");
                
                dto.setRoomTotalAmount(totalRoom);
                dto.setRoomNote("Tiền phòng (Theo Booking #" + bookingId + ")");
                dto.setRoomInvoiceId(0); // Chưa tạo Invoice thì để tạm là 0
            }
        } catch (Exception e) { e.printStackTrace(); }

        // B. Lấy Chi Tiết Dịch Vụ (Từ OrderDetails)
        // Logic: Lấy các đơn hàng thuộc phòng của Booking này và được tạo SAU khi check-in
        List<PaymentDTO.ServiceDetail> listDetails = new ArrayList<>();
        
        String sqlService = 
            "SELECT s.service_name, od.item_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS subtotal, so.order_date " +
            "FROM OrderDetails od " +
            "JOIN ServiceOrders so ON od.order_id = so.order_id " +
            "JOIN Services s ON od.service_id = s.service_id " +
            "JOIN Bookings b ON b.room_id = so.room_id " +
            "WHERE b.booking_id = ? " +
            "AND so.order_date >= b.check_in_date " + 
            "ORDER BY so.order_date DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sqlService);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String svcName = rs.getString("service_name");
                String itemName = rs.getString("item_name");
                
                // Nếu item_name null (ví dụ dịch vụ chung) thì lấy tên service
                if (itemName == null || itemName.isEmpty()) itemName = svcName; 

                double sub = rs.getDouble("subtotal");
                totalService += sub; // Cộng dồn tổng tiền dịch vụ

                listDetails.add(new PaymentDTO.ServiceDetail(
                    svcName,
                    itemName,
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price"),
                    sub,
                    rs.getTimestamp("order_date")
                ));
            }
            dto.setListServiceDetails(listDetails);
        } catch (Exception e) { e.printStackTrace(); }

        // Tổng cộng cuối cùng = Tiền Phòng + Tổng Tiền Dịch Vụ
        dto.setGrandTotal(totalRoom + totalService);
        return dto;
    }

    /**
     * 3. Xác nhận thu tiền
     * - Tạo hoặc cập nhật Invoice chính thức.
     * - Ghi vào bảng Transactions.
     * - Cập nhật trạng thái các đơn dịch vụ thành 'Paid'.
     */
    public void confirmPayment(int bookingId, int methodId, double amount) {
        try {
            // Mapping ID phương thức sang tên (để lưu vào bảng Invoice cũ nếu cần text)
            String methodName = (methodId == 1) ? "Cash" : (methodId == 2 ? "Card" : "Transfer");

            // BƯỚC 1: Xử lý bảng Invoices (Hóa đơn tổng)
            String checkSql = "SELECT invoice_id FROM Invoices WHERE booking_id = ?";
            PreparedStatement checkSt = connection.prepareStatement(checkSql);
            checkSt.setInt(1, bookingId);
            ResultSet rs = checkSt.executeQuery();
            
            if (rs.next()) {
                // Đã có Invoice -> Update số tiền thực thu
                String updateSql = "UPDATE Invoices SET total_amount = ?, payment_method = ?, created_at = GETDATE() WHERE booking_id = ?";
                PreparedStatement upSt = connection.prepareStatement(updateSql);
                upSt.setDouble(1, amount);
                upSt.setString(2, methodName);
                upSt.setInt(3, bookingId);
                upSt.executeUpdate();
            } else {
                // Chưa có Invoice -> Insert mới (Lấy dữ liệu gốc từ Booking)
                String insertSql = "INSERT INTO Invoices (booking_id, total_amount, payment_method, created_at, note) " +
                                   "SELECT booking_id, total_amount, ?, GETDATE(), N'Đã thanh toán' " +
                                   "FROM Bookings WHERE booking_id = ?";
                PreparedStatement inSt = connection.prepareStatement(insertSql);
                inSt.setString(1, methodName);
                inSt.setInt(2, bookingId);
                inSt.executeUpdate();
            }

            // BƯỚC 2: Ghi vào bảng Transactions (Lịch sử dòng tiền)
            String transSql = "INSERT INTO Transactions (booking_id, method_id, amount) VALUES (?, ?, ?)";
            PreparedStatement transSt = connection.prepareStatement(transSql);
            transSt.setInt(1, bookingId);
            transSt.setInt(2, methodId);
            transSt.setDouble(3, amount);
            transSt.executeUpdate();
            
            // BƯỚC 3: Cập nhật trạng thái các đơn dịch vụ (ServiceInvoices) thành 'Paid'
            // Tìm tất cả Order của Booking này để update
            String updateServiceSql = "UPDATE ServiceInvoices SET status = 'Paid' " +
                                      "WHERE order_id IN ( " +
                                          "SELECT so.order_id FROM ServiceOrders so " +
                                          "JOIN Bookings b ON so.room_id = b.room_id " +
                                          "WHERE b.booking_id = ? AND so.order_date >= b.check_in_date " +
                                      ")";
            PreparedStatement servSt = connection.prepareStatement(updateServiceSql);
            servSt.setInt(1, bookingId);
            servSt.executeUpdate();

        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}