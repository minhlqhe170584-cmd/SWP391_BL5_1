/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;
import java.sql.SQLException;
import dao.RoleDAO;
import dao.StaffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;
import models.Staff;

/**
 *
 * @author Admin
 */
public class StaffServlet extends HttpServlet {
    private StaffDAO staffDAO;
    private RoleDAO roleDAO; 

    @Override
    public void init() throws ServletException {
        staffDAO = new StaffDAO(); 
        roleDAO = new RoleDAO(); 
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StaffServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            List<Staff> staffs = staffDAO.getAllStaffs();
            List<Role> roles = roleDAO.getAllRoles(); // Dùng cho dropdown Role
            
            request.setAttribute("staffsList", staffs);
            request.setAttribute("rolesList", roles);        
            request.getRequestDispatcher("/views/staff/staffList.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn dữ liệu Staff.");
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Xử lý tiếng Việt
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
        String password = request.getParameter("password"); // Nếu form để trống, password sẽ là chuỗi rỗng
        
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


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
