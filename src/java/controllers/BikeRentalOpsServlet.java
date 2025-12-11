package controllers;

import dao.BikeTransactionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BikeRentalOpsServlet", urlPatterns = {"/bike-ops"})
public class BikeRentalOpsServlet extends HttpServlet {

    private final BikeTransactionDAO dao = new BikeTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String view = request.getParameter("view");
        if (view == null) view = "pending";

        if ("pending".equals(view)) {
            request.setAttribute("orders", dao.getOrdersByStatus("Pending"));
            request.setAttribute("bikes", dao.getPhysicalBikesForHandover());
        } else if ("active".equals(view)) {
            request.setAttribute("orders", dao.getOrdersByStatus("Confirmed"));
        }

        request.setAttribute("view", view);
        request.getRequestDispatcher("/WEB-INF/views/staff-task/bike_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String orderIdRaw = request.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdRaw);

            if ("handover".equals(action)) {
                String[] bikeIds = request.getParameterValues("bikeIds");
                if (bikeIds != null && bikeIds.length > 0) {
                    dao.handoverBikes(orderId, bikeIds);
                    response.sendRedirect("bike-ops?view=active");
                } else {
                    response.sendRedirect("bike-ops?view=pending&error=NoBikeSelected");
                }
            } else if ("return".equals(action)) {
                String paymentMethod = request.getParameter("paymentMethod");
                if (paymentMethod == null || paymentMethod.isEmpty()) {
                    paymentMethod = "Cash";
                }
                
                dao.returnBikesAndPay(orderId, paymentMethod);
                response.sendRedirect("bike-ops?view=active&msg=Returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("bike-ops");
        }
    }
}