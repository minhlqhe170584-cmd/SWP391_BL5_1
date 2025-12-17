package controllers;

import dao.BookingDAO;
import models.Booking;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ReceptionistServlet", urlPatterns = {"/receptionist"})
public class ReceptionistServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy danh sách để hiển thị dashboard
        List<Booking> list = bookingDAO.getAllBookings();
        request.setAttribute("listBookings", list);
        
        request.getRequestDispatcher("/WEB-INF/views/receptionist/receptionist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));

            if ("checkin".equals(action)) {
                if (bookingDAO.checkInBooking(bookingId, roomId)) {
                    request.getSession().setAttribute("msg", "Check-in thành công! Phòng đã được cập nhật thành Đang ở.");
                } else {
                    request.getSession().setAttribute("err", "Lỗi Check-in! Vui lòng thử lại.");
                }
            } 
            else if ("checkout".equals(action)) {
                if (bookingDAO.checkOutBooking(bookingId, roomId)) {
                    request.getSession().setAttribute("msg", "Check-out thành công! Phòng đã trống.");
                } else {
                    request.getSession().setAttribute("err", "Lỗi Check-out! Vui lòng thử lại.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Load lại trang Dashboard
        response.sendRedirect("receptionist");
    }
}