package controllers;

import java.sql.SQLException;
import dao.RoleDAO;
import dao.StaffDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;
import models.Staff;

@WebServlet(name = "StaffServlet", urlPatterns = {"/staffs"})
public class StaffServlet extends HttpServlet {

    private StaffDAO staffDAO;
    private RoleDAO roleDAO;

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO();
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
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listStaffs(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi Database");
        }
    }

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
                    insertStaff(request, response);
                    break;
                case "update":
                    updateStaff(request, response);
                    break;
                case "deactivate":
                    deactivateStaff(request, response);
                    break;
                case "activate":
                    activateStaff(request, response);
                    break;
                default:
                    response.sendRedirect("staffs");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("staffs");
        }
    }

    private void listStaffs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String roleIdStr = request.getParameter("roleFilter");
        String pageStr = request.getParameter("page");

        int pageSize = 5;
        int pageIndex = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        int totalRecords = staffDAO.countStaffs(keyword, roleIdStr);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        List<Staff> staffs = staffDAO.getStaffs(keyword, roleIdStr, pageIndex, pageSize);
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("staffsList", staffs);
        request.setAttribute("rolesList", roles);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("roleFilter", roleIdStr);

        request.getRequestDispatcher("/WEB-INF/views/staff/staffList.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("rolesList", roles);
        request.getRequestDispatcher("/WEB-INF/views/staff/staffDetail.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("staffId"));
            Staff existingStaff = staffDAO.getStaffById(id);
            List<Role> roles = roleDAO.getAllRoles();
            request.setAttribute("staff", existingStaff);
            request.setAttribute("rolesList", roles);
            request.getRequestDispatcher("/WEB-INF/views/staff/staffDetail.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("staffs");
        }
    }

    private void insertStaff(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Staff newStaff = new Staff();
        newStaff.setRole(new Role(roleId, ""));
        newStaff.setFullName(fullName);
        newStaff.setEmail(email);
        newStaff.setPassWordHash(password);

        try {
            staffDAO.createStaff(newStaff);
            request.getSession().setAttribute("message", "Thêm mới thành công!");
        } catch (SQLException e) {
            request.getSession().setAttribute("message", "Lỗi thêm mới: " + e.getMessage());
        }
        response.sendRedirect("staffs");
    }

    private void updateStaff(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("staffId"));
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Staff staff = new Staff();
        staff.setStaffId(id);
        staff.setRole(new Role(roleId, ""));
        staff.setFullName(fullName);
        staff.setEmail(email);
        staff.setPassWordHash(password);

        staffDAO.updateStaff(staff);
        request.getSession().setAttribute("message", "Cập nhật thành công!");
        response.sendRedirect("staffs");
    }

    private void deactivateStaff(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("staffId"));
        staffDAO.deactivateStaff(id);
        request.getSession().setAttribute("message", "Đã khóa tài khoản!");
        response.sendRedirect("staffs");
    }

    private void activateStaff(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("staffId"));
        staffDAO.activateStaff(id);
        request.getSession().setAttribute("message", "Đã mở khóa tài khoản!");
        response.sendRedirect("staffs");
    }
}
