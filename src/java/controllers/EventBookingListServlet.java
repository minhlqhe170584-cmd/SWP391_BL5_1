/*
 * EventBookingListServlet.java
 */
package controllers;

import dao.EventDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.EventRequestView;

@WebServlet(name = "EventBookingListServlet", urlPatterns = {"/admin/event-booking-list"})
public class EventBookingListServlet extends HttpServlet {

    // Helper: Xử lý logic chung cho cả GET và POST
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        EventDAO eventDAO = new EventDAO();
        String action = request.getParameter("action");

        // Nếu không có action thì mặc định là LIST
        if (action == null) {
            action = "LIST";
        }

        try {
            switch (action.toUpperCase()) {
                case "APPROVE":
                    // Xử lý Duyệt đơn
                    int idApprove = Integer.parseInt(request.getParameter("id"));
                    eventDAO.updateEventRequestStatus(idApprove, "ACCEPT");
                    request.getSession().setAttribute("successMessage", "Request #" + idApprove + " has been Approved!");
                    response.sendRedirect("event-booking-list");
                    break;

                case "REJECT":
                    // Xử lý Từ chối đơn
                    int idReject = Integer.parseInt(request.getParameter("id"));
                    eventDAO.updateEventRequestStatus(idReject, "REJECT");
                    request.getSession().setAttribute("successMessage", "Request #" + idReject + " has been Rejected!");
                    response.sendRedirect("event-booking-list");
                    break;

                case "FINISH": // Hoặc COMPLETE
                    // Xử lý Kết thúc sự kiện (đã diễn ra xong)
                    int idFinish = Integer.parseInt(request.getParameter("id"));
                    eventDAO.updateEventRequestStatus(idFinish, "COMPLETED");
                    request.getSession().setAttribute("successMessage", "Event #" + idFinish + " marked as Completed!");
                    response.sendRedirect("event-booking-list");
                    break;

                case "LIST":
                default:
                    // Lấy danh sách hiển thị
                    List<EventRequestView> list = eventDAO.getAllEventRequests();
                    request.setAttribute("eventRequests", list);
                    request.getRequestDispatcher("/WEB-INF/views/event/event-booking-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("event-booking-list?error=true");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Event Booking Management Servlet";
    }
}