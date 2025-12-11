/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * RoomTypeServlet.java
 */
package controllers;

import dao.RoomTypeDAO;
import models.RoomType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.io.File;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // QUAN TRỌNG
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part; // QUAN TRỌNG: Để xử lý file

/**
 * Servlet quản lý Loại Phòng (Room Types)
 */
@WebServlet(name = "RoomTypeServlet", urlPatterns = {"/admin/room-types"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class RoomTypeServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "LIST";
        }

        RoomTypeDAO dao = new RoomTypeDAO();

        try {
            switch (action) {
                case "LIST":
                    listRoomTypes(request, response, dao);
                    break;
                case "NEW":
                    showNewForm(request, response);
                    break;
                case "CREATE":
                    insertRoomType(request, response, dao);
                    break;
                case "EDIT":
                    showEditForm(request, response, dao);
                    break;
                case "UPDATE":
                    updateRoomType(request, response, dao);
                    break;
                case "DELETE":
                    deleteRoomType(request, response, dao);
                    break;
                default:
                    listRoomTypes(request, response, dao);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + ex.getMessage());
        }
    }

    // --- CÁC HÀM XỬ LÝ ---

    private void listRoomTypes(HttpServletRequest request, HttpServletResponse response, RoomTypeDAO dao)
            throws ServletException, IOException {
        List<RoomType> list = dao.getAllRoomTypes();
        request.setAttribute("listRoomTypes", list);
        request.getRequestDispatcher("/WEB-INF/views/roomtype/room-type-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chúng ta chưa làm file add, tạm thời trỏ về edit form (hoặc tạo file add riêng sau)
        // Để đúng quy trình bài trước, mình trỏ về room-type-add.jsp (bạn sẽ tạo file này sau)
        request.getRequestDispatcher("/WEB-INF/views/roomtype/room-type-add.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, RoomTypeDAO dao)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        RoomType existingType = dao.getRoomTypeById(id);
        request.setAttribute("roomType", existingType);
        request.getRequestDispatcher("/WEB-INF/views/roomtype/room-type-edit.jsp").forward(request, response);
    }

    // --- LOGIC THÊM MỚI (CÓ UPLOAD ẢNH) ---
    private void insertRoomType(HttpServletRequest request, HttpServletResponse response, RoomTypeDAO dao)
            throws IOException, ServletException {
        
        // 1. Nhận các trường Text
        String typeName = request.getParameter("typeName");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String description = request.getParameter("description");
        BigDecimal priceWeekday = new BigDecimal(request.getParameter("basePriceWeekday"));
        BigDecimal priceWeekend = new BigDecimal(request.getParameter("basePriceWeekend"));
        boolean isActive = request.getParameter("isActive") != null;

        // 2. Xử lý Upload Ảnh
        String imageUrl = uploadFile(request); 
        if (imageUrl == null) {
            imageUrl = ""; // Nếu không chọn ảnh, để rỗng hoặc set ảnh mặc định
        }

        RoomType newType = new RoomType();
        newType.setTypeName(typeName);
        newType.setCapacity(capacity);
        newType.setDescription(description);
        newType.setImageUrl(imageUrl); // Lưu đường dẫn ảnh vào DB
        newType.setBasePriceWeekday(priceWeekday);
        newType.setBasePriceWeekend(priceWeekend);
        newType.setIsActive(isActive);

        dao.insertRoomType(newType);

        HttpSession session = request.getSession();
        session.setAttribute("successMessage", "Added new room type successfully!");
        response.sendRedirect("room-types?action=LIST");
    }

    // --- LOGIC CẬP NHẬT (CÓ UPLOAD ẢNH) ---
    private void updateRoomType(HttpServletRequest request, HttpServletResponse response, RoomTypeDAO dao)
            throws IOException, ServletException {
        
        int id = Integer.parseInt(request.getParameter("typeId"));
        String typeName = request.getParameter("typeName");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String description = request.getParameter("description");
        BigDecimal priceWeekday = new BigDecimal(request.getParameter("basePriceWeekday"));
        BigDecimal priceWeekend = new BigDecimal(request.getParameter("basePriceWeekend"));
        boolean isActive = request.getParameter("isActive") != null;

        // Xử lý ảnh:
        // Bước 1: Thử upload ảnh mới
        String imageUrl = uploadFile(request);
        
        // Bước 2: Nếu người dùng KHÔNG chọn ảnh mới (imageUrl == null) -> Lấy lại ảnh cũ từ input hidden
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = request.getParameter("oldImageUrl");
        }

        RoomType rt = new RoomType(id, typeName, capacity, description, imageUrl, priceWeekday, priceWeekend, isActive);
        
        dao.updateRoomType(rt);

        HttpSession session = request.getSession();
        session.setAttribute("successMessage", "Updated room type successfully!");
        response.sendRedirect("room-types?action=LIST");
    }

    private void deleteRoomType(HttpServletRequest request, HttpServletResponse response, RoomTypeDAO dao)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.deleteRoomType(id);
        
        HttpSession session = request.getSession();
        session.setAttribute("successMessage", "Deleted room type successfully!");
        response.sendRedirect("room-types?action=LIST");
    }
    
    // --- HÀM HỖ TRỢ UPLOAD FILE ---
    // Hàm này sẽ lưu file vào folder /images và trả về đường dẫn "/images/tenfile.jpg"
    private String uploadFile(HttpServletRequest request) throws IOException, ServletException {
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + "images";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir(); // Tạo thư mục nếu chưa có

        try {
            Part part = request.getPart("imageFile"); // "imageFile" là name trong thẻ input type="file" bên JSP
            if (part == null || part.getSize() == 0 || part.getSubmittedFileName().isEmpty()) {
                return null; // Không có file được chọn
            }

            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            // Thêm timestamp để tránh trùng tên file
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            
            String fullPath = uploadPath + File.separator + uniqueFileName;
            part.write(fullPath);

            return "/images/" + uniqueFileName; // Trả về đường dẫn tương đối để lưu DB
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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