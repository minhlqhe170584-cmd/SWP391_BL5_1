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

    // doGet giữ nguyên như cũ...
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String roomIdParam = request.getParameter("roomId");

        if (roomIdParam != null && !roomIdParam.isEmpty()) {
            handleClientView(request, response, roomIdParam);
            return;
        }

        if (action == null) action = "list";
        if(action.equals("list")) response.sendRedirect("home");
    }

    // handleClientView giữ nguyên như cũ...
    private void handleClientView(HttpServletRequest request, HttpServletResponse response, String roomIdParam) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("USER") == null) {
            session.setAttribute("redirectUrl", "booking?roomId=" + roomIdParam);
            response.sendRedirect("login"); 
            return;
        }

        try {
            int roomId = Integer.parseInt(roomIdParam);
            Room room = bookingRoomDAO.getRoomForBooking(roomId);
            
            if (room != null) {
                request.setAttribute("room", room);
                request.getRequestDispatcher("/WEB-INF/views/booking/booking-form.jsp").forward(request, response);
            } else {
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
            if ("client_booking".equals(action)) {
                
                Customer customer = (Customer) session.getAttribute("USER");
                if (customer == null) {
                    response.sendRedirect("login");
                    return;
                }

                int roomId = Integer.parseInt(request.getParameter("roomId"));
                Date checkIn = Date.valueOf(request.getParameter("checkIn"));
                Date checkOut = Date.valueOf(request.getParameter("checkOut"));

                // --- 1. KIỂM TRA LOGIC NGÀY THÁNG ---
                // Ngày check-out phải sau ngày check-in
                if (!checkOut.after(checkIn)) {
                    request.setAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng!");
                    // Load lại form để khách nhập lại
                    handleClientView(request, response, String.valueOf(roomId));
                    return;
                }

                // --- 2. KIỂM TRA TRÙNG LỊCH (QUAN TRỌNG) ---
                boolean isAvailable = bookingDAO.checkAvailability(roomId, checkIn, checkOut);
                if (!isAvailable) {
                    request.setAttribute("error", "Rất tiếc, phòng này đã có người đặt trong khoảng thời gian bạn chọn. Vui lòng chọn ngày khác!");
                    // Load lại form để khách nhập lại ngày khác
                    handleClientView(request, response, String.valueOf(roomId));
                    return;
                }

                // --- 3. NẾU HỢP LỆ THÌ MỚI TẠO ĐƠN ---
                Booking b = new Booking();
                b.setCustomerId(customer.getCustomerId());
                b.setRoomId(roomId);
                b.setCheckInDate(checkIn);
                b.setCheckOutDate(checkOut);
                
                b.setBookingCode("BK-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
                b.setStatus("Pending");
                b.setTotalAmount(0); 

                if (bookingDAO.addBooking(b)) {
                    session.setAttribute("bookingSuccess", "Đặt phòng thành công! Mã đơn: " + b.getBookingCode());
                    response.sendRedirect("listRooms");
                } else {
                    request.setAttribute("error", "Lỗi hệ thống khi lưu đơn. Vui lòng thử lại.");
                    handleClientView(request, response, String.valueOf(roomId));
                }
            } else {
                response.sendRedirect("booking");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi định dạng ngày tháng hoặc lỗi khác
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ: " + e.getMessage());
            // Cố gắng lấy lại roomId từ request để reload trang
            String roomIdRetry = request.getParameter("roomId");
            if(roomIdRetry != null) {
                 handleClientView(request, response, roomIdRetry);
            } else {
                 response.sendRedirect("listRooms");
            }
        }
    }
}