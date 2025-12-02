<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <style>
            body { background: #f0f2f5; display: flex; align-items: center; justify-content: center; height: 100vh; }
            .card { width: 400px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        </style>
    </head>
    <body>
        <div class="card">
            <div class="card-header bg-primary text-white text-center">
                <h4>ĐĂNG NHẬP</h4>
            </div>
            <div class="card-body">
                
                <c:if test="${mess != null}">
                    <div class="alert alert-danger">${mess}</div>
                </c:if>

                <form action="login" method="post">
                    <div class="form-group">
                        <label>Tên đăng nhập:</label>
                        <input type="text" name="user" class="form-control" required placeholder="Nhập user">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu:</label>
                        <input type="password" name="pass" class="form-control" required placeholder="Nhập pass">
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Đăng Nhập</button>
                </form>
                
            </div>
            <div class="card-footer text-center">
                Chưa có tài khoản? <a href="register">Đăng ký ngay</a>
            </div>
        </div>
    </body>
</html>