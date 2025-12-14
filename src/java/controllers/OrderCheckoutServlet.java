package controllers;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.sql.SQLException;

@WebServlet(name = "OrderCheckoutServlet", urlPatterns = {"/checkout-order"})
public class OrderCheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
    }

    // Xử lý POST request từ form food_menu.jsp
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String roomNumber = request.getParameter("roomNumber");
        String note = request.getParameter("note");

        int roomId = -1;
        int orderId = -1;
        boolean transactionSuccess = false;

        try {
            // 1. Kiểm tra và lấy Room ID
            roomId = orderDAO.getRoomIdByNumber(roomNumber);

            if (roomId == -1) {
                request.setAttribute("errorMessage", "Số phòng " + roomNumber + " không hợp lệ hoặc không tồn tại.");
                request.getRequestDispatcher("/WEB-INF/views/menu/food_menu.jsp").forward(request, response);
                return;
            }

            // 2. Tạo Order chính (ServiceOrders)
            orderId = orderDAO.createOrder(roomId, note);

            if (orderId == -1) {
                throw new Exception("Lỗi hệ thống khi tạo đơn hàng chính.");
            }

            // 3. Lặp qua các tham số Form để tìm các món đã đặt
            Enumeration<String> parameterNames = request.getParameterNames();
            boolean hasItems = false;

            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();

                if (paramName.startsWith("item_serviceId_")) {
                    int serviceId = Integer.parseInt(paramName.substring(paramName.lastIndexOf("_") + 1));

                    // Xử lý ngoại lệ NumberFormatException nếu người dùng nhập ký tự
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(request.getParameter(paramName));
                    } catch (NumberFormatException nfe) {
                        quantity = 0;
                    }

                    if (quantity > 0) {
                        hasItems = true;

                        // 4. Lấy thông tin giá/tên Service
                        try (ResultSet rs = orderDAO.getServiceDetail(serviceId)) { // Dùng tên hàm đã sửa
                            if (rs.next()) {
                                String itemName = rs.getString("service_name");
                                double unitPrice = rs.getDouble("price"); // Cột 'price' bây giờ đã tồn tại do Alias

                                // 6. Tạo Order Detail
                                orderDAO.createOrderDetail(orderId, serviceId, itemName, quantity, unitPrice);
                            }
                        }
                    }
                }
            }

            if (!hasItems) {
                // Nếu không có món nào được chọn, hủy Order rỗng
                // (Bạn cần thêm hàm deleteOrder(orderId) vào OrderDAO nếu muốn xóa hoàn toàn)
                request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một món để đặt.");
                request.getRequestDispatcher("/WEB-INF/views/menu/food_menu.jsp").forward(request, response);
                return;
            }

            // 6. Cập nhật Total Amount
            orderDAO.updateTotalAmount(orderId);
            transactionSuccess = true;

            // 7. Chuyển hướng thành công
            request.setAttribute("orderId", orderId);
            request.getRequestDispatcher("/WEB-INF/views/menu/order_success.jsp").forward(request, response);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi SQL: Không thể lưu đơn hàng. Vui lòng kiểm tra database.");
            request.getRequestDispatcher("/WEB-INF/views/menu/food_menu.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi xử lý đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/menu/food_menu.jsp").forward(request, response);
        }
    }
}
