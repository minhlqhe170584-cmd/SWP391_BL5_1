package controllers;

import dao.CustomerDAO;
import dao.StaffDAO;
import dao.RoomDAO; // Giả sử bạn đã tạo RoomDAO
import models.Customer;
import models.Staff;
import models.Room; // Giả sử bạn đã tạo Room model

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    // 1. Mở trang login (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đường dẫn trỏ vào file JSP trong WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
    }

    // 2. Xử lý đăng nhập (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // Lấy thông tin từ form
        String u = request.getParameter("user");
        String p = request.getParameter("pass");
        
        HttpSession session = request.getSession();

        try {
            // --- BƯỚC 1: Ưu tiên kiểm tra STAFF (Quản lý/Lễ tân) ---
            StaffDAO staffDAO = new StaffDAO();
            Staff staff = staffDAO.checkLogin(u, p); // Hàm này trả về Staff hoặc null
            
            if (staff != null) {
                // Đăng nhập thành công -> Lưu vào Session
                session.setAttribute("ROLE", "STAFF");
                session.setAttribute("USER", staff);
                session.setMaxInactiveInterval(1800); // 30 phút

                // Phân quyền: Nếu là Admin (RoleID = 1) -> Vào trang Dashboard
                // (Bạn cần đảm bảo trong Staff đã có thuộc tính Role hoặc RoleId)
                if (staff.getRole() != null && staff.getRole().getRoleId() == 1) {
                    response.sendRedirect("admin/dashboard.jsp"); 
                } else {
                    response.sendRedirect("staff/home.jsp"); // Trang nhân viên thường
                }
                return; // Dừng lại
            }

            // --- BƯỚC 2: Kiểm tra CUSTOMER (Khách hàng) ---
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.checkLogin(u, p); // Hàm này trả về Customer hoặc null
            
            if (customer != null) {
                session.setAttribute("ROLE", "CUSTOMER");
                session.setAttribute("USER", customer);
                session.setMaxInactiveInterval(1800);
                
                response.sendRedirect("HomeServlet"); // Chuyển về trang chủ
                return;
            }

            // --- BƯỚC 3: Kiểm tra ROOM ACCOUNT (Smart Hotel) ---
            RoomDAO roomDAO = new RoomDAO();
            // Hàm này check: số phòng == user, pass == pass, is_active_login == true
            Room room = roomDAO.checkRoomLogin(u, p);
            
            if (room != null) {
                session.setAttribute("ROLE", "ROOM");
                session.setAttribute("CURRENT_ROOM", room);
                
                response.sendRedirect("services"); // Chuyển sang trang đặt dịch vụ
                return;
            }

            // --- NẾU KHÔNG TÌM THẤY AI ---
            request.setAttribute("mess", "Tài khoản hoặc mật khẩu không đúng!");
            request.setAttribute("user", u); // Giữ lại tên đăng nhập
            
            request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Có thể forward sang trang error.jsp nếu muốn
        }
    }
}