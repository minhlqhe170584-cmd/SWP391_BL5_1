<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản Lý Đặt Phòng | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    
    <style>
        /* CSS đồng bộ với trang Receptionist */
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

    <div class="container-fluid" style="padding-top: 40px; padding-bottom: 60px; min-height: calc(100vh - 200px);">
        <div class="row">
            
            <%-- ======================================================== --%>
            <%-- 2. SIDEBAR                                               --%>
            <%-- ======================================================== --%>
            <div class="col-md-2 mb-3">
                <%-- 
                   GỢI Ý: Bạn nên cập nhật file components/sidebar-receptionist.jsp 
                   theo mẫu list-group này để dùng chung cho gọn nhé.
                   Hiện tại mình giữ nguyên code HTML của bạn ở đây.
                --%>
                <div class="list-group shadow-sm">
                    <div class="list-group-item bg-light text-uppercase font-weight-bold">
                        Chức Năng
                    </div>
                    
                    <a href="${pageContext.request.contextPath}/receptionist" class="list-group-item list-group-item-action">
                        <i class="fa fa-desktop"></i> Check In / Check Out
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/booking-manager" class="list-group-item list-group-item-action active">
                        <i class="fa fa-list"></i> Danh Sách Đặt Phòng
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/receptionist/payment" class="list-group-item list-group-item-action">
                        <i class="fa fa-money"></i> Tính Tiền
                    </a>
                    
                </div>
            </div>

            <%-- ======================================================== --%>
            <%-- 3. MAIN CONTENT                                          --%>
            <%-- ======================================================== --%>
            <div class="col-md-10">
                <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                    <h3> Quản Lý Tất Cả Đơn Đặt</h3>
                </div>
                
                <%-- Filter Tabs --%>
                <ul class="nav nav-tabs mb-3" id="bookingTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <a class="nav-link active" id="all-tab" data-toggle="tab" href="#all" role="tab" aria-controls="all" aria-selected="true">
                            <i class="fa fa-list"></i> Tất Cả
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link" id="checkout-tab" data-toggle="tab" href="#checkout" role="tab" aria-controls="checkout" aria-selected="false">
                            <i class="fa fa-sign-out"></i> Cần Checkout
                        </a>
                    </li>
                    <li class="nav-item" role="presentation">
                        <a class="nav-link" id="payment-tab" data-toggle="tab" href="#payment" role="tab" aria-controls="payment" aria-selected="false">
                            <i class="fa fa-money"></i> Cần Thanh Toán
                        </a>
                    </li>
                </ul>

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

                <div class="tab-content" id="bookingTabContent">
                    <%-- Tab: Tất Cả --%>
                    <div class="tab-pane fade show active" id="all" role="tabpanel" aria-labelledby="all-tab">
                        <div class="card shadow-sm">
                            <div class="card-header bg-white">
                                <h5 class="mb-0 text-primary">Danh sách chi tiết</h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover table-striped mb-0">
                                        <thead class="thead-light"> <tr>
                                                <th>Mã Đơn</th>
                                                <th>Khách Hàng</th>
                                                <th>Phòng</th>
                                                <th>Check-In</th>
                                                <th>Check-Out</th>
                                                <th>Trạng Thái</th>
                                                <th class="text-center">Hành Động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${bookings}" var="b">
                                            <tr>
                                                <td><strong>${b.bookingCode}</strong></td>
                                                <td>${b.customerName}</td>
                                                <td><span class="badge badge-info">P.${b.roomNumber}</span></td>
                                                <td><fmt:formatDate value="${b.checkInDate}" pattern="dd/MM/yyyy"/></td>
                                                <td><fmt:formatDate value="${b.checkOutDate}" pattern="dd/MM/yyyy"/></td>
                                                
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.status == 'Pending'}">
                                                            <span class="badge badge-warning text-white">Chờ nhận phòng</span>
                                                        </c:when>
                                                        <c:when test="${b.status == 'CheckedIn'}">
                                                            <span class="badge badge-success">Đang ở</span>
                                                        </c:when>
                                                        <c:when test="${b.status == 'CheckedOut'}">
                                                            <span class="badge badge-secondary">Đã trả phòng</span>
                                                        </c:when>
                                                        <c:when test="${b.status == 'Cancelled'}">
                                                            <span class="badge badge-danger">Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>${b.status}</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                
                                                <td class="text-center">
                                                    <c:if test="${b.status == 'Pending'}">
                                                        <form action="${pageContext.request.contextPath}/booking-manager" method="post" onsubmit="return confirm('CẢNH BÁO: Bạn có chắc chắn muốn HỦY đơn đặt này không? Hành động này không thể hoàn tác.');">
                                                            <input type="hidden" name="action" value="cancel">
                                                            <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                            <input type="hidden" name="roomId" value="${b.roomId}">
                                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Hủy đơn này">
                                                                <i class="fa fa-times"></i> Hủy Đơn
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <c:if test="${b.status == 'CheckedIn'}">
                                                        <form action="${pageContext.request.contextPath}/receptionist" method="post" class="d-inline">
                                                            <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                            <input type="hidden" name="roomId" value="${b.roomId}">
                                                            <button type="submit" name="action" value="checkout" class="btn btn-sm btn-warning" 
                                                                    onclick="return confirm('Xác nhận khách trả phòng và Check-out?');" title="Checkout">
                                                                <i class="fa fa-sign-out"></i> Checkout
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                    <c:if test="${b.status == 'CheckedOut'}">
                                                        <a href="${pageContext.request.contextPath}/receptionist/payment?bookingId=${b.bookingId}" 
                                                           class="btn btn-sm btn-success" title="Thanh toán">
                                                            <i class="fa fa-money"></i> Thanh Toán
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${b.status != 'Pending' && b.status != 'CheckedOut' && b.status != 'CheckedIn'}">
                                                        <span class="text-muted small"><i class="fa fa-lock"></i></span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                            
                                            <c:if test="${empty bookings}">
                                                <tr>
                                                    <td colspan="7" class="text-center py-4 text-muted">
                                                        <i class="fa fa-folder-open-o fa-3x mb-2"></i><br>
                                                        Chưa có dữ liệu đặt phòng nào.
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <%-- Tab: Cần Checkout --%>
                    <div class="tab-pane fade" id="checkout" role="tabpanel" aria-labelledby="checkout-tab">
                        <div class="card shadow-sm">
                            <div class="card-header bg-white">
                                <h5 class="mb-0 text-warning"><i class="fa fa-sign-out"></i> Danh sách phòng cần Checkout</h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover table-striped mb-0">
                                        <thead class="thead-light">
                                            <tr>
                                                <th>Mã Đơn</th>
                                                <th>Khách Hàng</th>
                                                <th>Phòng</th>
                                                <th>Check-In</th>
                                                <th>Check-Out</th>
                                                <th>Trạng Thái</th>
                                                <th class="text-center">Hành Động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="hasCheckout" value="false" />
                                            <c:forEach items="${bookings}" var="b">
                                                <c:if test="${b.status == 'CheckedIn'}">
                                                    <c:set var="hasCheckout" value="true" />
                                                    <tr>
                                                        <td><strong>${b.bookingCode}</strong></td>
                                                        <td>${b.customerName}</td>
                                                        <td><span class="badge badge-info">P.${b.roomNumber}</span></td>
                                                        <td><fmt:formatDate value="${b.checkInDate}" pattern="dd/MM/yyyy"/></td>
                                                        <td><fmt:formatDate value="${b.checkOutDate}" pattern="dd/MM/yyyy"/></td>
                                                        <td><span class="badge badge-success">Đang ở</span></td>
                                                        <td class="text-center">
                                                            <form action="${pageContext.request.contextPath}/receptionist" method="post" class="d-inline">
                                                                <input type="hidden" name="bookingId" value="${b.bookingId}">
                                                                <input type="hidden" name="roomId" value="${b.roomId}">
                                                                <button type="submit" name="action" value="checkout" class="btn btn-sm btn-warning" 
                                                                        onclick="return confirm('Xác nhận khách trả phòng và Check-out?');" title="Checkout">
                                                                    <i class="fa fa-sign-out"></i> Checkout
                                                                </button>
                                                            </form>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${!hasCheckout}">
                                                <tr>
                                                    <td colspan="7" class="text-center py-4 text-muted">
                                                        <i class="fa fa-check-circle fa-3x mb-2"></i><br>
                                                        Không có phòng nào cần checkout.
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <%-- Tab: Cần Thanh Toán --%>
                    <div class="tab-pane fade" id="payment" role="tabpanel" aria-labelledby="payment-tab">
                        <div class="card shadow-sm">
                            <div class="card-header bg-white">
                                <h5 class="mb-0 text-success"><i class="fa fa-money"></i> Danh sách phòng cần Thanh Toán</h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover table-striped mb-0">
                                        <thead class="thead-light">
                                            <tr>
                                                <th>Mã Đơn</th>
                                                <th>Khách Hàng</th>
                                                <th>Phòng</th>
                                                <th>Check-In</th>
                                                <th>Check-Out</th>
                                                <th>Trạng Thái</th>
                                                <th class="text-center">Hành Động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="hasPayment" value="false" />
                                            <c:forEach items="${bookings}" var="b">
                                                <c:if test="${b.status == 'CheckedOut'}">
                                                    <c:set var="hasPayment" value="true" />
                                                    <tr>
                                                        <td><strong>${b.bookingCode}</strong></td>
                                                        <td>${b.customerName}</td>
                                                        <td><span class="badge badge-info">P.${b.roomNumber}</span></td>
                                                        <td><fmt:formatDate value="${b.checkInDate}" pattern="dd/MM/yyyy"/></td>
                                                        <td><fmt:formatDate value="${b.checkOutDate}" pattern="dd/MM/yyyy"/></td>
                                                        <td><span class="badge badge-secondary">Đã trả phòng</span></td>
                                                        <td class="text-center">
                                                            <a href="${pageContext.request.contextPath}/receptionist/payment?bookingId=${b.bookingId}" 
                                                               class="btn btn-sm btn-success" title="Thanh toán">
                                                                <i class="fa fa-money"></i> Thanh Toán
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${!hasPayment}">
                                                <tr>
                                                    <td colspan="7" class="text-center py-4 text-muted">
                                                        <i class="fa fa-check-circle fa-3x mb-2"></i><br>
                                                        Không có phòng nào cần thanh toán.
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div> </div> </div> </div> <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>