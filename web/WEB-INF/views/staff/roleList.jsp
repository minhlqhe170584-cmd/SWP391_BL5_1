<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Vai trò</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center; }
        .message.success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .message.error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        
        table { border-collapse: collapse; width: 50%; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-top: 15px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #007bff; color: white; }
        
        .btn-add { background-color: #28a745; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; display: inline-block; font-weight: bold; }
        .btn-delete { background-color: #dc3545; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>

    <h2>📋 Quản lý Vai trò (Roles)</h2>

    <% String message = (String) request.getSession().getAttribute("message");
       if (message != null) {
           String css = message.contains("LỖI") ? "error" : "success"; %>
           <div class="message <%= css %>"><%= message %></div>
    <% request.getSession().removeAttribute("message"); } %>

    <a href="staffRoles?action=add" class="btn-add">➕ Thêm Vai trò mới</a>

    <c:choose>
        <c:when test="${empty rolesList}">
            <p><i>Chưa có vai trò nào.</i></p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th style="width: 10%">ID</th>
                        <th style="width: 60%">Tên Vai trò</th>
                        <th style="width: 30%">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="role" items="${rolesList}">
                        <tr>
                            <td>${role.roleId}</td>
                            <td>${role.roleName}</td>
                            <td>
                                <form method="POST" action="staffRoles">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="roleId" value="${role.roleId}"/>
                                    <button type="submit" class="btn-delete" 
                                            onclick="return confirm('Bạn có chắc chắn muốn xóa vai trò [${role.roleName}]?');">
                                        Xóa
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>