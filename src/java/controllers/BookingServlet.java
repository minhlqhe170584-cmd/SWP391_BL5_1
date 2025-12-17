package controllers;

import dao.BookingDAO;
import dao.BookingRoomDAO; 
import models.Booking;
import models.Customer;
import models.Room;
import java.io.IOException;
import java.sql.Date;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();
    private BookingRoomDAO bookingRoomDAO = new BookingRoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String roomIdParam = request.getParameter("roomId");

        // 1. Khách bấm 'ĐẶT NGAY' -> Hiện Form
        if (roomIdParam != null && !roomIdParam.isEmpty()) {
            handleClientView(request, response, roomIdParam);
            return;
        }

        // 2. Logic Admin (Nếu không dùng thì về Home)
        if (action == null) action = "list";
        if(action.equals("list")) response.sendRedirect("home");
    }

    private void handleClientView(HttpServletRequest request, HttpServletResponse response, String roomIdParam) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // A. Kiểm tra đăng nhập
        if (session.getAttribute("USER") == null) {
            session.setAttribute("redirectUrl", "booking?roomId=" + roomIdParam);
            response.sendRedirect("login"); 
            return;
        }

        // B. Lấy dữ liệu phòng và chuyển sang JSP
        try {
            int roomId = Integer.parseInt(roomIdParam);
            Room room = bookingRoomDAO.getRoomForBooking(roomId);
            
            if (room != null) {
                request.setAttribute("room", room);
                // Đường dẫn chính xác tới file JSP của bạn
                request.getRequestDispatcher("/WEB-INF/views/booking/booking-form.jsp").forward(request, response);
            } else {
                // Không tìm thấy phòng -> Về danh sách
                response.sendRedirect("listRooms");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("listRooms");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            // Xử lý khi khách bấm nút "XÁC NHẬN"
            if ("client_booking".equals(action)) {
                
                Customer customer = (Customer) session.getAttribute("USER");
                if (customer == null) {
                    response.sendRedirect("login");
                    return;
                }

                int roomId = Integer.parseInt(request.getParameter("roomId"));
                Date checkIn = Date.valueOf(request.getParameter("checkIn"));
                Date checkOut = Date.valueOf(request.getParameter("checkOut"));
                // (Đã bỏ phần Note theo yêu cầu)

                Booking b = new Booking();
                b.setCustomerId(customer.getCustomerId());
                b.setRoomId(roomId);
                b.setCheckInDate(checkIn);
                b.setCheckOutDate(checkOut);
                
                // Sinh mã đơn hàng ngẫu nhiên (VD: BK-A1B2)
                b.setBookingCode("BK-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
                b.setStatus("Pending");
                b.setTotalAmount(0); // Tính tiền sau

                if (bookingDAO.addBooking(b)) {
                    session.setAttribute("bookingSuccess", "Đặt phòng thành công! Mã đơn: " + b.getBookingCode());
                    response.sendRedirect("listRooms");
                } else {
                    request.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại.");
                    handleClientView(request, response, String.valueOf(roomId));
                }
            } else {
                response.sendRedirect("booking");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            response.sendRedirect("listRooms");
        }
    }
}