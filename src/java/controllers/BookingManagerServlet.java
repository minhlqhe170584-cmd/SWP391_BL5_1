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

@WebServlet(name = "BookingManagerServlet", urlPatterns = {"/booking-manager"})
public class BookingManagerServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Kiểm tra đăng nhập
        if (session.getAttribute("USER") == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        if (action.equals("list")) {
            // Lấy toàn bộ danh sách đơn đặt phòng (Sắp xếp mới nhất lên đầu)
            List<Booking> list = bookingDAO.getAllBookings();
            request.setAttribute("bookings", list);
            request.getRequestDispatcher("/WEB-INF/views/booking/booking-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // Xử lý Hủy Đơn (Chỉ cho phép hủy khi trạng thái là Pending)
        if ("cancel".equals(action)) {
            try {
                int bookingId = Integer.parseInt(request.getParameter("bookingId"));
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                
                // Gọi DAO hủy đơn
                if (bookingDAO.cancelBooking(bookingId, roomId)) {
                    request.getSession().setAttribute("msg", "Đã hủy đơn đặt phòng thành công!");
                } else {
                    request.getSession().setAttribute("err", "Lỗi khi hủy đơn! Vui lòng thử lại.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("err", "Lỗi dữ liệu: " + e.getMessage());
            }
            response.sendRedirect("booking-manager");
        }
    }
}