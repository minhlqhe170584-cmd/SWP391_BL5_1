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

@WebServlet(name = "RoomServlet", urlPatterns = {"/admin/rooms"})
public class RoomServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RoomServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoomServlet</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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

                // === 1. ĐỔI TỪ BAN (STATUS) SANG LOCK (ACTIVE LOGIN) ===
                case "LOCK":
                    String idLockStr = request.getParameter("id");
                    if (idLockStr != null) {
                        try {
                            int id = Integer.parseInt(idLockStr);
                            // Lấy thông tin phòng hiện tại
                            Room room = roomDAO.getRoomById(id);

                            if (room != null) {
                                // Logic: Đảo ngược trạng thái Active Login (True <-> False)
                                boolean newLockState = !room.isActiveLogin();
                                room.setActiveLogin(newLockState);

                                // Cập nhật lại vào DB (Dùng hàm updateRoom chung để lưu thay đổi)
                                roomDAO.updateRoom(room);

                                String msg = newLockState ? "Unlocked room (Login Allowed)" : "Locked room (Login Disabled)";
                                request.getSession().setAttribute("successMessage", msg);
                            }
                        } catch (NumberFormatException e) {
                            // Bỏ qua lỗi ID
                        }
                    }
                    response.sendRedirect("rooms?action=LIST");
                    break;

                // === 2. HIỂN THỊ FORM SỬA ===
                case "EDIT":
                    String idEdit = request.getParameter("id");
                    if (idEdit != null) {
                        try {
                            int id = Integer.parseInt(idEdit);
                            Room room = roomDAO.getRoomById(id);
                            List<models.RoomType> listType = roomDAO.getAllRoomTypes();

                            request.setAttribute("room", room);
                            request.setAttribute("listType", listType);
                            request.getRequestDispatcher("/WEB-INF/views/room/room-edit.jsp").forward(request, response);
                        } catch (NumberFormatException e) {
                            response.sendRedirect("rooms?action=LIST");
                        }
                    } else {
                        response.sendRedirect("rooms?action=LIST");
                    }
                    break;

                // === 3. HIỂN THỊ FORM TẠO MỚI ===
                case "NEW":
                    List<models.RoomType> listTypeNew = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listTypeNew);
                    request.getRequestDispatcher("/WEB-INF/views/room/room-add.jsp").forward(request, response);
                    break;

                // === 4. XEM CHI TIẾT ===
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

                // === ĐÃ BỎ CASE DELETE ===
                // === 5. DANH SÁCH (ĐÃ BỎ LỌC STATUS) ===
                case "LIST":
                default:
                    List<models.RoomType> listType = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listType);
                    request.setAttribute("listFloors", roomDAO.getExistingFloors());

                    // Lấy tham số Filter (Đã bỏ status)
                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String active = request.getParameter("active");
                    String floor = request.getParameter("floor");

                    boolean isFiltering = (keyword != null && !keyword.trim().isEmpty())
                            || (typeId != null && !typeId.isEmpty())
                            || (active != null && !active.isEmpty())
                            || (floor != null && !floor.isEmpty());

                    request.setAttribute("isFiltering", isFiltering);

                    if (isFiltering) {
                        // Truyền null vào vị trí status
                        List<Room> fullList = roomDAO.findRooms(keyword, typeId, null, active, floor);

                        int count = fullList.size();
                        int endPage = count / 5;
                        if (count % 5 != 0) {
                            endPage++;
                        }
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) {
                            indexPage = "1";
                        }
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

                        request.setAttribute("keyword", keyword);
                        request.setAttribute("currentType", typeId);
                        request.setAttribute("currentActive", active);
                        request.setAttribute("currentFloor", floor);
                    } else {
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) {
                            indexPage = "1";
                        }
                        int index = Integer.parseInt(indexPage);
                        int count = roomDAO.getTotalRooms();
                        int endPage = count / 5;
                        if (count % 5 != 0) {
                            endPage++;
                        }
                        List<Room> list = roomDAO.pagingRooms(index);
                        request.setAttribute("roomsList", list);
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                    }
                    request.getRequestDispatcher("/WEB-INF/views/room/room-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        try {
            switch (action.toUpperCase()) {
                case "UPDATE":
                    // 1. Nhận dữ liệu (Đã bỏ status)
                    int roomId = Integer.parseInt(request.getParameter("roomId"));
                    String roomNumber = request.getParameter("roomNumber");
                    int typeId = Integer.parseInt(request.getParameter("typeId"));
                    String password = request.getParameter("roomPassword");
                    boolean isActive = request.getParameter("activeLogin") != null;

                    String error = null;
                    if (!roomNumber.matches("\\d+")) {
                        error = "Room Number must contain only digits (0-9)!";
                    } else if (roomDAO.checkRoomNumberExists(roomNumber, roomId)) {
                        error = "Room Number " + roomNumber + " already exists!";
                    }

                    if (error != null) {
                        Room room = new Room();
                        room.setRoomId(roomId);
                        room.setRoomNumber(roomNumber);
                        room.setTypeId(typeId);
                        room.setRoomPassword(password);
                        room.setActiveLogin(isActive);

                        request.setAttribute("error", error);
                        request.setAttribute("room", room);
                        request.setAttribute("listType", roomDAO.getAllRoomTypes());
                        request.getRequestDispatcher("/WEB-INF/views/room/room-edit.jsp").forward(request, response);
                        return;
                    }

                    // Lấy phòng cũ để giữ lại Status cũ (vì form không còn gửi lên)
                    Room oldRoom = roomDAO.getRoomById(roomId);
                    String currentStatus = (oldRoom != null) ? oldRoom.getStatus() : "Available";

                    Room updateRoom = new Room();
                    updateRoom.setRoomId(roomId);
                    updateRoom.setRoomNumber(roomNumber);
                    updateRoom.setTypeId(typeId);
                    updateRoom.setStatus(currentStatus); // Giữ nguyên status cũ
                    updateRoom.setRoomPassword(password);
                    updateRoom.setActiveLogin(isActive);

                    roomDAO.updateRoom(updateRoom);
                    request.getSession().setAttribute("successMessage", "Update room " + roomNumber + " successfully!");
                    response.sendRedirect("rooms?action=LIST");
                    break;

                case "CREATE":
                    String newRoomNumber = request.getParameter("roomNumber");
                    String newTypeIdStr = request.getParameter("typeId");
                    String newStatus = request.getParameter("status"); // Lấy cả status từ form
                    String newPassword = request.getParameter("roomPassword");
                    String activeLoginVal = request.getParameter("activeLogin"); // Lấy raw value để setAttribute

                    int newTypeId = Integer.parseInt(newTypeIdStr);
                    boolean newIsActive = (activeLoginVal != null);

                    String errorCreate = null;
                    if (!newRoomNumber.matches("\\d+")) {
                        errorCreate = "Room Number must contain only digits (0-9)!";
                    } else if (roomDAO.checkRoomNumberExists(newRoomNumber, 0)) {
                        errorCreate = "Room Number " + newRoomNumber + " already exists!";
                    }

                    if (errorCreate != null) {
                        request.setAttribute("error", errorCreate);

                        // --- QUAN TRỌNG: Gửi lại từng biến lẻ để JSP room-add.jsp nhận được ---
                        request.setAttribute("roomNumber", newRoomNumber);
                        request.setAttribute("typeId", newTypeId); // Tự động unbox sang int/string đều được
                        request.setAttribute("status", newStatus);
                        request.setAttribute("roomPassword", newPassword);
                        request.setAttribute("activeLogin", activeLoginVal); // Gửi chuỗi "true" hoặc null

                        request.setAttribute("listType", roomDAO.getAllRoomTypes());
                        request.getRequestDispatcher("/WEB-INF/views/room/room-add.jsp").forward(request, response);
                        return;
                    }

                    Room newRoom = new Room();
                    newRoom.setRoomNumber(newRoomNumber);
                    newRoom.setTypeId(newTypeId);
                    newRoom.setStatus(newStatus); // Dùng status người dùng chọn
                    newRoom.setRoomPassword(newPassword);
                    newRoom.setActiveLogin(newIsActive);

                    roomDAO.insertRoom(newRoom);
                    request.getSession().setAttribute("successMessage", "Create new room " + newRoomNumber + " successfully!");
                    response.sendRedirect("rooms?action=LIST");
                    break;
                case "LIST":
                default:
                    response.sendRedirect(request.getContextPath() + "/rooms");
                    break;
            }
        } catch (Exception ex) {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("<h1>ERROR</h1>");
                out.println("<pre>" + ex.getMessage() + "</pre>");
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Room Management Servlet";
    }
}
