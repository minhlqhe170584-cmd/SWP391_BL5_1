<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty staff ? 'Thêm Nhân viên mới' : 'Cập nhật Nhân viên'}</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .form-container { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 500px; margin: 0 auto; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn-submit { background-color: #28a745; color: white; border: none; padding: 10px 15px; cursor: pointer; border-radius: 3px; font-size: 16px; }
        .btn-cancel { background-color: #6c757d; color: white; text-decoration: none; padding: 10px 15px; border-radius: 3px; margin-left: 10px; font-size: 16px; }
        h2 { text-align: center; color: #333; }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>${empty staff ? '➕ Thêm Nhân viên' : '✏️ Cập nhật Nhân viên'}</h2>
        
        <form method="POST" action="staffs">
            <input type="hidden" name="action" value="${empty staff ? 'create' : 'update'}"/>
            
            <c:if test="${not empty staff}">
                <input type="hidden" name="staffId" value="${staff.staffId}"/>
            </c:if>
            
            <div class="form-group">
                <label>Họ và Tên:</label>
                <input type="text" name="fullName" value="${staff.fullName}" required placeholder="Nhập họ tên đầy đủ">
            </div>
            
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="${staff.email}" required placeholder="example@email.com">
            </div>
            
            <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" ${empty staff ? 'required' : ''} placeholder="${empty staff ? 'Nhập mật khẩu' : 'Nhập mật khẩu mới (Bỏ qua nếu không đổi)'}">
            </div>
            
            <div class="form-group">
                <label>Vai trò:</label>
                <select name="roleId" required>
                    <c:forEach var="role" items="${rolesList}">
                        <option value="${role.roleId}" <c:if test="${not empty staff && role.roleId == staff.role.roleId}">selected</c:if>>
                            ${role.roleName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div style="text-align: center; margin-top: 20px;">
                <button type="submit" class="btn-submit">Lưu lại</button>
                <a href="staffs" class="btn-cancel">Hủy bỏ</a>
            </div>
        </form>
    </div>

</body>
</html>