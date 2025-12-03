/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.RoomDAO;
import models.Room;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author My Lap
 */
@WebServlet(name = "RoomServlet", urlPatterns = {"/rooms"})
public class RoomServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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
            out.println("<title>Servlet RoomServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoomServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private RoomDAO roomDAO;
    
    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        try {
            switch (action.toUpperCase()) {
                // case "NEW": // Bổ sung sau: hiển thị form
                //     showNewForm(request, response);
                //     break;
                // case "DELETE": // Bổ sung sau: xử lý xóa
                //     deleteRoom(request, response); 
                //     break;
                case "LIST":
                default:
                    // Đặt logic LISTROOMS trực tiếp vào đây
                    List<Room> roomList = roomDAO.getAllRooms();
                    request.setAttribute("roomsList", roomList);
                    
                    // Chuyển tiếp đến trang JSP
                    request.getRequestDispatcher("/WEB-INF/views/room/room-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            // === PHẦN XỬ LÝ LỖI ĐÃ ĐƯỢC CẬP NHẬT ===
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>LỖI XỬ LÝ YÊU CẦU</h1>");
                out.println("<p>Đã xảy ra lỗi khi cố gắng lấy danh sách phòng:</p>");
                out.println("<pre>" + ex.getMessage() + "</pre>");
                System.err.println("LỖI SQL/DAO: " + ex.getMessage());
            }
            // ======================================
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
            action = "LIST"; // Mặc định chuyển sang LIST nếu POST không rõ mục đích
        }

        try {
            switch (action.toUpperCase()) {
                // case "CREATE": // Bổ sung sau: xử lý thêm mới
                //     insertRoom(request, response);
                //     break;
                // case "UPDATE": // Bổ sung sau: xử lý cập nhật
                //     updateRoom(request, response);
                //     break;
                case "LIST": // Xử lý POST không có action rõ ràng
                default:
                    // Thường chuyển hướng về GET LIST sau khi xử lý POST
                    response.sendRedirect(request.getContextPath() + "/rooms"); 
                    break;
            }
        } catch (Exception ex) {
            // === PHẦN XỬ LÝ LỖI ĐÃ ĐƯỢC CẬP NHẬT ===
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>LỖI XỬ LÝ YÊU CẦU</h1>");
                out.println("<p>Đã xảy ra lỗi khi cố gắng lấy danh sách phòng:</p>");
                out.println("<pre>" + ex.getMessage() + "</pre>");
                System.err.println("LỖI SQL/DAO: " + ex.getMessage());
            }
            // ======================================
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
