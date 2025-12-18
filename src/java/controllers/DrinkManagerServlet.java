package controllers;

import dao.DrinkDAO;
import models.Drink;
import models.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "DrinkManagerServlet", urlPatterns = {"/admin/drinks"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class DrinkManagerServlet extends HttpServlet {

    private DrinkDAO drinkDAO;

    @Override
    public void init() throws ServletException {
        drinkDAO = new DrinkDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listDrinks(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createDrink(request, response);
                    break;
                case "update":
                    updateDrink(request, response);
                    break;
                case "deactivate":
                    toggleStatus(request, response, false);
                    break;
                case "activate":
                    toggleStatus(request, response, true);
                    break;
                default:
                    response.sendRedirect("drinks");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("drinks");
        }
    }

    // --- 1. XỬ LÝ UPLOAD FILE (VALIDATION CHẶT CHẼ) ---
    private String handleFileUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("imageFile");

        if (filePart == null || filePart.getSize() == 0 || filePart.getSubmittedFileName().isEmpty()) {
            return null; // Không chọn file
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Kiểm tra MIME Type (Quan trọng)
        String mimeType = filePart.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new ServletException("File không hợp lệ! Vui lòng chỉ tải lên file ảnh.");
        }

        // Kiểm tra đuôi file
        String ext = "";
        if (fileName.contains(".")) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        if (!ext.equals("jpg") && !ext.equals("png") && !ext.equals("jpeg") && !ext.equals("gif")) {
            throw new ServletException("Chỉ chấp nhận đuôi file: .jpg, .png, .jpeg, .gif");
        }

        // Lưu file vật lý
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        filePart.write(uploadPath + File.separator + fileName);
        return fileName;
    }

    // --- 2. HÀM TẠO MỚI (CREATE) ---
    private void createDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String fileName = handleFileUpload(request); // Validate file trước

            Drink d = new Drink();
            d.setName(request.getParameter("name").trim()); // Map cột drink_name
            d.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            d.setDescription(request.getParameter("description"));
            d.setIsActive(true);

            // Xử lý Price (Double)
            try {
                d.setPrice(Double.parseDouble(request.getParameter("price")));
            } catch (NumberFormatException e) {
                throw new ServletException("Giá tiền phải là số hợp lệ!");
            }

            // Xử lý Volume (Int) - SỬA LẠI THÀNH INTEGER
            try {
                String volStr = request.getParameter("volumeMl");
                d.setVolumeMl((volStr != null && !volStr.isEmpty()) ? Integer.parseInt(volStr) : 0);
            } catch (NumberFormatException e) {
                d.setVolumeMl(0);
            }

            // Xử lý Alcoholic (Checkbox)
            // Nếu checkbox được tích -> request trả về value (ví dụ "true"), nếu không tích -> trả về null
            d.setIsAlcoholic(request.getParameter("isAlcoholic") != null);

            // Xử lý ảnh
            d.setImageUrl(fileName != null ? fileName : "default.jpg");

            drinkDAO.createDrink(d);
            request.getSession().setAttribute("message", "Thêm đồ uống thành công!");
            response.sendRedirect("drinks");

        } catch (ServletException e) {
            // Lỗi Validate (File sai, giá sai...) -> Quay lại Form Add
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("drinks?action=add");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("drinks");
        }
    }

    // --- 3. HÀM CẬP NHẬT (UPDATE) ---
    private void updateDrink(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("drinkId");
        try {
            String fileName = handleFileUpload(request); // Validate file trước

            int id = Integer.parseInt(idStr);
            Drink d = drinkDAO.getDrinkById(id); // Lấy thông tin cũ

            d.setName(request.getParameter("name").trim());
            d.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            d.setDescription(request.getParameter("description"));

            try {
                d.setPrice(Double.parseDouble(request.getParameter("price")));
            } catch (NumberFormatException e) {
                throw new ServletException("Giá tiền phải là số!");
            }

            // Xử lý Volume (Int)
            try {
                String volStr = request.getParameter("volumeMl");
                d.setVolumeMl((volStr != null && !volStr.isEmpty()) ? Integer.parseInt(volStr) : 0);
            } catch (NumberFormatException e) {
                d.setVolumeMl(0);
            }

            // Xử lý Alcoholic
            d.setIsAlcoholic(request.getParameter("isAlcoholic") != null);

            // Logic ảnh: Nếu có ảnh mới -> thay thế. Không -> giữ nguyên.
            if (fileName != null) {
                d.setImageUrl(fileName);
            }

            drinkDAO.updateDrink(d);
            request.getSession().setAttribute("message", "Cập nhật thành công!");
            response.sendRedirect("drinks");

        } catch (ServletException e) {
            // Lỗi Validate -> Quay lại Form Edit
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("drinks?action=edit&drinkId=" + idStr);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("drinks");
        }
    }

    // --- CÁC HÀM PHỤ TRỢ ---
    private void listDrinks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String serviceFilter = request.getParameter("serviceFilter");
        int pageIndex = 1;
        try {
            String page = request.getParameter("page");
            if (page != null) {
                pageIndex = Integer.parseInt(page);
            }
        } catch (Exception e) {
        }

        List<Drink> drinks = drinkDAO.getDrinks(keyword, serviceFilter, pageIndex, 5);
        int totalRecords = drinkDAO.countDrinks(keyword, serviceFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / 5);

        request.setAttribute("drinksList", drinks);
        request.setAttribute("listServices", drinkDAO.getAllServices());
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkList.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listServices", drinkDAO.getAllServices());
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkDetail.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("drinkId"));
        request.setAttribute("drink", drinkDAO.getDrinkById(id));
        request.setAttribute("listServices", drinkDAO.getAllServices());
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkDetail.jsp").forward(request, response);
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response, boolean status) throws Exception {
        int id = Integer.parseInt(request.getParameter("drinkId"));
        if (status) {
            drinkDAO.activateDrink(id);
        } else {
            drinkDAO.deactivateDrink(id);
        }
        request.getSession().setAttribute("message", "Đã cập nhật trạng thái!");
        response.sendRedirect("drinks");
    }
}
