package controllers;

import dao.CustomerDAO;
import dao.ServiceOrderDAO;
import models.Customer;
import models.Room;
import models.ServiceHistory;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("USER");
        String role = (String) session.getAttribute("ROLE");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // --- TRƯỜNG HỢP 1: KHÁCH HÀNG (Hiển thị thông tin) ---
            if ("CUSTOMER".equals(role)) {
                request.setAttribute("myProfile", user);
            } 
            // --- TRƯỜNG HỢP 2: PHÒNG (Hiển thị lịch sử dịch vụ) ---
            else if ("ROOM".equals(role)) {
                Room room = (Room) user;
                ServiceOrderDAO orderDAO = new ServiceOrderDAO();
                List<ServiceHistory> history = orderDAO.getHistoryByRoomId(room.getRoomId());
                
                request.setAttribute("roomInfo", room);
                request.setAttribute("serviceHistory", history);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/views/users/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Code xử lý update cho Customer (như đã làm bài trước)
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("ROLE");
        
        if ("CUSTOMER".equals(role)) {
            Customer current = (Customer) session.getAttribute("USER");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            
            if (fullName != null && !fullName.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                password != null && !password.trim().isEmpty()) {
                
                current.setFullName(fullName.trim());
                current.setPhone(phone.trim());
                current.setPassword(password.trim());
                
                CustomerDAO dao = new CustomerDAO();
                if(dao.updateProfile(current)) {
                    session.setAttribute("USER", current);
                    request.setAttribute("message", "Cập nhật thành công!");
                }
            } else {
                request.setAttribute("error", "Không được để trống!");
            }
        }
        doGet(request, response);
    }
}