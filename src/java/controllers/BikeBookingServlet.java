package controllers;

import dao.BikeBookingDAO;
import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.BikeRentalOption;

@WebServlet(name = "BikeBookingServlet", urlPatterns = {"/book-bike"})
public class BikeBookingServlet extends HttpServlet {

    private final BikeBookingDAO bikeDAO = new BikeBookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy danh sách các gói thuê xe (1h, 1 ngày...)
        ArrayList<BikeRentalOption> options = bikeDAO.getBikeOptions();

        request.setAttribute("options", options);
        request.getRequestDispatcher("/WEB-INF/views/client/bike_booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String optionIdRaw = request.getParameter("optionId");
        String quantityRaw = request.getParameter("quantity");
        String note = request.getParameter("note");
        
        // Giả lập RoomID (Sau này lấy từ Session login)
        int roomId = 1; 

        try {
            int optionId = Integer.parseInt(optionIdRaw);
            int quantity = Integer.parseInt(quantityRaw);

            boolean success = bikeDAO.createBikeOrder(roomId, optionId, quantity, note);

            if (success) {
                request.setAttribute("message", "Bike rental request submitted successfully!");
                request.getRequestDispatcher("/WEB-INF/views/client/success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to book bike. Please try again.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("book-bike");
        }
    }
}