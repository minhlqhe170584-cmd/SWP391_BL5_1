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