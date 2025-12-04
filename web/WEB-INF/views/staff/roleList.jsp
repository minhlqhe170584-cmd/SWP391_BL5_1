<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Vai Trò Nhân sự</title>
    <style>
        .message.success { color: green; font-weight: bold; }
        .message.error { color: red; font-weight: bold; }
        .form-inline input { margin-right: 5px; }
    </style>
</head>
<body>

    <h2>Quản lý Vai trò (Staff Roles)</h2>
    <hr>

    <% 
        // Lấy thông báo từ Session Scope và hiển thị
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            String cssClass = message.toLowerCase().contains("lỗi") ? "error" : "success";
    %>
            <p class="message <%= cssClass %>"><%= message %></p>
    <%
            // Xóa thông báo khỏi Session sau khi hiển thị để tránh lặp lại
            request.getSession().removeAttribute("message");
        }
    %>

    <h3>📝 Thêm Vai trò mới</h3>
    <form method="POST" action="staffRoles" class="form-inline">
        <input type="hidden" name="action" value="create"/>
        <input type="text" name="roleName" required placeholder="Tên Vai trò (ví dụ: Lễ tân)" style="width: 250px;"/>
        <button type="submit">Thêm Vai trò</button>
    </form>
    
    <hr>
    
    <h3>Danh sách Vai trò hiện tại</h3>
    
    <c:choose>
        <c:when test="${empty rolesList}">
            <p>Không có vai trò nào được tìm thấy.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="5">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên Vai trò</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="role" items="${rolesList}">
                        <tr>
                            <td><c:out value="${role.roleId}"/></td>
                            
                            <form method="POST" action="staffRoles" onsubmit="return validateRoleForm(this);">
                                <td>
                                    <input type="text" name="roleName" value="<c:out value="${role.roleName}"/>" required/>
                                </td>
                                <td>
                                    <input type="hidden" name="roleId" value="<c:out value="${role.roleId}"/>"/>
                                    
                                    <button type="submit" name="action" value="update">Cập nhật</button>
                                    
                                    <button type="submit" name="action" value="delete" 
                                            onclick="return confirm('Cảnh báo: Xóa vai trò sẽ thất bại nếu có nhân viên đang sử dụng. Bạn có chắc chắn muốn XÓA ID ${role.roleId}?');">Xóa</button>
                                </td>
                            </form>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

</body>
</html>