package controllers;

import dao.PaymentDAO;
import models.PaymentDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/receptionist/payment"})
public class PaymentServlet extends HttpServlet {

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
                request.setAttribute("error", "Phòng " + roomNumber + " hiện không có khách!");
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/receptionist/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String methodStr = request.getParameter("paymentMethod");
            String amountStr = request.getParameter("totalAmount");
            double totalAmount = (amountStr != null) ? Double.parseDouble(amountStr) : 0;

            int methodId = 1; // Mặc định Cash
            if ("Card".equals(methodStr)) methodId = 2;
            else if ("Transfer".equals(methodStr)) methodId = 3;
            else if ("Momo".equals(methodStr)) methodId = 4;

            PaymentDAO dao = new PaymentDAO();
            dao.confirmPayment(bookingId, methodId, totalAmount);
            
            request.setAttribute("message", "Thanh toán thành công!");
            // Gọi lại doGet để hiển thị trạng thái "Đã thanh toán"
            doGet(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("payment?error=1");
        }
    }
}