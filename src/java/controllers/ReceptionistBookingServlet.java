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

@WebServlet(name = "ReceptionistBookingServlet", urlPatterns = {"/receptionist/bookings"})
public class ReceptionistBookingServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("checkin".equals(action)) {
            // Xử lý Check-in
            int bookingId = Integer.parseInt(request.getParameter("id"));
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            
            if (bookingDAO.checkInBooking(bookingId, roomId)) {
                request.getSession().setAttribute("msgSuccess", "Check-in thành công! Khách đã nhận phòng.");
            } else {
                request.getSession().setAttribute("msgError", "Lỗi hệ thống! Không thể Check-in.");
            }
            response.sendRedirect("bookings");
            return;
        }

        // Mặc định: Hiển thị danh sách
        List<Booking> list = bookingDAO.getAllBookings(); // Bạn có thể viết hàm getAllBookings sắp xếp theo ngày gần nhất
        request.setAttribute("listBooking", list);
        request.getRequestDispatcher("/WEB-INF/views/receptionist/booking-list.jsp").forward(request, response);
    }
}