/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.LaundryItemDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import models.LaundryItem;
import models.Service;

@WebServlet(name = "LaundryItemServlet", urlPatterns = {"/admin/laundry-item"})
public class LaundryItemServlet extends HttpServlet {

    private LaundryItemDAO itemDAO;
   
    
    @Override
    public void init() throws ServletException {
        itemDAO = new LaundryItemDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
//            case "list":
//                listItems(request, response);
//                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "activate":
                activateItem(request, response);
                break;
            case "deactivate":
                deactivateItem(request, response);
                break;
            default:
                listItems(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "add":
                addItem(request, response);
                break;
            case "edit":
                updateItem(request, response);
                break;
            default:
                listItems(request, response);
                break;
        }
    }
    
    // List all items with search, filter, and pagination
    private void listItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String serviceIdStr = request.getParameter("serviceId");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");
        
        Integer serviceId = null;
        if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
            try {
                 serviceId = Integer.valueOf(serviceIdStr);
            } catch (NumberFormatException e) {
                serviceId = null;
            }
        }
        
        int page = 1;
        int pageSize = 10;
        
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        ArrayList<LaundryItem> items = itemDAO.search(search, serviceId, status, sort, page, pageSize);
        int totalRecords = itemDAO.countSearch(search, serviceId, status);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        ArrayList<Service> services = itemDAO.getActiveServices();
        
        request.setAttribute("items", items);
        request.setAttribute("services", services);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("search", search);
        request.setAttribute("serviceId", serviceId);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        
        request.getRequestDispatcher("/WEB-INF/views/laundry/item-list.jsp").forward(request, response);
    }
    
    // Show add item form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Service> services = itemDAO.getActiveServices();
        request.setAttribute("services", services);
        request.getRequestDispatcher("/WEB-INF/views/laundry/item-form.jsp").forward(request, response);
    }
    
    // Add new item
    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serviceId = request.getParameter("serviceId");
            String itemName = request.getParameter("itemName");
            String description = request.getParameter("description");
            Double defaultPrice = Double.valueOf(request.getParameter("defaultPrice"));
            String unit = request.getParameter("unit");
            Boolean isActive = request.getParameter("isActive") != null;
            
            LaundryItem item = new LaundryItem();
                //item.setServiceId(serviceId); 
                item.setItemName(itemName.trim());
                item.setDescription(description.trim());
                item.setDefaultPrice(defaultPrice);
                item.setUnit(unit);
                item.setIsActive(isActive);
            
            if (serviceId != null && !serviceId.trim().isEmpty()) {
            try { item.setServiceId(Integer.parseInt(serviceId)); } catch (NumberFormatException e) {}
            }
            
            String errorMessage = null;
            if(itemName == null || itemName.trim().isEmpty()){
                 errorMessage = "Item name is required.";
            }else if(unit == null || unit.trim().isEmpty()){
                errorMessage = "Unit type is required.";
            }else if (itemDAO.isExistName(itemName)){
                errorMessage = "Laundry item name " + itemName + " already existed.";
            }else if(defaultPrice <= 0){
                errorMessage = "Item price must larger than zero.";
            }
                
            if(errorMessage != null){
                request.setAttribute("item", item);
                request.setAttribute("error",errorMessage);
                request.setAttribute("services",itemDAO.getActiveServices());
                request.getRequestDispatcher("/WEB-INF/views/laundry/item-form.jsp").forward(request, response);
                return;
            }
            boolean success = itemDAO.insertItem(item);
            
            if (success) {
                response.sendRedirect("laundry-item?success=added");
            } else {
                request.setAttribute("error", "Không thể thêm mục mới");
                showAddForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    // Show edit item form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                Integer itemId = Integer.valueOf(idStr);
                LaundryItem item = itemDAO.getItemById(itemId);
                ArrayList<Service> services = itemDAO.getActiveServices();
                
                if (item != null) {
                    request.setAttribute("item", item);
                    request.setAttribute("services", services);
                    request.getRequestDispatcher("/WEB-INF/views/laundry/item-form.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy mục");
                    listItems(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID không hợp lệ");
                listItems(request, response);
            }
        } else {
            listItems(request, response);
        }
    }
    
    // Update item
    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String itemIdStr = request.getParameter("itemId");
            if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Item ID is required");
                listItems(request, response);
                return;
            }
            
            Integer itemId = Integer.valueOf(itemIdStr);
            String serviceIdStr = request.getParameter("serviceId");
            String itemName = request.getParameter("itemName");
            String description = request.getParameter("description");
            String defaultPriceStr = request.getParameter("defaultPrice");
            String unit = request.getParameter("unit");
            Boolean isActive = request.getParameter("isActive") != null;
            
            if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Service is required");
                showEditForm(request, response);
                return;
            }
            
            Integer serviceId = Integer.valueOf(serviceIdStr);
            Double defaultPrice = Double.parseDouble(defaultPriceStr);
            
            LaundryItem item = new LaundryItem();
            item.setLaundryItemId(itemId);
            item.setServiceId(serviceId);
            item.setItemName(itemName.trim());
            item.setDescription(description.trim());
            item.setDefaultPrice(defaultPrice);
            item.setUnit(unit);
            item.setIsActive(isActive);
            
            // Validate
            String errorMessage = null;
            if(itemName == null || itemName.trim().isEmpty()){
                errorMessage = "Item name is required.";
            }else if(unit == null || unit.trim().isEmpty()){
                errorMessage = "Unit type is required.";
            }else if (itemDAO.isExistNameForOtherItem(itemName, itemId)){
                errorMessage = "Laundry item name " + itemName + " already existed.";
            }else if(defaultPrice <= 0){
                errorMessage = "Item price must larger than zero.";
            }
            
            if(errorMessage != null){
                request.setAttribute("item", item);
                request.setAttribute("error", errorMessage);
                request.setAttribute("services", itemDAO.getActiveServices());
                request.getRequestDispatcher("/WEB-INF/views/laundry/item-form.jsp").forward(request, response);
                return;
            }
            
            boolean success = itemDAO.updateItem(item);
            
            if (success) {
                response.sendRedirect("laundry-item?success=updated");
            } else {
                request.setAttribute("error", "Không thể cập nhật mục");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format: " + e.getMessage());
            showEditForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showEditForm(request, response);
        }
    }
    
    // Activate item
    private void activateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                Integer itemId = Integer.valueOf(idStr);
                boolean success = itemDAO.activateItem(itemId);
                
                if (success) {
                    response.sendRedirect("laundry-item?success=activated");
                } else {
                    response.sendRedirect("laundry-item?error=activate_failed");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("laundry-item?error=invalid_id");
            }
        } else {
            response.sendRedirect("laundry-item");
        }
    }
    
    // Deactivate item
    private void deactivateItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                Integer itemId = Integer.valueOf(idStr);
                boolean success = itemDAO.deactivateItem(itemId);
                
                if (success) {
                    response.sendRedirect("laundry-item?success=deactivated");
                } else {
                    response.sendRedirect("laundry-item?error=deactivate_failed");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("laundry-item?error=invalid_id");
            }
        } else {
            response.sendRedirect("laundry-item");
        }
    }

}
