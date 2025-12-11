<%-- 
    Document   : navbar
    Description: Menu PC (Đã sửa: Dịch chuyển sang phía bên Phải)
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="header-section header-normal">
    
    <div class="top-nav">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <ul class="tn-left">
                        <li><i class="fa fa-phone"></i> (024) 6688 8888</li>
                        <li><i class="fa fa-envelope"></i> contact@smarthotel.com</li>
                    </ul>
                </div>
                <div class="col-lg-6">
                    <div class="tn-right">
                        <div class="top-social">
                            <a href="#"><i class="fa fa-facebook"></i></a>
                            <a href="#"><i class="fa fa-instagram"></i></a>
                        </div>
                        
                        <a href="${pageContext.request.contextPath}/listRooms" class="bk-btn">Đặt Phòng Ngay</a>
                        
                        <div class="language-option">
                            <c:choose>
                                <c:when test="${sessionScope.USER == null}">
                                    <span class="text-dark" onclick="window.location.href='${pageContext.request.contextPath}/login'" style="cursor: pointer;">
                                        <i class="fa fa-user"></i> Đăng nhập
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-warning font-weight-bold">
                                        <i class="fa fa-user-circle"></i> 
                                        <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">${sessionScope.USER.fullName}</c:if>
                                        <c:if test="${sessionScope.ROLE == 'STAFF'}">${sessionScope.USER.fullName} (NV)</c:if>
                                        <c:if test="${sessionScope.ROLE == 'ROOM'}">Phòng ${sessionScope.USER.roomNumber}</c:if>
                                    </span>
                                    <div class="flag-dropdown">
                                        <ul>
                                            <li><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                                        </ul>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="menu-item">
        <div class="container">
            <div class="row">
                <div class="col-lg-2">
                    <div class="logo">
                        <a href="${pageContext.request.contextPath}/home">
                            <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="Smart Hotel">
                        </a>
                    </div>
                </div>
                <div class="col-lg-10">
                    <div class="nav-menu">
                        <nav class="mainmenu">
                            
                            <ul class="text-right" id="pc-nav-list"> 
                                
                                <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                                <li><a href="${pageContext.request.contextPath}/listRooms">Phòng Nghỉ</a></li>
                                <li><a href="${pageContext.request.contextPath}/services">Dịch Vụ</a></li>
                                <li><a href="${pageContext.request.contextPath}/contact">Liên Hệ</a></li>

                                <c:if test="${sessionScope.USER != null}">
                                    <c:choose>
                                        <c:when test="${sessionScope.ROLE == 'STAFF'}">
                                            <li><a href="${pageContext.request.contextPath}/staffs">Quản Trị</a></li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="${pageContext.request.contextPath}/profile">Hồ Sơ</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        var currentUrl = window.location.pathname; 
        var menuItems = document.querySelectorAll('#pc-nav-list li a');
        
        menuItems.forEach(function(link) {
            var linkHref = link.getAttribute('href');
            if (linkHref && currentUrl.includes(linkHref) && linkHref.length > 2) {
                link.parentElement.classList.add('active');
            } 
            else if (currentUrl.endsWith('/home') && linkHref.endsWith('/home')) {
                link.parentElement.classList.add('active');
            }
        });
    });
</script>