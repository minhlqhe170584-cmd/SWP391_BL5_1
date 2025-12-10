package controllers;

import dao.ServiceCategoryDAO;
import dao.ServiceDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Service;
import models.ServiceCategory;

@WebServlet(name = "ServiceServlet", urlPatterns = {"/service"})
public class ServiceServlet extends HttpServlet {

    private final ServiceDAO serviceDAO = new ServiceDAO();
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
                toggleDeleteService(request, response);
                break;
            case "toggle-status":
                toggleStatus(request, response);
                break;
            default:
                listService(request, response);
                break;
        }
    }

    private void listService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String categoryId = request.getParameter("categoryId");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");
        String view = request.getParameter("view"); 

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

        ArrayList<Service> list = serviceDAO.search(search, categoryId, view, sort, pageIndex, pageSize);
        int totalRecords = serviceDAO.countSearch(search, categoryId, view);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        ArrayList<ServiceCategory> categories = categoryDAO.getAll();

        request.setAttribute("list", list);
        request.setAttribute("categories", categories);
        request.setAttribute("search", search);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("sort", sort);
        request.setAttribute("view", view);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/service/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        Service service = new Service();
        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                service = serviceDAO.getById(id);
            } catch (NumberFormatException e) {}
        }
        ArrayList<ServiceCategory> categories = categoryDAO.getAll();
        request.setAttribute("service", service);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/service/detail.jsp").forward(request, response);
    }

    private void toggleDeleteService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idRaw = request.getParameter("id");
        HttpSession session = request.getSession();

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                boolean isDeleted = serviceDAO.isDeleted(id);
                
                if (isDeleted) {
                    serviceDAO.restore(id);
                    session.setAttribute("message", "Service restored successfully.");
                } else {
                    serviceDAO.softDelete(id);
                    session.setAttribute("message", "Service moved to trash.");
                }
            } catch (Exception e) {
                session.setAttribute("message", "Error: " + e.getMessage());
            }
        }
        String currentView = request.getParameter("view");
        response.sendRedirect("service" + (currentView != null ? "?view=" + currentView : ""));
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idRaw = request.getParameter("id");
        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                Service s = serviceDAO.getById(id);
                if (s != null && !s.isIsDeleted()) {
                    boolean newStatus = !s.isIsActive();
                    serviceDAO.updateStatus(id, newStatus);
                    request.getSession().setAttribute("message", "Status updated successfully!");
                }
            } catch (Exception e) {}
        }
        String currentView = request.getParameter("view");
        response.sendRedirect("service" + (currentView != null ? "?view=" + currentView : ""));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String idRaw = request.getParameter("serviceId");
        String name = request.getParameter("serviceName");
        String image = request.getParameter("imageUrl");
        String categoryIdStr = request.getParameter("categoryId");
        boolean active = request.getParameter("isActive") != null;

        Service s = new Service();
        s.setServiceName(name);
        s.setImageUrl(image);
        s.setIsActive(active);

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try { s.setServiceId(Integer.parseInt(idRaw)); } catch (NumberFormatException e) {}
        }
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try { s.setCategoryId(Integer.parseInt(categoryIdStr)); } catch (NumberFormatException e) {}
        }

        String errorMessage = null;
        if (name == null || name.trim().isEmpty()) {
            errorMessage = "Service name is required.";
        } else if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            errorMessage = "Category is required.";
        } else if (serviceDAO.isExistName(name, s.getServiceId())) {
            errorMessage = "Service name '" + name + "' already exists.";
        }

        if (errorMessage != null) {
            request.setAttribute("service", s);
            request.setAttribute("categories", categoryDAO.getAll());
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/service/detail.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        try {
            if (s.getServiceId() == 0) {
                serviceDAO.insert(s);
                session.setAttribute("message", "Service created successfully.");
            } else {
                serviceDAO.update(s);
                session.setAttribute("message", "Service updated successfully.");
            }
            response.sendRedirect("service");
        } catch (Exception e) {
            request.setAttribute("service", s);
            request.setAttribute("categories", categoryDAO.getAll());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/service/detail.jsp").forward(request, response);
        }
    }
}