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
import java.util.List;
import models.Customer;

/**
 *
 * @author Acer
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerManagement"})
public class CustomerServlet extends HttpServlet {


private final CustomerDAO customerDAO = new CustomerDAO(); // Or use Dependency Injection

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         // 1. Get Parameters with defaults
        String keyword = request.getParameter("keyword");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        
        if (sortBy == null || sortBy.isEmpty()) sortBy = "customer_id";
        if (sortOrder == null || sortOrder.isEmpty()) sortOrder = "ASC";
        
        int page = 1;
        int pageSize = 5; // Records per page
        
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("pageSize") != null) {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            }
        } catch (NumberFormatException e) {
            // Keep defaults if parsing fails
        }

        // 2. Call DAO
        List<Customer> customers = customerDAO.searchCustomers(keyword, sortBy, sortOrder, page, pageSize);
        int totalRecords = customerDAO.countCustomers(keyword);
        
        // 3. Calculate Pagination Info
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // 4. Set attributes for JSP
        request.setAttribute("customerList", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        // 5. Forward to JSP
        request.getRequestDispatcher("customer_list.jsp").forward(request, response);
      
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
     
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
