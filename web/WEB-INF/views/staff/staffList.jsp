<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Nhân viên</title>
    <style>
        .message.success { color: green; font-weight: bold; }
        .message.error { color: red; font-weight: bold; }
        .form-container { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; }
        .staff-inactive { background-color: #fdd; }
    </style>
</head>
<body>

    <h2>Quản lý Nhân viên (Staffs)</h2>
    <hr>

    <% 
        String message = (String) request.getSession().getAttribute("message");
        if (message != null) {
            String cssClass = message.toLowerCase().contains("lỗi") ? "error" : "success";
    %>
            <p class="message <%= cssClass %>"><%= message %></p>
    <%
            request.getSession().removeAttribute("message");
        }
    %>

    <div class="form-container">
        <h3>➕ Thêm Nhân viên mới</h3>
        <form method="POST" action="staffs">
            <input type="hidden" name="action" value="create"/>
            
            Tên đầy đủ: <input type="text" name="fullName" required><br>
            Email: <input type="email" name="email" required><br>
            Mật khẩu: <input type="password" name="password" required><br>
            
            Vai trò: 
            <select name="roleId" required>
                <c:forEach var="role" items="${rolesList}">
                    <option value="${role.roleId}">${role.roleName}</option>
                </c:forEach>
            </select><br>
            
            <button type="submit" style="margin-top: 10px;">Thêm Nhân viên</button>
        </form>
    </div>
    
    <hr>
    
    <h3>Danh sách Nhân viên hiện tại</h3>
    
    <c:choose>
        <c:when test="${empty staffsList}">
            <p>Không có nhân viên nào được tìm thấy.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="5" width="100%">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ và Tên</th>
                        <th>Email</th>
                        <th>Vai trò</th>
                        <th>Ngày tạo</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="staff" items="${staffsList}">
                        <tr class="<c:if test="${!staff.isActive()}">staff-inactive</c:if>">
                            <td><c:out value="${staff.staffId}"/></td>
                            
                            <form method="POST" action="staffs">
                                <input type="hidden" name="action" value="update"/>
                                <input type="hidden" name="staffId" value="${staff.staffId}"/>
                                
                                <td><input type="text" name="fullName" value="${staff.fullName}" required/></td>
                                <td><input type="email" name="email" value="${staff.email}" required/></td>
                                
                                <td>
                                    <select name="roleId" required>
                                        <c:forEach var="role" items="${rolesList}">
                                            <option value="${role.roleId}" 
                                                <c:if test="${role.roleId == staff.role.roleId}">selected</c:if>>
                                                ${role.roleName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                
                                <td><fmt:formatDate value="${staff.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${staff.isActive()}"><span style="color: green;">Hoạt động</span></c:when>
                                        <c:otherwise><span style="color: red;">Đã vô hiệu hóa</span></c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <input type="password" name="password" placeholder="Mật khẩu mới (Bỏ qua nếu không đổi)">
                                    <button type="submit" style="margin-top: 5px;">Sửa</button>
                                    
                                    <c:if test="${staff.isActive()}">
                                        <button type="submit" name="action" value="deactivate" 
                                                onclick="return confirm('Bạn có chắc chắn muốn VÔ HIỆU HÓA nhân viên ${staff.fullName}?');">Vô hiệu hóa</button>
                                    </c:if>
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