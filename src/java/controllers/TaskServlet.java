/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.TaskDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import models.Staff;
import models.Task;

/**
 *
 * @author Acer
 */
@WebServlet(name = "TaskServlet", urlPatterns = {"/task"})
public class TaskServlet extends HttpServlet {

    private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Staff loggedIn = session != null ? (Staff) session.getAttribute("USER") : null;

        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String sort = request.getParameter("sort");
        String pageRaw = request.getParameter("page");

        int page = 1;
        int pageSize = 10;
        try {
            if (pageRaw != null) {
                page = Integer.parseInt(pageRaw);
                if (page < 1) {
                    page = 1;
                }
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        // If user is Staff, force filter by their own staffId
        String staffFilterId = null;
        if (loggedIn != null && loggedIn.getRole() != null
                && "Staff".equalsIgnoreCase(loggedIn.getRole().getRoleName())) {
            staffFilterId = String.valueOf(loggedIn.getStaffId());
        } else {
            staffFilterId = request.getParameter("staffId");
        }

        int totalRecords = taskDAO.countSearch(search, status, staffFilterId);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        ArrayList<Task> list = taskDAO.search(search, status, staffFilterId, sort, page, pageSize);

        request.setAttribute("list", list);
        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("sort", sort);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("staffId", staffFilterId);

        request.getRequestDispatcher("/WEB-INF/views/task/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
