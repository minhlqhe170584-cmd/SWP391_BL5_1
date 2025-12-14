package controllers;

import dao.ServiceHomeDAO;
import models.ServiceHome;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ServiceHomeServlet", urlPatterns = {"/services-home"})
public class ServiceHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<ServiceHome> list = new ArrayList<>();
        int page = 1;
        int pageSize = 6; // Giới hạn 6 dịch vụ/trang
        int totalPages = 1;

        try {
            ServiceHomeDAO dao = new ServiceHomeDAO();
            
            // 1. Lấy trang hiện tại từ URL (ví dụ: /services?page=2)
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // 2. Tính tổng số trang
            int totalServices = dao.countTotalServices();
            totalPages = (int) Math.ceil((double) totalServices / pageSize);

            // 3. Lấy dữ liệu phân trang
            list = dao.getServicesByPage(page, pageSize);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 4. Gửi dữ liệu sang JSP
        request.setAttribute("listServices", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        
        request.getRequestDispatcher("/WEB-INF/views/home/service.jsp").forward(request, response);
    }
}