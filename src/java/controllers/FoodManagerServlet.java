package controllers;

import dao.FoodDAO;
import models.Food;
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

@WebServlet(name = "FoodManagerServlet", urlPatterns = {"/admin/foods"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class FoodManagerServlet extends HttpServlet {

    private FoodDAO foodDAO;

    @Override
    public void init() throws ServletException {
        foodDAO = new FoodDAO();
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
                    listFoods(request, response);
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
                    createFood(request, response);
                    break;
                case "update":
                    updateFood(request, response);
                    break;
                case "deactivate":
                    toggleStatus(request, response, false);
                    break;
                case "activate":
                    toggleStatus(request, response, true);
                    break;
                default:
                    response.sendRedirect("foods");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect("foods");
        }
    }

    // --- 1. HÀM UPLOAD CÓ VALIDATION (CHẶN FILE RÁC) ---
    private String handleFileUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("imageFile");

        // Nếu không chọn file -> Return null (để giữ ảnh cũ)
        if (filePart == null || filePart.getSize() == 0 || filePart.getSubmittedFileName().isEmpty()) {
            return null;
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // A. Kiểm tra loại file (MIME Type)
        String mimeType = filePart.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new ServletException("File tải lên không hợp lệ! Vui lòng chỉ chọn file ảnh.");
        }

        // B. Kiểm tra đuôi file
        String ext = "";
        if (fileName.contains(".")) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
        if (!ext.equals("jpg") && !ext.equals("png") && !ext.equals("jpeg") && !ext.equals("gif")) {
            throw new ServletException("Chỉ chấp nhận định dạng: .jpg, .png, .jpeg, .gif");
        }

        // C. Lưu file
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        filePart.write(uploadPath + File.separator + fileName);
        return fileName;
    }

    // --- 2. TẠO MỚI (XỬ LÝ LỖI -> QUAY LẠI FORM) ---
    private void createFood(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String fileName = handleFileUpload(request); // Có thể ném lỗi tại đây

            Food f = new Food();
            f.setName(request.getParameter("name").trim());
            f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            f.setDescription(request.getParameter("description"));
            f.setIsActive(true);

            try {
                f.setPrice(Double.parseDouble(request.getParameter("price")));
            } catch (NumberFormatException e) {
                throw new ServletException("Giá tiền phải là số hợp lệ!");
            }

            f.setImageUrl(fileName != null ? fileName : "default.jpg");

            foodDAO.createFood(f);
            request.getSession().setAttribute("message", "Thêm món mới thành công!");
            response.sendRedirect("foods");

        } catch (ServletException e) {
            // Lỗi Validate -> Quay lại trang ADD để hiện thông báo lỗi
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("foods?action=add");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("foods");
        }
    }

    // --- 3. CẬP NHẬT (XỬ LÝ LỖI -> QUAY LẠI FORM) ---
    private void updateFood(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("foodId");
        try {
            String fileName = handleFileUpload(request);

            int id = Integer.parseInt(idStr);
            Food f = foodDAO.getFoodById(id); // Lấy thông tin cũ

            f.setName(request.getParameter("name").trim());
            f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            f.setDescription(request.getParameter("description"));

            try {
                f.setPrice(Double.parseDouble(request.getParameter("price")));
            } catch (NumberFormatException e) {
                throw new ServletException("Giá tiền phải là số!");
            }

            // Chỉ thay ảnh nếu có upload mới
            if (fileName != null) {
                f.setImageUrl(fileName);
            }

            foodDAO.updateFood(f);
            request.getSession().setAttribute("message", "Cập nhật thành công!");
            response.sendRedirect("foods");

        } catch (ServletException e) {
            // Lỗi Validate -> Quay lại trang EDIT
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("foods?action=edit&foodId=" + idStr);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("foods");
        }
    }

    // --- CÁC HÀM PHỤ TRỢ ---
    private void listFoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        List<Food> foods = foodDAO.getFoods(keyword, serviceFilter, pageIndex, 5);
        int totalRecords = foodDAO.countFoods(keyword, serviceFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / 5);

        request.setAttribute("foodsList", foods);
        request.setAttribute("listServices", foodDAO.getAllServices());
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/food/foodList.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listServices", foodDAO.getAllServices());
        request.getRequestDispatcher("/WEB-INF/views/food/foodDetail.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("foodId"));
        request.setAttribute("food", foodDAO.getFoodById(id));
        request.setAttribute("listServices", foodDAO.getAllServices());
        request.getRequestDispatcher("/WEB-INF/views/food/foodDetail.jsp").forward(request, response);
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response, boolean status) throws Exception {
        int id = Integer.parseInt(request.getParameter("foodId"));
        if (status) {
            foodDAO.activateFood(id);
        } else {
            foodDAO.deactivateFood(id);
        }
        request.getSession().setAttribute("message", "Đã cập nhật trạng thái!");
        response.sendRedirect("foods");
    }
}
