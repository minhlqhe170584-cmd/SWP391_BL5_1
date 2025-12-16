package controllers;

import dao.BookingCheckInDAO;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Booking;

/**
 * Servlet cho nghiệp vụ Check-in / Check-out tại lễ tân.
 *
 * URL:
 *  - GET /admin/checkin    : Tìm kiếm & hiển thị Booking để check-in
 *  - POST /admin/checkin   : Thực hiện check-in (đổi trạng thái + thời gian)
 *  - GET /admin/checkout   : Hiển thị thông tin Booking để checkout
 *  - POST /admin/checkout  : Thực hiện checkout (đổi trạng thái + thời gian + trả phòng)
 */
@WebServlet(name = "CheckInOutServlet", urlPatterns = {"/receptionist/checkin", "/receptionist/checkout"})
public class CheckInOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();
        BookingCheckInDAO dao = new BookingCheckInDAO();

        try {
            if ("/receptionist/checkin".equals(servletPath)) {
                // --- MÀN CHECK-IN ---
                String code = request.getParameter("code");
                String customerName = request.getParameter("customer");
                String checkInDateStr = request.getParameter("checkInDate");

                if (code != null && !code.trim().isEmpty()) {
                    Booking booking = dao.findByBookingCode(code.trim());
                    if (booking == null) {
                        request.setAttribute("error", "Không tìm thấy booking với mã: " + code);
                    } else {
                        request.setAttribute("booking", booking);
                    }
                } else if (customerName != null && !customerName.trim().isEmpty()
                        && checkInDateStr != null && !checkInDateStr.trim().isEmpty()) {
                    Date date = Date.valueOf(checkInDateStr);
                    request.setAttribute("bookingList",
                            dao.searchByCustomerNameAndCheckInDate(customerName, date));
                }

                request.getRequestDispatcher("/WEB-INF/views/booking/checkin.jsp")
                        .forward(request, response);

            } else if ("/receptionist/checkout".equals(servletPath)) {
                // --- MÀN CHECK-OUT ---
                String idStr = request.getParameter("bookingId");
                if (idStr == null || idStr.trim().isEmpty()) {
                    request.setAttribute("error", "Thiếu tham số bookingId.");
                } else {
                    try {
                        int bookingId = Integer.parseInt(idStr);
                        Booking booking = dao.findById(bookingId);
                        if (booking == null) {
                            request.setAttribute("error", "Không tìm thấy booking với ID: " + bookingId);
                        } else {
                            request.setAttribute("booking", booking);
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "bookingId không hợp lệ.");
                    }
                }

                request.getRequestDispatcher("/WEB-INF/views/booking/checkout.jsp")
                        .forward(request, response);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();
        BookingCheckInDAO dao = new BookingCheckInDAO();

        try {
            String idStr = request.getParameter("bookingId");
            if (idStr == null || idStr.trim().isEmpty()) {
                request.setAttribute("error", "Thiếu tham số bookingId.");
                if ("/receptionist/checkin".equals(servletPath)) {
                    request.getRequestDispatcher("/WEB-INF/views/booking/checkin.jsp")
                            .forward(request, response);
                } else {
                    request.getRequestDispatcher("/WEB-INF/views/booking/checkout.jsp")
                            .forward(request, response);
                }
                return;
            }

            int bookingId = Integer.parseInt(idStr);

            if ("/admin/checkin".equals(servletPath)) {
                // Thực hiện CHECK-IN (status = CheckedIn, check_in_date = GETDATE())
                boolean ok = dao.doCheckInWithNow(bookingId);
                if (ok) {
                    request.getSession().setAttribute("successMessage", "Check-in thành công!");
                } else {
                    request.getSession().setAttribute("errorMessage",
                            "Không thể check-in. Booking phải đang ở trạng thái Confirmed.");
                }
                response.sendRedirect(request.getContextPath() + "/receptionist/frontdesk?view=checkin");

            } else if ("/admin/checkout".equals(servletPath)) {
                // Thực hiện CHECK-OUT (status = CheckedOut, check_out_date = GETDATE(), trả phòng)
                boolean ok = dao.doCheckOutAndReleaseRoom(bookingId);
                if (ok) {
                    request.getSession().setAttribute("successMessage", "Check-out thành công, phòng đã được trả!");
                } else {
                    request.getSession().setAttribute("errorMessage",
                            "Không thể check-out. Booking phải đang ở trạng thái CheckedIn.");
                }
                response.sendRedirect(request.getContextPath() + "/receptionist/frontdesk?view=checkout");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "bookingId không hợp lệ.");
            if ("/admin/checkin".equals(servletPath)) {
                request.getRequestDispatcher("/WEB-INF/views/booking/checkin.jsp")
                        .forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/booking/checkout.jsp")
                        .forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống.");
        }
    }
}


