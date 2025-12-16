package controllers;

import dao.BookingCheckInDAO;
import dao.RoomDAO;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Booking;

/**
 * Dashboard tổng hợp cho lễ tân:
 * - Danh sách booking cần CHECK-IN trong ngày
 * - Danh sách booking cần CHECK-OUT trong ngày
 *
 * URL: /admin/frontdesk
 */
@WebServlet(name = "FrontDeskBookingServlet", urlPatterns = {"/admin/frontdesk"})
public class FrontDeskBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        BookingCheckInDAO bookingDao = new BookingCheckInDAO();
        RoomDAO roomDao = new RoomDAO();

        try {
            String dateStr = request.getParameter("date"); // yyyy-MM-dd
            String view = request.getParameter("view");    // checkin | checkout
            String search = request.getParameter("search");
            if (view == null || view.trim().isEmpty()) {
                view = "checkin";
            }
            Date sqlDate;
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                sqlDate = Date.valueOf(dateStr.trim());
            } else {
                sqlDate = Date.valueOf(LocalDate.now());
            }

            List<Booking> rawList;
            if ("checkout".equalsIgnoreCase(view)) {
                rawList = bookingDao.getCheckOutBookingsByDate(sqlDate);
            } else {
                rawList = bookingDao.getCheckInBookingsByDate(sqlDate);
            }

            // Lọc theo search (mã, tên khách, phòng)
            List<Booking> bookingList = new java.util.ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                String key = search.trim().toLowerCase();
                for (Booking b : rawList) {
                    String roomNumber = b.getRoom() != null && b.getRoom().getRoomNumber() != null
                            ? b.getRoom().getRoomNumber().toLowerCase() : "";
                    String customerName = b.getCustomer() != null && b.getCustomer().getFullName() != null
                            ? b.getCustomer().getFullName().toLowerCase() : "";
                    String bookingCode = b.getBookingCode() != null ? b.getBookingCode().toLowerCase() : "";

                    if (roomNumber.contains(key) || customerName.contains(key) || bookingCode.contains(key)) {
                        bookingList.add(b);
                    }
                }
            } else {
                bookingList = rawList;
            }

            // Stats cards
            int availableCount = roomDao.countAvailableRooms();
            int reservedCount = roomDao.countReservedRooms();
            int occupiedCount = roomDao.countOccupiedRooms();

            request.setAttribute("selectedDate", sqlDate.toString());
            request.setAttribute("view", view);
            request.setAttribute("search", search);
            request.setAttribute("bookingList", bookingList);
            request.setAttribute("availableCount", availableCount);
            request.setAttribute("reservedCount", reservedCount);
            request.setAttribute("occupiedCount", occupiedCount);

            request.getRequestDispatcher("/WEB-INF/views/booking/frontdesk.jsp")
                    .forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống.");
        }
    }
}


