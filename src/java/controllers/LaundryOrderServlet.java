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
import java.time.format.DateTimeParseException;
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
                    request.setAttribute("errorMessage", "Order not found");
                    listOrders(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid order ID");
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
    
 
    private void addOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer roomId = null;
        String deliveryTimeStr = null;
        String returnTimeStr = null;
        String status = null;
        String note = null;
        String[] itemIds = null;
        String[] quantities = null;
        String[] prices = null;
        
        try {
            String roomIdStr = request.getParameter("roomId");
            if (roomIdStr == null || roomIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn phòng");
                showAddFormWithData(request, response, null, null);
                return;
            }
            
            try {
                roomId = Integer.valueOf(roomIdStr);
                models.Room room = roomDao.getRoomById(roomId);
                if (room == null) {
                    request.setAttribute("errorMessage", "Phòng không tồn tại");
                    showAddFormWithData(request, response, null, null);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Mã phòng không hợp lệ");
                showAddFormWithData(request, response, null, null);
                return;
            }
            
            deliveryTimeStr = request.getParameter("deliveryTime");
            returnTimeStr = request.getParameter("returnTime");
            status = request.getParameter("status");
            note = request.getParameter("note");
            
            LaundryOrder order = new LaundryOrder();
            order.setStatus(status != null && !status.isEmpty() ? status : "PENDING");
            order.setNote(note);
            
            LocalDateTime pickupTime = null;
            LocalDateTime returnTime = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String errorMessage = null;
            
            if (deliveryTimeStr != null && !deliveryTimeStr.isEmpty()) {
                try {
                    pickupTime = LocalDateTime.parse(deliveryTimeStr, formatter);
                    order.setExpectedPickupTime(pickupTime);
                } catch (DateTimeParseException e) {
                    errorMessage = "Định dạng thời gian lấy đồ không hợp lệ";
                }
            }
            
            if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                try {
                    returnTime = LocalDateTime.parse(returnTimeStr, formatter);
                    order.setExpectedReturnTime(returnTime);
                } catch (DateTimeParseException e) {
                    errorMessage = "Định dạng thời gian trả đồ không hợp lệ";
                }
            }

            //Validate  ngày tháng
            if (errorMessage == null) {
               
                if (note != null && note.length() > 500) {
                    errorMessage = "Ghi chú không được vượt quá 500 ký tự";
                }      
                else {
                    LocalDateTime now = LocalDateTime.now();

                    if (pickupTime != null && returnTime != null) {
                        if (returnTime.isBefore(pickupTime)) {
                            errorMessage = "Thời gian trả đồ phải sau thời gian lấy đồ";
                        } 
                        else if (returnTime.isBefore(now)) {
                            errorMessage = "Thời gian trả đồ phải lớn hơn thời gian hiện tại";
                        } 
                        else if (pickupTime.isBefore(now)) {
                            errorMessage = "Thời gian lấy đồ phải lớn hơn thời gian hiện tại";
                        }
                    } 
                    else if (pickupTime != null) {
                        if (pickupTime.isBefore(now)) {
                            errorMessage = "Thời gian lấy đồ phải lớn hơn thời gian hiện tại";
                        }
                    } 
                    else if (returnTime != null) {
                        if (returnTime.isBefore(now)) {
                            errorMessage = "Thời gian trả đồ phải lớn hơn thời gian hiện tại";
                        }
                    }
                }
            }
            
            itemIds = request.getParameterValues("itemId");
            quantities = request.getParameterValues("quantity");
            prices = request.getParameterValues("price");
            
            ArrayList<LaundryOrderDetail> details = parseOrderDetails(itemIds, quantities, prices);
            order.setOrderDetails(details);
            

            if (errorMessage == null) {
              
                if (details.isEmpty()) {
                    errorMessage = "Vui lòng thêm ít nhất một mục vào đơn hàng";
                } else {
                    // Validate từng item
                    for (LaundryOrderDetail detail : details) {
                        if (detail.getLaundryItemId() <= 0) {
                            errorMessage = "Mã sản phẩm không hợp lệ";
                            break;
                        }
                        if (detail.getQuantity() <= 0) {
                            errorMessage = "Số lượng phải lớn hơn 0";
                            break;
                        }
                        if (detail.getUnitPrice() < 0) {
                            errorMessage = "Giá không được âm";
                            break;
                        }
                    }
                }
            }
                      
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                showAddFormWithData(request, response, order, roomId);
                return;
            }
            
           
            Integer newId = orderDAO.insertOrder(order, roomId);
            
            if (newId != null) {
                request.setAttribute("success", "Thêm đơn hàng thành công!");
                response.sendRedirect("laundry-order?action=view&id=" + newId);
            } else {
                request.setAttribute("errorMessage", "Thêm đơn hàng thất bại");
                showAddFormWithData(request, response, order, roomId);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi thêm đơn hàng: " + e.getMessage());
            LaundryOrder order = new LaundryOrder();
            order.setStatus(status != null && !status.isEmpty() ? status : "PENDING");
            order.setNote(note);
            try {
                if (deliveryTimeStr != null && !deliveryTimeStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    order.setExpectedPickupTime(LocalDateTime.parse(deliveryTimeStr, formatter));
                }
                if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    order.setExpectedReturnTime(LocalDateTime.parse(returnTimeStr, formatter));
                }
            } catch (Exception ex) {}
            if (itemIds != null || request.getParameterValues("itemId") != null) {
                String[] reqItemIds = itemIds != null ? itemIds : request.getParameterValues("itemId");
                String[] reqQuantities = quantities != null ? quantities : request.getParameterValues("quantity");
                String[] reqPrices = prices != null ? prices : request.getParameterValues("price");
                ArrayList<LaundryOrderDetail> details = parseOrderDetails(reqItemIds, reqQuantities, reqPrices);
                order.setOrderDetails(details);
            }
            showAddFormWithData(request, response, order, roomId);
        }
    }
    
   
    private ArrayList<LaundryOrderDetail> parseOrderDetails(String[] itemIds, String[] quantities, String[] prices) {
        ArrayList<LaundryOrderDetail> details = new ArrayList<>();
        
        if (itemIds != null && quantities != null && prices != null && itemIds.length > 0) {
            for (int i = 0; i < itemIds.length; i++) {
                if (itemIds[i] != null && !itemIds[i].isEmpty() && 
                    quantities[i] != null && !quantities[i].isEmpty() &&
                    prices[i] != null && !prices[i].isEmpty()) {
                    
                    try {
                        int itemId = Integer.parseInt(itemIds[i]);
                        int quantity = Integer.parseInt(quantities[i]);
                        double price = Double.parseDouble(prices[i]);
                        
                        LaundryOrderDetail detail = new LaundryOrderDetail();
                        detail.setLaundryItemId(itemId);
                        detail.setQuantity(quantity);
                        detail.setUnitPrice(price);
                        detail.setSubtotal(quantity * price);
                        
                        details.add(detail);
                    } catch (NumberFormatException e) {
                        try {
                            LaundryOrderDetail detail = new LaundryOrderDetail();
                            detail.setLaundryItemId(itemIds[i].isEmpty() ? 0 : Integer.parseInt(itemIds[i]));
                            detail.setQuantity(quantities[i].isEmpty() ? 0 : Integer.parseInt(quantities[i]));
                            detail.setUnitPrice(prices[i].isEmpty() ? 0 : Double.parseDouble(prices[i]));
                            detail.setSubtotal(detail.getQuantity() * detail.getUnitPrice());
                            details.add(detail);
                        } catch (NumberFormatException ex) {
                            // Bỏ qua 
                        }
                    }
                }
            }
        }
        
        return details;
    }
    
 
    private void showAddFormWithData(HttpServletRequest request, HttpServletResponse response, LaundryOrder order, Integer roomId)
            throws ServletException, IOException {
        ArrayList<LaundryItem> items = itemDAO.getAllActiveItems();
        ArrayList<Room> rooms = (ArrayList<Room>) roomDao.getAllActiveLoginRooms();
        request.setAttribute("items", items);
        request.setAttribute("rooms", rooms);
        request.setAttribute("order", order);
        request.setAttribute("roomId", roomId);
        request.getRequestDispatcher("/WEB-INF/views/laundry/add.jsp").forward(request, response);
    }
    

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
    
    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int laundryId = 0;
        int orderId = 0;
        String expectedPickupTimeStr = null;
        String returnTimeStr = null;
        String status = null;
        String note = null;
        String[] itemIds = null;
        String[] quantities = null;
        String[] prices = null;
        
        try {
            try {
                laundryId = Integer.parseInt(request.getParameter("laundryId"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Mã đơn giặt là không hợp lệ");
                showEditFormWithData(request, response, null);
                return;
            }
            
            try {
                orderId = Integer.parseInt(request.getParameter("orderId"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Mã đơn dịch vụ không hợp lệ");
                showEditFormWithData(request, response, null);
                return;
            }
            
            expectedPickupTimeStr = request.getParameter("deliveryTime");
            returnTimeStr = request.getParameter("returnTime");
            status = request.getParameter("status");
            note = request.getParameter("note");
            
            LaundryOrder order = new LaundryOrder();
            order.setLaundryId(laundryId);
            order.setOrderId(orderId);
            order.setStatus(status);
            order.setNote(note);
            
            LocalDateTime pickupTime = null;
            LocalDateTime returnTime = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String errorMessage = null;
            
            // Parse và validate ngày tháng
            if (expectedPickupTimeStr != null && !expectedPickupTimeStr.isEmpty()) {
                try {
                    pickupTime = LocalDateTime.parse(expectedPickupTimeStr, formatter);
                    order.setExpectedPickupTime(pickupTime);
                } catch (DateTimeParseException e) {
                    errorMessage = "Định dạng thời gian lấy đồ không hợp lệ";
                }
            }
            
            if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                try {
                    returnTime = LocalDateTime.parse(returnTimeStr, formatter);
                    order.setExpectedReturnTime(returnTime);
                } catch (DateTimeParseException e) {
                    errorMessage = "Định dạng thời gian trả đồ không hợp lệ";
                }
            }

            if (errorMessage == null) {
                if (note != null && note.length() > 500) {
                    errorMessage = "Ghi chú không được vượt quá 500 ký tự";
                } 
                else {
                    LocalDateTime now = LocalDateTime.now();

                    if (pickupTime != null && returnTime != null) {
                        if (returnTime.isBefore(pickupTime)) {
                            errorMessage = "Thời gian trả đồ phải sau thời gian lấy đồ";
                        } 
                        else if (returnTime.isBefore(now)) {
                            errorMessage = "Thời gian trả đồ phải lớn hơn thời gian hiện tại";
                        } 
                        else if (pickupTime.isBefore(now)) {
                            errorMessage = "Thời gian lấy đồ phải lớn hơn thời gian hiện tại";
                        }
                    } 
                    else if (pickupTime != null) {
                        if (pickupTime.isBefore(now)) {
                            errorMessage = "Thời gian lấy đồ phải lớn hơn thời gian hiện tại";
                        }
                    } 
                    else if (returnTime != null) {
                        if (returnTime.isBefore(now)) {
                            errorMessage = "Thời gian trả đồ phải lớn hơn thời gian hiện tại";
                        }
                    }
                }
            }
            
            itemIds = request.getParameterValues("itemId");
            quantities = request.getParameterValues("quantity");
            prices = request.getParameterValues("price");
            
            ArrayList<LaundryOrderDetail> details = parseOrderDetails(itemIds, quantities, prices);
            order.setOrderDetails(details);
            
            if (errorMessage == null) {
                if (details.isEmpty()) {
                    errorMessage = "Vui lòng thêm ít nhất một mục vào đơn hàng";
                } else {
                    for (LaundryOrderDetail detail : details) {
                        if (detail.getLaundryItemId() <= 0) {
                            errorMessage = "Mã sản phẩm không hợp lệ";
                            break;
                        }
                        if (detail.getQuantity() <= 0) {
                            errorMessage = "Số lượng phải lớn hơn 0";
                            break;
                        }
                        if (detail.getUnitPrice() < 0) {
                            errorMessage = "Giá không được âm";
                            break;
                        }
                    }
                }
            }
            
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                showEditFormWithData(request, response, order);
                return;
            }
            
            boolean success = orderDAO.updateOrder(order);
            
            if (success) {
                response.sendRedirect("laundry-order?action=view&id=" + laundryId + "&success=updated");
            } else {
                request.setAttribute("errorMessage", "Cập nhật đơn hàng thất bại");
                showEditFormWithData(request, response, order);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cập nhật đơn hàng: " + e.getMessage());
            LaundryOrder order = new LaundryOrder();
            order.setLaundryId(laundryId);
            order.setOrderId(orderId);
            order.setStatus(status);
            order.setNote(note);
            try {
                if (expectedPickupTimeStr != null && !expectedPickupTimeStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    order.setExpectedPickupTime(LocalDateTime.parse(expectedPickupTimeStr, formatter));
                }
                if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    order.setExpectedReturnTime(LocalDateTime.parse(returnTimeStr, formatter));
                }
            } catch (Exception ex) {}
            // Parse order details từ request để giữ lại dữ liệu
            if (itemIds != null || request.getParameterValues("itemId") != null) {
                String[] reqItemIds = itemIds != null ? itemIds : request.getParameterValues("itemId");
                String[] reqQuantities = quantities != null ? quantities : request.getParameterValues("quantity");
                String[] reqPrices = prices != null ? prices : request.getParameterValues("price");
                ArrayList<LaundryOrderDetail> details = parseOrderDetails(reqItemIds, reqQuantities, reqPrices);
                order.setOrderDetails(details);
            }
            showEditFormWithData(request, response, order);
        }
    }
    
    // method hiển thị form edit với dữ liệu đã nhập
    private void showEditFormWithData(HttpServletRequest request, HttpServletResponse response, LaundryOrder order)
            throws ServletException, IOException {
        ArrayList<LaundryItem> items = itemDAO.getAllActiveItems();
        request.setAttribute("items", items);
        
        // Nếu order null, lấy từ database
        if (order == null || order.getLaundryId() == 0) {
            String idStr = request.getParameter("laundryId");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int laundryId = Integer.parseInt(idStr);
                    order = orderDAO.getOrderById(laundryId);
                } catch (NumberFormatException e) {}
            }
        }
        
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/views/laundry/edit.jsp").forward(request, response);
    }
    
    
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
