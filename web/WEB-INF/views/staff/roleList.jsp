<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Vai trò</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 5px; }
        .message.success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .message.error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        
        table { border-collapse: collapse; width: 50%; margin-top: 20px; } /* Giảm chiều rộng bảng */
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        
        .btn-delete { background-color: #dc3545; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px; }
        .btn-add { background-color: #28a745; color: white; border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px; }
    </style>
</head>
<body>

    <h2>Quản lý Vai trò (Roles)</h2>

    <% 
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            String cssClass = message.toLowerCase().contains("lỗi") ? "error" : "success";
    %>
            <div class="message <%= cssClass %>"><%= message %></div>
    <%
            request.getSession().removeAttribute("message");
        }
    %>

    <div style="border: 1px solid #ddd; padding: 15px; width: 48%; background: #f9f9f9;">
        <strong>Thêm Vai trò mới:</strong>
        <form method="POST" action="staffRoles" style="display: inline-block; margin-left: 10px;">
            <input type="hidden" name="action" value="create"/>
            <input type="text" name="roleName" required placeholder="Nhập tên vai trò..." style="padding: 5px;">
            <button type="submit" class="btn-add">Thêm</button>
        </form>
    </div>

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
                    <td>${role.roleName}</td> <td>
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

</body>
</html>