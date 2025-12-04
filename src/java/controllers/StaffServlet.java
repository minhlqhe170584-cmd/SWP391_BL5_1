package controllers;

import java.sql.SQLException;
import dao.RoleDAO;
import dao.StaffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;
import models.Staff;

/**
 * @author Admin
 */
// SỬA: Thêm chữ 's' vào urlPatterns để khớp với redirect
@WebServlet(name = "StaffServlet", urlPatterns = {"/staffs"}) 
public class StaffServlet extends HttpServlet {

    private StaffDAO staffDAO;
    private RoleDAO roleDAO; 

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO(); 
        roleDAO = new RoleDAO(); 
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StaffServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            // SỬA: Xóa dấu cách thừa
            out.println("<h1>Servlet StaffServlet at " + request.getContextPath() + "</h1>"); 
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            List<Staff> staffs = staffDAO.getAllStaffs();
            List<Role> roles = roleDAO.getAllRoles(); 
            
            request.setAttribute("staffsList", staffs);
            request.setAttribute("rolesList", roles);        
            request.getRequestDispatcher("/WEB-INF/views/staff/staffList.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn dữ liệu Staff.");
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
                case "activate": // Case Mở khóa
                    activateStaff(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/staffs");
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "LỖI HỆ THỐNG: Lỗi SQL trong quá trình xử lý Staff.");
            response.sendRedirect(request.getContextPath() + "/staffs");
        }
    }

    private void insertStaff(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty() || password.isEmpty()) {
            request.getSession().setAttribute("message", "LỖI THÊM: Vui lòng điền đủ thông tin bắt buộc.");
        } else {
            Role role = new Role();
            role.setRoleId(roleId);
            
            Staff newStaff = new Staff();
            newStaff.setRole(role);
            newStaff.setFullName(fullName.trim());
            newStaff.setEmail(email.trim());
            newStaff.setPassWordHash(password); 
            
            try {
                boolean success = staffDAO.createStaff(newStaff);

                if (success) {
                    request.getSession().setAttribute("message", "Đã thêm nhân viên " + fullName + " thành công!");
                } else {
                    request.getSession().setAttribute("message", "LỖI: Không thể thêm nhân viên (Lỗi thao tác DB).");
                }
                
            } catch (SQLException e) {
                if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                    request.getSession().setAttribute("message", "LỖI THÊM: Email [" + email + "] đã được sử dụng.");
                } else {
                    throw e;
                }
            }
        }        
        response.sendRedirect(request.getContextPath() + "/staffs");
    }

    private void updateStaff(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        int staffId = Integer.parseInt(request.getParameter("staffId"));
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); 
        
        Role role = new Role();
        role.setRoleId(roleId);
        
        Staff updatedStaff = new Staff();
        updatedStaff.setStaffId(staffId);
        updatedStaff.setRole(role);
        updatedStaff.setFullName(fullName.trim());
        updatedStaff.setEmail(email.trim());
        updatedStaff.setPassWordHash(password); 

        try {            
            boolean success = staffDAO.updateStaff(updatedStaff); 

            if (success) {
                request.getSession().setAttribute("message", "Cập nhật nhân viên " + fullName + " thành công!");
            } else {
                 request.getSession().setAttribute("message", "LỖI: Không tìm thấy nhân viên ID=" + staffId + " để cập nhật.");
            }

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                request.getSession().setAttribute("message", "LỖI CẬP NHẬT: Email [" + email + "] đã được sử dụng bởi người khác.");
            } else {
                throw e; 
            }
        }

        response.sendRedirect(request.getContextPath() + "/staffs");
    }

    private void deactivateStaff(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        String staffIdStr = request.getParameter("staffId");
        
        try {
            int staffId = Integer.parseInt(staffIdStr);
            boolean success = staffDAO.deactivateStaff(staffId);
            
            if (success) {
                 request.getSession().setAttribute("message", "Đã vô hiệu hóa nhân viên ID=" + staffId + " thành công!");
            } else {
                 request.getSession().setAttribute("message", "LỖI: Không tìm thấy nhân viên để vô hiệu hóa.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "LỖI ĐẦU VÀO: ID nhân viên không hợp lệ.");
        }
        
        response.sendRedirect(request.getContextPath() + "/staffs");
    }

    private void activateStaff(HttpServletRequest request, HttpServletResponse response) 
        throws SQLException, IOException, ServletException {
    
        String staffIdStr = request.getParameter("staffId");
        
        try {
            int staffId = Integer.parseInt(staffIdStr); 
            boolean success = staffDAO.activateStaff(staffId); 
            
            if (success) {
                 request.getSession().setAttribute("message", "Đã MỞ KHÓA nhân viên ID=" + staffId + " thành công!");
            } else {
                 request.getSession().setAttribute("message", "LỖI: Không tìm thấy nhân viên để mở khóa.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "LỖI ĐẦU VÀO: ID nhân viên không hợp lệ.");
        }
        
        response.sendRedirect(request.getContextPath() + "/staffs");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}