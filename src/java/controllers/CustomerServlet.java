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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Customer;
import utils.UpdateCustomerValidator;

/**
 *
 * @author Acer
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customer"})
public class CustomerServlet extends HttpServlet {


private final CustomerDAO customerDAO = new CustomerDAO(); // Or use Dependency Injection

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
            default:
                listCustomer(request, response);
                break;
        }
    }
      
    private void showDetailForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String id = request.getParameter("id");
       Customer customer = new Customer();
       if(id != null && !id.trim().isEmpty()){
            try {
                customer = customerDAO.getCustomerById(Integer.parseInt(id));
            } catch (NumberFormatException e) {       
                request.getSession().setAttribute("message", "Invalid Service ID.");
                e.printStackTrace();
            }
        }
       request.setAttribute("customer", customer);
       request.getRequestDispatcher("/WEB-INF/views/customer/detail.jsp").forward(request, response);
    }

    private void listCustomer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");
        
        int pageIndex = 1;
        int pageSize = 5;
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
        
        ArrayList<Customer> list = customerDAO.search(search, status, sort, pageIndex, pageSize);
        int totalCustomers = customerDAO.countSearch(search, status);
        int totalPages = (int) Math.ceil((double)totalCustomers/pageSize);
        
        request.setAttribute("list", list);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        //request.setAttribute("pageSize", pageSize);
        
        request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
            case "active":
                activateCustomer(request, response);
                break;
            case "deactive":
                deactivateCustomer(request, response);
                break;
            case "update":
                updateCustomer(request, response);
                break;   
            default:
                response.sendRedirect("customer");
                break;
            }
        } 
        catch (SQLException ex) 
        {
            request.getSession().setAttribute("message",ex.getMessage());
            response.sendRedirect("customers");
            System.getLogger(CustomerServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private void deactivateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("customerId"));
        customerDAO.deactivateCustomer(id);
        request.getSession().setAttribute("message", "Deactivate Account");
        response.sendRedirect("customer");
    }

    private void activateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("customerId"));
        customerDAO.activateCustomer(id);
        request.getSession().setAttribute("message", "Activate Account");
        response.sendRedirect("customer");
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        String id = request.getParameter("customerId");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        
        List<String> errors = UpdateCustomerValidator.validate(fullName, email, phone, password);
        
        Integer customerId = null;
        if(id != null && !id.trim().isEmpty()){
            try {
                customerId = Integer.valueOf(id);
            } catch (NumberFormatException e) {
                String errorMessage = "Invalid Customer ID.";
                errors.add(errorMessage);
            }
        }
         
        if (!errors.isEmpty()) {
        request.getSession().setAttribute("errorMessage", String.join(", ", errors));
        
        // Preserve form data
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        request.setAttribute("customer", customer);
        
        request.getRequestDispatcher("/WEB-INF/views/customer/detail.jsp").forward(request, response);
        return; 
    }
        
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setFullName(fullName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setPassword(password);
        
//        if (!errors.isEmpty()) {
//        request.setAttribute("customer", customer);
//        request.getSession().setAttribute("errorMessage", String.join(", ", errors));
//        request.getRequestDispatcher("/WEB-INF/views/customer/detail.jsp").forward(request, response);  
//        return;
//        }
        
        try{
        customerDAO.updateCustomer(customer);
        request.getSession().setAttribute("message", "Customer updated successfully!");
        response.sendRedirect("customer");
        }
        catch (Exception e)
        {
        e.printStackTrace();
        request.getSession().setAttribute("errorMessage", "Database error: " + e.getMessage());
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/WEB-INF/views/customer/detail.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
