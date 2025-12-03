<%-- 
    Document   : register
    Description: Trang đăng ký Compact (Đã sửa link quay về Login)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Ký - Smart Hotel System</title>
        
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        
        <style>
            body {
                background-image: url('https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                overflow: hidden;
            }
            .overlay {
                position: absolute;
                top: 0; left: 0; width: 100%; height: 100%;
                background: rgba(0, 0, 0, 0.5);
                z-index: 0;
            }
            .login-card {
                position: relative;
                z-index: 1;
                width: 700px; 
                background: rgba(255, 255, 255, 0.95);
                border-radius: 15px;
                box-shadow: 0 15px 25px rgba(0,0,0,0.5);
                padding: 25px 40px; 
            }
            .login-header {
                text-align: center;
                margin-bottom: 15px;
            }
            .login-header h3 {
                color: #e67e22;
                font-weight: 700;
                text-transform: uppercase;
                margin-top: 5px;
                font-size: 24px;
            }
            .form-control {
                border-radius: 20px;
                height: 38px;
                padding-left: 15px;
                font-size: 13px;
            }
            .form-control:focus {
                box-shadow: none;
                border-color: #e67e22;
            }
            .input-group-text {
                background: transparent;
                border: none;
                border-bottom: 1px solid #ced4da;
                color: #e67e22;
                font-size: 13px;
                padding: 5px 10px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .btn-login {
                border-radius: 20px;
                height: 40px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none;
                color: white;
                font-weight: bold;
                text-transform: uppercase;
                margin-top: 10px;
                transition: 0.3s;
                font-size: 14px;
            }
            .btn-login:hover {
                background: linear-gradient(to right, #d35400, #e67e22);
                box-shadow: 0 5px 15px rgba(230, 126, 34, 0.4);
                color: white;
            }
            .extra-links {
                text-align: center;
                margin-top: 15px;
                font-size: 13px;
            }
            .extra-links a {
                color: #d35400;
                text-decoration: none;
                font-weight: 600;
            }
            .alert-custom {
                font-size: 12px;
                padding: 8px;
                border-radius: 10px;
                text-align: center;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>

        <div class="overlay"></div>

        <div class="login-card">
            <div class="login-header">
                <i class="fas fa-user-plus fa-2x" style="color: #e67e22;"></i>
                <h3>Đăng Ký</h3>
            </div>

            <c:if test="${mess != null}">
                <div class="alert alert-danger alert-custom">
                    <i class="fas fa-exclamation-triangle"></i> ${mess}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                        </div>
                        <input type="text" name="fullname" class="form-control" placeholder="Họ và tên đầy đủ" required value="${fullname}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                </div>
                                <input type="email" name="email" class="form-control" placeholder="Email" required value="${email}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                </div>
                                <input type="text" name="phone" class="form-control" placeholder="Số điện thoại" required value="${phone}">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="form-row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                </div>
                                <input type="password" name="pass" class="form-control" placeholder="Mật khẩu" required>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-check-circle"></i></span>
                                </div>
                                <input type="password" name="repass" class="form-control" placeholder="Xác nhận mật khẩu" required>
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-login btn-block">
                    ĐĂNG KÝ NGAY
                </button>
            </form>

            <div class="extra-links">
                <span class="text-muted">Đã có tài khoản?</span> 
                <a href="${pageContext.request.contextPath}/login" class="ml-1">Đăng nhập ngay</a>
                
                <span class="mx-2">|</span>
                <a href="home" style="color: #6c757d;"><i class="fas fa-home"></i> Trang chủ</a>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>