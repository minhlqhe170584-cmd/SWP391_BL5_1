package controllers;

import dao.BicycleDAO;
import dao.BikeTransactionDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Bicycle;
import models.Service;

@WebServlet(name = "BicycleServlet", urlPatterns = {"/admin/bicycle"})
public class BicycleServlet extends HttpServlet {

    private final BicycleDAO bicycleDAO = new BicycleDAO();
    private final BikeTransactionDAO bikeTransactionDAO = new BikeTransactionDAO();

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
                toggleDeleteBicycle(request, response);
                break;
            default:
                listBicycles(request, response);
                break;
        }
    }

    private void listBicycles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String statusFilter = request.getParameter("status");
        String pageRaw = request.getParameter("page");
        String view = request.getParameter("view");

        int pageIndex = 1;
        int pageSize = 10;

        if (pageRaw != null && !pageRaw.trim().isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageRaw);
                if (pageIndex < 1) pageIndex = 1;
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        ArrayList<Bicycle> list = bicycleDAO.search(search, statusFilter, view, sort, pageIndex, pageSize);
        int totalRecords = bicycleDAO.countSearch(search, statusFilter, view);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("list", list);
        request.setAttribute("search", search);
        request.setAttribute("sort", sort);
        request.setAttribute("status", statusFilter);
        request.setAttribute("view", view);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/views/bicycle/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        Bicycle b = new Bicycle();
        
        b.setStatus("Available"); 

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                b = bicycleDAO.getById(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Service> services = bikeTransactionDAO.getAllBikeServices();
        
        request.setAttribute("bicycle", b);
        request.setAttribute("services", services);
        request.getRequestDispatcher("/WEB-INF/views/bicycle/detail.jsp").forward(request, response);
    }

    private void toggleDeleteBicycle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idRaw = request.getParameter("id");
        HttpSession session = request.getSession();

        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                bicycleDAO.toggleDeleteStatus(id);
                session.setAttribute("message", "Bicycle status changed successfully.");
            } catch (Exception e) {
                session.setAttribute("message", "Error updating bicycle status: " + e.getMessage());
            }
        }
        String currentView = request.getParameter("view");
        response.sendRedirect("bicycle" + (currentView != null ? "?view=" + currentView : ""));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("bikeId");
        String bikeCode = request.getParameter("bikeCode");
        String serviceIdStr = request.getParameter("serviceId");
        String condition = request.getParameter("condition");

        Bicycle b = new Bicycle();
        b.setBikeCode(bikeCode);
        b.setCondition(condition);

        int bikeId = 0;
        if (idRaw != null && !idRaw.trim().isEmpty()) {
            try {
                bikeId = Integer.parseInt(idRaw);
                b.setBikeId(bikeId);
            } catch (NumberFormatException e) {
            }
        }

        if (serviceIdStr != null && !serviceIdStr.trim().isEmpty()) {
            try {
                b.setServiceId(Integer.parseInt(serviceIdStr));
            } catch (NumberFormatException e) {
            }
        }
        
        String errorMessage = null;
        if (bikeCode == null || bikeCode.trim().isEmpty()) {
            errorMessage = "Bike Code is required.";
        } else if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
            errorMessage = "Service Type is required.";
        } else if (bicycleDAO.isExistCode(bikeCode, bikeId)) {
            errorMessage = "Bike Code '" + bikeCode + "' already exists. Please choose another one.";
        }

        if (errorMessage != null) {
            request.setAttribute("bicycle", b);
            request.setAttribute("services", bikeTransactionDAO.getAllBikeServices());
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/bicycle/detail.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        try {
            if (b.getBikeId() == 0) {
                bicycleDAO.insert(b);
                session.setAttribute("message", "New bicycle added successfully.");
            } else {
                bicycleDAO.update(b);
                session.setAttribute("message", "Bicycle updated successfully.");
            }
            response.sendRedirect("bicycle");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("bicycle", b);
            request.setAttribute("services", bikeTransactionDAO.getAllBikeServices());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bicycle/detail.jsp").forward(request, response);
        }
    }
}