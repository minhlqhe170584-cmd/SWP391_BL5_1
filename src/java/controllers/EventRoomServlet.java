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

    // ========================================================================
    // doGet: XỬ LÝ ĐIỀU HƯỚNG & HIỂN THỊ (List, Form Add, Form Edit, Lock)
    // ========================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                
                // 1. HIỂN THỊ DANH SÁCH (Kèm Search & Filter)
                case "LIST":
                    List<RoomType> listType = roomDAO.getAllEventRoomTypes();
                    request.setAttribute("listType", listType);

                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String minCap = request.getParameter("minCapacity");
                    String maxCap = request.getParameter("maxCapacity");
                    String active = request.getParameter("active");

                    List<Room> fullList = roomDAO.findEventRooms(keyword, typeId, minCap, maxCap, active);

                    // Phân trang
                    int count = fullList.size();
                    int endPage = count / 5;
                    if (count % 5 != 0) endPage++;

                    String indexPage = request.getParameter("index");
                    if (indexPage == null) indexPage = "1";
                    int index = 1;
                    try { index = Integer.parseInt(indexPage); } catch(Exception e) {}

                    int start = (index - 1) * 5;
                    int end = Math.min(start + 5, count);
                    List<Room> pagedList = new ArrayList<>();
                    if (start < count) pagedList = fullList.subList(start, end);

                    request.setAttribute("eventRooms", pagedList);
                    request.setAttribute("endPage", endPage);
                    request.setAttribute("tag", index);
                    request.setAttribute("totalRooms", count);

                    // Gửi lại tham số
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("currentType", typeId);
                    request.setAttribute("minCapacity", minCap);
                    request.setAttribute("maxCapacity", maxCap);
                    request.setAttribute("currentActive", active);

                    boolean isFiltering = (keyword != null && !keyword.isEmpty()) ||
                                          (typeId != null && !typeId.isEmpty()) ||
                                          (minCap != null && !minCap.isEmpty()) ||
                                          (maxCap != null && !maxCap.isEmpty()) ||
                                          (active != null && !active.isEmpty());
                    request.setAttribute("isFiltering", isFiltering);

                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-list.jsp").forward(request, response);
                    break;

                // 2. HIỂN THỊ FORM THÊM MỚI
                case "NEW":
                    request.setAttribute("listType", roomDAO.getAllEventRoomTypes());
                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-add.jsp").forward(request, response);
                    break;

                // 3. HIỂN THỊ FORM CHỈNH SỬA
                case "EDIT":
                    try {
                        int id = Integer.parseInt(request.getParameter("id"));
                        Room room = roomDAO.getEventRoomById(id);
                        if (room != null) {
                            request.setAttribute("room", room);
                            request.setAttribute("listType", roomDAO.getAllEventRoomTypes());
                            request.getRequestDispatcher("/WEB-INF/views/event/event-room-edit.jsp").forward(request, response);
                        } else {
                            response.sendRedirect("event-rooms?action=LIST");
                        }
                    } catch (Exception e) {
                        response.sendRedirect("event-rooms?action=LIST");
                    }
                    break;

                // 4. XỬ LÝ KHÓA NHANH (LOCK/UNLOCK)
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
                                
                                String msg = newLockState ? "Unlocked hall " + room.getRoomNumber() + " successfully!" 
                                                          : "Locked hall " + room.getRoomNumber() + " successfully!";
                                request.getSession().setAttribute("successMessage", msg);
                            }
                        } catch (Exception e) {}
                    }
                    response.sendRedirect("event-rooms?action=LIST");
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

    // ========================================================================
    // doPost: XỬ LÝ DỮ LIỆU FORM (CREATE, UPDATE)
    // ========================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("CREATE".equals(action)) {
                // 1. Nhận dữ liệu
                String roomNumber = request.getParameter("roomNumber");
                int typeId = Integer.parseInt(request.getParameter("typeId"));
                String password = request.getParameter("roomPassword");
                boolean isActive = request.getParameter("activeLogin") != null;

                // 2. Validate (Gửi biến roomNumberError để hiện chữ đỏ)
                if (roomDAO.checkRoomNumberExists(roomNumber, 0)) {
                    request.setAttribute("roomNumberError", "Hall Name '" + roomNumber + "' already exists!");
                    
                    // Giữ lại dữ liệu cũ
                    Room r = new Room();
                    r.setRoomNumber(roomNumber); r.setTypeId(typeId); r.setRoomPassword(password); r.setActiveLogin(isActive);
                    request.setAttribute("room", r);
                    request.setAttribute("listType", roomDAO.getAllEventRoomTypes());
                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-add.jsp").forward(request, response);
                    return;
                }

                // 3. Insert
                Room newRoom = new Room();
                newRoom.setRoomNumber(roomNumber);
                newRoom.setTypeId(typeId);
                newRoom.setStatus("Available");
                newRoom.setRoomPassword(password);
                newRoom.setActiveLogin(isActive);
                roomDAO.insertEventRoom(newRoom); 

                request.getSession().setAttribute("successMessage", "New Event Hall created successfully!");
                response.sendRedirect("event-rooms?action=LIST");

            } else if ("UPDATE".equals(action)) {
                // 1. Nhận dữ liệu
                int roomId = Integer.parseInt(request.getParameter("roomId"));
                String roomNumber = request.getParameter("roomNumber");
                int typeId = Integer.parseInt(request.getParameter("typeId"));
                String password = request.getParameter("roomPassword");
                boolean isActive = request.getParameter("activeLogin") != null;

                // 2. Validate (Gửi biến roomNumberError để hiện chữ đỏ)
                if (roomDAO.checkRoomNumberExists(roomNumber, roomId)) {
                    request.setAttribute("roomNumberError", "Hall Name '" + roomNumber + "' is already taken!");
                    
                    Room r = roomDAO.getEventRoomById(roomId);
                    // Override dữ liệu nhập
                    r.setRoomNumber(roomNumber); r.setTypeId(typeId); r.setRoomPassword(password); r.setActiveLogin(isActive);
                    
                    request.setAttribute("room", r);
                    request.setAttribute("listType", roomDAO.getAllEventRoomTypes());
                    request.getRequestDispatcher("/WEB-INF/views/event/event-room-edit.jsp").forward(request, response);
                    return;
                }

                // 3. Update
                Room room = roomDAO.getEventRoomById(roomId);
                room.setRoomNumber(roomNumber);
                room.setTypeId(typeId);
                room.setRoomPassword(password);
                room.setActiveLogin(isActive);
                roomDAO.updateRoom(room);

                request.getSession().setAttribute("successMessage", "Hall updated successfully!");
                response.sendRedirect("event-rooms?action=LIST");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("event-rooms?action=LIST");
        }
    }
}