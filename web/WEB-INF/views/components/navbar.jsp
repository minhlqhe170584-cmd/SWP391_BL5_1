<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-light navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            <i class="fas fa-hotel"></i> SMART HOTEL
        </a>
        
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#mainMenu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainMenu">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/rooms">Phòng nghỉ</a></li>

                <%-- 1. DÀNH CHO NHÂN VIÊN (STAFF) --%>
                <c:if test="${sessionScope.ROLE == 'STAFF'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-primary" href="#" data-toggle="dropdown">
                            <i class="fas fa-briefcase"></i> Quản lý
                        </a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/reception/checkin">Check-in / Check-out</a>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/reception/booking">Đặt phòng tại quầy</a>
                            
                            <c:if test="${sessionScope.USER.role.roleName == 'Admin'}">
                                <div class="dropdown-divider"></div>
                                <h6 class="dropdown-header">Administrator</h6>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">Báo cáo doanh thu</a>
                                <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/staffs">Quản lý nhân viên</a>
                            </c:if>
                        </div>
                    </li>
                </c:if>

                <%-- 2. DÀNH CHO KHÁCH TẠI PHÒNG (ROOM ACCOUNT) --%>
                <c:if test="${sessionScope.ROLE == 'ROOM'}">
                    <li class="nav-item">
                        <a class="nav-link text-success font-weight-bold" href="${pageContext.request.contextPath}/services">
                            <i class="fas fa-utensils"></i> GỌI DỊCH VỤ
                        </a>
                    </li>
                </c:if>
                    
                <%-- 3. DÀNH CHO KHÁCH ONLINE (CUSTOMER) --%>
                <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/history">Lịch sử đặt</a></li>
                </c:if>
            </ul>

            <ul class="navbar-nav ml-auto">
                <c:choose>
                    <%-- CHƯA ĐĂNG NHẬP --%>
                    <c:when test="${sessionScope.USER == null && sessionScope.CURRENT_ROOM == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        </li>
                        <li class="nav-item">
                            <a class="btn btn-primary text-white btn-sm ml-2" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                        </li>
                    </c:when>

                    <%-- ĐÃ ĐĂNG NHẬP --%>
                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle font-weight-bold" href="#" data-toggle="dropdown">
                                <i class="fas fa-user-circle"></i> 
                                <c:if test="${sessionScope.USER != null}"> ${sessionScope.USER.fullName} </c:if>
                                <c:if test="${sessionScope.CURRENT_ROOM != null}"> Phòng ${sessionScope.CURRENT_ROOM.roomNumber} </c:if>
                            </a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Hồ sơ cá nhân</a>
                                </c:if>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                            </div>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>