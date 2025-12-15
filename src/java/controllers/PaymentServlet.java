package controllers;

import dao.PaymentDAO;
import models.PaymentDTO;
import models.User; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/receptionist/payment"})
public class PaymentServlet extends HttpServlet {

    // GET: Giữ nguyên không đổi
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String roomNumber = request.getParameter("roomNumber");
        
        if (roomNumber != null && !roomNumber.trim().isEmpty()) {
            PaymentDAO dao = new PaymentDAO();
            int bookingId = dao.getActiveBookingIdByRoom(roomNumber);
            
            if (bookingId != -1) {
                PaymentDTO bill = dao.getPaymentInfo(bookingId);
                request.setAttribute("bill", bill);
                request.setAttribute("currBookingId", bookingId);
                request.setAttribute("currRoom", roomNumber);
            } else {
                request.setAttribute("error", "Phòng " + roomNumber + " hiện đang trống hoặc không tồn tại!");
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/receptionist/payment.jsp").forward(request, response);
    }

    // POST: ĐÃ SỬA LẠI ĐỂ KHỚP VỚI DAO
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Lấy dữ liệu từ Form
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String methodStr = request.getParameter("paymentMethod");
            
            // Lấy tổng tiền từ input ẩn (Lưu ý: Input này phải có trong JSP)
            String amountStr = request.getParameter("totalAmount");
            double totalAmount = (amountStr != null) ? Double.parseDouble(amountStr) : 0;

            // 2. Chuyển đổi Payment Method từ String sang ID (để khớp với Database mới)
            int methodId = 1; // Mặc định 1 = Cash
            if ("Card".equalsIgnoreCase(methodStr)) {
                methodId = 2;
            } else if ("Transfer".equalsIgnoreCase(methodStr) || "Banking".equalsIgnoreCase(methodStr)) {
                methodId = 3;
            } else if ("Momo".equalsIgnoreCase(methodStr)) {
                methodId = 4;
            } else if ("VNPAY".equalsIgnoreCase(methodStr)) {
                methodId = 5;
            }

            // 3. Gọi DAO thực hiện thanh toán
            // Hàm confirmPayment giờ nhận: (bookingId, methodId, amount)
            // Đã bỏ staffId như yêu cầu trước đó
            PaymentDAO dao = new PaymentDAO();
            dao.confirmPayment(bookingId, methodId, totalAmount);
            
            request.setAttribute("message", "Đã cập nhật thanh toán thành công!");
            
            // Load lại trang để reset
            request.getRequestDispatcher("/WEB-INF/views/receptionist/payment.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/receptionist/payment?error=1");
        }
    }
}