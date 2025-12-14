/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /*
 * EventRoomServlet.java
 */
package controllers;

import dao.RoomDAO;
import models.Room;
import models.RoomType;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "EventRoomServlet", urlPatterns = {"/admin/event-rooms"})
public class EventRoomServlet extends HttpServlet {

    private RoomDAO roomDAO;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        try {
            switch (action.toUpperCase()) {
                case "LOCK":
                    String idLockStr = request.getParameter("id");
                    if (idLockStr != null) {
                        try {
                            int id = Integer.parseInt(idLockStr);
                            Room room = roomDAO.getEventRoomById(id);
                            
                            if (room != null) {
                                boolean newLockState = !room.isActiveLogin();
                                room.setActiveLogin(newLockState);
                                roomDAO.updateRoom(room);
                                
                                // --- [THÊM ĐOẠN NÀY] ---
                                String msg = newLockState ? "Unlocked hall " + room.getRoomNumber() + " successfully!" : "Locked hall " + room.getRoomNumber() + " successfully!";
                                request.getSession().setAttribute("successMessage", msg);
                                // -----------------------
                            } else {
                                request.getSession().setAttribute("errorMessage", "Hall ID not found!");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    response.sendRedirect("event-rooms?action=LIST");
                    break;
                    
                case "LIST":
                    // 1. Chuẩn bị dữ liệu Dropdown
                    List<RoomType> listType = roomDAO.getAllEventRoomTypes();
                    request.setAttribute("listType", listType);

                    // 2. Lấy tham số từ Form Filter (5 tham số)
                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String minCap = request.getParameter("minCapacity");
                    String maxCap = request.getParameter("maxCapacity");
                    String active = request.getParameter("active"); // <--- [MỚI]

                    // 3. Gọi hàm DAO (Đã cập nhật nhận 5 tham số)
                    // Hàm này sẽ xử lý logic AND: Tên + Loại + Sức chứa + Trạng thái Login
                    List<Room> fullList = roomDAO.findEventRooms(keyword, typeId, minCap, maxCap, active);

                    // 4. Xử lý phân trang trên List kết quả
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

                    List<Room> pagedList = new ArrayList<>();
                    if (start < count) {
                        pagedList = fullList.subList(start, end);
                    }

                    // 5. Gửi dữ liệu hiển thị về JSP
                    request.setAttribute("eventRooms", pagedList);
                    request.setAttribute("endPage", endPage);
                    request.setAttribute("tag", index);
                    request.setAttribute("totalRooms", count); 

                    // 6. Gửi lại tham số để giữ trạng thái Form và Link phân trang
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("currentType", typeId);
                    request.setAttribute("minCapacity", minCap);
                    request.setAttribute("maxCapacity", maxCap);
                    request.setAttribute("currentActive", active); // <--- [MỚI]

                    // 7. Biến cờ để hiện nút Reset
                    boolean isFiltering = (keyword != null && !keyword.trim().isEmpty())
                            || (typeId != null && !typeId.isEmpty())
                            || (minCap != null && !minCap.isEmpty())
                            || (maxCap != null && !maxCap.isEmpty())
                            || (active != null && !active.isEmpty()); // <--- [MỚI]
                    
                    request.setAttribute("isFiltering", isFiltering);

                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-list.jsp").forward(request, response);
                    break;

                case "NEW":
                    // Chuyển sang form thêm mới
                    List<RoomType> listTypeNew = roomDAO.getAllEventRoomTypes();
                    request.setAttribute("listType", listTypeNew);
                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-add.jsp").forward(request, response);
                    break;

                default:
                    response.sendRedirect("event-rooms?action=LIST");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
