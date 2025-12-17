<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Bảng Điều Khiển Lễ Tân | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    
    <style>
        /* Style tùy chỉnh để menu trái đẹp hơn */
        .list-group-item.active {
            background-color: #e67e22; /* Màu cam chủ đạo */
            border-color: #e67e22;
        }
        .badge { font-size: 90%; padding: 0.4em 0.6em; }
        .table td { vertical-align: middle; }
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="container-fluid mt-4 mb-5">
        <div class="row">
            
            <%-- ======================================================== --%>
            <%-- 2. SIDEBAR (MENU TRÁI) - Dùng cột col-md-2               --%>
            <%-- ======================================================== --%>
            <div class="col-md-2 mb-3">
                <div class="list-group shadow-sm">
                    <div class="list-group-item bg-light text-uppercase font-weight-bold">
                        Chức Năng
                    </div>
                    <a href="${pageContext.request.contextPath}/receptionist" class="list-group-item list-group-item-action active">
                        <i class="fa fa-desktop"></i> Check In / Check Out
                    </a>
                    <a href="${pageContext.request.contextPath}/booking-manager" class="list-group-item list-group-item-action">
                        <i class="fa fa-list"></i> Danh Sách Đặt Phòng
                    </a>
                    <a href="${pageContext.request.contextPath}/receptionist/payment" class="list-group-item list-group-item-action">
                        <i class="fa fa-money"></i> Tính Tiền
                    </a>
                </div>
            </div>

            <%-- ======================================================== --%>
            <%-- 3. MAIN CONTENT (NỘI DUNG) - Dùng cột col-md-10          --%>
            <%-- ======================================================== --%>
            <div class="col-md-10">
                <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                    <h3><i class="fa fa-concierge-bell"></i> Lễ Tân - Check In / Out</h3>
                    <a href="${pageContext.request.contextPath}/booking-manager" class="btn btn-outline-primary btn-sm">
                        <i class="fa fa-plus"></i> Xem tất cả Booking
                    </a>
                </div>

                <c:if test="${not empty sessionScope.msg}">
                    <div class="alert alert-success alert-dismissible fade show">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${sessionScope.msg}
                    </div>
                    <c:remove var="msg" scope="session"/>
                </c:if>
                <c:if test="${not empty sessionScope.err}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                        ${sessionScope.err}
                    </div>
                    <c:remove var="err" scope="session"/>
                </c:if>

                <div class="card shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="mb-0 text-primary">Danh sách khách hôm nay</h5>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover table-striped mb-0">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Mã Đơn</th>
                                        <th>Khách Hàng</th>
                                        <th>Phòng</th>
                                        <th>Ngày Đến</th>
                                        <th>Ngày Đi</th>
                                        <th>Trạng Thái</th>
                                        <th class="text-center" style="width: 150px;">Thao Tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="hasData" value="false" />
                                    
                                    <c:forEach items="${listBookings}" var="b">
                                        <%-- Chỉ hiện đơn Pending (đến checkin) hoặc CheckedIn (cần checkout) --%>
                                        <c:if test="${b.status == 'Pending' || b.status == 'CheckedIn'}">
                                            <c:set var="hasData" value="true" />
                                            <tr>
                                                <td><strong>${b.bookingCode}</strong></td>
                                                <td>${b.customerName}</td>
                                                <td><span class="badge badge-warning text-white">P.${b.roomNumber}</span></td>
                                                <td><fmt:formatDate value="${b.checkInDate}" pattern="dd/MM/yyyy"/></td>
                                                <td><fmt:formatDate value="${b.checkOutDate}" pattern="dd/MM/yyyy"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.status == 'Pending'}"><span class="badge badge-info">Chờ Check-in</span></c:when>
                                                        <c:when test="${b.status == 'CheckedIn'}"><span class="badge badge-success">Đang ở</span></c:when>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center">
                                                    <form action="${pageContext.request.contextPath}/receptionist" method="post">
                                                        <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                        <input type="hidden" name="roomId" value="${b.roomId}">
                                                        
                                                        <c:choose>
                                                            <%-- Nút CHECK-IN --%>
                                                            <c:when test="${b.status == 'Pending'}">
                                                                <button type="submit" name="action" value="checkin" class="btn btn-sm btn-success w-100" onclick="return confirm('Xác nhận khách đã đến và Check-in?');">
                                                                    <i class="fa fa-check"></i> Check-in
                                                                </button>
                                                            </c:when>

                                                            <%-- Nút CHECK-OUT --%>
                                                            <c:when test="${b.status == 'CheckedIn'}">
                                                                <button type="submit" name="action" value="checkout" class="btn btn-sm btn-warning w-100" onclick="return confirm('Xác nhận khách trả phòng và Check-out?');">
                                                                    <i class="fa fa-sign-out"></i> Check-out
                                                                </button>
                                                            </c:when>
                                                        </c:choose>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    
                                    <%-- Nếu không có dữ liệu --%>
                                    <c:if test="${!hasData}">
                                        <tr>
                                            <td colspan="7" class="text-center py-4 text-muted">
                                                <i class="fa fa-check-circle-o fa-3x mb-2"></i><br>
                                                Hiện tại không có khách nào cần xử lý.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                </div>
            </div>
    </div>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>