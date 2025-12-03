package controllers;

import dao.ServiceCategoryDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import models.ServiceCategory;

@WebServlet(name = "ServiceCategoryServlet", urlPatterns = {"/service-category"})
public class ServiceCategoryServlet extends HttpServlet {

    ServiceCategoryDAO categoryDAO = new ServiceCategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "detail":
                showDetailForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            default:
                listCategory(request, response);
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

        request.getRequestDispatcher("/WEB-INF/views/category/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        ServiceCategory category = null;
        if (idRaw != null && !idRaw.isEmpty()) {
            int id = Integer.parseInt(idRaw);
            category = categoryDAO.getById(id);
        }
        request.setAttribute("category", category);
        request.getRequestDispatcher("/WEB-INF/views/category/detail.jsp").forward(request, response);
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw != null && !idRaw.isEmpty()) {
            int id = Integer.parseInt(idRaw);
            categoryDAO.delete(id);
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

        if (errorMessage != null) {
            ServiceCategory c = new ServiceCategory();
            if (idRaw != null && !idRaw.isEmpty()) {
                try {
                    c.setCategoryId(Integer.parseInt(idRaw));
                } catch (NumberFormatException e) {
                }
            }
            c.setCategoryName(name);
            c.setDescription(description);
            request.setAttribute("category", c);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/category/detail.jsp").forward(request, response);
            return;
        }

        ServiceCategory c = new ServiceCategory();
        c.setCategoryName(name);
        c.setDescription(description);

        if (idRaw == null || idRaw.isEmpty()) {
            categoryDAO.insert(c);
        } else {
            c.setCategoryId(Integer.parseInt(idRaw));
            categoryDAO.update(c);
        }

        response.sendRedirect("service-category");
    }
}
