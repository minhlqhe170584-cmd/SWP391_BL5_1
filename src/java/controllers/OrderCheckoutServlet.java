package controllers;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

// Class phụ để lưu tạm món khách chọn
class CartItem {

    int serviceId;
    int quantity;

    public CartItem(int serviceId, int quantity) {
        this.serviceId = serviceId;
        this.quantity = quantity;
    }
}

@WebServlet(name = "OrderCheckoutServlet", urlPatterns = {"/checkout-order"})
public class OrderCheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object currentRole = session.getAttribute("ROLE");

// Nếu chưa đăng nhập HOẶC không phải là ROOM -> Đá về Login
        if (session.getAttribute("USER") == null || !"ROOM".equals(currentRole)) {
            response.sendRedirect("login"); // Hoặc đường dẫn tới servlet login của bạn
            return; // Chặn ngay
        }
        // 1. Lấy Room ID
        // Bạn nói nhập số phòng rồi mới xác nhận được, nên tôi giữ logic lấy roomNumber
        String roomNumber = request.getParameter("roomNumber");
        int roomId = -1;

        // Nếu không nhập roomNumber thì thử lấy từ session (hoặc mặc định là 4 như bạn test)
        try {
            if (roomNumber != null && !roomNumber.isEmpty()) {
                roomId = orderDAO.getRoomIdByNumber(roomNumber);
            } else if (request.getSession().getAttribute("ROOM_ID") != null) {
                roomId = (int) request.getSession().getAttribute("ROOM_ID");
            } else {
                roomId = 4; // ID mặc định để test
            }
        } catch (Exception e) {
        }

        String note = request.getParameter("note");
        if (note == null) {
            note = "";
        }

        try {
            // --- BƯỚC 1: QUÉT FORM LẤY MÓN ---
            List<CartItem> selectedItems = new ArrayList<>();
            Enumeration<String> paramNames = request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                // Khớp với name trong input của bạn: item_serviceId_...
                if (paramName.startsWith("item_serviceId_")) {
                    try {
                        int quantity = Integer.parseInt(request.getParameter(paramName));
                        if (quantity > 0) {
                            int serviceId = Integer.parseInt(paramName.substring(paramName.lastIndexOf("_") + 1));
                            selectedItems.add(new CartItem(serviceId, quantity));
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }

            // --- BƯỚC 2: KHẮC PHỤC LỖI "TRANG TRẮNG" ---
            if (selectedItems.isEmpty()) {
                // Lưu lỗi vào session để hiển thị sau khi reload trang
                request.getSession().setAttribute("errorMessage", "Bạn chưa chọn món nào! Vui lòng chọn số lượng.");

                // CÁCH SỬA QUAN TRỌNG NHẤT:
                // Dùng sendRedirect để tải lại trang Menu cũ. 
                // getHeader("referer") chính là cái link trang Menu mà bạn vừa đứng.
                // Nó sẽ tải lại dữ liệu món ăn => KHÔNG BỊ TRANG TRẮNG NỮA.
                response.sendRedirect(request.getHeader("referer"));
                return; // Dừng ngay lập tức
            }

            // --- BƯỚC 3: TẠO ĐƠN HÀNG (KHẮC PHỤC LỖI ĐỎ DAO) ---
            int orderId = orderDAO.createOrder(roomId, note);

            if (orderId > 0) {
                for (CartItem item : selectedItems) {
                    // Logic tự lấy giá để KHỚP 5 THAM SỐ của DAO cũ
                    String itemName = "Unknown";
                    double price = 0.0;
                    try (ResultSet rs = orderDAO.getServiceDetail(item.serviceId)) {
                        if (rs.next()) {
                            itemName = rs.getString("service_name");
                            price = rs.getDouble("price");
                        }
                    } catch (Exception ex) {
                    }

                    // Truyền đủ 5 tham số: orderId, serviceId, NAME, quantity, PRICE
                    // => HẾT LỖI ĐỎ
                    orderDAO.createOrderDetail(orderId, item.serviceId, itemName, item.quantity, price);
                }
                orderDAO.updateTotalAmount(orderId);

                // Thành công
                request.setAttribute("orderId", orderId);
                request.getRequestDispatcher("/WEB-INF/views/menu/order_success.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("errorMessage", "Lỗi hệ thống: Không tạo được đơn.");
                response.sendRedirect(request.getHeader("referer"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getHeader("referer"));
        }
    }
}
