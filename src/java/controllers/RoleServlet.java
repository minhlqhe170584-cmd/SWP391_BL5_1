/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dao.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class RoleServlet extends HttpServlet {
   private RoleDAO roleDAO;

    @Override
    public void init() throws ServletException {
        // Khởi tạo DAO khi Servlet được tạo
        // (Sử dụng hàm tạo không tham số của RoleDAO mà gọi super() để lấy kết nối)
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
            out.println("<title>Servlet RoleServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoleServlet at " + request.getContextPath () + "</h1>");
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
            List<Role> roles = roleDAO.getAllRoles();
            request.setAttribute("rolesList", roles);
            request.getRequestDispatcher("/views/staff/roleList.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn dữ liệu vai trò.");
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
                    updateRole(request, response);
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
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi SQL: " + e.getMessage());
        }
    }
    private void insertRole(HttpServletRequest request, HttpServletResponse response) 
        throws SQLException, IOException, ServletException {
    request.setCharacterEncoding("UTF-8");
    String roleName = request.getParameter("roleName");
    if (roleName == null || roleName.trim().isEmpty()) {
        request.getSession().setAttribute("message", "LỖI THÊM MỚI: Tên vai trò không được để trống.");
    } else {
        try {
            boolean success = roleDAO.createRole(roleName.trim());
            if (success) {
                request.getSession().setAttribute("message", "Đã thêm vai trò [" + roleName.trim() + "] thành công!");
            } else {
                request.getSession().setAttribute("message", "LỖI THÊM MỚI: Không thể thêm vai trò (Lỗi thao tác DB).");
            }
            
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("UNIQUE")) {
                request.getSession().setAttribute("message", "LỖI THÊM MỚI: Tên vai trò [" + roleName + "] đã tồn tại.");
            } else {
                request.getSession().setAttribute("message", "LỖI SQL: " + errorMessage);
            }
        }
    }
    response.sendRedirect(request.getContextPath() + "/staffRoles");
}
    
    private void updateRole(HttpServletRequest request, HttpServletResponse response) 
        throws SQLException, IOException, ServletException {
    
    String roleIdStr = request.getParameter("roleId");
    String roleName = request.getParameter("roleName");
    
    if (roleIdStr == null || roleIdStr.isEmpty() || roleName == null || roleName.trim().isEmpty()) {
        request.getSession().setAttribute("message", "LỖI CẬP NHẬT: ID hoặc Tên vai trò không được để trống.");
        response.sendRedirect(request.getContextPath() + "/staffRoles");
        return;
    }
    
    try {
        int roleId = Integer.parseInt(roleIdStr);
        boolean success = roleDAO.updateRole(roleId, roleName.trim());
        if (success) {
            request.getSession().setAttribute("message", "Cập nhật vai trò [" + roleName + "] thành công!");
        } else {
            request.getSession().setAttribute("message", "LỖI CẬP NHẬT: Không tìm thấy vai trò ID=" + roleId + " để cập nhật.");
        }
        
    } catch (NumberFormatException e) {
        request.getSession().setAttribute("message", "LỖI ĐẦU VÀO: ID vai trò không hợp lệ (phải là số).");
    }
    response.sendRedirect(request.getContextPath() + "/staffRoles");
}
    
    private void deleteRole(HttpServletRequest request, HttpServletResponse response) 
        throws SQLException, IOException, ServletException {
    
    String roleIdStr = request.getParameter("roleId");
    
    if (roleIdStr == null || roleIdStr.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/staffRoles");
        return;
    }
    
    try {
        int roleId = Integer.parseInt(roleIdStr);
        roleDAO.deleteRole(roleId);
        request.getSession().setAttribute("message", "Vai trò ID=" + roleId + " đã được xóa thành công.");

    } catch (NumberFormatException e) {
        request.getSession().setAttribute("message", "LỖI XÓA: ID vai trò không hợp lệ.");
    } catch (SQLException e) {
        if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY")) {
            request.getSession().setAttribute("message", "LỖI XÓA: Không thể xóa vì vai trò này đang được sử dụng bởi các nhân viên khác.");
        } else {
            request.getSession().setAttribute("message", "LỖI SQL: " + e.getMessage());
        }
    }
    
    response.sendRedirect(request.getContextPath() + "/staffRoles");
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
