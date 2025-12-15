package controllers;

import dao.DrinkDAO; // Dùng DrinkDAO
import models.Drink;   // Dùng Model Drink
import models.Service;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DrinkManagerServlet", urlPatterns = {"/admin/drinks"}) // SỬA: Tên và URL Pattern
public class DrinkManagerServlet extends HttpServlet {

    // Đổi từ FoodDAO sang DrinkDAO
    private DrinkDAO drinkDAO;

    @Override
    public void init() throws ServletException {
        drinkDAO = new DrinkDAO();
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
                    listDrinks(request, response); // Đổi hàm listFoods -> listDrinks
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
                case "create": createDrink(request, response); break; // Đổi hàm
                case "update": updateDrink(request, response); break; // Đổi hàm
                
                // Xử lý nút Khóa/Mở (Deactivate/Activate)
                case "deactivate": 
                    int dId = Integer.parseInt(request.getParameter("drinkId")); // Đổi foodId -> drinkId
                    drinkDAO.deactivateDrink(dId); // Dùng drinkDAO
                    request.getSession().setAttribute("message", "Đã ngừng kinh doanh đồ uống!");
                    response.sendRedirect("drinks"); // Redirect về drinks
                    break;
                case "activate":
                    int aId = Integer.parseInt(request.getParameter("drinkId")); // Đổi foodId -> drinkId
                    drinkDAO.activateDrink(aId); // Dùng drinkDAO
                    request.getSession().setAttribute("message", "Đã mở bán lại đồ uống!");
                    response.sendRedirect("drinks"); // Redirect về drinks
                    break;
                    
                default: response.sendRedirect("drinks"); break; // Redirect về drinks
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            response.sendRedirect("drinks"); // Redirect về drinks
        }
    }

    // --- HÀM QUAN TRỌNG: LIST DRINKS (Đổi tên, Dùng DrinkDAO) ---
    private void listDrinks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        List<Drink> drinks = drinkDAO.getDrinks(keyword, serviceFilter, pageIndex, 5); // Dùng drinkDAO.getDrinks
        int totalRecords = drinkDAO.countDrinks(keyword, serviceFilter); // Dùng drinkDAO.countDrinks
        int totalPages = (int) Math.ceil((double) totalRecords / 5);
        List<Service> services = drinkDAO.getAllServices(); // Dùng drinkDAO.getAllServices

        // 2. Đẩy dữ liệu sang JSP
        // SỬA TÊN ATTRIBUTE: Đổi "foodsList" -> "drinksList" (để JSP dễ hiểu)
        request.setAttribute("drinksList", drinks); 
        
        // Giữ nguyên listServices vì đây là tên chung
        request.setAttribute("listServices", services); 
        
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword);
        request.setAttribute("serviceFilter", serviceFilter);

        // SỬA ĐƯỜNG DẪN JSP
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkList.jsp").forward(request, response);
    }

    // --- HÀM SHOW ADD FORM (Đổi tên, Dùng DrinkDAO) ---
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("LIST_GROUPS", drinkDAO.getAllServices()); 
        // SỬA ĐƯỜNG DẪN JSP
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkDetail.jsp").forward(request, response);
    }

    // --- HÀM SHOW EDIT FORM (Đổi tên, Dùng DrinkDAO) ---
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("drinkId")); // Đổi foodId -> drinkId
        request.setAttribute("drink", drinkDAO.getDrinkById(id)); // Đổi food -> drink, Dùng drinkDAO
        request.setAttribute("LIST_GROUPS", drinkDAO.getAllServices());
        // SỬA ĐƯỜNG DẪN JSP
        request.getRequestDispatcher("/WEB-INF/views/drink/drinkDetail.jsp").forward(request, response);
    }

    // --- HÀM CREATE DRINK (Đổi tên, Dùng DrinkDAO, Thêm trường) ---
    private void createDrink(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Drink d = new Drink(); // Tạo đối tượng Drink
        d.setName(request.getParameter("name"));
        
        String priceStr = request.getParameter("price");
        d.setPrice(priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr) : 0);
        
        d.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        d.setImageUrl(request.getParameter("image"));
        d.setDescription(request.getParameter("description"));
        
        // THÊM 2 TRƯỜNG ĐẶC BIỆT CỦA DRINK
        String volumeStr = request.getParameter("volumeMl"); // Giả sử name="volumeMl" trong form
        d.setVolumeMl(volumeStr != null && !volumeStr.isEmpty() ? Integer.parseInt(volumeStr) : 0);
        
        String isAlcoholicStr = request.getParameter("isAlcoholic"); // Giả sử checkbox/radio name="isAlcoholic"
        d.setIsAlcoholic("on".equals(isAlcoholicStr) || "1".equals(isAlcoholicStr) || "true".equalsIgnoreCase(isAlcoholicStr));
        
        d.setIsActive(true);
        
        drinkDAO.createDrink(d); // Dùng drinkDAO
        request.getSession().setAttribute("message", "Thêm đồ uống mới thành công!");
        response.sendRedirect("drinks"); // Redirect về drinks
    }

    // --- HÀM UPDATE DRINK (Đổi tên, Dùng DrinkDAO, Thêm trường) ---
    private void updateDrink(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Drink d = new Drink(); // Tạo đối tượng Drink
        d.setDrinkId(Integer.parseInt(request.getParameter("drinkId"))); // Đổi foodId -> drinkId
        d.setName(request.getParameter("name"));
        
        String priceStr = request.getParameter("price");
        d.setPrice(priceStr != null && !priceStr.isEmpty() ? Double.parseDouble(priceStr) : 0);
        
        d.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        d.setImageUrl(request.getParameter("image"));
        d.setDescription(request.getParameter("description"));

        // Cập nhật 2 TRƯỜNG ĐẶC BIỆT CỦA DRINK
        String volumeStr = request.getParameter("volumeMl");
        d.setVolumeMl(volumeStr != null && !volumeStr.isEmpty() ? Integer.parseInt(volumeStr) : 0);
        
        String isAlcoholicStr = request.getParameter("isAlcoholic");
        d.setIsAlcoholic("on".equals(isAlcoholicStr) || "1".equals(isAlcoholicStr) || "true".equalsIgnoreCase(isAlcoholicStr));

        // KHÔNG CẬP NHẬT isActive ở đây vì đã có hàm activate/deactivate riêng

        drinkDAO.updateDrink(d); // Dùng drinkDAO
        request.getSession().setAttribute("message", "Cập nhật đồ uống thành công!");
        response.sendRedirect("drinks"); // Redirect về drinks
    }
}