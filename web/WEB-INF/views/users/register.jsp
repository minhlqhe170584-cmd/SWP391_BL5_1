<%-- 
    Document   : register
    Project    : Smart Hotel Management
    Description: Trang Đăng ký (Style Bo Tròn - Rounded)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Đăng Ký - Smart Hotel System</title>
        <jsp:include page="../components/head.jsp"></jsp:include>
        
        <style>
            /* --- CSS TÙY CHỈNH --- */
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
                font-family: 'Cabin', sans-serif; /* Font chuẩn Sona */
            }
            .overlay {
                position: absolute; top: 0; left: 0; width: 100%; height: 100%;
                background: rgba(0, 0, 0, 0.4); z-index: 0;
            }
            
            .login-card {
                position: relative; z-index: 1;
                width: 700px; /* Rộng để chia 2 cột */
                background: #ffffff;
                /* Bo tròn khung thẻ */
                border-radius: 15px; 
                box-shadow: 0 15px 25px rgba(0,0,0,0.2);
                padding: 30px 50px; 
            }
            
            .login-header { text-align: center; margin-bottom: 25px; }
            .login-header h3 { 
                color: #e67e22; font-weight: 700; text-transform: uppercase; 
                margin-top: 5px; font-size: 28px; letter-spacing: 1px;
            }
            
            /* --- STYLE BO TRÒN INPUT --- */
            .form-control {
                height: 45px;
                border: 1px solid #ebebeb;
                /* Bo tròn hình viên thuốc */
                border-radius: 50px; 
                padding-left: 20px;
                font-size: 14px;
                color: #19191a;
            }
            .form-control:focus { box-shadow: none; border-color: #e67e22; }
            
            /* Icon input */
            .input-group-text { 
                background: transparent; border: none; 
                border-bottom: 1px solid #ced4da; 
                color: #e67e22; padding: 0 15px; font-size: 14px;
            }
            .form-group { margin-bottom: 20px; }

            /* --- STYLE BO TRÒN BUTTON --- */
            .btn-login {
                border-radius: 50px; /* Bo tròn nút */
                height: 50px;
                background: linear-gradient(to right, #e67e22, #d35400);
                border: none; color: white; font-weight: 700;
                text-transform: uppercase; margin-top: 15px; 
                font-size: 14px; letter-spacing: 2px; width: 100%;
                cursor: pointer; transition: all 0.3s;
                box-shadow: 0 5px 15px rgba(230, 126, 34, 0.3);
            }
            .btn-login:hover { 
                background: linear-gradient(to right, #d35400, #e67e22); 
                transform: translateY(-2px);
            }

            .extra-links { text-align: center; margin-top: 20px; font-size: 14px; }
            .extra-links a { color: #e67e22; text-decoration: none; font-weight: 600; }
            .extra-links a:hover { text-decoration: underline; color: #d35400; }
            
            .alert-custom { 
                font-size: 13px; padding: 10px; border-radius: 10px; 
                background: #ffe3e3; color: #d63031; border-left: 4px solid #d63031;
                text-align: center; margin-bottom: 20px; 
            }
        </style>
    </head>
    <body>

        <div class="overlay"></div>

        <div class="login-card">
            
            <div class="login-header">
                <i class="fa fa-user-plus fa-3x" style="color: #e67e22;"></i>
                <h3>Đăng Ký</h3>
            </div>

            <c:if test="${mess != null}">
                <div class="alert-custom">
                    <i class="fa fa-exclamation-circle"></i> ${mess}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fa fa-user"></i></span>
                        </div>
                        <input type="text" name="fullname" class="form-control" placeholder="Họ và tên đầy đủ" required value="${fullname}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa fa-envelope"></i></span>
                                </div>
                                <input type="email" name="email" class="form-control" placeholder="Email" required value="${email}">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa fa-phone"></i></span>
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
                                    <span class="input-group-text"><i class="fa fa-lock"></i></span>
                                </div>
                                <input type="password" name="pass" class="form-control" placeholder="Mật khẩu" required maxlength="20">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fa fa-check-circle"></i></span>
                                </div>
                                <input type="password" name="repass" class="form-control" placeholder="Nhập lại mật khẩu" required maxlength="20">
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn-login">
                    ĐĂNG KÝ NGAY
                </button>
            </form>

            <div class="extra-links">
                <span class="text-muted">Đã có tài khoản?</span> 
                <a href="${pageContext.request.contextPath}/login" class="ml-1">Đăng nhập ngay</a>
                <span class="mx-2">|</span>
                <a href="${pageContext.request.contextPath}/home" style="color: #6c757d;"><i class="fa fa-home"></i> Trang chủ</a>
            </div>
        </div>
        
        <script src="${pageContext.request.contextPath}/assets/js/jquery-3.3.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
    </body>
</html>