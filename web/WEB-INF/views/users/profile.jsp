<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Hồ Sơ | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    <style>
        .profile-card { background: #fff; padding: 40px; border-radius: 10px; box-shadow: 0 0 20px rgba(0,0,0,0.1); margin-bottom: 50px; }
        .profile-header h3 { color: #e67e22; font-weight: 700; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px;}
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>
    
    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Hồ Sơ</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                            <span>
                                <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">Thông tin cá nhân</c:if>
                                <c:if test="${sessionScope.ROLE == 'ROOM'}">Lịch sử dịch vụ</c:if>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="spad">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="profile-card">
                        
                        <c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
                        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

                        <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">
                            <div class="profile-header"><h3>Thông Tin Cá Nhân</h3></div>
                            <form action="${pageContext.request.contextPath}/profile" method="post">
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label>ID</label>
                                        <input type="text" class="form-control" value="${sessionScope.USER.customerId}" readonly style="background:#eee;">
                                    </div>
                                    <div class="form-group col-md-10">
                                        <label>Họ Tên</label>
                                        <input type="text" name="fullName" class="form-control" value="${sessionScope.USER.fullName}" required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label>Email</label>
                                        <input type="text" class="form-control" value="${sessionScope.USER.email}" readonly style="background:#eee;">
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label>SĐT</label>
                                        <input type="text" name="phone" class="form-control" value="${sessionScope.USER.phone}" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Mật khẩu</label>
                                    <input type="text" name="password" class="form-control" value="${sessionScope.USER.password}" required>
                                </div>
                                <div class="text-center mt-3">
                                    <button class="primary-btn border-0">Lưu Thay Đổi</button>
                                </div>
                            </form>
                        </c:if>

                        <c:if test="${sessionScope.ROLE == 'ROOM'}">
                            <div class="profile-header">
                                <h3>Lịch Sử Dịch Vụ - Phòng ${sessionScope.USER.roomNumber}</h3>
                            </div>
                            <table class="table table-bordered table-hover">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Thời gian</th>
                                        <th>Tên Dịch Vụ</th>
                                        <th>SL</th>
                                        <th>Tổng tiền</th>
                                        <th>Trạng thái</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${serviceHistory}" var="h">
                                        <tr>
                                            <td><fmt:formatDate value="${h.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>${h.serviceName}</td>
                                            <td>${h.quantity}</td>
                                            <td><fmt:formatNumber value="${h.totalPrice}" type="currency" currencySymbol="đ"/></td>
                                            <td><span class="badge badge-info">${h.status}</span></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty serviceHistory}">
                                        <tr><td colspan="5" class="text-center">Chưa có lịch sử gọi dịch vụ.</td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <div class="text-center mt-3">
                                <a href="${pageContext.request.contextPath}/services" class="primary-btn">Đặt Dịch Vụ Mới</a>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>