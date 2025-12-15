package controllers;

import dao.FoodDAO;
import dao.DrinkDAO;
import models.Food;
import models.Drink;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FoodMenuServlet", urlPatterns = {"/order"})
public class FoodMenuServlet extends HttpServlet {

    private static final String MENU_PAGE = "/WEB-INF/views/menu/food_menu.jsp";
    private FoodDAO foodDAO;
    private DrinkDAO drinkDAO;

    @Override
    public void init() throws ServletException {
        super.init(); 
        foodDAO = new FoodDAO();
        drinkDAO = new DrinkDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
    
    // 1. Lấy thông tin User và Role từ Session (đã lưu lúc Login)
    Object currentUser = session.getAttribute("USER");
    Object currentRole = session.getAttribute("ROLE");

    // 2. Kiểm tra điều kiện chặn:
    // - Nếu chưa đăng nhập (currentUser == null)
    // - HOẶC Đã đăng nhập nhưng KHÔNG PHẢI là tài khoản phòng (!"ROOM".equals...)
    if (currentUser == null || !"ROOM".equals(currentRole)) {
        
        // (Tuỳ chọn) Lưu thông báo lỗi để hiện ở trang Login
        request.setAttribute("mess", "Vui lòng đăng nhập bằng tài khoản Phòng để gọi dịch vụ!");
        
        // Chuyển hướng thẳng về trang Login
        request.getRequestDispatcher("/WEB-INF/views/users/login.jsp").forward(request, response);
        // Hoặc dùng: response.sendRedirect("login");
        
        return; // QUAN TRỌNG: Dừng lại ngay, không cho code phía dưới chạy
    }
        try {
            // SỬ DỤNG HÀM MỚI ĐÃ THÊM: getAllActiveFoods/Drinks()
            List<Food> foodList = foodDAO.getAllActiveFoods();
            List<Drink> drinkList = drinkDAO.getAllActiveDrinks(); 

            request.setAttribute("foodList", foodList);
            request.setAttribute("drinkList", drinkList);
            
            // Chuyển hướng đến trang hiển thị menu
            request.getRequestDispatcher(MENU_PAGE).forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy dữ liệu Menu!");
        }
    }
}