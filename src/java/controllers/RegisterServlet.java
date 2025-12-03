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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu (Bỏ dòng lấy cccd)
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String pass = request.getParameter("pass");
        String rePass = request.getParameter("repass");

        CustomerDAO dao = new CustomerDAO();

        // 2. Validation
        if (!pass.equals(rePass)) {
            request.setAttribute("mess", "Mật khẩu xác nhận không khớp!");
            saveInputData(request, fullName, email, phone);
            request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
            return;
        }

        if (dao.checkEmailExist(email)) {
            request.setAttribute("mess", "Email này đã được sử dụng!");
            saveInputData(request, fullName, email, phone);
            request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
            return;
        }

        // 3. Lưu vào DB
        try {
            Customer c = new Customer();
            c.setFullName(fullName);
            c.setEmail(email);
            c.setPassword(pass);
            c.setPhone(phone);
            c.setIsActive(true);
            c.setCreateAt(LocalDateTime.now());
            
            // Không set IdentityCard nữa

            dao.register(c);

            request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.setAttribute("user", email);
            request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/users/register.jsp").forward(request, response);
        }
    }

    // Hàm lưu dữ liệu (Đã bỏ cccd)
    private void saveInputData(HttpServletRequest request, String name, String email, String phone) {
        request.setAttribute("fullname", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
    }
}