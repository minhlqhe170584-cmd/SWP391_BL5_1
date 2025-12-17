package controllers;

import dao.BikeOptionDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BikeRentalOption;

@WebServlet(name = "BikeOptionServlet", urlPatterns = {"/admin/bike-options"})
public class BikeOptionServlet extends HttpServlet {

    private final BikeOptionDAO optionDAO = new BikeOptionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "detail":
                showForm(request, response);
                break;
            case "quick-status":
                quickUpdateStatus(request, response);
                break;
            default:
                listOptions(request, response);
                break;
        }
    }

    private void listOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String serviceId = request.getParameter("serviceId");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");

        int pageIndex = 1;
        int pageSize = 10;
        try {
            if (pageRaw != null) pageIndex = Integer.parseInt(pageRaw);
        } catch (NumberFormatException e) {}

        ArrayList<BikeRentalOption> list = optionDAO.search(search, serviceId, status, sort, pageIndex, pageSize);
        int totalRecords = optionDAO.countSearch(search, serviceId, status);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        request.setAttribute("list", list);
        request.setAttribute("services", optionDAO.getBikeServices());
        request.setAttribute("search", search);
        request.setAttribute("serviceId", serviceId);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/bicycle/option_list.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        BikeRentalOption opt = new BikeRentalOption();
        opt.setActive(true); 

        if (idRaw != null && !idRaw.isEmpty()) {
            int id = Integer.parseInt(idRaw);
            opt = optionDAO.getById(id);
        }
        
        request.setAttribute("option", opt);
        request.setAttribute("services", optionDAO.getBikeServices());
        request.getRequestDispatcher("/WEB-INF/views/bicycle/option_detail.jsp").forward(request, response);
    }

    private void quickUpdateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idRaw = request.getParameter("id");
        String statusRaw = request.getParameter("newStatus");
        
        if (idRaw != null && statusRaw != null) {
            boolean newStatus = Boolean.parseBoolean(statusRaw);
            optionDAO.toggleStatus(Integer.parseInt(idRaw), newStatus);
            request.getSession().setAttribute("message", "Status updated successfully.");
        }
        response.sendRedirect("bike-options");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idRaw = request.getParameter("itemId");
        String serviceIdRaw = request.getParameter("serviceId");
        String name = request.getParameter("optionName");
        String durationRaw = request.getParameter("duration");
        String priceRaw = request.getParameter("price");
        boolean isActive = request.getParameter("isActive") != null;

        BikeRentalOption opt = new BikeRentalOption();
        opt.setServiceId(Integer.parseInt(serviceIdRaw));
        opt.setOptionName(name);
        opt.setDurationMinutes(Integer.parseInt(durationRaw));
        opt.setPrice(Double.parseDouble(priceRaw));
        opt.setActive(isActive);

        if (idRaw != null && !idRaw.isEmpty() && !idRaw.equals("0")) {
            opt.setItemId(Integer.parseInt(idRaw));
            optionDAO.update(opt);
            request.getSession().setAttribute("message", "Option updated successfully.");
        } else {
            optionDAO.insert(opt);
            request.getSession().setAttribute("message", "New option created successfully.");
        }
        response.sendRedirect("bike-options");
    }
}