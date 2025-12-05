<%-- 
    Document   : login
    Project    : Smart Hotel Management (SWP391)
    Description: Màn hình đăng nhập (Đã bỏ "Ghi nhớ tôi")
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Nhập - Smart Hotel System</title>
        
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        
        <style>
            body {
                background-image: url('https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-1.2.1&auto=format&fit=crop&w=1920&q=80');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                
                /* Căn giữa màn hình */
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                
                margin: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
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
                width: 400px;
                background: rgba(255, 255, 255, 0.95);
                border-radius: 15px;
                box-shadow: 0 15px 25px rgba(0,0,0,0.5);
                padding: 40px 30px;
            }

            .login-header {
                text-align: center;
                margin-bottom: 30px;
            }
            .login-header h3 {
                color: #e67e22;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 1px;
                margin-top: 10px;
            }

            .form-control {
                border-radius: 25px;
                height: 45px;
                padding-left: 20px;
                font-size: 14px;
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
            }

            .btn-login {
                border-radius: 25px;
                height: 45px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none;
                color: white;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 1px;
                margin-top: 20px;
                transition: 0.3s;
            }
            .btn-login:hover {
                background: linear-gradient(to right, #d35400, #e67e22);
                box-shadow: 0 5px 15px rgba(230, 126, 34, 0.4);
                color: white;
            }

            .extra-links {
                text-align: center;
                margin-top: 25px;
                font-size: 14px;
            }
            .extra-links a {
                color: #d35400;
                text-decoration: none;
                font-weight: 600;
            }
            .extra-links a:hover {
                text-decoration: underline;
            }
            
            .alert-custom {
                font-size: 13px;
                border-radius: 10px;
                text-align: center;
            }
        </style>
    </head>
    <body>

        <div class="overlay"></div>

        <div class="login-card">
            
            <div class="login-header">
                <i class="fas fa-hotel fa-3x" style="color: #e67e22;"></i>
                <h3>Smart Hotel</h3>
                <p class="text-muted">Đăng nhập hệ thống</p>
            </div>

            <c:if test="${mess != null}">
                <div class="alert alert-danger alert-custom">
                    <i class="fas fa-exclamation-triangle"></i> ${mess}
                </div>
            </c:if>

            <form action="login" method="post">
                
                <div class="form-group">
                    <label class="text-muted small">Tên đăng nhập / Số phòng</label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                        </div>
                        <input type="text" name="user" class="form-control" placeholder="Nhập username..." required autofocus>
                    </div>
                </div>

                <div class="form-group">
                    <label class="text-muted small">Mật khẩu</label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                        </div>
                        <input type="password" name="pass" class="form-control" placeholder="Nhập password..." required>
                    </div>
                </div>

                <div class="d-flex justify-content-end align-items-center small">
                    <a href="#" class="text-muted">Quên mật khẩu?</a>
                </div>

                <button type="submit" class="btn btn-login btn-block">
                    Đăng Nhập <i class="fas fa-sign-in-alt ml-2"></i>
                </button>
            </form>

            <div class="extra-links">
                <p>Chưa có tài khoản? <a href="register">Đăng ký ngay</a></p>

                <p><a href="HomeServlet" style="color: #6c757d;"><i class="fas fa-home"></i> Quay về trang chủ</a></p>
            </div>

        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>