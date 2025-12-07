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
                listService(request, response);
                break;
        }
    }
      
    private void showDetailForm(HttpServletRequest request, HttpServletResponse response) {
       
    }

    private void listService(HttpServletRequest request, HttpServletResponse response) 
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
      
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
