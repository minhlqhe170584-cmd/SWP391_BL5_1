package controllers;

import dao.RoleDAO;
import models.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "RoleServlet", urlPatterns = {"/staffRoles"})
public class RoleServlet extends HttpServlet {
    
    private RoleDAO roleDAO;

    @Override
    public void init() throws ServletException {
        roleDAO = new RoleDAO(); 
    }

    // --- XỬ LÝ ĐIỀU HƯỚNG (GET) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "add":
                    // Chuyển sang màn hình thêm mới
                    request.getRequestDispatcher("/WEB-INF/views/staff/roleDetail.jsp").forward(request, response);
                    break;
                default:
                    // Mặc định: Hiển thị danh sách
                    listRoles(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi Server");
        }
    }

    private void listRoles(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("rolesList", roles);
        request.getRequestDispatcher("/WEB-INF/views/staff/roleList.jsp").forward(request, response);
    }

    // --- XỬ LÝ HÀNH ĐỘNG (POST) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "create":
                    insertRole(request, response);
                    break;
                case "delete":
                    deleteRole(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/staffRoles");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "LỖI SQL: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/staffRoles");
        }
    }

    private void insertRole(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        String roleName = request.getParameter("roleName");
        
        if (roleName == null || roleName.trim().isEmpty()) {
            request.getSession().setAttribute("message", "LỖI: Tên vai trò không được để trống.");
        } else {
            try {
                boolean success = roleDAO.createRole(roleName.trim());
                if (success) {
                    request.getSession().setAttribute("message", "Thêm vai trò thành công!");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("UNIQUE")) {
                    request.getSession().setAttribute("message", "LỖI: Tên vai trò đã tồn tại.");
                } else {
                    throw e;
                }
            }
        }
        // Sau khi thêm xong, quay về trang danh sách
        response.sendRedirect(request.getContextPath() + "/staffRoles");
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            roleDAO.deleteRole(roleId);
            request.getSession().setAttribute("message", "Xóa vai trò thành công!");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "LỖI: ID không hợp lệ.");
        } catch (SQLException e) {
            if (e.getMessage().contains("REFERENCE") || e.getMessage().contains("FOREIGN KEY")) {
                request.getSession().setAttribute("message", "LỖI: Không thể xóa vai trò này vì đang có nhân viên nắm giữ.");
            } else {
                throw e;
            }
        }
        response.sendRedirect(request.getContextPath() + "/staffRoles");
    }
}