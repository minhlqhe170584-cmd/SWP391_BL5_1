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
                String keyword = request.getParameter("keyword");
                
                // KIỂM TRA: Nếu có từ khóa tìm kiếm -> Gọi hàm Search
                if (keyword != null && !keyword.trim().isEmpty()) {
                    List<Room> list = roomDAO.searchRoomsByNumber(keyword.trim());
                    request.setAttribute("roomsList", list);
                    request.setAttribute("keyword", keyword); // Gửi lại keyword để hiện ở ô input
                    // Khi search thì không cần tính phân trang (hoặc logic phân trang tìm kiếm phức tạp hơn)
                }
                // NẾU KHÔNG tìm kiếm -> Gọi hàm Phân trang cũ
                else {
                // 1. Xác định trang hiện tại (Mặc định là trang 1 nếu không truyền vào)
                String indexPage = request.getParameter("index");
                if (indexPage == null) {
                    indexPage = "1";
                }
                int index = Integer.parseInt(indexPage);

                // 2. Tính toán tổng số trang (để hiển thị dãy số 1 2 3...)
                // Ta cần biết tổng có bao nhiêu phòng để chia cho 5
                int count = roomDAO.getTotalRooms(); 
                int endPage = count / 5;
                if (count % 5 != 0) {
                    endPage++; // Nếu còn lẻ (ví dụ 12 phòng chia 5 = 2 dư 2) thì cần thêm 1 trang nữa
                }

                // 3. Lấy dữ liệu CHỈ CỦA TRANG ĐÓ (Thay thế getAllRooms bằng pagingRooms)
                List<Room> list = roomDAO.pagingRooms(index);

                // 4. Đẩy dữ liệu sang JSP
                request.setAttribute("roomsList", list); // Danh sách 5 phòng
                request.setAttribute("endPage", endPage); // Tổng số trang (để vẽ nút)
                request.setAttribute("tag", index);       // Trang hiện tại (để tô màu nút active)
                }
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
