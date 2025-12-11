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
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
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
                        } catch (NumberFormatException e) {}
                    }
                    response.sendRedirect("rooms?action=LIST");
                    break;

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

                case "NEW":
                    List<models.RoomType> listTypeNew = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listTypeNew);
                    request.getRequestDispatcher("/WEB-INF/views/room/room-edit&add.jsp").forward(request, response);
                    break;    

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
                
                case "DELETE":
                    String idDel = request.getParameter("id");
                    if (idDel != null) {
                        try {
                            int id = Integer.parseInt(idDel);
                            Room roomToDelete = roomDAO.getRoomById(id);
                            if (roomToDelete != null) {
                                String roomNum = roomToDelete.getRoomNumber(); 
                                roomDAO.deleteRoom(id);
                                request.getSession().setAttribute("successMessage", "Deleted room " + roomNum + " successfully!");
                            } else {
                                request.getSession().setAttribute("errorMessage", "Room not found to delete!");
                            }
                        } catch (Exception e) {}
                    }
                    response.sendRedirect("rooms?action=LIST");
                    break;

                case "LIST":
                default:
                    List<models.RoomType> listType = roomDAO.getAllRoomTypes();
                    request.setAttribute("listType", listType);
                    request.setAttribute("listFloors", roomDAO.getExistingFloors());

                    String keyword = request.getParameter("keyword");
                    String typeId = request.getParameter("typeId");
                    String status = request.getParameter("status");
                    String active = request.getParameter("active");
                    String floor = request.getParameter("floor");

                    boolean isFiltering = (keyword != null && !keyword.trim().isEmpty()) ||
                                          (typeId != null && !typeId.isEmpty()) ||
                                          (status != null && !status.isEmpty()) ||
                                          (active != null && !active.isEmpty()) ||
                                          (floor != null && !floor.isEmpty());
                    
                    request.setAttribute("isFiltering", isFiltering);

                    if (isFiltering) {
                        List<Room> fullList = roomDAO.findRooms(keyword, typeId, status, active, floor);
                        int count = fullList.size();
                        int endPage = count / 5; if (count % 5 != 0) endPage++;
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) indexPage = "1";
                        int index = Integer.parseInt(indexPage);
                        int start = (index - 1) * 5;
                        int end = Math.min(start + 5, count);
                        List<Room> pagedList = new java.util.ArrayList<>();
                        if (start < count) pagedList = fullList.subList(start, end);
                        
                        request.setAttribute("roomsList", pagedList);
                        request.setAttribute("endPage", endPage);
                        request.setAttribute("tag", index);
                        
                        request.setAttribute("keyword", keyword);
                        request.setAttribute("currentType", typeId);
                        request.setAttribute("currentStatus", status);
                        request.setAttribute("currentActive", active);
                        request.setAttribute("currentFloor", floor);
                    } else {
                        String indexPage = request.getParameter("index");
                        if (indexPage == null) indexPage = "1";
                        int index = Integer.parseInt(indexPage);
                        int count = roomDAO.getTotalRooms();
                        int endPage = count / 5; if (count % 5 != 0) endPage++;
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
        if (action == null) action = "LIST";

        try {
            switch (action.toUpperCase()) {
                case "UPDATE":
                    int roomId = Integer.parseInt(request.getParameter("roomId"));
                    String roomNumber = request.getParameter("roomNumber");
                    int typeId = Integer.parseInt(request.getParameter("typeId"));
                    String status = request.getParameter("status");
                    String password = request.getParameter("roomPassword");
                    boolean isActive = request.getParameter("activeLogin") != null;

                    String error = null;
                    if (!roomNumber.matches("\\d+")) error = "Room Number must contain only digits (0-9)!";
                    else if (roomDAO.checkRoomNumberExists(roomNumber, roomId)) error = "Room Number " + roomNumber + " already exists!";

                    if (error != null) {
                        Room room = new Room();
                        room.setRoomId(roomId);
                        room.setRoomNumber(roomNumber);
                        room.setTypeId(typeId);
                        room.setStatus(status);
                        room.setRoomPassword(password);
                        room.setActiveLogin(isActive);
                        
                        request.setAttribute("error", error);
                        request.setAttribute("room", room); 
                        request.setAttribute("listType", roomDAO.getAllRoomTypes()); 
                        request.getRequestDispatcher("/WEB-INF/views/room/room-edit.jsp").forward(request, response);
                        return; 
                    }
                    
                    Room updateRoom = new Room();
                    updateRoom.setRoomId(roomId);
                    updateRoom.setRoomNumber(roomNumber);
                    updateRoom.setTypeId(typeId);
                    updateRoom.setStatus(status);
                    updateRoom.setRoomPassword(password);
                    updateRoom.setActiveLogin(isActive);

                    roomDAO.updateRoom(updateRoom);
                    request.getSession().setAttribute("successMessage", "Update room " + roomNumber + " successfully!");
                    response.sendRedirect("rooms?action=LIST");
                    break;
                    
                case "CREATE":
                    String newRoomNumber = request.getParameter("roomNumber");
                    int newTypeId = Integer.parseInt(request.getParameter("typeId"));
                    String newStatus = request.getParameter("status");
                    String newPassword = request.getParameter("roomPassword");
                    boolean newIsActive = request.getParameter("activeLogin") != null;

                    String errorCreate = null;
                    if (!newRoomNumber.matches("\\d+")) errorCreate = "Room Number must contain only digits (0-9)!";
                    else if (roomDAO.checkRoomNumberExists(newRoomNumber, 0)) errorCreate = "Room Number " + newRoomNumber + " already exists!";

                    if (errorCreate != null) {
                        Room roomError = new Room();
                        roomError.setRoomNumber(newRoomNumber);
                        roomError.setTypeId(newTypeId);
                        roomError.setStatus(newStatus);
                        roomError.setRoomPassword(newPassword);
                        roomError.setActiveLogin(newIsActive);
                        
                        request.setAttribute("error", errorCreate);
                        request.setAttribute("room", roomError); 
                        request.setAttribute("listType", roomDAO.getAllRoomTypes()); 
                        request.getRequestDispatcher("/WEB-INF/views/room/room-add.jsp").forward(request, response);
                        return;
                    }

                    Room newRoom = new Room();
                    newRoom.setRoomNumber(newRoomNumber);
                    newRoom.setTypeId(newTypeId);
                    newRoom.setStatus(newStatus);
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
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Room Management Servlet";
    }
}