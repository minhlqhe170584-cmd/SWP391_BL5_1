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
        
        // Chỉ hiển thị trang forgot, các trang verify/reset không cho truy cập trực tiếp bằng GET
        if (path.equals("/forgot")) {
            request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
        } else {
            response.sendRedirect("forgot"); // Đẩy về trang đầu nếu cố tình vào link khác
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // Hỗ trợ tiếng Việt
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        CustomerDAO dao = new CustomerDAO();

        try {
            // 1. XỬ LÝ NHẬP EMAIL -> GỬI OTP
            if (path.equals("/forgot")) {
                String email = request.getParameter("email");
                
                if (dao.checkEmailExist(email)) { // Đảm bảo CustomerDAO có hàm này
                    
                    // SỬA 1: Dùng hàm generateRandomCode(6) thay vì generateOTP()
                    String otp = EmailUtils.generateRandomCode(6); 
                    
                    // SỬA 2: Dùng hàm sendEmailOTP (2 tham số)
                    boolean isSent = EmailUtils.sendEmailOTP(email, otp); 
                    
                    if (isSent) {
                        // Lưu vào Session
                        session.setAttribute("otp", otp);
                        session.setAttribute("resetEmail", email);
                        session.setMaxInactiveInterval(300); // 5 phút
                        
                        request.setAttribute("mess", "Mã OTP đã gửi vào email: " + email);
                        request.getRequestDispatcher("/WEB-INF/views/users/verify.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Lỗi gửi mail! Vui lòng thử lại sau.");
                        request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("error", "Email không tồn tại trong hệ thống!");
                    request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
                }
            }
            
            // 2. XỬ LÝ XÁC THỰC MÃ OTP
            else if (path.equals("/verify")) {
                String inputOtp = request.getParameter("otp");
                String sessionOtp = (String) session.getAttribute("otp");
                
                if (inputOtp != null && inputOtp.equals(sessionOtp)) {
                    // Đúng OTP -> Chuyển sang trang đổi pass
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
                
                if (email == null) {
                    response.sendRedirect("forgot"); // Session hết hạn thì làm lại
                    return;
                }
                
                if (pass.equals(rePass)) {
                    dao.updatePassword(email, pass); // Đảm bảo CustomerDAO có hàm updatePassword
                    
                    // Dọn dẹp Session
                    session.removeAttribute("otp");
                    session.removeAttribute("resetEmail");
                    
                    request.setAttribute("mess", "Đổi mật khẩu thành công! Mời đăng nhập.");
                    request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                    request.getRequestDispatcher("/WEB-INF/views/users/reset.jsp").forward(request, response);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/users/forgot.jsp").forward(request, response);
        }
    }
}