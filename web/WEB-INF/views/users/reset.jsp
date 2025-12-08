<%-- 
    Document   : reset
    Description: Trang Đặt Lại Mật Khẩu Mới
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Đặt Lại Mật Khẩu | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>
        
        <style>
            /* --- STYLE ĐỒNG BỘ --- */
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
            }
            
            .login-header { text-align: center; margin-bottom: 20px; }
            .login-header i { color: #e67e22; margin-bottom: 10px; }
            .login-header h3 { color: #e67e22; font-weight: 700; text-transform: uppercase; }
            
            .form-control {
                border-radius: 50px; height: 45px; padding-left: 20px; font-size: 14px;
                border: 1px solid #ebebeb; color: #19191a;
            }
            .form-control:focus { border-color: #e67e22; box-shadow: none; }
            .input-group-text { background: transparent; border: none; border-bottom: 1px solid #ced4da; color: #e67e22; }
            .form-group { margin-bottom: 20px; }
            
            .btn-login {
                border-radius: 50px; height: 50px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none; color: white; font-weight: 700;
                text-transform: uppercase; margin-top: 10px; width: 100%;
                cursor: pointer; transition: 0.3s;
            }
            .btn-login:hover { background: #2c3e50; transform: translateY(-2px); }

            .alert-custom { font-size: 13px; padding: 10px; border-radius: 10px; background: #ffe3e3; color: #d63031; margin-bottom: 20px; text-align: center;}
        </style>
    </head>
    <body>
        <div class="overlay"></div>
        <div class="login-card">
            
            <div class="login-header">
                <i class="fa fa-key fa-3x"></i>
                <h3>Mật Khẩu Mới</h3>
            </div>

            <c:if test="${error != null}">
                <div class="alert-custom"><i class="fa fa-exclamation-triangle"></i> ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset" method="post">
                
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fa fa-lock"></i></span>
                        </div>
                        <input type="password" name="pass" class="form-control" placeholder="Mật khẩu mới" required>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fa fa-check-circle"></i></span>
                        </div>
                        <input type="password" name="repass" class="form-control" placeholder="Nhập lại mật khẩu" required>
                    </div>
                </div>

                <button type="submit" class="btn-login">ĐỔI MẬT KHẨU</button>
            </form>
        </div>
    </body>
</html>