<%-- 
    Document   : forgot
    Description: Trang Quên Mật Khẩu (Style Smart Hotel)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Quên Mật Khẩu | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>
        
        <style>
            /* --- COPY STYLE TỪ LOGIN.JSP --- */
            body {
                background-image: url('${pageContext.request.contextPath}/assets/img/hero/hero-1.jpg');
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: 'Cabin', sans-serif;
                overflow: hidden;
            }
            .overlay {
                position: absolute; top: 0; left: 0; width: 100%; height: 100%;
                background: rgba(0, 0, 0, 0.4); z-index: 0;
            }
            .login-card {
                position: relative; z-index: 1;
                width: 450px; /* Rộng hơn xíu cho thoáng */
                background: #ffffff;
                border-radius: 15px; 
                padding: 40px;
                box-shadow: 0 15px 25px rgba(0,0,0,0.2);
            }
            .login-header { text-align: center; margin-bottom: 25px; }
            .login-header h3 { 
                color: #e67e22; font-weight: 700; text-transform: uppercase;
                margin-top: 10px; font-size: 24px; letter-spacing: 1px;
            }
            .form-control {
                border-radius: 50px; height: 45px; padding-left: 20px; font-size: 14px;
                border: 1px solid #ebebeb; color: #19191a;
            }
            .form-control:focus { border-color: #e67e22; box-shadow: none; }
            .input-group-text { background: transparent; border: none; border-bottom: 1px solid #ced4da; color: #e67e22; padding: 0 15px; }
            .form-group { margin-bottom: 20px; }
            
            .btn-login {
                border-radius: 50px; height: 50px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none; color: white; font-weight: 700;
                text-transform: uppercase; margin-top: 10px; 
                font-size: 14px; letter-spacing: 2px; width: 100%;
                cursor: pointer; transition: all 0.3s;
                box-shadow: 0 5px 15px rgba(230, 126, 34, 0.3);
            }
            .btn-login:hover { background: #2c3e50; transform: translateY(-2px); }

            .extra-links { text-align: center; margin-top: 25px; font-size: 14px; }
            .extra-links a { color: #e67e22; text-decoration: none; font-weight: 600; }
            .alert-custom { 
                font-size: 13px; padding: 10px; border-radius: 10px; 
                background: #ffe3e3; color: #d63031; border-left: 4px solid #d63031;
                text-align: center; margin-bottom: 20px; 
            }
            .alert-success-custom {
                background: #e3ffe3; color: #00b894; border-left: 4px solid #00b894;
            }
        </style>
    </head>
    <body>

        <div class="overlay"></div>

        <div class="login-card">
            
            <div class="login-header">
                <i class="fa fa-lock fa-3x" style="color: #e67e22;"></i>
                <h3>Khôi Phục Mật Khẩu</h3>
                <p class="text-muted small">Nhập email để nhận mã xác nhận</p>
            </div>

            <c:if test="${error != null}">
                <div class="alert-custom">
                    <i class="fa fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            <c:if test="${message != null}">
                <div class="alert-custom alert-success-custom">
                    <i class="fa fa-check-circle"></i> ${message}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/forgot" method="post">
                
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fa fa-envelope"></i></span>
                        </div>
                        <input type="email" name="email" class="form-control" placeholder="Nhập địa chỉ Email của bạn" required>
                    </div>
                </div>

                <button type="submit" class="btn-login">
                    GỬI MÃ XÁC NHẬN
                </button>
            </form>

            <div class="extra-links">
                <a href="${pageContext.request.contextPath}/login">
                    <i class="fa fa-long-arrow-left"></i> Quay lại Đăng nhập
                </a>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/assets/js/jquery-3.3.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    </body>
</html>