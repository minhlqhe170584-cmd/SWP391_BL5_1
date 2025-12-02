/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

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

@WebServlet(name = "ServiceController", urlPatterns = {"/service"})
public class ServiceController extends HttpServlet {

    ServiceDAO serviceDAO = new ServiceDAO();
    ServiceCategoryDAO categoryDAO = new ServiceCategoryDAO();

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

        ArrayList<Service> list = serviceDAO.searchFilter(search, categoryId);
        ArrayList<ServiceCategory> categories = categoryDAO.getAll();

        request.setAttribute("list", list);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("service/list.jsp").forward(request, response);
    }

    private void showDetailForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        Service service = null;

        if (idRaw != null) {
            int id = Integer.parseInt(idRaw);
            service = serviceDAO.getById(id);
        }

        ArrayList<ServiceCategory> categories = categoryDAO.getAll();

        request.setAttribute("service", service);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("service/detail.jsp").forward(request, response);
    }

    private void deleteService(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        serviceDAO.delete(id);
        response.sendRedirect("service"); // load lại list
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
        double price = Double.parseDouble(request.getParameter("price"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

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