package controllers;

import dao.UsersDAO; // Import package dao
import models.User;  // Import model Users
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
    // Sửa đường dẫn trỏ đúng vào nơi bạn để file jsp
    request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String u = request.getParameter("user");
            String p = request.getParameter("pass");

            UsersDAO dao = new UsersDAO(); // Khởi tạo UsersDAO
            User account = dao.login(u, p); // Trả về đối tượng Users

            if (account == null) {
                request.setAttribute("mess", "Tài khoản hoặc mật khẩu sai rồi!");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("acc", account);
                session.setMaxInactiveInterval(1800);

                // Check quyền admin
                if(account.getIsAdmin() == 1) {
                    response.sendRedirect("Admin.jsp");
                } else {
                    response.sendRedirect("Home.jsp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}