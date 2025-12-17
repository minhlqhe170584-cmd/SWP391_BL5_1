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
//sử dụng Annotation @MultipartConfig
//nhận diện và xử lý các Part (phần dữ liệu file) từ request.
//không lưu file trực tiếp vào Database (dạng BLOB) vì sẽ làm Database nặng 
//Lưu file vật lý vào thư mục uploads
//ghép đường dẫn thư mục uploads + tên file trong Database để hiển thị ảnh ra thẻ <img>.
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
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
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("foods");
        }
    }

    // --- XỬ LÝ UPLOAD FILE ---
    private String handleFileUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("imageFile"); // Tên input file trong JSP
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        // Đường dẫn tuyệt đối đến thư mục uploads trong project
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        filePart.write(uploadPath + File.separator + fileName);
        return fileName; // Chỉ lưu tên file vào DB
    }

    private void listFoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String serviceFilter = request.getParameter("serviceFilter");
        int pageIndex = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) {
                pageIndex = Integer.parseInt(pageStr);
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

    private void createFood(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Food f = new Food();
        f.setName(request.getParameter("name").trim());
        f.setPrice(Double.parseDouble(request.getParameter("price")));
        f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        f.setDescription(request.getParameter("description"));
        f.setIsActive(true);
        String fileName = handleFileUpload(request);
        f.setImageUrl(fileName != null ? fileName : "default.jpg");

        foodDAO.createFood(f);
        request.getSession().setAttribute("message", "Thêm món mới thành công!");
        response.sendRedirect("foods");
    }

    private void updateFood(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("foodId"));
        Food f = foodDAO.getFoodById(id);

        f.setName(request.getParameter("name").trim());
        f.setPrice(Double.parseDouble(request.getParameter("price")));
        f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        f.setDescription(request.getParameter("description"));

        String fileName = handleFileUpload(request);
        if (fileName != null) {
            f.setImageUrl(fileName); // Nếu ko chọn ảnh mới thì giữ ảnh cũ
        }
        foodDAO.updateFood(f);
        request.getSession().setAttribute("message", "Cập nhật thành công!");
        response.sendRedirect("foods");
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response, boolean status) throws Exception {
        int id = Integer.parseInt(request.getParameter("foodId"));
        if (status) {
            foodDAO.activateFood(id);
        } else {
            foodDAO.deactivateFood(id);
        }
        request.getSession().setAttribute("message", "Đã cập nhật trạng thái kinh doanh!");
        response.sendRedirect("foods");
    }
}
