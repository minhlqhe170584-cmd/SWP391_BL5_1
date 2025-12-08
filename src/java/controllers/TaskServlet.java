/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.TaskDAO;
import dao.RoomDAO;
import java.util.ArrayList;
import java.util.List;
import models.Room;
import models.Task;

/**
 *
 * @author Acer
 */
@WebServlet(name = "TaskServlet", urlPatterns = {"/task"})
public class TaskServlet extends HttpServlet {
    
    private final TaskDAO taskDao = new TaskDAO();
    private final RoomDAO roomDao = new RoomDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "detail":
                showDetailForm(request, response);
                break;
            default:
                listTask(request, response);
                break;
        }
    }
    
    private void showDetailForm(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void listTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String roomId = request.getParameter("RoomId");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");
        
        int pageIndex = 1;
        int pageSize = 5;
         if (pageParam != null && !pageParam.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageParam);
                if (pageIndex < 1) {
                    pageIndex = 1;
                }
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        ArrayList<Task> list = taskDao.search(search, status, roomId, sort, pageIndex, pageSize);
        int totalTask = taskDao.countSearch(search, status, roomId);
        int totalPages = (int) Math.ceil((double)totalTask/pageSize);
        List<Room> rooms = roomDao.getAllRooms();
        
        request.setAttribute("tasks", list);
        request.setAttribute("rooms", rooms);
        request.setAttribute("roomId", roomId);
        request.setAttribute("sort", sort);
        request.setAttribute("status", status);
        request.setAttribute("page", pageIndex);
        request.setAttribute("totalPages", totalPages);
         
        request.getRequestDispatcher("/WEB-INF/views/task/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
