<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Nhân viên</title>
        <style>
            body {
                font-family: sans-serif;
                margin: 20px;
            }

            /* CSS cho thông báo */
            .message {
                padding: 10px;
                margin-bottom: 15px;
                border-radius: 5px;
            }
            .message.success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .message.error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            /* CSS cho bảng và form */
            .form-container {
                border: 1px solid #ddd;
                padding: 20px;
                background-color: #f9f9f9;
                border-radius: 5px;
                margin-bottom: 30px;
            }
            .form-group {
                margin-bottom: 10px;
            }
            .form-group label {
                display: inline-block;
                width: 100px;
                font-weight: bold;
            }
            .form-group input, .form-group select {
                padding: 5px;
                width: 250px;
            }

            table {
                border-collapse: collapse;
                width: 100%;
                border: 1px solid #ddd;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }

            /* Tô màu nền đỏ nhạt cho nhân viên bị khóa */
            .staff-inactive {
                background-color: #ffe6e6 !important;
            }

            /* CSS cho các nút bấm */
            .btn-add {
                background-color: #28a745;
                color: white;
                border: none;
                padding: 8px 15px;
                cursor: pointer;
                border-radius: 3px;
            }
            .btn-update {
                background-color: #007bff;
                color: white;
                border: none;
                padding: 5px 10px;
                cursor: pointer;
                border-radius: 3px;
            }
            .btn-deactivate {
                background-color: #dc3545;
                color: white;
                border: none;
                padding: 5px 10px;
                cursor: pointer;
                border-radius: 3px;
            }
        </style>
    </head>
    <body>

        <h2>Quản lý Nhân viên (Staffs)</h2>

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

        <div class="form-container">
            <h3>➕ Thêm Nhân viên mới</h3>
            <form method="POST" action="staffs">
                <input type="hidden" name="action" value="create"/>

                <div class="form-group">
                    <label>Họ tên:</label>
                    <input type="text" name="fullName" required placeholder="Nhập họ và tên">
                </div>

                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" required placeholder="example@email.com">
                </div>

                <div class="form-group">
                    <label>Mật khẩu:</label>
                    <input type="password" name="password" required placeholder="Nhập mật khẩu">
                </div>

                <div class="form-group">
                    <label>Vai trò:</label>
                    <select name="roleId" required>
                        <c:forEach var="role" items="${rolesList}">
                            <option value="${role.roleId}">${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="btn-add">Thêm Nhân viên</button>
            </form>
        </div>

        <hr>

        <h3>Danh sách Nhân viên hiện tại</h3>

        <c:choose>
            <c:when test="${empty staffsList}">
                <p><i>Chưa có dữ liệu nhân viên nào.</i></p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th style="width: 5%;">ID</th>
                            <th style="width: 20%;">Họ và Tên</th>
                            <th style="width: 20%;">Email</th>
                            <th style="width: 15%;">Vai trò</th>
                            <th style="width: 15%;">Ngày tạo</th>
                            <th style="width: 10%;">Trạng thái</th>
                            <th style="width: 15%;">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="staff" items="${staffsList}">
                            <tr class="<c:if test="${!staff.isActive()}">staff-inactive</c:if>">

                            <form method="POST" action="staffs">
                                <input type="hidden" name="staffId" value="${staff.staffId}"/>

                            <td>${staff.staffId}</td>

                            <td>
                                <input type="text" name="fullName" value="${staff.fullName}" required style="width: 90%;"/>
                            </td>

                            <td>
                                <input type="email" name="email" value="${staff.email}" required style="width: 90%;"/>
                            </td>

                            <td>
                                <select name="roleId" style="width: 100%;">
                                    <c:forEach var="role" items="${rolesList}">
                                        <option value="${role.roleId}" 
                                                <c:if test="${role.roleId == staff.role.roleId}">selected</c:if>>
                                            ${role.roleName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>

                            <td>
                                <c:catch var="dateError">
                                    <fmt:formatDate value="${staff.createdAtAsDate}" pattern="yyyy-MM-dd HH:mm"/>
                                </c:catch>
                                <c:if test="${not empty dateError}">
                                    ${staff.createdAt}
                                </c:if>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${staff.isActive()}">
                                        <span style="color: green; font-weight: bold;">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: red; font-weight: bold;">Đang khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <input type="password" name="password" placeholder="Mật khẩu mới" style="width: 100%; margin-bottom: 5px; font-size: 11px;">

                                <div style="display: flex; gap: 5px;">
                                    <button type="submit" name="action" value="update" class="btn-update">Sửa</button>

                                    <c:choose>
                                        <c:when test="${staff.isActive()}">
                                            <button type="submit" name="action" value="deactivate" class="btn-deactivate"
                                                    onclick="return confirm('Bạn có chắc chắn muốn KHÓA nhân viên này?');">
                                                Khóa
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" name="action" value="activate" class="btn-add" 
                                                    style="background-color: #17a2b8;" onclick="return confirm('Bạn có muốn MỞ KHÓA lại nhân viên này?');">
                                                Mở khóa
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
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
