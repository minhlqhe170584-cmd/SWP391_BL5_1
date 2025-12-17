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
import java.util.ArrayList;
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

                case "FINISH": 
                    // Xử lý Kết thúc sự kiện
                    int idFinish = Integer.parseInt(request.getParameter("id"));
                    eventDAO.updateEventRequestStatus(idFinish, "COMPLETED");
                    request.getSession().setAttribute("successMessage", "Event #" + idFinish + " marked as Completed!");
                    response.sendRedirect("event-booking-list");
                    break;

                case "LIST":
                default:
                    // --- BẮT ĐẦU LOGIC PHÂN TRANG & SEARCH ---
                    
                    // 1. Lấy tham số từ Form
                    String keyword = request.getParameter("keyword");
                    String status = request.getParameter("status");
                    String indexPage = request.getParameter("index");
                    
                    if (indexPage == null) indexPage = "1";
                    int index = Integer.parseInt(indexPage);

                    // 2. Gọi DAO lấy danh sách ĐẦY ĐỦ theo điều kiện tìm kiếm (chưa cắt trang)
                    // Lưu ý: Đảm bảo bạn đã thêm hàm searchEventRequests vào EventDAO như hướng dẫn
                    List<EventRequestView> fullList = eventDAO.searchEventRequests(keyword, status);

                    // 3. Thuật toán Phân trang (Cắt list)
                    int count = fullList.size();
                    int pageSize = 5; // Số dòng trên mỗi trang
                    int endPage = count / pageSize;
                    if (count % pageSize != 0) {
                        endPage++;
                    }

                    int start = (index - 1) * pageSize;
                    int end = Math.min(start + pageSize, count);

                    List<EventRequestView> pagedList = new ArrayList<>();
                    if (start < count) {
                        pagedList = fullList.subList(start, end);
                    }

                    // 4. Đẩy dữ liệu ra JSP
                    request.setAttribute("eventRequests", pagedList); // List đã cắt
                    request.setAttribute("endPage", endPage);
                    request.setAttribute("tag", index); // Trang hiện tại
                    
                    // 5. Giữ lại giá trị Search/Filter để hiện trên Form
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("status", status);

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