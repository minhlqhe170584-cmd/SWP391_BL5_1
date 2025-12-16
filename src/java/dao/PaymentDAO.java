package dao;

import dbContext.DBContext;
import models.PaymentDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DBContext {

    // 1. Tìm Booking ID đang hoạt động
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
            if (rs.next()) return rs.getInt("booking_id");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 2. Lấy thông tin thanh toán (Kèm kiểm tra Đã Thanh Toán chưa)
    public PaymentDTO getPaymentInfo(int bookingId) {
        PaymentDTO dto = new PaymentDTO();
        double totalService = 0;
        double totalRoom = 0;

        // A. KIỂM TRA TRẠNG THÁI THANH TOÁN (Ưu tiên check Invoices)
        String sqlInvoice = "SELECT total_amount, created_at, note FROM Invoices WHERE booking_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sqlInvoice);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                // CASE 1: ĐÃ THANH TOÁN
                dto.setPaid(true);
                dto.setPaymentDate(rs.getTimestamp("created_at"));
                totalRoom = rs.getDouble("total_amount");
                dto.setRoomNote("Đã thanh toán lúc: " + rs.getTimestamp("created_at"));
            } else {
                // CASE 2: CHƯA THANH TOÁN (Lấy giá gốc từ Booking)
                dto.setPaid(false);
                String sqlBooking = "SELECT total_amount FROM Bookings WHERE booking_id = ?";
                PreparedStatement st2 = connection.prepareStatement(sqlBooking);
                st2.setInt(1, bookingId);
                ResultSet rs2 = st2.executeQuery();
                if (rs2.next()) {
                    totalRoom = rs2.getDouble("total_amount");
                    dto.setRoomNote("Chưa thanh toán");
                }
            }
            dto.setRoomTotalAmount(totalRoom);
        } catch (Exception e) { e.printStackTrace(); }

        // B. LẤY CHI TIẾT DỊCH VỤ
        List<PaymentDTO.ServiceDetail> listDetails = new ArrayList<>();
        String sqlService = 
            "SELECT s.service_name, od.item_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS subtotal, so.order_date " +
            "FROM OrderDetails od " +
            "JOIN ServiceOrders so ON od.order_id = so.order_id " +
            "JOIN Services s ON od.service_id = s.service_id " +
            "JOIN Bookings b ON b.room_id = so.room_id " +
            "WHERE b.booking_id = ? AND so.order_date >= b.check_in_date " + 
            "ORDER BY so.order_date DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sqlService);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String svcName = rs.getString("service_name");
                String itemName = rs.getString("item_name");
                if (itemName == null || itemName.isEmpty()) itemName = svcName; 

                double sub = rs.getDouble("subtotal");
                totalService += sub;

                listDetails.add(new PaymentDTO.ServiceDetail(
                    svcName, itemName, rs.getInt("quantity"), rs.getDouble("unit_price"), sub, rs.getTimestamp("order_date")
                ));
            }
            dto.setListServiceDetails(listDetails);
        } catch (Exception e) { e.printStackTrace(); }

        // Tính tổng
        if (dto.isPaid()) {
            dto.setGrandTotal(totalRoom); // Nếu đã trả, lấy số tiền trong Invoice
        } else {
            dto.setGrandTotal(totalRoom + totalService); // Nếu chưa, cộng dồn lại
        }
        
        return dto;
    }

    // 3. Xác nhận thu tiền
    public void confirmPayment(int bookingId, int methodId, double amount) {
        try {
            String methodName = (methodId == 1) ? "Cash" : (methodId == 2 ? "Card" : (methodId == 4 ? "Momo" : "Banking"));

            // Insert vào Invoices
            String insertSql = "INSERT INTO Invoices (booking_id, total_amount, payment_method, created_at, note) " +
                               "SELECT booking_id, total_amount, ?, GETDATE(), N'Đã thanh toán' " +
                               "FROM Bookings WHERE booking_id = ?";
            
            // Nếu đã có thì Update (đề phòng click 2 lần)
            String checkSql = "SELECT invoice_id FROM Invoices WHERE booking_id = ?";
            PreparedStatement checkSt = connection.prepareStatement(checkSql);
            checkSt.setInt(1, bookingId);
            
            if (checkSt.executeQuery().next()) {
                String updateSql = "UPDATE Invoices SET total_amount = ?, payment_method = ?, created_at = GETDATE() WHERE booking_id = ?";
                PreparedStatement upSt = connection.prepareStatement(updateSql);
                upSt.setDouble(1, amount);
                upSt.setString(2, methodName);
                upSt.setInt(3, bookingId);
                upSt.executeUpdate();
            } else {
                PreparedStatement inSt = connection.prepareStatement(insertSql);
                inSt.setString(1, methodName);
                inSt.setInt(2, bookingId);
                inSt.executeUpdate();
            }

            // Ghi Transaction
            String transSql = "INSERT INTO Transactions (booking_id, method_id, amount) VALUES (?, ?, ?)";
            PreparedStatement transSt = connection.prepareStatement(transSql);
            transSt.setInt(1, bookingId);
            transSt.setInt(2, methodId);
            transSt.setDouble(3, amount);
            transSt.executeUpdate();

            // Cập nhật Service thành Paid
            String updateSvc = "UPDATE ServiceInvoices SET status = 'Paid' WHERE order_id IN " +
                               "(SELECT so.order_id FROM ServiceOrders so JOIN Bookings b ON so.room_id = b.room_id " +
                               "WHERE b.booking_id = ? AND so.order_date >= b.check_in_date)";
            PreparedStatement svcSt = connection.prepareStatement(updateSvc);
            svcSt.setInt(1, bookingId);
            svcSt.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }
    
}