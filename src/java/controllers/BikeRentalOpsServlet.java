package controllers;

import dao.BikeTransactionDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import models.Bicycle;
import models.BikeServiceOrder;
import models.Staff;

@WebServlet(name = "BikeRentalOpsServlet", urlPatterns = {"/bike-ops"})
public class BikeRentalOpsServlet extends HttpServlet {

    private final BikeTransactionDAO dao = new BikeTransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String view = request.getParameter("view");
        if (view == null) view = "pending";

        if ("pending".equals(view)) {
            ArrayList<BikeServiceOrder> orders = dao.getOrdersByStatus("Pending");
            request.setAttribute("orders", orders);
            
            if (!orders.isEmpty()) {
                int firstServiceId = dao.getServiceIdByOrderId(orders.get(0).getOrderId());
                if(firstServiceId > 0) {
                    request.setAttribute("bikes", dao.getPhysicalBikesForHandover(firstServiceId));
                } else {
                    request.setAttribute("bikes", new ArrayList<Bicycle>());
                }
            } else {
                request.setAttribute("bikes", new ArrayList<Bicycle>());
            }
            
        } else if ("active".equals(view)) {
            request.setAttribute("orders", dao.getOrdersByStatus("Confirmed"));
        }

        request.setAttribute("view", view);
        request.getRequestDispatcher("/WEB-INF/views/staff-task/bike_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Staff staff = (Staff) session.getAttribute("USER");
        
        if (staff == null) {
            response.sendRedirect("login");
            return;
        }
        
        int staffId = staff.getStaffId();
        String action = request.getParameter("action");
        String orderIdRaw = request.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdRaw);

            if ("handover".equals(action)) {
                String[] bikeIds = request.getParameterValues("bikeIds");
                if (bikeIds != null && bikeIds.length > 0) {
                    String result = dao.handoverBikes(orderId, bikeIds, staffId);
                    
                    switch (result) {
                        case "SUCCESS":
                            response.sendRedirect("bike-ops?view=active&msg=HandoverSuccess");
                            break;
                        case "EXPIRED":
                            response.sendRedirect("bike-ops?view=pending&error=OrderExpiredAndCancelled");
                            break;
                        case "TOO_EARLY":
                            response.sendRedirect("bike-ops?view=pending&error=TooEarly");
                            break;
                        case "TOO_LATE":
                            response.sendRedirect("bike-ops?view=pending&error=TooLate");
                            break;
                        default:
                            response.sendRedirect("bike-ops?view=pending&error=SystemError");
                            break;
                    }
                } else {
                    response.sendRedirect("bike-ops?view=pending&error=NoBikeSelected");
                }
            } else if ("return".equals(action)) {
                dao.returnBikes(orderId, staffId);
                response.sendRedirect("bike-ops?view=active&msg=Returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("bike-ops");
        }
    }
}