package controllers;

import dao.CustomerDAO;
import dao.ServiceOrderDAO;
import models.Customer;
import models.Room;
import models.ServiceHistory;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("USER");
        String role = (String) session.getAttribute("ROLE");

        // 1. Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // --- TRƯỜNG HỢP 1: KHÁCH HÀNG (Hiển thị thông tin cá nhân) ---
            if ("CUSTOMER".equals(role)) {
                request.setAttribute("myProfile", user);
            } 
            // --- TRƯỜNG HỢP 2: PHÒNG (Hiển thị Tablet Mode - Lịch sử dịch vụ) ---
            else if ("ROOM".equals(role)) {
                Room room = (Room) user;
                ServiceOrderDAO orderDAO = new ServiceOrderDAO();
                
                // Lấy danh sách đơn chưa thanh toán
                List<ServiceHistory> history = orderDAO.getHistoryByRoomId(room.getRoomId());
                
                // [NÂNG CẤP] Tính tổng tiền tạm tính để hiển thị cho khách biết
                double totalPending = 0;
                if (history != null) {
                    for (ServiceHistory h : history) {
                        totalPending += h.getSubtotal();
                    }
                }
                
                request.setAttribute("roomInfo", room);
                request.setAttribute("serviceHistory", history);
                request.setAttribute("totalPending", totalPending); // Gửi tổng tiền sang JSP
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/views/users/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("ROLE");
        
        // Xử lý cập nhật thông tin cho Khách hàng
        if ("CUSTOMER".equals(role)) {
            Customer current = (Customer) session.getAttribute("USER");
            
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            
            if (fullName != null && !fullName.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                password != null && !password.trim().isEmpty()) {
                
                // [NÂNG CẤP] Clone dữ liệu để update thử
                Customer updateReq = new Customer();
                updateReq.setCustomerId(current.getCustomerId()); // Giả sử có getter ID
                updateReq.setFullName(fullName.trim());
                updateReq.setPhone(phone.trim());
                updateReq.setPassword(password.trim());
                updateReq.setEmail(current.getEmail()); // Giữ nguyên email
                
                CustomerDAO dao = new CustomerDAO();
                
                // Chỉ cập nhật vào Session khi DB update thành công
                if(dao.updateProfile(updateReq)) {
                    // Update thành công -> Cập nhật lại Session
                    current.setFullName(fullName.trim());
                    current.setPhone(phone.trim());
                    current.setPassword(password.trim());
                    
                    session.setAttribute("USER", current);
                    request.setAttribute("message", "Cập nhật hồ sơ thành công!");
                } else {
                    request.setAttribute("error", "Lỗi hệ thống, không thể cập nhật!");
                }
            } else {
                request.setAttribute("error", "Vui lòng không để trống thông tin!");
            }
        }
        
        // Load lại trang để hiển thị kết quả
        doGet(request, response);
    }
}