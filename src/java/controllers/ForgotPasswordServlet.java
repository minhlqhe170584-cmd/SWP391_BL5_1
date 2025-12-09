package controllers;

import dao.CustomerDAO;
import utils.EmailUtils;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot", "/verify", "/reset"})
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/forgot")) {
            request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        CustomerDAO dao = new CustomerDAO();

        // 1. XỬ LÝ NHẬP EMAIL -> GỬI OTP
        if (path.equals("/forgot")) {
            String email = request.getParameter("email");
            
            if (dao.checkEmailExist(email)) {
                String otp = EmailUtils.generateOTP();
                EmailUtils.sendEmail(email, otp); // Gửi mail
                
                // Lưu OTP và Email vào Session để check ở bước sau
                session.setAttribute("otp", otp);
                session.setAttribute("resetEmail", email);
                session.setMaxInactiveInterval(300); // OTP sống 5 phút
                
                request.setAttribute("mess", "Mã OTP đã gửi vào email của bạn!");
                request.getRequestDispatcher("/WEB-INF/views/users/verify.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Email không tồn tại trong hệ thống!");
                request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
            }
        }
        
        // 2. XỬ LÝ XÁC THỰC MÃ OTP
        else if (path.equals("/verify")) {
            String inputOtp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("otp");
            
            if (inputOtp.equals(sessionOtp)) {
                // Đúng OTP -> Cho phép đổi mật khẩu
                request.getRequestDispatcher("/WEB-INF/views/users/reset.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Mã OTP sai hoặc đã hết hạn!");
                request.getRequestDispatcher("/WEB-INF/views/users/verify.jsp").forward(request, response);
            }
        }
        
        // 3. XỬ LÝ ĐỔI MẬT KHẨU MỚI
        else if (path.equals("/reset")) {
            String pass = request.getParameter("pass");
            String rePass = request.getParameter("repass");
            String email = (String) session.getAttribute("resetEmail");
            
            if (pass.equals(rePass)) {
                dao.updatePassword(email, pass); // Cập nhật DB
                
                // Xóa session dọn rác
                session.removeAttribute("otp");
                session.removeAttribute("resetEmail");
                
                request.setAttribute("mess", "Đổi mật khẩu thành công! Mời đăng nhập.");
                request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                request.getRequestDispatcher("/WEB-INF/views/users/reset.jsp").forward(request, response);
            }
        }
    }
}