package controllers;

import dao.BikeTransactionDAO;
import dao.ServiceDAO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.BikeRentalOption;
import models.Service;
import models.SlotAvailability;

@WebServlet(name = "BikeBookingServlet", urlPatterns = {"/book-bike"})
public class BikeBookingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        BikeTransactionDAO dao = new BikeTransactionDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        
        String serviceIdRaw = request.getParameter("serviceId");

        if (serviceIdRaw == null || serviceIdRaw.trim().isEmpty()) {
            ArrayList<Service> bikeServices = dao.getAllBikeServices();
            request.setAttribute("bikeServices", bikeServices);
            request.getRequestDispatcher("/WEB-INF/views/client/bike_list.jsp").forward(request, response);
        } else {
            try {
                int serviceId = Integer.parseInt(serviceIdRaw);
                Service service = serviceDAO.getById(serviceId);
                
                if (service != null) {
                    request.setAttribute("service", service);
                    request.setAttribute("options", dao.getBikeOptions(serviceId));
                    request.getRequestDispatcher("/WEB-INF/views/client/bike_booking.jsp").forward(request, response);
                } else {
                    response.sendRedirect("book-bike"); 
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("book-bike");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        BikeTransactionDAO dao = new BikeTransactionDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        HttpSession session = request.getSession();
        
        Integer roomId = (Integer) session.getAttribute("ROOM_ID");
        if (roomId == null) roomId = 1; 

        String action = request.getParameter("action");
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

            Service service = serviceDAO.getById(serviceId);
            request.setAttribute("service", service);
            request.setAttribute("options", dao.getBikeOptions(serviceId));

            if ("check".equals(action)) {
                ArrayList<SlotAvailability> slots = dao.getHourlyAvailability(start, end, serviceId);
                request.setAttribute("checkResult", slots);
                request.setAttribute("requestQty", quantity);
                request.getRequestDispatcher("/WEB-INF/views/client/bike_booking.jsp").forward(request, response);
            } 
            else if ("book".equals(action)) {
                boolean success = dao.createBikeBooking(roomId, serviceId, optionId, quantity, note, start, end);
                if (success) {
                    request.setAttribute("message", "Booking successful! Please come to lobby to pick up your bike.");
                    request.getRequestDispatcher("/WEB-INF/views/client/success.jsp").forward(request, response);
                } else {
                    ArrayList<SlotAvailability> slots = dao.getHourlyAvailability(start, end, serviceId);
                    request.setAttribute("checkResult", slots);
                    request.setAttribute("requestQty", quantity);
                    request.setAttribute("errorMessage", "Booking Failed. Please check availability again.");
                    request.getRequestDispatcher("/WEB-INF/views/client/bike_booking.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("book-bike");
        }
    }
}