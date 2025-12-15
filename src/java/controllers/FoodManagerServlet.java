package controllers;

import dao.FoodDAO;
import models.Food;
import models.Service;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "FoodManagerServlet", urlPatterns = {"/admin/foods"})
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
        if (action == null) action = "list";

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
        if (action == null) action = "list";

        try {
            switch (action) {
                case "create": createFood(request, response); break;
                case "update": updateFood(request, response); break;
                
                // Xử lý nút Khóa/Mở trong JSP của bạn
                case "deactivate": 
                    int dId = Integer.parseInt(request.getParameter("foodId"));
                    foodDAO.deactivateFood(dId);
                    request.getSession().setAttribute("message", "Đã ngừng kinh doanh món ăn!");
                    response.sendRedirect("foods");
                    break;
                case "activate":
                    int aId = Integer.parseInt(request.getParameter("foodId"));
                    foodDAO.activateFood(aId);
                    request.getSession().setAttribute("message", "Đã mở bán lại món ăn!");
                    response.sendRedirect("foods");
                    break;
                    
                default: response.sendRedirect("foods"); break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("foods");
        }
    }

    // --- HÀM QUAN TRỌNG: LIST FOODS ---
    private void listFoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String serviceFilter = request.getParameter("serviceFilter");
        int pageIndex = 1;
        try { 
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                pageIndex = Integer.parseInt(pageStr);
            }
        } catch (Exception e) {}
        
        // 1. Lấy dữ liệu từ DAO
        List<Food> foods = foodDAO.getFoods(keyword, serviceFilter, pageIndex, 5);
        int totalRecords = foodDAO.countFoods(keyword, serviceFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / 5);
        List<Service> services = foodDAO.getAllServices();

        // 2. Đẩy dữ liệu sang JSP (TÊN BIẾN PHẢI CHUẨN)
        
        // JSP dùng: items="${foodsList}" (có chữ s)
        request.setAttribute("foodsList", foods); 
        
        // JSP dùng: items="${listServices}"
        request.setAttribute("listServices", services);
        
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        
        // Không cần set lại keyword/serviceFilter vào attribute vì JSP dùng ${param.keyword}
        // Nhưng set vào để an toàn nếu sau này đổi logic
        request.setAttribute("keyword", keyword);
        request.setAttribute("serviceFilter", serviceFilter);

        // Forward về đúng file JSP của bạn
        request.getRequestDispatcher("/WEB-INF/views/food/foodList.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gửi listServices sang để Detail Dropdown dùng (nếu Detail JSP dùng tên biến này)
        // Nếu Detail JSP của bạn dùng "LIST_GROUPS", hãy đổi tên dòng dưới thành LIST_GROUPS
        request.setAttribute("LIST_GROUPS", foodDAO.getAllServices()); 
        request.getRequestDispatcher("/WEB-INF/views/food/foodDetail.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("foodId"));
        request.setAttribute("food", foodDAO.getFoodById(id));
        request.setAttribute("LIST_GROUPS", foodDAO.getAllServices());
        request.getRequestDispatcher("/WEB-INF/views/food/foodDetail.jsp").forward(request, response);
    }

    private void createFood(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Food f = new Food();
        f.setName(request.getParameter("name"));
        String priceStr = request.getParameter("price");
        f.setPrice(priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr) : 0);
        f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        f.setImageUrl(request.getParameter("image"));
        f.setDescription(request.getParameter("description"));
        f.setIsActive(true);
        
        foodDAO.createFood(f);
        request.getSession().setAttribute("message", "Thêm món mới thành công!");
        response.sendRedirect("foods");
    }

    private void updateFood(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Food f = new Food();
        f.setFoodId(Integer.parseInt(request.getParameter("foodId")));
        f.setName(request.getParameter("name"));
        String priceStr = request.getParameter("price");
        f.setPrice(priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr) : 0);
        f.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        f.setImageUrl(request.getParameter("image"));
        f.setDescription(request.getParameter("description"));
        
        foodDAO.updateFood(f);
        request.getSession().setAttribute("message", "Cập nhật thành công!");
        response.sendRedirect("foods");
    }
}