<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="offcanvas-menu-overlay"></div>
<div class="canvas-open">
    <i class="icon_menu"></i>
</div>
<div class="offcanvas-menu-wrapper">
    <div class="canvas-close">
        <i class="icon_close"></i>
    </div>
    <div class="search-icon search-switch">
        <i class="icon_search"></i>
    </div>
    
    <div class="header-configure-area">
        <a href="${pageContext.request.contextPath}/rooms" class="bk-btn">Đặt Phòng Ngay</a>
    </div>
    
    <nav class="mainmenu mobile-menu">
        <ul>
            <li class="${pageContext.request.servletPath.contains('home') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            </li>
            <li class="${pageContext.request.servletPath.contains('room') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/rooms">Phòng</a>
            </li>
            <li><a href="${pageContext.request.contextPath}/services">Dịch vụ</a></li>
            
            <c:if test="${sessionScope.USER == null}">
                <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
                <li><a href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
            </c:if>
            <c:if test="${sessionScope.USER != null}">
                <li><a href="#">Tài khoản</a>
                    <ul class="dropdown">
                        <li><a href="${pageContext.request.contextPath}/profile">Hồ sơ</a></li>
                        <li><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                    </ul>
                </li>
            </c:if>
        </ul>
    </nav>
    <div id="mobile-menu-wrap"></div>
    <ul class="top-widget">
        <li><i class="fa fa-phone"></i> (12) 345 67890</li>
        <li><i class="fa fa-envelope"></i> info@smarthotel.com</li>
    </ul>
</div>
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
                            <a href="#"><i class="fa fa-twitter"></i></a>
                            <a href="#"><i class="fa fa-instagram"></i></a>
                        </div>
                        <a href="${pageContext.request.contextPath}/rooms" class="bk-btn">Đặt Phòng Ngay</a>
                        
                        <div class="language-option">
                            <c:choose>
                                <c:when test="${sessionScope.USER == null && sessionScope.CURRENT_ROOM == null}">
                                    <span class="text-dark">Tài khoản <i class="fa fa-angle-down"></i></span>
                                    <div class="flag-dropdown">
                                        <ul>
                                            <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
                                            <li><a href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
                                        </ul>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <span class="text-warning font-weight-bold">
                                        <i class="fa fa-user"></i> 
                                        <c:if test="${sessionScope.USER != null}">${sessionScope.USER.fullName}</c:if>
                                        <c:if test="${sessionScope.CURRENT_ROOM != null}">Phòng ${sessionScope.CURRENT_ROOM.roomNumber}</c:if>
                                        <i class="fa fa-angle-down"></i>
                                    </span>
                                    <div class="flag-dropdown">
                                        <ul>
                                            <c:if test="${sessionScope.ROLE == 'STAFF'}">
                                                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Quản trị</a></li>
                                            </c:if>
                                            <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">
                                                <li><a href="${pageContext.request.contextPath}/profile">Hồ sơ</a></li>
                                                <li><a href="${pageContext.request.contextPath}/history">Lịch sử</a></li>
                                            </c:if>
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
                            <ul>
                                <li class="${pageContext.request.servletPath.contains('home') ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                                </li>
                                <li class="${pageContext.request.servletPath.contains('room') ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/rooms">Phòng Nghỉ</a>
                                </li>
                                <li><a href="${pageContext.request.contextPath}/services">Dịch Vụ</a></li>
                                
                                <c:if test="${sessionScope.ROLE == 'ROOM'}">
                                    <li><a href="${pageContext.request.contextPath}/order" style="color: #e67e22;">GỌI MÓN</a></li>
                                </c:if>

                                <li><a href="#">Tin Tức</a>
                                    <ul class="dropdown">
                                        <li><a href="#">Sự kiện</a></li>
                                        <li><a href="#">Khuyến mãi</a></li>
                                    </ul>
                                </li>
                                <li><a href="${pageContext.request.contextPath}/contact">Liên Hệ</a></li>
                            </ul>
                        </nav>
                        <div class="nav-right search-switch">
                            <i class="icon_search"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>