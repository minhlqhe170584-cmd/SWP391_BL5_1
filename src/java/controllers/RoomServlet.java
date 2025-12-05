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
                // case "NEW": ...
                // case "DELETE": ...
                
                case "LIST":
                default:
                    // --- BƯỚC 1: Chuẩn bị dữ liệu cho Dropdown (Loại phòng) ---
                    // Cần import models.RoomType ở đầu file
                    List<models.RoomType> listType = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listType);

                    // --- BƯỚC 2: Lấy tất cả tham số từ Form tìm kiếm ---
                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String status = request.getParameter("status");
                    String active = request.getParameter("active");
                    String floor = request.getParameter("floor");

                    // --- BƯỚC 3: Kiểm tra xem có đang Lọc/Tìm kiếm không ---
                    // Chỉ cần 1 trong 5 ô có dữ liệu thì coi là đang lọc
                    boolean isFiltering = (keyword != null && !keyword.trim().isEmpty()) ||
                                          (typeId != null && !typeId.isEmpty()) ||
                                          (status != null && !status.isEmpty()) ||
                                          (active != null && !active.isEmpty()) ||
                                          (floor != null && !floor.isEmpty());
                    
                    request.setAttribute("isFiltering", isFiltering);

                    if (isFiltering) {
                        // === TRƯỜNG HỢP 1: CÓ LỌC (VÀ CÓ PHÂN TRANG) ===
                        
                        // 1. Lấy TOÀN BỘ kết quả tìm kiếm từ DB
                        List<Room> fullList = roomDAO.findRooms(keyword, typeId, status, active, floor);
                        
                        // 2. Tính toán phân trang trên danh sách kết quả này (In-Memory Pagination)
                        int count = fullList.size(); // Tổng số kết quả tìm được (ví dụ: 8)
                        int endPage = count / 5;
                        if (count % 5 != 0) {
                            endPage++;
                        }
                        
                        // Xác định trang hiện tại
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) indexPage = "1";
                        int index = Integer.parseInt(indexPage);
                        
                        // Cắt danh sách (SubList) để lấy 5 phần tử cho trang hiện tại
                        int start = (index - 1) * 5;
                        int end = Math.min(start + 5, count);
                        
                        List<Room> pagedList = new java.util.ArrayList<>();
                        if (start < count) {
                            pagedList = fullList.subList(start, end);
                        }
                        
                        // 3. Gửi dữ liệu sang JSP
                        request.setAttribute("roomsList", pagedList); // Chỉ gửi 5 phòng của trang này
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                        request.setAttribute("isFiltering", true); 
                        
                        // Gửi lại các tham số filter để JSP giữ trạng thái và tạo link phân trang
                        request.setAttribute("keyword", keyword);
                        request.setAttribute("currentType", typeId);
                        request.setAttribute("currentStatus", status);
                        request.setAttribute("currentActive", active);
                        request.setAttribute("currentFloor", floor);
                        
                    }   else {
                        // === TRƯỜNG HỢP 2: KHÔNG LỌC (CHẠY PHÂN TRANG MẶC ĐỊNH) ===
                        // 1. Xác định trang hiện tại
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) {
                            indexPage = "1";
                        }
                        int index = Integer.parseInt(indexPage);

                        // 2. Tính toán tổng số trang
                        int count = roomDAO.getTotalRooms(); 
                        int endPage = count / 5;
                        if (count % 5 != 0) {
                            endPage++; 
                        }

                        // 3. Lấy dữ liệu trang hiện tại
                        List<Room> list = roomDAO.pagingRooms(index);

                        // 4. Đẩy dữ liệu sang JSP
                        request.setAttribute("roomsList", list);
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                    }
                    
                    // Chuyển tiếp đến trang JSP
                    request.getRequestDispatcher("/WEB-INF/views/room/room-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            // === XỬ LÝ LỖI ===
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>LỖI XỬ LÝ YÊU CẦU</h1>");
                out.println("<p>Chi tiết lỗi:</p>");
                out.println("<pre>" + ex.getMessage() + "</pre>");
                ex.printStackTrace(); // In lỗi ra console server để debug
            }
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