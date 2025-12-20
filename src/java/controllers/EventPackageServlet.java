/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
 /*
 * EventPackageServlet.java
 */
package controllers;

import dao.EventDAO;
import dao.RoomDAO;
import models.Event;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import models.Room;

@WebServlet(name = "EventPackageServlet", urlPatterns = {"/admin/event-packages"})
public class EventPackageServlet extends HttpServlet {

    private EventDAO eventDAO;

    @Override
    public void init() throws ServletException {
        eventDAO = new EventDAO();
    }

    // Xử lý hiển thị danh sách và xóa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        RoomDAO roomDAO = new RoomDAO(); // Khởi tạo DAO

        try {
            switch (action.toUpperCase()) {

                case "NEW":
// 1. Lấy danh mục (Đã triển khai ở bước 1)
                    request.setAttribute("categories", eventDAO.getAllEventCategories());

                    // 2. Lấy danh sách SẢNH TIỆC (Event Rooms)
                    List<Room> eventRooms = roomDAO.getAllEventRooms();
                    request.setAttribute("rooms", eventRooms);

                    // 3. Chuyển sang form THÊM MỚI
                    request.getRequestDispatcher("/WEB-INF/views/event/event-package-add.jsp").forward(request, response);
                    break;

                case "EDIT":
                    String idStr = request.getParameter("id");
                    if (idStr != null && idStr.matches("\\d+")) {
                        int id = Integer.parseInt(idStr);
                        Event event = eventDAO.getEventById(id);
                        if (event != null) {
                            request.setAttribute("event", event); // Đẩy đối tượng Event lên form
                            request.setAttribute("categories", eventDAO.getAllEventCategories());

                            // ----------------------------------------------------------------
                            // !!! PHẦN BỔ SUNG CẦN THIẾT !!!
                            // 1. Lấy danh sách SẢNH TIỆC (Event Rooms)
                            eventRooms = roomDAO.getAllEventRooms();
                            // 2. Đẩy danh sách phòng vào request scope để JSP sử dụng
                            request.setAttribute("rooms", eventRooms);
                            // ----------------------------------------------------------------

                            // Chuyển sang form CHỈNH SỬA (event-package-edit.jsp)
                            request.getRequestDispatcher("/WEB-INF/views/event/event-package-edit.jsp").forward(request, response);
                        } else {
                            request.getSession().setAttribute("errorMessage", "Event Package not found.");
                            response.sendRedirect("event-packages?action=LIST");
                        }
                    } else {
                        response.sendRedirect("event-packages?action=LIST");
                    }
                    break;

                case "DEACTIVATE":
                case "ACTIVATE":
                    String idStrStatus = request.getParameter("id");

                    // Xác định trạng thái mới và động từ cho thông báo
                    String newStatus = action.equalsIgnoreCase("ACTIVATE") ? "Active" : "Inactive";
                    String verb = action.equalsIgnoreCase("ACTIVATE") ? "activated" : "deactivated";

                    if (idStrStatus != null && idStrStatus.matches("\\d+")) {
                        int id = Integer.parseInt(idStrStatus);

                        // Gọi DAO để cập nhật trạng thái
                        boolean success = eventDAO.updateEventStatus(id, newStatus);

                        if (success) {
                            request.getSession().setAttribute("successMessage", "Successfully " + verb + " Event Package ID: " + id);
                        } else {
                            request.getSession().setAttribute("errorMessage", "Failed to " + verb + " Event Package ID: " + id + ". Database error.");
                        }
                    } else {
                        request.getSession().setAttribute("errorMessage", "Invalid Event ID provided for status update.");
                    }
                    response.sendRedirect("event-packages?action=LIST");
                    break;

                case "LIST":
                default:
                    // --- BẮT ĐẦU LOGIC MỚI: SEARCH & PAGINATION ---

                    // 1. Lấy tham số
                    String keyword = request.getParameter("keyword");
                    String status = request.getParameter("status");
                    String indexPage = request.getParameter("index");

                    if (indexPage == null) {
                        indexPage = "1";
                    }
                    int index = Integer.parseInt(indexPage);

                    // 2. Gọi DAO lấy danh sách FULL (theo điều kiện search)
                    List<Event> fullList = eventDAO.searchEventPackages(keyword, status);

                    // --- PHẦN BỔ SUNG: Chuyển đổi ID thành Name cho cột Location ---
                    // Xử lý chuyển đổi ID -> Name cho từng Event
                    for (Event e : fullList) {
                        if (e.getLocation() != null && !e.getLocation().isEmpty()) {
                            // 1. Gọi hàm DAO bạn đã có
                            List<Room> rooms = roomDAO.getRoomsByIds(e.getLocation());

                            // 2. Trích xuất roomNumber và nối lại thành chuỗi "Room 101, Room 102"
                            List<String> roomNumbers = new ArrayList<>();
                            for (Room r : rooms) {
                                roomNumbers.add(r.getRoomNumber());
                            }
                            String displayLocation = String.join(", ", roomNumbers);

                            // 3. Ghi đè lại vào field location để hiển thị ra JSP
                            e.setLocation(displayLocation);
                        }
                    }

                    // 3. Phân trang (SubList)
                    int count = fullList.size();
                    int pageSize = 5; // Số dòng mỗi trang
                    int endPage = count / pageSize;
                    if (count % pageSize != 0) {
                        endPage++;
                    }

                    int start = (index - 1) * pageSize;
                    int end = Math.min(start + pageSize, count);

                    List<Event> pagedList = new ArrayList<>();
                    if (start < count) {
                        pagedList = fullList.subList(start, end);
                    }

                    // 4. Đẩy dữ liệu ra JSP
                    request.setAttribute("listEventPackages", pagedList);
                    request.setAttribute("endPage", endPage);
                    request.setAttribute("tag", index);

                    // Giữ giá trị Search/Filter
                    request.setAttribute("keyword", keyword);
                    request.setAttribute("status", status);

                    request.getRequestDispatcher("/WEB-INF/views/event/event-package-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("event-packages?error=true");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        String currentEventId = request.getParameter("eventId");

        // Khởi tạo Map để lưu lỗi (Field Name -> Error Message)
        java.util.Map<String, String> errors = new java.util.HashMap<>();

        // Lấy dữ liệu từ form và lưu tạm
        String eventName = request.getParameter("eventName");
        String priceStr = request.getParameter("pricePerTable");
        String catIdStr = request.getParameter("eventCatId");
        String[] roomIdsArray = request.getParameterValues("roomIds");
        String status = request.getParameter("status");

        java.math.BigDecimal price = null;

        try {

            // --- VALIDATION START ---
            // 1. Kiểm tra Package Name (Không được null/empty)
            if (eventName == null || eventName.trim().isEmpty()) {
                errors.put("eventName", "Package Name is required.");
            }

            // 2. Kiểm tra Price (Bắt buộc và phải >= 0)
            if (priceStr == null || priceStr.trim().isEmpty()) {
                errors.put("pricePerTable", "Price per Table is required.");
            } else {
                try {
                    // Đảm bảo loại bỏ dấu phẩy trước khi parse BigDecimal
                    price = new java.math.BigDecimal(priceStr.replace(",", "").replace(".", ""));
                    if (price.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        errors.put("pricePerTable", "Price cannot be negative.");
                    }
                } catch (NumberFormatException e) {
                    errors.put("pricePerTable", "Price format is invalid.");
                }
            }

            // 3. Kiểm tra Associated Room Numbers (Bắt buộc)
            if (roomIdsArray == null || roomIdsArray.length == 0) {
                errors.put("roomIds", "Associated Room Numbers is required.");
            }

            // 4. Kiểm tra trùng tên (Cần DAO hỗ trợ kiểm tra trùng tên khi UPDATE)
            if (eventName != null && errors.get("eventName") == null) {
                // Giả định bạn có hàm checkEventNameExists(name, id) trong DAO
                // TẠM THỜI CHỈ KIỂM TRA CHO INSERT (như code gốc của bạn)
                // Nếu cần kiểm tra cho UPDATE, bạn phải sửa lại DAO.
                if (action.equalsIgnoreCase("INSERT") && eventDAO.checkEventNameExists(eventName.trim())) {
                    errors.put("eventName", "Package Name already exists.");
                }
            }

            // --- VALIDATION END ---
            // Nếu có lỗi, chuyển đến khối xử lý lỗi để forward lại form
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Validation failed. Returning to form.");
            }

            // --- LOGIC INSERT/UPDATE THÀNH CÔNG ---
            String location = (roomIdsArray != null) ? String.join(",", roomIdsArray) : "";
            int catId = Integer.parseInt(catIdStr);

            Event event = new Event();
            event.setEventName(eventName.trim());
            event.setPricePerTable(price);
            event.setEventCatId(catId);
            event.setLocation(location);
            event.setStatus(status);

            String successMessage = "";

            switch (action.toUpperCase()) {
                case "INSERT":
                    eventDAO.insertEvent(event);
                    successMessage = "Successfully created new Event Package!";
                    break;
                case "UPDATE":
                    if (currentEventId != null && !currentEventId.isEmpty()) {
                        event.setEventId(Integer.parseInt(currentEventId));
                        eventDAO.updateEvent(event); // Giả định eventDAO.updateEvent(event) đã hoạt động
                        successMessage = "Successfully updated Event Package ID: " + currentEventId;
                    } else {
                        throw new IllegalArgumentException("Missing Event ID for update.");
                    }
                    break;
                default:
                    response.sendRedirect("event-packages?action=LIST");
                    return;
            }

            // Chuyển hướng về danh sách sau khi thành công
            request.getSession().setAttribute("successMessage", successMessage);
            response.sendRedirect("event-packages?action=LIST");

        } catch (Exception e) {
            // --- XỬ LÝ LỖI (Validation hoặc lỗi DB) ---

            // 1. Đẩy Map lỗi và thông báo lỗi chung
            request.setAttribute("validationErrors", errors);
            if (errors.isEmpty()) {
                // Đây là lỗi DB hoặc lỗi logic khác không phải do validation
                request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
                e.printStackTrace(); // In lỗi ra console để debug
            }

            // 2. TÁI TẠO lại các thuộc tính cần thiết cho form (categories, rooms)
            RoomDAO roomDAO = new RoomDAO();
            try {
                request.setAttribute("categories", eventDAO.getAllEventCategories());
                request.setAttribute("rooms", roomDAO.getAllEventRooms());
            } catch (Exception ex) {
                // Log lỗi nếu không thể tải categories/rooms
                ex.printStackTrace();
            }

            String forwardPath = "/WEB-INF/views/event/event-package-add.jsp";

            // *** ĐIỀU CHỈNH QUAN TRỌNG: XỬ LÝ FORWARD KHI UPDATE LỖI ***
            if (action.equalsIgnoreCase("UPDATE")) {
                // Tái tạo đối tượng Event để giữ lại các trường đã nhập trên form EDIT
                Event eventForEdit = new Event();
                if (currentEventId != null && currentEventId.matches("\\d+")) {
                    eventForEdit.setEventId(Integer.parseInt(currentEventId));
                }
                // Tái tạo các giá trị đã nhập bị lỗi
                eventForEdit.setEventName(eventName);
                eventForEdit.setPricePerTable(price);
                // Location không cần set vì JSP sẽ lấy từ paramValues.roomIds
                eventForEdit.setStatus(status);

                request.setAttribute("event", eventForEdit); // Cung cấp lại cho form Edit
                forwardPath = "/WEB-INF/views/event/event-package-edit.jsp";
            }

            // 3. FORWARD quay lại form (giữ lại request attributes)
            request.getRequestDispatcher(forwardPath).forward(request, response);
        }
    }

}
