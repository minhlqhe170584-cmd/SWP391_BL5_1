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
    
   
}
