/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import models.Customer;

/**
 *
 * @author Acer
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customermanagement"})
public class CustomerServlet extends HttpServlet {


 private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get parameters from request
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");
        
        // Set default values
        int pageIndex = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageParam);
                if (pageIndex < 1) {
                    pageIndex = 1;
                }
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        
        // Initialize DAO
        CustomerDAO dao = new CustomerDAO();
        
        // Get customers with search, filter, sort, and pagination
        ArrayList<Customer> customers = dao.search(search, status, sort, pageIndex, PAGE_SIZE);
        
        // Get total count for pagination
        int totalCustomers = dao.countSearch(search, status);
        int totalPages = (int) Math.ceil((double) totalCustomers / PAGE_SIZE);
        
        // Set attributes for JSP
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("pageSize", PAGE_SIZE);
        
        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/customer/customer_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Customer List Servlet with search, filter, sort, and pagination";
    }

}
