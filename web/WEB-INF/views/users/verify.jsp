<%-- 
    Document   : verify
    Description: Trang nhập mã OTP (Giao diện chuẩn Smart Hotel)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Xác Thực OTP | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>
        
        <style>
            /* --- COPY STYLE LOGIN --- */
            body {
                background-image: url('${pageContext.request.contextPath}/assets/img/hero/hero-1.jpg');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                overflow: hidden;
                margin: 0;
                font-family: 'Cabin', sans-serif;
            }
            .overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.4); z-index: 0; }
            
            .login-card {
                position: relative; z-index: 1; width: 450px;
                background: #ffffff; border-radius: 15px;
                padding: 40px; box-shadow: 0 15px 25px rgba(0,0,0,0.2);
                text-align: center;
            }
            
            .login-header i { color: #e67e22; margin-bottom: 10px; }
            .login-header h3 { color: #e67e22; font-weight: 700; text-transform: uppercase; margin-bottom: 5px; }
            .text-muted { font-size: 14px; margin-bottom: 20px; display: block; }
            
            .form-control {
                border-radius: 50px; height: 45px; padding-left: 20px; font-size: 16px;
                border: 1px solid #ebebeb; text-align: center; letter-spacing: 2px; font-weight: bold;
            }
            .form-control:focus { border-color: #e67e22; box-shadow: none; }
            
            .btn-login {
                border-radius: 50px; height: 45px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none; color: white; font-weight: 700;
                text-transform: uppercase; margin-top: 20px; width: 100%;
                cursor: pointer; transition: 0.3s;
            }
            .btn-login:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(230, 126, 34, 0.3); }

            .alert-custom { font-size: 13px; padding: 10px; border-radius: 10px; background: #ffe3e3; color: #d63031; margin-bottom: 15px; }
            .alert-success-custom { background: #e3ffe3; color: #00b894; border-left: 4px solid #00b894; }
        </style>
    </head>
    <body>
        <div class="overlay"></div>
        <div class="login-card">
            
            <div class="login-header">
                <i class="fa fa-shield fa-3x"></i>
                <h3>Xác Thực OTP</h3>
                <span class="text-muted">Vui lòng kiểm tra email và nhập mã 6 số</span>
            </div>

            <c:if test="${error != null}">
                <div class="alert-custom"><i class="fa fa-exclamation-circle"></i> ${error}</div>
            </c:if>
            <c:if test="${mess != null}">
                <div class="alert-custom alert-success-custom"><i class="fa fa-check-circle"></i> ${mess}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/verify" method="post">
                <div class="form-group">
                    <input type="text" name="otp" class="form-control" placeholder="_ _ _ _ _ _" maxlength="6" required autofocus autocomplete="off">
                </div>
                <button type="submit" class="btn-login">XÁC NHẬN</button>
            </form>
            
            <div style="margin-top: 20px; font-size: 13px;">
                <a href="${pageContext.request.contextPath}/forgot" style="color: #666;">Gửi lại mã?</a>
            </div>
        </div>
    </body>
</html>