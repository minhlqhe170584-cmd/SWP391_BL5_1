package controllers;

import dao.RoomDAO;
import models.Room;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ListRoomServlet", urlPatterns = {"/listRooms"})
public class ListRoomsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            RoomDAO dao = new RoomDAO();
            
            // Giữ nguyên logic phân trang 6 phòng
            int pageSize = 6; 
            
            String pageStr = request.getParameter("page");
            int page = 1;
            
            if (pageStr != null && !pageStr.isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                } catch (NumberFormatException e) {
                    page = 1; 
                }
            }
            
            // Tính toán tổng số trang
            int totalRooms = dao.countTotalRooms();
            int totalPages = (int) Math.ceil((double) totalRooms / pageSize);
            
            // Lấy danh sách phòng theo trang
            List<Room> list = dao.getRoomsByPage(page, pageSize);
            
            request.setAttribute("listRooms", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            
            request.getRequestDispatcher("/WEB-INF/views/home/listRooms.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}