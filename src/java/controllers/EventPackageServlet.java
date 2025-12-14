/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
/*
 * EventPackageServlet.java
 */
package controllers;

import dao.EventDAO;
import models.Event;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "EventPackageServlet", urlPatterns = {"/admin/event-packages"})
public class EventPackageServlet extends HttpServlet {

    private EventDAO eventDAO;

    @Override
    public void init() throws ServletException {
        eventDAO = new EventDAO();
    }

    // Xử lý hiển thị danh sách và xóa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                case "LIST":
                    // 1. Gọi DAO lấy tất cả gói
                    List<Event> list = eventDAO.getAllEventPackages();
                    
                    // 2. Đẩy dữ liệu sang JSP
                    request.setAttribute("packages", list);
                    request.getRequestDispatcher("/WEB-INF/views/event/event-package-list.jsp").forward(request, response);
                    break;
                
//                case "DELETE":
//                    // Xử lý xóa nhanh tại đây luôn
//                    String idStr = request.getParameter("id");
//                    if(idStr != null) {
//                        int id = Integer.parseInt(idStr);
//                        eventDAO.deleteEventPackage(id);
//                        request.getSession().setAttribute("successMessage", "Deleted package successfully!");
//                    }
//                    response.sendRedirect("event-packages?action=LIST");
//                    break;

                default:
                    response.sendRedirect("event-packages?action=LIST");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); 
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chưa làm Create/Update nên cứ chuyển về doGet
        doGet(request, response);
    }
}