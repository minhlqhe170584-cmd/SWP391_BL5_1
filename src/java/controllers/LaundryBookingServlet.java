package controllers;

import dao.LaundryItemDAO;
import dao.LaundryOrderDAO;
import dao.ServiceOrderDAO;
import dao.ServiceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import models.LaundryItem;
import models.LaundryOrder;
import models.LaundryOrderDetail;
import models.Service;

/**
 *
 * @author Acer
 */
@WebServlet(name = "LaundryBookingServlet", urlPatterns = {"/laundry-book"})
public class LaundryBookingServlet extends HttpServlet {

    
    private LaundryItemDAO itemDAO;
    private ServiceDAO serviceDAO;
    private ServiceOrderDAO serviceOrderDAO;
    private LaundryOrderDAO laundryOrderDAO;
    
    @Override
    public void init() throws ServletException {
        itemDAO = new LaundryItemDAO();
        serviceDAO = new ServiceDAO();
        serviceOrderDAO = new ServiceOrderDAO();
        laundryOrderDAO = new LaundryOrderDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer roomId = (Integer) session.getAttribute("ROOM_ID");
        
        // Default room for testing
        if (roomId == null) {
            roomId = 1;
            session.setAttribute("ROOM_ID", roomId);
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "order";
        }
        
        switch (action) {
            case "order":
                showOrderForm(request, response, roomId);
                break;
            default:
                showOrderForm(request, response, roomId);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer roomId = (Integer) session.getAttribute("ROOM_ID");
        
        if (roomId == null) {
            roomId = 1;
            session.setAttribute("ROOM_ID", roomId);
        }
        
        String action = request.getParameter("action");
        
        if ("order".equals(action)) {
            placeOrder(request, response, roomId);
        } else {
            showOrderForm(request, response, roomId);
        }
    }
    
    /**
     * Show order form with available items grouped by service
     */
    private void showOrderForm(HttpServletRequest request, HttpServletResponse response, Integer roomId)
            throws ServletException, IOException {
        // Get active services and items
        ArrayList<Service> services = itemDAO.getActiveServices();
        ArrayList<LaundryItem> items = itemDAO.getAllActiveItems();
        
        request.setAttribute("services", services);
        request.setAttribute("items", items);
        request.setAttribute("roomId", roomId);
        
        request.getRequestDispatcher("/WEB-INF/views/client/laundry-order.jsp").forward(request, response);
    }
    
    /**
     * Place new laundry order
     */
    private void placeOrder(HttpServletRequest request, HttpServletResponse response, Integer roomId)
            throws ServletException, IOException {
        try {
            // Get form parameters
            String pickupTimeStr = request.getParameter("pickupTime");
            String returnTimeStr = request.getParameter("returnTime");
            String note = request.getParameter("note");
            
            // Parse pickup time if provided
            LocalDateTime pickupTime = null;
            if (pickupTimeStr != null && !pickupTimeStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                pickupTime = LocalDateTime.parse(pickupTimeStr, formatter);
            }
            
            LocalDateTime returnTime = null;
            if (returnTimeStr != null && !returnTimeStr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                returnTime = LocalDateTime.parse(returnTimeStr, formatter);
            }
            
            // Get ALL form parameters
            String[] itemIds = request.getParameterValues("itemId");
            String[] quantities = request.getParameterValues("quantity");
            String[] prices = request.getParameterValues("price");
            
            // Validate basic arrays exist
            if (itemIds == null || quantities == null || prices == null) {
                request.setAttribute("error", "Vui lòng chọn ít nhất một mục để đặt đơn");
                showOrderForm(request, response, roomId);
                return;
            }
            
            // Create order details list
            ArrayList<LaundryOrderDetail> orderDetails = new ArrayList<>();
            
            // Process each item - only add if checkbox is checked
            for (int i = 0; i < itemIds.length; i++) {
                String itemId = itemIds[i];
                
                // Check if this item was selected (checkbox checked)
                String checkboxValue = request.getParameter("selected_" + itemId);
                
                if (checkboxValue != null && "true".equals(checkboxValue)) {
                    // Item is selected, add to order
                    Integer laundryItemId = Integer.valueOf(itemId);
                    Integer quantity = Integer.valueOf(quantities[i]);
                    Double price = Double.valueOf(prices[i]);                  
                    
                    LaundryOrderDetail detail = new LaundryOrderDetail();
                    detail.setLaundryItemId(laundryItemId);
                    detail.setQuantity(quantity);
                    detail.setUnitPrice(price);                   
                    
                    orderDetails.add(detail);
                }
            }

            if (orderDetails.isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn ít nhất một mục để đặt đơn");
                showOrderForm(request, response, roomId);
                return;
            }

            Integer laundryId = serviceOrderDAO.createLaundryOrder(roomId, pickupTime, returnTime, note, orderDetails);
            
            if (laundryId != null) {
                request.setAttribute("message", "Booking successful! Wait until staff go to take your stuffs.");
                request.getRequestDispatcher("/WEB-INF/views/client/success.jsp").forward(request, response);
            } else {              
                request.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại.");
                showOrderForm(request, response, roomId);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            showOrderForm(request, response, roomId);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi đặt đơn: " + e.getMessage());
            showOrderForm(request, response, roomId);
        }
    }
}
