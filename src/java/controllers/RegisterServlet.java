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

    // 1. GET: Hiển thị form đăng ký
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
    }

    // 2. POST: Xử lý dữ liệu
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // --- A. LẤY DỮ LIỆU (Đã bỏ CCCD) ---
        String fullName = request.getParameter("fullname").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        
        // Mật khẩu lấy raw (chưa trim vội để check dấu cách nếu cần, hoặc trim luôn tùy policy)
        // Ở đây mình trim() để loại bỏ lỗi vô tình gõ dấu cách ở cuối
        String pass = request.getParameter("pass").trim(); 
        String rePass = request.getParameter("repass").trim();
        
        CustomerDAO dao = new CustomerDAO();
        String targetUrl = "/WEB-INF/views/users/register.jsp";
        String errorMsg = null;
        
        // --- B. GỬI LẠI DỮ LIỆU VỀ FORM (Để người dùng không phải nhập lại) ---
        request.setAttribute("fullname", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        // Không gửi lại pass để bảo mật
        
        // --- C. KIỂM TRA DỮ LIỆU (VALIDATION) ---
        
        // 1. Kiểm tra để trống
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            errorMsg = "Vui lòng nhập đầy đủ thông tin!";
        }
        
        // 2. Kiểm tra Mật khẩu (Yêu cầu mới)
        else if (pass.contains(" ")) {
            errorMsg = "Mật khẩu không được chứa khoảng trắng!";
        }
        else if (pass.length() < 6) {
            errorMsg = "Mật khẩu phải có ít nhất 6 ký tự!";
        }
        else if (pass.length() > 20) { // <--- YÊU CẦU CỦA BẠN
            errorMsg = "Mật khẩu không được quá 20 ký tự!";
        }
        
        // 3. Kiểm tra mật khẩu xác nhận
        else if (!pass.equals(rePass)) {
            errorMsg = "Mật khẩu xác nhận không khớp!";
        }
        
        // 4. Kiểm tra Email trùng lặp
        else if (dao.checkEmailExist(email)) {
            errorMsg = "Email này đã được sử dụng!";
        }

        // --- D. XỬ LÝ KẾT QUẢ ---
        
        // Nếu có lỗi -> Quay lại trang đăng ký
        if (errorMsg != null) {
            request.setAttribute("mess", errorMsg);
            request.getRequestDispatcher(targetUrl).forward(request, response);
            return;
        }

        // Nếu hợp lệ -> Lưu vào Database
        try {
            Customer c = new Customer();
            c.setFullName(fullName);
            c.setEmail(email);
            c.setPassword(pass);
            c.setPhone(phone);
            // Không set IdentityCard nữa
            c.setIsActive(true); 
            c.setCreateAt(LocalDateTime.now());
            
            dao.register(c);

            // Chuyển sang trang Login và điền sẵn email
            request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.setAttribute("user", email); 
            
            request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher(targetUrl).forward(request, response);
        }
    }
}