<%-- 
    Document   : head
    Description: Chứa Meta, Link CSS và Style Custom (Màu cam, Menu Active, Service Image)
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<meta charset="UTF-8">
<meta name="description" content="Smart Hotel System">
<meta name="keywords" content="Sona, unica, creative, html">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">

<link href="https://fonts.googleapis.com/css?family=Lora:400,700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Cabin:400,500,600,700&display=swap" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/elegant-icons.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/flaticon.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/owl.carousel.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/nice-select.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/magnific-popup.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/slicknav.min.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" type="text/css">

<style>
    /* --- 1. MÀU CHỦ ĐẠO CAM (#e67e22) --- */
    .primary-btn { background: #e67e22; }
    .check-date input { color: #e67e22; }
    .check-date i { color: #e67e22; }
    .section-title span { color: #e67e22; }
    
    .room-item .ri-text .primary-btn { color: #2c3e50; }
    .room-item .ri-text .primary-btn:hover { color: #e67e22; }
    
    .footer-section .ft-newslatter button { background: #e67e22; }
    
    .header-section .nav-menu .mainmenu ul li .dropdown {
        border-top: 2px solid #e67e22;
    }
    
    /* Fix lỗi menu bị đè lên nội dung */
    .header-section {
        background: #fff;
        box-shadow: 0 5px 10px rgba(0,0,0,0.05);
    }

    /* --- 2. MENU ACTIVE (Tô cam + Gạch chân) --- */
    .nav-menu .mainmenu ul li a {
        position: relative; /* Để gạch chân bám theo */
        transition: 0.3s;
    }
    
    .nav-menu .mainmenu ul li a:hover,
    .nav-menu .mainmenu ul li.active a {
        color: #e67e22 !important;
    }

    /* Vẽ đường gạch chân */
    .nav-menu .mainmenu ul li.active a::after {
        content: "";
        position: absolute;
        left: 0;
        bottom: -3px;
        width: 100%;
        height: 3px;
        background-color: #e67e22;
        border-radius: 2px;
    }

    /* --- 3. ẢNH DỊCH VỤ (Service Images) --- */
    .service-item img {
        width: 100%;
        height: 180px;           /* Chiều cao cố định cho đều */
        object-fit: cover;       /* Cắt ảnh đẹp không bị méo */
        border-radius: 10px;
        margin-bottom: 20px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .service-item {
        padding: 20px;
        background: #fff;
        border-radius: 10px;
        transition: 0.3s;
    }

    .service-item:hover {
        transform: translateY(-5px); /* Hiệu ứng nổi lên khi di chuột */
        box-shadow: 0 10px 20px rgba(0,0,0,0.15);
    }
</style>