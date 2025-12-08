<%-- 
    Document   : login
    Description: Trang Đăng Nhập (Style Sona - Bo tròn mềm mại)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">
<head>
    <title>Đăng Nhập | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    
    <style>
        body {
            /* Ảnh nền Sona */
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
            width: 420px;
            background: #ffffff;
            /* Bo tròn khung thẻ */
            border-radius: 15px; 
            padding: 40px;
            box-shadow: 0 15px 25px rgba(0,0,0,0.2);
        }

        .login-header { text-align: center; margin-bottom: 30px; }
        .login-header h2 {
            color: #e67e22; font-weight: 700; text-transform: uppercase;
            font-size: 28px; margin-bottom: 10px;
        }
        .login-header p { color: #666; font-size: 14px; }

        .input-group { margin-bottom: 20px; position: relative; }
        
        /* --- CHỈNH SỬA: BO TRÒN Ô INPUT --- */
        .form-control {
            height: 50px;
            border: 1px solid #ebebeb;
            /* Bo tròn 50px để tạo hình viên thuốc */
            border-radius: 50px; 
            padding-left: 20px;
            font-size: 14px;
            color: #19191a;
        }
        .form-control:focus { border-color: #e67e22; box-shadow: none; }

        /* --- CHỈNH SỬA: BO TRÒN NÚT BẤM --- */
        .btn-login {
            display: inline-block;
            font-size: 13px; font-weight: 700; text-transform: uppercase;
            color: #ffffff;
            background: #e67e22;
            border: none;
            padding: 14px 20px;
            letter-spacing: 2px;
            width: 100%;
            /* Bo tròn nút */
            border-radius: 50px; 
            cursor: pointer;
            transition: all 0.3s;
            box-shadow: 0 5px 15px rgba(230, 126, 34, 0.3); /* Thêm bóng cho đẹp */
        }
        .btn-login:hover {
            background: #2c3e50;
            transform: translateY(-2px); /* Hiệu ứng nhấn */
        }

        .extra-links { text-align: center; margin-top: 25px; font-size: 14px; }
        .extra-links a { color: #e67e22; font-weight: 600; transition: 0.3s; }
        .extra-links a:hover { color: #dfa974; text-decoration: underline; }
        
        .alert-custom {
            font-size: 13px; padding: 10px; border-radius: 10px;
            background: #ffe3e3; color: #d63031;
            border-left: 4px solid #d63031; margin-bottom: 20px;
        }
    </style>
</head>
<body>

    <div class="overlay"></div>

    <div class="login-card">
        <div class="login-header">
            <h2>Đăng Nhập</h2>
            <p>Chào mừng bạn đến với Smart Hotel</p>
        </div>

        <c:if test="${mess != null}">
            <div class="alert-custom">
                <i class="fa fa-exclamation-circle"></i> ${mess}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="input-group">
                <input type="text" name="user" class="form-control" placeholder="Email hoặc Số phòng" required autofocus value="${user}">
            </div>
            
            <div class="input-group">
                <input type="password" name="pass" class="form-control" placeholder="Mật khẩu" required>
            </div>
            
            <div class="input-group d-flex justify-content-end">
                <a href="${pageContext.request.contextPath}/forgot" style="font-size: 13px; color: #666;">Quên mật khẩu?</a>
            </div>

            <button type="submit" class="btn-login">Đăng Nhập Ngay</button>
        </form>

        <div class="extra-links">
            <span>Chưa có tài khoản?</span>
            <a href="${pageContext.request.contextPath}/register">Đăng ký tại đây</a>
            <br><br>
            <a href="${pageContext.request.contextPath}/home"><i class="fa fa-long-arrow-left"></i> Quay về Trang chủ</a>
        </div>
    </div>

</body>
</html>