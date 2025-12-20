/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.LaundryItemDAO;
import dao.LaundryOrderDAO;
import dao.RoomDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import models.LaundryItem;
import models.LaundryOrder;
import models.LaundryOrderDetail;
import models.Room;

/**
 *
 * @author Acer
 */
@WebServlet(name = "LaundryOrderServlet", urlPatterns = {"/laundry-order"})
public class LaundryOrderServlet extends HttpServlet {
    private LaundryOrderDAO orderDAO;
    private LaundryItemDAO itemDAO;
    private RoomDAO roomDao;
    
    @Override
    public void init() throws ServletException {
        orderDAO = new LaundryOrderDAO();
        itemDAO = new LaundryItemDAO();
        roomDao = new RoomDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listOrders(request, response);
                break;
            case "view":
                viewOrder(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "cancel":
                deleteOrder(request, response);
                break;
//            case "updateStatus":
//                showUpdateStatusForm(request, response);
//                break;
            default:
                listOrders(request, response);
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
                addOrder(request, response);
                break;
            case "edit":
                updateOrder(request, response);
                break;
            case "updateStatus":
                updateStatus(request, response);
                break;
            default:
                listOrders(request, response);
                break;
        }
    }
    
    private void listOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String view = request.getParameter("view"); 
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");
        
        int page = 1;
        int pageSize = 10;
        
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        ArrayList<LaundryOrder> orders = orderDAO.search(search, status, view, sort, page, pageSize);
        int totalRecords = orderDAO.countSearch(search, status, view);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("view", view);
        
        request.getRequestDispatcher("/WEB-INF/views/laundry/list.jsp").forward(request, response);
    }
    
    // View single order details
    private void viewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int laundryId = Integer.parseInt(idStr);
                LaundryOrder order = orderDAO.getOrderById(laundryId);
                
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/laundry/view.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Order not found");
                    listOrders(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid order ID");
                listOrders(request, response);
            }
        } else {
            listOrders(request, response);
        }
    }
    
    // Show add order form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<LaundryItem> items = itemDAO.getAllActiveItems();
        ArrayList<Room> rooms = (ArrayList<Room>) roomDao.getAllActiveLoginRooms();
        request.setAttribute("items", items);
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/WEB-INF/views/laundry/add.jsp").forward(request, response);
    }
    
    // Add new order
    private void addOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer roomId = Integer.valueOf(request.getParameter("roomId"));
            String pickupTimeStr = request.getParameter("pickupTime");
            String deliveryTimeStr = request.getParameter("deliveryTime");
            String returnTimeStr = request.getParameter("returnTime");
            String status = request.getParameter("status");
            String note = request.getParameter("note");
            
            LaundryOrder order = new LaundryOrder();
            order.setStatus(status != null && !status.isEmpty() ? status : "PENDING");
            order.setNote(note);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            
            if (deliveryTimeStr != null && !deliveryTimeStr.isEmpty()) {
                order.setExpectedPickupTime(LocalDateTime.parse(deliveryTimeStr, formatter));
            }
            if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                order.setExpectedReturnTime(LocalDateTime.parse(returnTimeStr, formatter));
            }
            
            // Get order details
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            
            ArrayList<LaundryOrderDetail> details = new ArrayList<>();
            
            if (itemIds != null && quantities != null && prices != null) {
                for (int i = 0; i < itemIds.length; i++) {
                    if (itemIds[i] != null && !itemIds[i].isEmpty() && 
                        quantities[i] != null && !quantities[i].isEmpty()) {
                        
                        LaundryOrderDetail detail = new LaundryOrderDetail();
                        detail.setLaundryItemId(Integer.parseInt(itemIds[i]));
                        detail.setQuantity(Integer.valueOf(quantities[i]));
                        detail.setUnitPrice(Double.valueOf(prices[i]));
                        detail.setSubtotal(detail.getQuantity() * detail.getUnitPrice());
                        
                        details.add(detail);
                    }
                }
            }           
            order.setOrderDetails(details);
            
            Integer newId = orderDAO.insertOrder(order, roomId);
            
            if (newId != null) {
                request.setAttribute("success", "Order added successfully!");
                response.sendRedirect("laundry-order?action=view&id=" + newId);
            } else {
                request.setAttribute("error", "Failed to add order");
                showAddForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error adding order: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    // Show edit order form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int laundryId = Integer.parseInt(idStr);
                LaundryOrder order = orderDAO.getOrderById(laundryId);
                ArrayList<LaundryItem> items = itemDAO.getAllActiveItems();
                
                if (order != null) {
                    request.setAttribute("order", order);
                    request.setAttribute("items", items);
                    request.getRequestDispatcher("/WEB-INF/views/laundry/edit.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Order not found");
                    listOrders(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid order ID");
                listOrders(request, response);
            }
        } else {
            listOrders(request, response);
        }
    }
    
    // Update order
    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int laundryId = Integer.parseInt(request.getParameter("laundryId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String expectedPickupTimeStr = request.getParameter("deliveryTime");
            String returnTimeStr = request.getParameter("returnTime");
            String status = request.getParameter("status");
            String note = request.getParameter("note");
            
            LaundryOrder order = new LaundryOrder();
            order.setLaundryId(laundryId);
            order.setOrderId(orderId);
            order.setStatus(status);
            order.setNote(note);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            
            if (expectedPickupTimeStr != null && !expectedPickupTimeStr.isEmpty()) {
                order.setExpectedPickupTime(LocalDateTime.parse(expectedPickupTimeStr, formatter));
            }
            if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                order.setExpectedReturnTime(LocalDateTime.parse(returnTimeStr, formatter));
            }
            
            // Get order details
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            
            ArrayList<LaundryOrderDetail> details = new ArrayList<>();
            
            if (itemIds != null && quantities != null && prices != null) {
                for (int i = 0; i < itemIds.length; i++) {
                    if (itemIds[i] != null && !itemIds[i].isEmpty() && 
                        quantities[i] != null && !quantities[i].isEmpty()) {
                        
                        LaundryOrderDetail detail = new LaundryOrderDetail();
                        detail.setLaundryItemId(Integer.parseInt(itemIds[i]));
                        detail.setQuantity(Integer.valueOf(quantities[i]));
                        detail.setUnitPrice(Double.valueOf(prices[i]));
                        detail.setSubtotal(detail.getQuantity() * detail.getUnitPrice());
                        
                        details.add(detail);
                    }
                }
            }
            
            order.setOrderDetails(details);
            
            boolean success = orderDAO.updateOrder(order);
            
            if (success) {
                response.sendRedirect("laundry-order?action=view&id=" + laundryId + "&success=updated");
            } else {
                request.setAttribute("error", "Failed to update order");
                showEditForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error updating order: " + e.getMessage());
            showEditForm(request, response);
        }
    }
    
    // Delete order
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int laundryId = Integer.parseInt(idStr);
                boolean success = orderDAO.cancelOrder(laundryId);
                
                if (success) {
                    response.sendRedirect("laundry-order?success=canceled");
                } else {
                    response.sendRedirect("laundry-order?error=cancel_failed");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("laundry-order?error=invalid_id");
            }
        } else {
            response.sendRedirect("laundry-order");
        }
    }
       
//    private void showUpdateStatusForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String idStr = request.getParameter("id");
//        
//        if (idStr != null && !idStr.isEmpty()) {
//            try {
//                int laundryId = Integer.parseInt(idStr);
//                LaundryOrder order = orderDAO.getOrderById(laundryId);
//                
//                if (order != null) {
//                    request.setAttribute("order", order);
//                    request.getRequestDispatcher("/WEB-INF/views/laundry/update-status.jsp").forward(request, response);
//                } else {
//                    request.setAttribute("error", "Order not found");
//                    listOrders(request, response);
//                }
//            } catch (NumberFormatException e) {
//                request.setAttribute("error", "Invalid order ID");
//                listOrders(request, response);
//            }
//        } else {
//            listOrders(request, response);
//        }
//    }
    

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int laundryId = Integer.parseInt(request.getParameter("laundryId"));
            String status = request.getParameter("status");
            
            if (status == null || status.trim().isEmpty()) {
                response.sendRedirect("laundry-order?action=view&id=" + laundryId + "&error=invalid_status");
                return;
            }
            
            boolean success = orderDAO.updateOrderStatus(laundryId, status);
            
            if (success) {
                response.sendRedirect("laundry-order?action=view&id=" + laundryId + "&success=status_updated");
            } else {
                response.sendRedirect("laundry-order?action=view&id=" + laundryId + "&error=update_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("laundry-order?error=invalid_id");
        } catch (Exception e) {
            response.sendRedirect("laundry-order?error=update_error");
        }
    }
}
