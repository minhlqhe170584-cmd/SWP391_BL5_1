package controllers;

import dao.CustomerDAO;
import dao.StaffDAO;
import dao.RoomDAO; 
import models.Customer;
import models.Staff;
import models.Room; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String u = request.getParameter("user");
        String p = request.getParameter("pass");
        HttpSession session = request.getSession();

        try {
            // 1. STAFF
            StaffDAO staffDAO = new StaffDAO();
            Staff staff = staffDAO.checkLogin(u, p); 
            if (staff != null) {
                session.setAttribute("ROLE", "STAFF");
                session.setAttribute("USER", staff);
                session.setMaxInactiveInterval(1800); 
                if (staff.getRole() != null && staff.getRole().getRoleName().equals("Admin")) {
                    response.sendRedirect("staffs"); 
                } 
                else if(staff.getRole() != null && staff.getRole().getRoleName().equals("Staff"))
                {
                    response.sendRedirect("bike-ops");
                }
                else
                {
                    response.sendRedirect("receptionist/payment"); 
                }
                return;
            }

            // 2. CUSTOMER
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.login(u, p); // Đã dùng hàm login trả về Object
            if (customer != null) {
                session.setAttribute("ROLE", "CUSTOMER");
                session.setAttribute("USER", customer);
                session.setMaxInactiveInterval(1800);
                response.sendRedirect("home"); 
                return;
            }

            // 3. ROOM (Quan trọng nhất cho yêu cầu của bạn)
            RoomDAO roomDAO = new RoomDAO();
            Room room = roomDAO.checkRoomLogin(u, p);
            if (room != null) {
                session.setAttribute("ROLE", "ROOM");
                
                // --- QUAN TRỌNG: Lưu Room vào USER để ProfileServlet lấy được ---
                session.setAttribute("USER", room); 
                
                session.setAttribute("CURRENT_ROOM", room);
                session.setAttribute("ROOM_ID", room.getRoomId());
                response.sendRedirect("services"); 
                return;
            }

            // Fail
            request.setAttribute("mess", "Tài khoản hoặc mật khẩu không đúng!");
            request.setAttribute("user", u); 
            request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}