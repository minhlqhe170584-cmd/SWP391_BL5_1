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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    request.getRequestDispatcher("/WEB-INF/views/staff/roleDetail.jsp").forward(request, response);
                    break;

                case "edit":
                    showEditForm(request, response);
                    break;

                default:
                    listRoles(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi Server");
        }
    }

    // Hàm hỗ trợ lấy dữ liệu Role cũ để hiển thị lên form sửa
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("roleId"));
            Role existingRole = roleDAO.getRoleById(id);

            if (existingRole != null) {
                // Gửi object role sang JSP để điền vào các ô input
                request.setAttribute("role", existingRole);
                request.getRequestDispatcher("/WEB-INF/views/staff/roleDetail.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("message", "LỖI: Không tìm thấy vai trò này.");
                response.sendRedirect(request.getContextPath() + "/staffRoles");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/staffRoles");
        }
    }

    private void listRoles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy parameters
        String keyword = request.getParameter("search");
        String pageStr = request.getParameter("page");

        int pageIndex = 1;
        int pageSize = 2; 
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        // 2. Gọi DAO
        List<Role> roles = roleDAO.getRoles(keyword, pageIndex, pageSize);
        int totalRecords = roleDAO.countRoles(keyword);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 3. Set Attributes cho JSP
        request.setAttribute("rolesList", roles);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchQuery", keyword);

        request.getRequestDispatcher("/WEB-INF/views/staff/roleList.jsp").forward(request, response);
    }

    // --- XỬ LÝ HÀNH ĐỘNG (POST) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "create":
                    insertRole(request, response);
                    break;
                case "update":
                    updateRole(request, response); // <--- MỚI THÊM
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

    // 1. THÊM MỚI
    private void insertRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String roleName = request.getParameter("roleName");

        // VALIDATION: Chặn nhập toàn dấu cách
        if (roleName == null || roleName.trim().isEmpty()) {
            request.getSession().setAttribute("message", "LỖI: Tên vai trò không được để trống hoặc chỉ chứa khoảng trắng!");
            response.sendRedirect(request.getContextPath() + "/staffRoles?action=add");
            return;
        }

        try {
            boolean success = roleDAO.createRole(roleName.trim());
            if (success) {
                request.getSession().setAttribute("message", "Thêm vai trò thành công!");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("Duplicate")) {
                request.getSession().setAttribute("message", "LỖI: Tên vai trò đã tồn tại.");
            } else {
                throw e;
            }
        }
        response.sendRedirect(request.getContextPath() + "/staffRoles");
    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            String roleName = request.getParameter("roleName");
            if (roleName == null || roleName.trim().isEmpty()) {
                request.getSession().setAttribute("message", "LỖI: Tên vai trò không được để trống!");
                response.sendRedirect(request.getContextPath() + "/staffRoles?action=edit&roleId=" + roleId);
                return;
            }

            boolean success = roleDAO.updateRole(roleId, roleName.trim());
            if (success) {
                request.getSession().setAttribute("message", "Cập nhật vai trò thành công!");
            } else {
                request.getSession().setAttribute("message", "LỖI: Không tìm thấy vai trò để cập nhật.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "LỖI: ID không hợp lệ.");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("Duplicate")) {
                request.getSession().setAttribute("message", "LỖI: Tên vai trò đã trùng với vai trò khác.");
            } else {
                throw e;
            }
        }
        response.sendRedirect(request.getContextPath() + "/staffRoles");
    }

    // 3. XÓA
    private void deleteRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            String idStr = request.getParameter("roleId");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int roleId = Integer.parseInt(idStr.trim());
                roleDAO.deleteRole(roleId);
                request.getSession().setAttribute("message", "Xóa vai trò thành công!");
            } else {
                request.getSession().setAttribute("message", "LỖI: ID không hợp lệ.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "LỖI: ID phải là số.");
        } catch (SQLException e) {
            if (e.getMessage().contains("REFERENCE") || e.getMessage().contains("FOREIGN KEY")) {
                request.getSession().setAttribute("message", "LỖI: Không thể xóa vì đang có nhân viên giữ vai trò này.");
            } else {
                throw e;
            }
        }
        response.sendRedirect(request.getContextPath() + "/staffRoles");
    }
}
