package controllers;

import dao.BookingDAO;
import dao.RoomDAO;
import models.Customer;
import models.Room;
import models.Booking;
import utils.EmailUtils;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

    // --- 1. GET: HIỂN THỊ FORM ĐẶT PHÒNG ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("USER");
        String role = (String) session.getAttribute("ROLE");

        // Chặn tài khoản ROOM
        if ("ROOM".equals(role)) {
            response.sendRedirect("listRooms");
            return;
        }

        // Chặn nếu chưa đăng nhập hoặc không phải Customer
        if (userObj == null || !"CUSTOMER".equals(role) || !(userObj instanceof Customer)) {
            String roomId = request.getParameter("roomId");
            if (roomId != null) {
                session.setAttribute("PENDING_ROOM_ID", roomId);
            }
            response.sendRedirect("login");
            return;
        }

        // Hiển thị Form
        try {
            String roomIdStr = request.getParameter("roomId");
            if (roomIdStr == null || roomIdStr.isEmpty()) {
                response.sendRedirect("listRooms");
                return;
            }
            
            int roomId = Integer.parseInt(roomIdStr);
            RoomDAO roomDAO = new RoomDAO();
            Room room = roomDAO.getRoomById(roomId);
            
            if (room != null) {
                request.setAttribute("room", room);
                request.getRequestDispatcher("/WEB-INF/views/room/booking.jsp").forward(request, response);
            } else {
                response.sendRedirect("listRooms");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("listRooms");
        }
    }

    // --- 2. POST: XỬ LÝ ĐẶT PHÒNG ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Object userObj = session.getAttribute("USER");
        String role = (String) session.getAttribute("ROLE");
        
        // Bảo mật lớp 2
        if ("ROOM".equals(role)) {
            response.sendRedirect("listRooms");
            return;
        }
        if (userObj == null || !"CUSTOMER".equals(role) || !(userObj instanceof Customer)) {
            response.sendRedirect("login");
            return;
        }
        
        Customer user = (Customer) userObj;

        try {
            // 1. Nhận dữ liệu
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");

            LocalDate inDate = LocalDate.parse(checkInStr);
            LocalDate outDate = LocalDate.parse(checkOutStr);
            Timestamp checkIn = Timestamp.valueOf(inDate.atStartOfDay());
            Timestamp checkOut = Timestamp.valueOf(outDate.atStartOfDay());

            // 2. Validate
            if (!outDate.isAfter(inDate)) {
                request.setAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng!");
                reloadForm(request, response, roomId);
                return;
            }
            if (inDate.isBefore(LocalDate.now())) {
                request.setAttribute("error", "Không thể đặt phòng trong quá khứ!");
                reloadForm(request, response, roomId);
                return;
            }

            BookingDAO bookingDAO = new BookingDAO();
            if (!bookingDAO.isRoomAvailable(roomId, checkIn, checkOut)) {
                request.setAttribute("error", "Phòng này đã kín lịch trong thời gian bạn chọn.");
                reloadForm(request, response, roomId);
                return;
            }

            // 3. Lưu Booking
            Booking booking = new Booking();
            booking.setCustomerId(user.getCustomerId());
            booking.setRoomId(roomId);
            booking.setCheckInDate(checkIn);
            booking.setCheckOutDate(checkOut);
            
            if (bookingDAO.insertBooking(booking)) {
                
                // === BƯỚC QUAN TRỌNG: Cập nhật trạng thái phòng thành 'Occupied' ===
                RoomDAO roomDAO = new RoomDAO();
                roomDAO.updateRoomStatus(roomId, "Occupied");
                
                // === Gửi Mail ===
                String randomCode = EmailUtils.generateRandomCode(8); 
                String subject = "Xác nhận đặt phòng thành công - Smart Hotel";
                String body = "Xin chào " + user.getFullName() + ",\n\n"
                        + "Đặt phòng thành công!\n"
                        + "Phòng số: " + roomId + "\n"
                        + "Check-in: " + checkInStr + "\n"
                        + "Check-out: " + checkOutStr + "\n\n"
                        + "MÃ VÉ CỦA BẠN: " + randomCode;
                
                new Thread(() -> EmailUtils.sendEmail(user.getEmail(), subject, body)).start();

                // === Thông báo Success & Chuyển trang ===
                request.getSession().setAttribute("bookingSuccess", "Đặt phòng thành công! Mã vé đã được gửi tới email.");
                response.sendRedirect("listRooms"); 
            } else {
                request.setAttribute("error", "Lỗi hệ thống.");
                reloadForm(request, response, roomId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("listRooms");
        }
    }

    private void reloadForm(HttpServletRequest request, HttpServletResponse response, int roomId) 
            throws ServletException, IOException {
        RoomDAO roomDAO = new RoomDAO();
        Room room = roomDAO.getRoomById(roomId);
        request.setAttribute("room", room);
        request.getRequestDispatcher("/WEB-INF/views/room/booking.jsp").forward(request, response);
    }
}