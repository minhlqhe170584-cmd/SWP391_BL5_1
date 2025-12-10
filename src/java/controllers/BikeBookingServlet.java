package controllers;

import dao.BikeTransactionDAO;
import java.io.IOException;
import java.time.LocalDateTime;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.BikeRentalOption;
import models.Customer; 

@WebServlet(name = "BikeBookingServlet", urlPatterns = {"/book-bike"})
public class BikeBookingServlet extends HttpServlet {

    private final BikeTransactionDAO dao = new BikeTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int serviceId = 1;
        try {
            String serviceIdRaw = request.getParameter("serviceId");
            if(serviceIdRaw != null) serviceId = Integer.parseInt(serviceIdRaw);
            else serviceId = dao.getBikeServiceId(); 
        } catch(Exception e) { serviceId = dao.getBikeServiceId(); }

        request.setAttribute("serviceId", serviceId); 
        request.setAttribute("options", dao.getBikeOptions(serviceId));
        request.getRequestDispatcher("/WEB-INF/views/client/bike_booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        Integer roomIdObj = (Integer) session.getAttribute("ROOM_ID");
        
        if (roomIdObj == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int roomId = roomIdObj;

        String serviceIdRaw = request.getParameter("serviceId");
        String optionIdRaw = request.getParameter("optionId");
        String quantityRaw = request.getParameter("quantity");
        String startTimeRaw = request.getParameter("startTime");
        String note = request.getParameter("note");

        try {
            int serviceId = Integer.parseInt(serviceIdRaw);
            int optionId = Integer.parseInt(optionIdRaw);
            int quantity = Integer.parseInt(quantityRaw);
            
            BikeRentalOption option = dao.getOptionById(optionId);
            LocalDateTime startLDT = LocalDateTime.parse(startTimeRaw);
            LocalDateTime endLDT = startLDT.plusMinutes(option.getDurationMinutes());
            
            java.sql.Timestamp start = java.sql.Timestamp.valueOf(startLDT);
            java.sql.Timestamp end = java.sql.Timestamp.valueOf(endLDT);

            boolean success = dao.createBikeBooking(roomId, serviceId, optionId, quantity, note, start, end);

            if (success) {
                request.setAttribute("message", "Booking successful!");
                request.getRequestDispatcher("/WEB-INF/views/client/success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Not enough bikes available for this time slot.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("book-bike");
        }
    }
}