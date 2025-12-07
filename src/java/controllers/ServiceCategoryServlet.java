package controllers;

import dao.ServiceCategoryDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.ServiceCategory;

@WebServlet(name = "ServiceCategoryServlet", urlPatterns = {"/service-category"})
public class ServiceCategoryServlet extends HttpServlet {

    private final ServiceCategoryDAO categoryDAO = new ServiceCategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "detail":
                showDetailForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            default:
                listCategory(request, response);
                break;
        }
    }

    private void listCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");

        int pageIndex = 1;
        int pageSize = 5;

        if (pageRaw != null && !pageRaw.trim().isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageRaw);
                if (pageIndex < 1) pageIndex = 1;
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        ArrayList<ServiceCategory> categories = categoryDAO.search(search, sort, pageIndex, pageSize);
        int totalRecords = categoryDAO.countSearch(search);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("categories", categories);
        request.setAttribute("search", search);
        request.setAttribute("sort", sort);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/service-category/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        ServiceCategory category = new ServiceCategory();

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                category = categoryDAO.getById(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        request.setAttribute("category", category);
        request.getRequestDispatcher("/WEB-INF/views/service-category/detail.jsp").forward(request, response);
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idRaw = request.getParameter("id");
        HttpSession session = request.getSession();

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                categoryDAO.delete(id);
                session.setAttribute("message", "Category deleted successfully.");
            } catch (Exception e) {
                session.setAttribute("message", "Error deleting category: " + e.getMessage());
            }
        }
        response.sendRedirect("service-category");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("categoryId");
        String name = request.getParameter("categoryName");
        String description = request.getParameter("description");

        String errorMessage = null;

        if (name == null || name.trim().isEmpty()) {
            errorMessage = "Category name is required.";
        }

        ServiceCategory c = new ServiceCategory();
        c.setCategoryName(name);
        c.setDescription(description);
        
        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                c.setCategoryId(Integer.parseInt(idRaw));
            } catch (NumberFormatException e) {
                 errorMessage = "Invalid Category ID.";
            }
        }

        if (errorMessage != null) {
            request.setAttribute("category", c);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/service-category/detail.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        try {
            if (c.getCategoryId() == 0) {
                categoryDAO.insert(c);
                session.setAttribute("message", "New category created successfully.");
            } else {
                categoryDAO.update(c);
                session.setAttribute("message", "Category updated successfully.");
            }
            response.sendRedirect("service-category");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("category", c);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/service-category/detail.jsp").forward(request, response);
        }
    }
}