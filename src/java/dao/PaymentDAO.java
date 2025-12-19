package dao;

import dbContext.DBContext;
import models.PaymentDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DBContext {

    // 1. Tìm Booking ID đang hoạt động (hoặc đã checkout)
    public int getActiveBookingIdByRoom(String roomNumber) {
        String sql = "SELECT TOP 1 b.booking_id " +
                     "FROM Bookings b " +
                     "JOIN Rooms r ON b.room_id = r.room_id " +
                     "WHERE r.room_number = ? " +
                     "AND b.status NOT IN ('Cancelled') " + 
                     "ORDER BY b.booking_id DESC"; 
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, roomNumber);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("booking_id");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }
    
    // 1.1. Lấy Booking ID đã checkout theo bookingId
    public int getCheckedOutBookingById(int bookingId) {
        String sql = "SELECT booking_id FROM Bookings WHERE booking_id = ? AND status = 'CheckedOut'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt("booking_id");
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 2. Lấy thông tin thanh toán (Kèm kiểm tra Đã Thanh Toán chưa)
    public PaymentDTO getPaymentInfo(int bookingId) {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(bookingId);
        double totalService = 0;
        double totalRoom = 0;
        double totalServiceInvoice = 0;

        // A. LẤY THÔNG TIN BOOKING
        String sqlBooking = "SELECT b.total_amount, b.booking_code, r.room_number, c.full_name " +
                           "FROM Bookings b " +
                           "LEFT JOIN Rooms r ON b.room_id = r.room_id " +
                           "LEFT JOIN Customers c ON b.customer_id = c.customer_id " +
                           "WHERE b.booking_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sqlBooking);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                dto.setRoomNumber(rs.getString("room_number"));
                dto.setCustomerName(rs.getString("full_name"));
                dto.setBookingCode(rs.getString("booking_code"));
                totalRoom = rs.getDouble("total_amount");
            }
        } catch (Exception e) { e.printStackTrace(); }

        // B. KIỂM TRA TRẠNG THÁI THANH TOÁN (Kiểm tra Invoice status và ServiceInvoices status)
        String sqlInvoice = "SELECT total_amount, created_at, note, status FROM Invoices WHERE booking_id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sqlInvoice);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                String invoiceStatus = rs.getString("status");
                if (invoiceStatus != null && invoiceStatus.equals("Paid")) {
                    // CASE 1: ĐÃ THANH TOÁN
                    dto.setPaid(true);
                    dto.setPaymentDate(rs.getTimestamp("created_at"));
                    totalRoom = rs.getDouble("total_amount");
                    dto.setRoomNote("Đã thanh toán lúc: " + rs.getTimestamp("created_at"));
                } else {
                    // CASE 2: CÓ INVOICE NHƯNG CHƯA PAID
                    dto.setPaid(false);
                    dto.setRoomNote("Chưa thanh toán (Invoice: " + invoiceStatus + ")");
                }
            } else {
                // CASE 3: CHƯA CÓ INVOICE
                dto.setPaid(false);
                dto.setRoomNote("Chưa thanh toán");
            }
            dto.setRoomTotalAmount(totalRoom);
        } catch (Exception e) { e.printStackTrace(); }

        // C. LẤY CHI TIẾT DỊCH VỤ (OrderDetails)
        List<PaymentDTO.ServiceDetail> listDetails = new ArrayList<>();
        String sqlService = 
            "SELECT s.service_name, od.item_name, od.quantity, od.unit_price, (od.quantity * od.unit_price) AS subtotal, so.order_date, so.order_id " +
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

        // D. LẤY THÔNG TIN SERVICEINVOICES
        List<PaymentDTO.ServiceInvoiceDetail> listServiceInvoices = new ArrayList<>();
        String sqlServiceInvoice = 
            "SELECT si.invoice_id, si.order_id, si.final_amount, si.status, si.payment_method, si.created_at, " +
            "so.status AS service_order_status, so.order_date " +
            "FROM ServiceInvoices si " +
            "JOIN ServiceOrders so ON si.order_id = so.order_id " +
            "JOIN Bookings b ON b.room_id = so.room_id " +
            "WHERE b.booking_id = ? AND so.order_date >= b.check_in_date " +
            "ORDER BY so.order_date DESC";
        
        try {
            PreparedStatement st = connection.prepareStatement(sqlServiceInvoice);
            st.setInt(1, bookingId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                double finalAmount = rs.getDouble("final_amount");
                totalServiceInvoice += finalAmount;
                
                listServiceInvoices.add(new PaymentDTO.ServiceInvoiceDetail(
                    rs.getInt("invoice_id"),
                    rs.getInt("order_id"),
                    finalAmount,
                    rs.getString("status"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("created_at"),
                    rs.getString("service_order_status"),
                    rs.getTimestamp("order_date")
                ));
            }
            dto.setListServiceInvoices(listServiceInvoices);
        } catch (Exception e) { e.printStackTrace(); }

        // E. KIỂM TRA SERVICEINVOICES - Nếu có ServiceInvoice chưa Paid thì chưa thanh toán hết
        boolean allServiceInvoicesPaid = true;
        for (PaymentDTO.ServiceInvoiceDetail inv : listServiceInvoices) {
            if (inv.getStatus() == null || !inv.getStatus().equals("Paid")) {
                allServiceInvoicesPaid = false;
                break;
            }
        }
        
        // Nếu Invoice đã Paid NHƯNG có ServiceInvoice chưa Paid thì vẫn chưa thanh toán hết
        if (dto.isPaid() && !listServiceInvoices.isEmpty() && !allServiceInvoicesPaid) {
            dto.setPaid(false);
            dto.setRoomNote("Đã thanh toán phòng nhưng còn dịch vụ chưa thanh toán");
        }
        
        // Tính tổng
        if (dto.isPaid() && allServiceInvoicesPaid) {
            dto.setGrandTotal(totalRoom); // Nếu đã trả hết, lấy số tiền trong Invoice
        } else {
            // Nếu chưa trả, tính từ ServiceInvoices (nếu có) hoặc từ OrderDetails
            // Chỉ tính các ServiceInvoice chưa Paid
            double unpaidServiceInvoice = 0;
            for (PaymentDTO.ServiceInvoiceDetail inv : listServiceInvoices) {
                if (inv.getStatus() == null || !inv.getStatus().equals("Paid")) {
                    unpaidServiceInvoice += inv.getFinalAmount();
                }
            }
            
            if (unpaidServiceInvoice > 0) {
                dto.setGrandTotal(totalRoom + unpaidServiceInvoice);
            } else if (totalServiceInvoice > 0) {
                dto.setGrandTotal(totalRoom + totalServiceInvoice);
            } else {
                dto.setGrandTotal(totalRoom + totalService);
            }
        }
        
        return dto;
    }
    
    // 4. Lấy danh sách booking cần thanh toán (đã checkout, chưa thanh toán Invoice)
    public List<PaymentDTO> getPendingPaymentBookings() {
        List<PaymentDTO> list = new ArrayList<>();
        
        // Query lấy các booking đã checkout nhưng chưa thanh toán
        // Điều kiện:
        // 1. Đã checkout (real_check_out IS NOT NULL)
        // 2. Invoice chưa có hoặc status != 'Paid'
        String sql = "SELECT DISTINCT b.booking_id, b.booking_code, r.room_number, c.full_name, " +
                     "b.total_amount, b.real_check_out " +
                     "FROM Bookings b " +
                     "LEFT JOIN Rooms r ON b.room_id = r.room_id " +
                     "LEFT JOIN Customers c ON b.customer_id = c.customer_id " +
                     "LEFT JOIN Invoices inv ON b.booking_id = inv.booking_id " +
                     "WHERE b.real_check_out IS NOT NULL " +
                     "AND b.status = 'CheckedOut' " +
                     "AND (inv.invoice_id IS NULL OR inv.status IS NULL OR inv.status != 'Paid') " +
                     "ORDER BY b.real_check_out DESC";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                PaymentDTO dto = new PaymentDTO();
                dto.setBookingId(rs.getInt("booking_id"));
                dto.setBookingCode(rs.getString("booking_code"));
                dto.setRoomNumber(rs.getString("room_number"));
                dto.setCustomerName(rs.getString("full_name"));
                dto.setRoomTotalAmount(rs.getDouble("total_amount"));
                dto.setPaid(false);
                
                // Tính tổng dịch vụ chưa thanh toán
                int bookingId = rs.getInt("booking_id");
                double totalUnpaidService = 0;
                
                // Tính từ ServiceInvoices chưa Paid
                String sqlUnpaidService = 
                    "SELECT COALESCE(SUM(si.final_amount), 0) AS total " +
                    "FROM ServiceInvoices si " +
                    "JOIN ServiceOrders so ON si.order_id = so.order_id " +
                    "JOIN Bookings b ON b.room_id = so.room_id " +
                    "WHERE b.booking_id = ? " +
                    "AND so.order_date >= b.check_in_date " +
                    "AND (si.status IS NULL OR si.status != 'Paid')";
                
                try {
                    PreparedStatement stService = connection.prepareStatement(sqlUnpaidService);
                    stService.setInt(1, bookingId);
                    ResultSet rsService = stService.executeQuery();
                    if (rsService.next()) {
                        totalUnpaidService = rsService.getDouble("total");
                    }
                } catch (Exception e) { e.printStackTrace(); }
                
                // Nếu không có ServiceInvoice, tính từ OrderDetails
                if (totalUnpaidService == 0) {
                    String sqlOrderDetails = 
                        "SELECT COALESCE(SUM(od.quantity * od.unit_price), 0) AS total " +
                        "FROM OrderDetails od " +
                        "JOIN ServiceOrders so ON od.order_id = so.order_id " +
                        "JOIN Bookings b ON b.room_id = so.room_id " +
                        "WHERE b.booking_id = ? " +
                        "AND so.order_date >= b.check_in_date " +
                        "AND NOT EXISTS ( " +
                        "    SELECT 1 FROM ServiceInvoices si WHERE si.order_id = so.order_id " +
                        ")";
                    
                    try {
                        PreparedStatement stOrder = connection.prepareStatement(sqlOrderDetails);
                        stOrder.setInt(1, bookingId);
                        ResultSet rsOrder = stOrder.executeQuery();
                        if (rsOrder.next()) {
                            totalUnpaidService = rsOrder.getDouble("total");
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
                
                dto.setGrandTotal(dto.getRoomTotalAmount() + totalUnpaidService);
                list.add(dto);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        
        return list;
    }

    // 3. Xác nhận thu tiền
    public void confirmPayment(int bookingId, int methodId, double amount) {
        try {
            String methodName = (methodId == 1) ? "Cash" : (methodId == 2 ? "Card" : (methodId == 4 ? "Momo" : "Banking"));

            // Insert vào Invoices (sẽ được dùng nếu chưa có invoice)
            
            // Nếu đã có thì Update (đề phòng click 2 lần)
            String checkSql = "SELECT invoice_id FROM Invoices WHERE booking_id = ?";
            PreparedStatement checkSt = connection.prepareStatement(checkSql);
            checkSt.setInt(1, bookingId);
            
            if (checkSt.executeQuery().next()) {
                String updateSql = "UPDATE Invoices SET total_amount = ?, payment_method = ?, created_at = GETDATE(), status = 'Paid' WHERE booking_id = ?";
                PreparedStatement upSt = connection.prepareStatement(updateSql);
                upSt.setDouble(1, amount);
                upSt.setString(2, methodName);
                upSt.setInt(3, bookingId);
                upSt.executeUpdate();
            } else {
                String insertSqlWithStatus = "INSERT INTO Invoices (booking_id, total_amount, payment_method, created_at, note, status) " +
                               "SELECT booking_id, total_amount, ?, GETDATE(), N'Đã thanh toán', 'Paid' " +
                               "FROM Bookings WHERE booking_id = ?";
                PreparedStatement inSt = connection.prepareStatement(insertSqlWithStatus);
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