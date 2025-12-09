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
@WebServlet(name = "RoomServlet", urlPatterns = {"/admin/rooms"})
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
                
                // === 1. CHỨC NĂNG BAN / UNBAN (ĐỔI TRẠNG THÁI NHANH) ===
                case "BAN":
                    String idBanStr = request.getParameter("id");
                    if (idBanStr != null) {
                        try {
                            int id = Integer.parseInt(idBanStr);
                            Room room = roomDAO.getRoomById(id);
                            if (room != null) {
                                String newStatus;
                                String msg;
                                if ("Maintenance".equals(room.getStatus())) {
                                    newStatus = "Available"; 
                                    msg = "Unbanned room " + room.getRoomNumber() + ". Status set to Available.";
                                } else {
                                    newStatus = "Maintenance"; 
                                    msg = "Banned room " + room.getRoomNumber() + ". Status set to Maintenance.";
                                }
                                roomDAO.updateRoomStatus(id, newStatus);
                                request.getSession().setAttribute("successMessage", msg);
                            }
                        } catch (NumberFormatException e) {
                            // Id lỗi thì bỏ qua
                        }
                    }
                    response.sendRedirect("rooms?action=LIST");
                    break;

                // === 2. CHỨC NĂNG HIỂN THỊ FORM UPDATE ===
                case "EDIT":
                    String idEdit = request.getParameter("id");
                    if (idEdit != null) {
                        try {
                            int id = Integer.parseInt(idEdit);
                            Room room = roomDAO.getRoomById(id);
                            List<models.RoomType> listType = roomDAO.getAllRoomTypes();
                            
                            request.setAttribute("room", room); 
                            request.setAttribute("listType", listType); 
                            request.getRequestDispatcher("/WEB-INF/views/room/room-edit&add.jsp").forward(request, response);
                        } catch (NumberFormatException e) {
                            response.sendRedirect("rooms?action=LIST");
                        }
                    } else {
                        response.sendRedirect("rooms?action=LIST");
                    }
                    break;

                // === 3. CHỨC NĂNG HIỂN THỊ FORM TẠO MỚI ===
                case "NEW":
                    List<models.RoomType> listTypeNew = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listTypeNew);
                    // Chuyển sang form rỗng
                    request.getRequestDispatcher("/WEB-INF/views/room/room-edit&add.jsp").forward(request, response);
                    break;    

                // === 4. CHỨC NĂNG XEM CHI TIẾT ===
                case "VIEW":
                    String idView = request.getParameter("id");
                    if (idView != null) {
                        try {
                            int id = Integer.parseInt(idView);
                            Room room = roomDAO.getRoomById(id);
                            if (room != null) {
                                request.setAttribute("room", room);
                                request.getRequestDispatcher("/WEB-INF/views/room/room-details.jsp").forward(request, response);
                            } else {
                                request.setAttribute("errorMessage", "Room not found with ID: " + id);
                                request.getRequestDispatcher("/rooms?action=LIST").forward(request, response);
                            }
                        } catch (NumberFormatException e) {
                            response.sendRedirect("rooms?action=LIST");
                        }
                    } else {
                        response.sendRedirect("rooms?action=LIST");
                    }
                    break;
                
                // === 5. CHỨC NĂNG XÓA (DELETE) - BỔ SUNG ===
                case "DELETE":
                    String idDel = request.getParameter("id");
                    if (idDel != null) {
                        try {
                            int id = Integer.parseInt(idDel);
                            
                            // 1. Lấy thông tin phòng TRƯỚC khi xóa để lấy số phòng
                            Room roomToDelete = roomDAO.getRoomById(id);
                            
                            if (roomToDelete != null) {
                                String roomNum = roomToDelete.getRoomNumber(); // Lưu số phòng vào biến tạm
                                
                                // 2. Gọi hàm xóa bên DAO
                                roomDAO.deleteRoom(id);
                                
                                // 3. Thông báo thành công (Có thêm dấu cách để không bị dính chữ)
                                request.getSession().setAttribute("successMessage", "Deleted room " + roomNum + " successfully!");
                            } else {
                                request.getSession().setAttribute("errorMessage", "Room not found to delete!");
                            }
                            
                        } catch (Exception e) {
                            System.err.println("Error Delete Servlet: " + e.getMessage());
                        }
                    }
                    response.sendRedirect("rooms?action=LIST");
                    break;

                // === 6. CHỨC NĂNG LIỆT KÊ (LIST & FILTER & PAGINATION) ===
                case "LIST":
                default:
                    // 1. Chuẩn bị dữ liệu Dropdown
                    List<models.RoomType> listType = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listType);
                    
                    // Lấy danh sách tầng động 
                    List<Integer> listFloors = roomDAO.getExistingFloors();
                    request.setAttribute("listFloors", listFloors);

                    // 2. Lấy tham số Filter
                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String status = request.getParameter("status");
                    String active = request.getParameter("active");
                    String floor = request.getParameter("floor");

                    // 3. Kiểm tra xem có đang lọc không
                    boolean isFiltering = (keyword != null && !keyword.trim().isEmpty()) ||
                                          (typeId != null && !typeId.isEmpty()) ||
                                          (status != null && !status.isEmpty()) ||
                                          (active != null && !active.isEmpty()) ||
                                          (floor != null && !floor.isEmpty());
                    
                    request.setAttribute("isFiltering", isFiltering);

                    if (isFiltering) {
                        // --- TRƯỜNG HỢP CÓ LỌC ---
                        List<Room> fullList = roomDAO.findRooms(keyword, typeId, status, active, floor);
                        
                        int count = fullList.size();
                        int endPage = count / 5;
                        if (count % 5 != 0) endPage++;
                        
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) indexPage = "1";
                        int index = Integer.parseInt(indexPage);
                        
                        int start = (index - 1) * 5;
                        int end = Math.min(start + 5, count);
                        
                        List<Room> pagedList = new java.util.ArrayList<>();
                        if (start < count) {
                            pagedList = fullList.subList(start, end);
                        }
                        
                        request.setAttribute("roomsList", pagedList);
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                        
                        // Gửi lại tham số
                        request.setAttribute("keyword", keyword);
                        request.setAttribute("currentType", typeId);
                        request.setAttribute("currentStatus", status);
                        request.setAttribute("currentActive", active);
                        request.setAttribute("currentFloor", floor);
                        
                    } else {
                        // --- TRƯỜNG HỢP KHÔNG LỌC ---
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) indexPage = "1";
                        int index = Integer.parseInt(indexPage);

                        int count = roomDAO.getTotalRooms();
                        int endPage = count / 5;
                        if (count % 5 != 0) endPage++;

                        List<Room> list = roomDAO.pagingRooms(index);
                        request.setAttribute("roomsList", list);
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                    }
                    
                    request.getRequestDispatcher("/WEB-INF/views/room/room-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>ERROR</h1>");
                out.println("<p>" + ex.getMessage() + "</p>");
                ex.printStackTrace();
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
                case "UPDATE":
                    // 1. Nhận dữ liệu
                    String roomIdStr = request.getParameter("roomId");
                    int roomId = Integer.parseInt(roomIdStr);
                    String roomNumber = request.getParameter("roomNumber");
                    int typeId = Integer.parseInt(request.getParameter("typeId"));
                    String status = request.getParameter("status");
                    String password = request.getParameter("roomPassword");
                    boolean isActive = request.getParameter("activeLogin") != null;

                    // --- 2. VALIDATE DỮ LIỆU ---
                    String error = null;

                    // Check 1: Định dạng số (Regex: chỉ chứa ký tự số 0-9)
                    if (!roomNumber.matches("\\d+")) {
                        error = "Room Number must contain only digits (0-9)!";
                    } 
                    // Check 2: Trùng lặp
                    else if (roomDAO.checkRoomNumberExists(roomNumber, roomId)) {
                        error = "Room Number " + roomNumber + " already exists!";
                    }

                    // Nếu có lỗi -> Quay lại trang Form và báo lỗi
                    if (error != null) {
                        Room room = new Room();
                        room.setRoomId(roomId);
                        room.setRoomNumber(roomNumber);
                        room.setTypeId(typeId);
                        room.setStatus(status);
                        room.setRoomPassword(password);
                        room.setActiveLogin(isActive);
                        
                        request.setAttribute("error", error);
                        request.setAttribute("room", room); // Gửi lại dữ liệu vừa nhập để user không phải nhập lại
                        request.setAttribute("listType", roomDAO.getAllRoomTypes()); // Gửi lại dropdown
                        request.getRequestDispatcher("/WEB-INF/views/room/room-edit&add.jsp").forward(request, response);
                        return; // Dừng xử lý, không chạy code update bên dưới
                    }
                    // ----------------------------

                    // 3. Nếu không có lỗi -> Update
                    Room updateRoom = new Room();
                    updateRoom.setRoomId(roomId);
                    updateRoom.setRoomNumber(roomNumber);
                    updateRoom.setTypeId(typeId);
                    updateRoom.setStatus(status);
                    updateRoom.setRoomPassword(password);
                    updateRoom.setActiveLogin(isActive);

                    // 4. Gọi DAO update
                    roomDAO.updateRoom(updateRoom);

                    // 5. Thêm thông báo thành công vào Session
                    request.getSession().setAttribute("successMessage", "Update room " + roomNumber + " successfully!");

                    // 6. Quay về trang danh sách
                    response.sendRedirect("rooms?action=LIST");
                    break;
                    
                case "CREATE":
                    // 1. Nhận dữ liệu từ form
                    String newRoomNumber = request.getParameter("roomNumber");
                    int newTypeId = Integer.parseInt(request.getParameter("typeId"));
                    String newStatus = request.getParameter("status");
                    String newPassword = request.getParameter("roomPassword");
                    boolean newIsActive = request.getParameter("activeLogin") != null;

                    // --- 2. VALIDATE DỮ LIỆU ---
                    String errorCreate = null;

                    // Check 1: Định dạng số
                    if (!newRoomNumber.matches("\\d+")) {
                        errorCreate = "Room Number must contain only digits (0-9)!";
                    } 
                    // Check 2: Trùng lặp (Truyền ID = 0 vì đang tạo mới)
                    else if (roomDAO.checkRoomNumberExists(newRoomNumber, 0)) {
                        errorCreate = "Room Number " + newRoomNumber + " already exists!";
                    }

                    // Nếu có lỗi -> Quay lại trang Form
                    if (errorCreate != null) {
                        Room roomError = new Room();
                        roomError.setRoomNumber(newRoomNumber);
                        roomError.setTypeId(newTypeId);
                        roomError.setStatus(newStatus);
                        roomError.setRoomPassword(newPassword);
                        roomError.setActiveLogin(newIsActive);
                        
                        request.setAttribute("error", errorCreate);
                        request.setAttribute("room", roomError); // Giữ lại dữ liệu đã nhập
                        request.setAttribute("listType", roomDAO.getAllRoomTypes()); // Gửi lại dropdown
                        request.getRequestDispatcher("/WEB-INF/views/room/room-edit&add.jsp").forward(request, response);
                        return;
                    }
                    // ----------------------------

                    // 3. Nếu không lỗi -> Insert
                    Room newRoom = new Room();
                    newRoom.setRoomNumber(newRoomNumber);
                    newRoom.setTypeId(newTypeId);
                    newRoom.setStatus(newStatus);
                    newRoom.setRoomPassword(newPassword);
                    newRoom.setActiveLogin(newIsActive);

                    roomDAO.insertRoom(newRoom);

                    // 4. Thông báo và chuyển trang
                    request.getSession().setAttribute("successMessage", "Create new room " + newRoomNumber + " successfully!");
                    response.sendRedirect("rooms?action=LIST");
                    break;
                
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