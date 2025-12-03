package controllers;

import dao.CustomerDAO;
import models.Customer;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    // 1. Nếu người dùng gọi trang này (GET), chuyển sang file JSP để hiện form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    // 2. Khi người dùng bấm nút "Đăng Ký" (POST), xử lý dữ liệu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
//        // Cấu hình tiếng Việt
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        // Lấy dữ liệu từ form
//        String fullName = request.getParameter("fullname");
//        String email = request.getParameter("email");
//        String phone = request.getParameter("phone");
//        String cccd = request.getParameter("cccd"); // Identity Card
//        String pass = request.getParameter("pass");
//        String rePass = request.getParameter("repass");
//
//        // Gọi DAO
//        CustomerDAO dao = new CustomerDAO();
//
//        // --- VALIDATION (Kiểm tra dữ liệu) ---
//        
//        // 1. Kiểm tra mật khẩu nhập lại có khớp không
//        if (!pass.equals(rePass)) {
//            request.setAttribute("mess", "Mật khẩu nhập lại không khớp!");
//            // Giữ lại thông tin người dùng đã nhập để họ đỡ phải gõ lại (trừ pass)
//            request.setAttribute("fullname", fullName);
//            request.setAttribute("email", email);
//            request.setAttribute("phone", phone);
//            request.setAttribute("cccd", cccd);
//            
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//            return;
//        }
//
//        // 2. Kiểm tra Email đã tồn tại chưa
//        if (dao.checkEmailExist(email)) {
//            request.setAttribute("mess", "Email này đã được sử dụng!");
//            request.setAttribute("fullname", fullName);
//            request.setAttribute("phone", phone);
//            request.setAttribute("cccd", cccd);
//            
//            request.getRequestDispatcher("register.jsp").forward(request, response);
//            return;
//        }
//
//        // --- NẾU MỌI THỨ OK ---
//        
//        // Tạo đối tượng Customer mới
//        Customer c = new Customer();
//        c.setFullName(fullName);
//        c.setEmail(email);
//        c.setPhone(phone);
//        c.setIdentityCard(cccd);
//        c.setPassword(pass); 
//
//        // Lưu vào Database
//        dao.register(c);
//
//        // Chuyển hướng về trang Login và báo thành công
//        request.setAttribute("mess", "Đăng ký thành công! Mời đăng nhập.");
//        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}