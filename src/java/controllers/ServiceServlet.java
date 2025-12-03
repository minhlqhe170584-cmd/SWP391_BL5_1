package controllers;

import dao.ServiceCategoryDAO;
import dao.ServiceDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import models.Service;
import models.ServiceCategory;

@WebServlet(name = "ServiceServlet", urlPatterns = {"/service"})
public class ServiceServlet extends HttpServlet {

    ServiceDAO serviceDAO = new ServiceDAO();
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
                deleteService(request, response);
                break;
            default:
                listService(request, response);
        }
    }

    private void listService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String categoryId = request.getParameter("categoryId");
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

        ArrayList<Service> list = serviceDAO.search(search, categoryId, sort, pageIndex, pageSize);
        int totalRecords = serviceDAO.countSearch(search, categoryId);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        ArrayList<ServiceCategory> categories = categoryDAO.getAll();

        request.setAttribute("list", list);
        request.setAttribute("categories", categories);
        request.setAttribute("search", search);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("sort", sort);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/service/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        Service service = null;
        if (idRaw != null && !idRaw.isEmpty()) {
            int id = Integer.parseInt(idRaw);
            service = serviceDAO.getById(id);
        }

        ArrayList<ServiceCategory> categories = categoryDAO.getAll();

        request.setAttribute("service", service);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/views/service/detail.jsp").forward(request, response);
    }

    private void deleteService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw != null && !idRaw.isEmpty()) {
            int id = Integer.parseInt(idRaw);
            serviceDAO.delete(id);
        }
        response.sendRedirect("service");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("serviceId");
        String name = request.getParameter("serviceName");
        String unit = request.getParameter("unit");
        String image = request.getParameter("imageUrl");
        boolean active = request.getParameter("isActive") != null;
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");

        String errorMessage = null;

        if (name == null || name.trim().isEmpty()) {
            errorMessage = "Service name is required.";
        } else if (priceStr == null || priceStr.trim().isEmpty()) {
            errorMessage = "Price is required.";
        } else if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            errorMessage = "Category is required.";
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    errorMessage = "Price must be greater than zero.";
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid price format.";
            }
        }

        if (errorMessage != null) {
            Service s = new Service();
            if (idRaw != null && !idRaw.isEmpty()) {
                try {
                    s.setServiceId(Integer.parseInt(idRaw));
                } catch (NumberFormatException e) {
                }
            }
            s.setServiceName(name);
            s.setUnit(unit);
            s.setImageUrl(image);
            s.setIsActive(active);
            try {
                if (priceStr != null && !priceStr.trim().isEmpty()) {
                    s.setPrice(Double.parseDouble(priceStr));
                }
            } catch (NumberFormatException e) {
                s.setPrice(0);
            }
            try {
                if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                    s.setCategoryId(Integer.parseInt(categoryIdStr));
                }
            } catch (NumberFormatException e) {
            }

            ArrayList<ServiceCategory> categories = categoryDAO.getAll();
            request.setAttribute("categories", categories);
            request.setAttribute("service", s);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/service/detail.jsp").forward(request, response);
            return;
        }

        int categoryId = Integer.parseInt(categoryIdStr);
        double price = Double.parseDouble(priceStr);

        Service s = new Service();
        s.setServiceName(name);
        s.setUnit(unit);
        s.setPrice(price);
        s.setImageUrl(image);
        s.setIsActive(active);
        s.setCategoryId(categoryId);

        if (idRaw == null || idRaw.isEmpty()) {
            serviceDAO.insert(s);
        } else {
            s.setServiceId(Integer.parseInt(idRaw));
            serviceDAO.update(s);
        }

        response.sendRedirect("service");
    }
}
