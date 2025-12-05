<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Vai trò mới</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .form-container { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 400px; margin: 50px auto; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn-submit { background-color: #28a745; color: white; border: none; padding: 10px 15px; cursor: pointer; border-radius: 3px; font-size: 16px; }
        .btn-cancel { background-color: #6c757d; color: white; text-decoration: none; padding: 10px 15px; border-radius: 3px; margin-left: 10px; font-size: 16px; }
        h2 { text-align: center; color: #333; margin-top: 0; }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>➕ Thêm Vai trò mới</h2>
        
        <form method="POST" action="staffRoles">
            <input type="hidden" name="action" value="create"/>
            
            <div class="form-group">
                <label>Tên Vai trò:</label>
                <input type="text" name="roleName" required placeholder="Ví dụ: Bảo vệ, Kế toán...">
            </div>
            
            <div style="text-align: center; margin-top: 20px;">
                <button type="submit" class="btn-submit">Lưu lại</button>
                <a href="staffRoles" class="btn-cancel">Hủy bỏ</a>
            </div>
        </form>
    </div>

</body>
</html>