<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang chủ SWP391</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="Home.jsp">SWP391 PROJECT</a>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ml-auto">
                    
                    <c:if test="${sessionScope.acc == null}">
                        <li class="nav-item">
                            <a class="nav-link btn btn-primary text-white" href="login">Đăng Nhập</a>
                        </li>
                        <li class="nav-item ml-2">
                             <a class="nav-link" href="register">Đăng Ký</a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.acc != null}">
                        <li class="nav-item">
                            <span class="nav-link text-white">Xin chào, <b>${sessionScope.acc.user}</b></span>
                        </li>
                        
                        <c:if test="${sessionScope.acc.isAdmin == 1}">
                            <li class="nav-item">
                                <a class="nav-link text-warning" href="#">[Trang Admin]</a>
                            </li>
                        </c:if>

                        <li class="nav-item ml-3">
                            <a class="nav-link btn btn-danger text-white" href="logout">Đăng Xuất</a>
                        </li>
                    </c:if>
                    
                </ul>
            </div>
        </nav>

        <div class="container mt-5 text-center">
            <div class="jumbotron">
                <h1 class="display-4">Chào mừng đến với Website!</h1>
                <p class="lead">Đây là trang chủ đơn giản để test chức năng Login/Logout.</p>
                <hr class="my-4">
                
                <c:if test="${sessionScope.acc == null}">
                    <p>Bạn đang xem với tư cách là <b>Khách</b>.</p>
                    <a class="btn btn-primary btn-lg" href="login" role="button">Đăng nhập ngay</a>
                </c:if>

                <c:if test="${sessionScope.acc != null}">
                    <p>Bạn đang đăng nhập với quyền: 
                        <b>${sessionScope.acc.isAdmin == 1 ? "QUẢN TRỊ VIÊN (ADMIN)" : "KHÁCH HÀNG (USER)"}</b>
                    </p>
                </c:if>
            </div>
        </div>

    </body>
</html>