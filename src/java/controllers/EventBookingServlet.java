/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.EventDAO;
import dao.OrderDAO;
import dao.RoomDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Customer;
import models.Event;
import models.EventRequest;
import models.Room;

/**
 *
 * @author My Lap
 */
@WebServlet(name = "EventBookingServlet", urlPatterns = {"/event-booking"})
public class EventBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getAllEventPackages();
        request.setAttribute("events", events);

        String eventIdRaw = request.getParameter("eventId");

        if (eventIdRaw != null) {
            int eventId = Integer.parseInt(eventIdRaw);
            Event event = eventDAO.getEventById(eventId);

            String roomIdsStr = event.getLocation();

            // 1. Lấy danh sách tên phòng 
            dao.RoomDAO roomDAO = new dao.RoomDAO();
            request.setAttribute("roomList", roomDAO.getRoomsByIds(roomIdsStr));
            request.setAttribute("event", event);

            // 2. [MỚI] Lấy danh sách ngày đã kín lịch để chặn
            List<String> bookedRanges = eventDAO.getBookedRanges(roomIdsStr);
            // Chuyển List thành chuỗi JSON mảng: [ {from:..}, {from:..} ]
            String jsonDisable = "[" + String.join(",", bookedRanges) + "]";

            request.setAttribute("disableDates", jsonDisable);
        }

        request.getRequestDispatcher("/WEB-INF/views/event/event-booking.jsp")
                .forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Check login
        Room u = (Room) request.getSession().getAttribute("USER");
        if (u == null) {
            response.sendRedirect("/WEB-INF/views/user/login.jsp");
            return;
        }

        try {
            EventRequest er = new EventRequest();

            er.setEventId(Integer.parseInt(request.getParameter("eventId")));

            String[] roomIds = request.getParameterValues("roomIds");
            if (roomIds == null || roomIds.length == 0) {
                throw new IllegalArgumentException("Chưa chọn phòng");
            }
            er.setRoomIds(String.join(",", roomIds));

            // 2. set customer đã login
            er.setRoomId(u.getRoomId());

            er.setCheckInDate(request.getParameter("checkInDate"));
            er.setCheckOutDate(request.getParameter("checkOutDate"));
            er.setMessage(request.getParameter("message"));
            er.setStatus("PENDING");

            EventDAO eventDAO = new EventDAO();
            boolean success = eventDAO.insertEventRequest(er);
            OrderDAO dbOrder = new OrderDAO();
            int orderId = dbOrder.createOrder(er.getRoomId(), er.getMessage());
            Event event = eventDAO.getEventById(er.getEventId());
            dbOrder.createOrderDetail(orderId, 5, event.getEventName(), 1, Double.parseDouble(event.getPricePerTable().toString()));
            if (!success) {
                throw new Exception("Insert failed");
            }

            request.setAttribute("message", "Booking successful!");
                request.getRequestDispatcher("/WEB-INF/views/client/success_event.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/views/event/event-booking.jsp")
                    .forward(request, response);
        }
    }

}
