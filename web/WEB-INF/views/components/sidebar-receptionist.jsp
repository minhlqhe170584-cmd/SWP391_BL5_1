<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .sidebar-widget {
        background: #ffffff;
        border: 1px solid #e1e1e1;
        margin-bottom: 30px;
    }
    
    .sidebar-title {
        background: #f5f5f5; /* Màu nền tiêu đề xám nhạt */
        padding: 15px 20px;
        border-bottom: 1px solid #e1e1e1;
        margin: 0;
    }
    
    .sidebar-title h5 {
        font-size: 16px;
        font-weight: 700;
        text-transform: uppercase;
        margin: 0;
        color: #333;
    }
    
    .sidebar-menu {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    
    .sidebar-menu li {
        border-bottom: 1px solid #f0f0f0;
    }
    
    .sidebar-menu li:last-child {
        border-bottom: none;
    }
    
    .sidebar-menu a {
        display: block;
        padding: 15px 20px;
        color: #666;
        font-size: 15px;
        transition: all 0.3s;
        text-decoration: none;
        display: flex;
        align-items: center;
    }
    
    .sidebar-menu a i {
        margin-right: 10px;
        width: 20px;
        text-align: center;
    }
    
    .sidebar-menu a:hover {
        background: #f9f9f9;
        color: #e67e22; /* Màu cam khi di chuột */
    }
    
    /* Class Active: Màu cam khi đang ở trang đó */
    .sidebar-menu a.active-item {
        background: #e67e22; /* Màu cam chủ đạo */
        color: #ffffff;
        font-weight: bold;
    }
    
    .sidebar-menu a.active-item:hover {
        color: #ffffff; /* Giữ màu chữ trắng khi hover vào item đang active */
    }
</style>

<div class="sidebar-widget">
    <div class="sidebar-title">
        <h5>CHỨC NĂNG</h5>
    </div>
    <ul class="sidebar-menu">
        <li>
            <a href="${pageContext.request.contextPath}/receptionist/checkin" 
               class="${param.activePage == 'checkin' ? 'active-item' : ''}">
                <i class="fa fa-desktop"></i> Check In / Check Out
            </a>
        </li>
        
        <li>
            <a href="${pageContext.request.contextPath}/booking-manager" 
               class="${param.activePage == 'booking-manager' ? 'active-item' : ''}">
                <i class="fa fa-list-alt"></i> Danh Sách Đặt Phòng
            </a>
        </li>
        
        <li>
            <a href="${pageContext.request.contextPath}/receptionist/payment" 
               class="${param.activePage == 'payment' ? 'active-item' : ''}">
                <i class="fa fa-money"></i> Tính Tiền
            </a>
        </li>
    </ul>
</div>