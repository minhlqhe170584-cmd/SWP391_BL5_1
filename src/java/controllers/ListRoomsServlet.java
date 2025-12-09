package controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Tên Servlet: ListRoomsServlet
// Đường dẫn chạy: /rooms (Khớp với menu của bạn)
@WebServlet(name = "ListRoomsServlet", urlPatterns = {"/listRooms"})
public class ListRoomsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. (Sau này code: Gọi DAO lấy danh sách phòng từ Database)
        // RoomDAO dao = new RoomDAO();
        // List<Room> list = dao.getAllRooms();
        // request.setAttribute("listRoom", list);

        // 2. Chuyển hướng vào trang giao diện
        // File rooms.jsp nằm trong thư mục bảo mật WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/home/listRooms.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}