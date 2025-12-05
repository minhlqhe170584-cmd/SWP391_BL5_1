<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Nhân viên</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 5px; text-align: center;}
        .message.success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .message.error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        
        table { border-collapse: collapse; width: 100%; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #007bff; color: white; }
        
        .staff-inactive { background-color: #ffe6e6 !important; color: #777; }
        
        .btn-add { background-color: #28a745; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; display: inline-block; margin-bottom: 15px; font-weight: bold;}
        .btn-edit { background-color: #ffc107; color: black; padding: 5px 10px; text-decoration: none; border-radius: 3px; font-size: 14px; border: 1px solid #e0a800;}
        .btn-action { border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px; font-size: 14px; color: white;}
        .btn-lock { background-color: #dc3545; }
        .btn-unlock { background-color: #17a2b8; }
    </style>
</head>
<body>

    <h2>📋 Quản lý Nhân viên (Staff List)</h2>

    <% String message = (String) request.getSession().getAttribute("message");
       if (message != null) {
           String css = message.contains("LỖI") ? "error" : "success"; %>
           <div class="message <%= css %>"><%= message %></div>
    <% request.getSession().removeAttribute("message"); } %>

    <a href="staffs?action=add" class="btn-add">➕ Thêm Nhân viên mới</a>
    
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>Vai trò</th>
                <th>Ngày tạo</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="staff" items="${staffsList}">
                <tr class="${!staff.isActive() ? 'staff-inactive' : ''}">
                    <td>${staff.staffId}</td>
                    <td><strong>${staff.fullName}</strong></td>
                    <td>${staff.email}</td>
                    <td>${staff.role.roleName}</td>
                    <td>
                        <c:catch><fmt:formatDate value="${staff.createdAtAsDate}" pattern="dd/MM/yyyy"/></c:catch>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${staff.isActive()}"><span style="color:green; font-weight:bold">Hoạt động</span></c:when>
                            <c:otherwise><span style="color:red; font-weight:bold">Đang khóa</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="staffs?action=edit&staffId=${staff.staffId}" class="btn-edit">Sửa</a>
                        
                        <form method="POST" action="staffs" style="display:inline;">
                            <input type="hidden" name="staffId" value="${staff.staffId}"/>
                            <c:choose>
                                <c:when test="${staff.isActive()}">
                                    <button type="submit" name="action" value="deactivate" class="btn-action btn-lock"
                                            onclick="return confirm('Khóa nhân viên này?');">Khóa</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" name="action" value="activate" class="btn-action btn-unlock"
                                            onclick="return confirm('Mở khóa nhân viên này?');">Mở</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>