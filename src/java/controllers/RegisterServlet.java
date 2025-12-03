package controllers;

import dao.CustomerDAO;
import models.Customer;
import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    // GET: Hiển thị form đăng ký
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
    }

    // POST: Xử lý dữ liệu khi bấm nút Đăng Ký
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String cccd = request.getParameter("cccd"); // CCCD/CMND
        String pass = request.getParameter("pass");
        String rePass = request.getParameter("repass");
        
        CustomerDAO dao = new CustomerDAO();
        String targetUrl = "/WEB-INF/views/users/register.jsp";
        String errorMsg = null;
        
        // --- Chuẩn bị dữ liệu Input để giữ lại trên form khi lỗi ---
        request.setAttribute("fullname", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("cccd", cccd); // Giữ lại CCCD
        
        // 2. VALIDATION LOGIC
        
        // B1. Kiểm tra mật khẩu xác nhận
        if (!pass.equals(rePass)) {
            errorMsg = "Mật khẩu xác nhận không khớp!";
        } 
        // B2. Kiểm tra CCCD (Phải là 9 số và toàn chữ số)
        else if (cccd != null && !cccd.isEmpty() && !cccd.matches("\\d{9}")) {
            errorMsg = "CCCD/CMND phải có đúng 9 chữ số.";
        }
        // B3. Kiểm tra Email đã tồn tại chưa
        else if (dao.checkEmailExist(email)) {
            errorMsg = "Email này đã được sử dụng!";
        }

        // 3. XỬ LÝ KHI CÓ LỖI
        if (errorMsg != null) {
            request.setAttribute("mess", errorMsg);
            request.getRequestDispatcher(targetUrl).forward(request, response);
            return;
        }

        // 4. LƯU VÀO DATABASE
        try {
            Customer c = new Customer();
            c.setFullName(fullName);
            c.setEmail(email);
            c.setPassword(pass);
            c.setPhone(phone);
            c.setIdentityCard(cccd); // Set CCCD
            c.setIsActive(true); 
            c.setCreateAt(LocalDateTime.now());
            
            dao.register(c); 

            // 5. THÀNH CÔNG -> Chuyển sang trang Login
            request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.setAttribute("user", email); // Điền sẵn email vào form login
            
            request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "Lỗi hệ thống: Không thể hoàn tất đăng ký. Vui lòng thử lại.");
            request.getRequestDispatcher(targetUrl).forward(request, response);
        }
    }
}