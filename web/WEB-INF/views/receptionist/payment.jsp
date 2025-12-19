<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Thanh Toán | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>

        <style>
        .list-group-item.active {
            background-color: #e67e22;
            border-color: #e67e22;
        }
        .badge { font-size: 90%; padding: 0.4em 0.6em; }
        .table td { vertical-align: middle; }
        .card-custom {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .card-header-custom {
            padding: 15px 20px;
            font-weight: 700;
            text-transform: uppercase;
            color: white;
        }
        .bg-gradient-room { background: linear-gradient(135deg, #ff9f1c, #ff6b35); }
        .bg-gradient-service { background: linear-gradient(135deg, #2ec4b6, #20a4f3); }
        .bg-gradient-invoice { background: linear-gradient(135deg, #667eea, #764ba2); }
        .bg-gradient-total { background: linear-gradient(135deg, #ff6b35, #d00000); }
        .paid-stamp {
            border: 3px solid #198754;
            color: #198754;
            font-weight: 900;
            text-transform: uppercase;
            padding: 10px 20px;
            border-radius: 10px;
            display: inline-block;
            transform: rotate(-5deg);
            font-size: 1.2rem;
            letter-spacing: 2px;
            background-color: rgba(25, 135, 84, 0.1);
        }
    </style>
    </head>
    <body>
        <jsp:include page="../components/navbar.jsp"></jsp:include>

        <div class="container-fluid"
            style="padding-top: 40px; padding-bottom: 60px; min-height: calc(100vh - 200px);">
            <div class="row">

                <%-- SIDEBAR --%>
                <div class="col-md-2 mb-3">
                    <div class="list-group shadow-sm">
                        <div
                            class="list-group-item bg-light text-uppercase font-weight-bold">
                            Chức Năng
                        </div>

                        <a
                            href="${pageContext.request.contextPath}/receptionist"
                            class="list-group-item list-group-item-action">
                            <i class="fa fa-desktop"></i> Check In / Check Out
                        </a>

                        <a
                            href="${pageContext.request.contextPath}/booking-manager"
                            class="list-group-item list-group-item-action">
                            <i class="fa fa-list"></i> Danh Sách Đặt Phòng
                        </a>

                        <a
                            href="${pageContext.request.contextPath}/receptionist/payment"
                            class="list-group-item list-group-item-action active">
                            <i class="fa fa-money"></i> Tính Tiền
                        </a>
                    </div>
                </div>

                <%-- MAIN CONTENT --%>
                <div class="col-md-10">
                    <div
                        class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                        <h3> Thanh Toán & Trả
                            Phòng</h3>
                    </div>

                    <c:if test="${not empty message}">
                        <div
                            class="alert alert-success alert-dismissible fade show">
                            <button type="button" class="close"
                                data-dismiss="alert">&times;</button>
                            <i class="fas fa-check-circle me-2"></i> ${message}
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div
                            class="alert alert-danger alert-dismissible fade show">
                            <button type="button" class="close"
                                data-dismiss="alert">&times;</button>
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            ${error}
                        </div>
                    </c:if>

                    <%-- Danh Sách Booking Cần Thanh Toán --%>
                    <c:if test="${not empty pendingBookings}">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-warning text-dark">
                                <h5 class="mb-0">
                                    <i class="fas fa-list me-2"></i>
                                    Danh Sách Booking Cần Thanh Toán
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table class="table table-hover align-middle mb-0">
                                        <thead class="bg-light">
                                            <tr>
                                                <th class="ps-4">Booking ID</th>
                                                <th>Mã đơn</th>
                                                <th>Số phòng</th>
                                                <th>Khách hàng</th>
                                                <th class="text-end">Tổng tiền</th>
                                                <th class="text-center">Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${pendingBookings}" var="booking">
                                                <tr>
                                                    <td class="ps-4 fw-bold">#${booking.bookingId}</td>
                                                    <td>${booking.bookingCode != null ? booking.bookingCode : '-'}</td>
                                                    <td>
                                                        <span class="badge bg-info">${booking.roomNumber}</span>
                                                    </td>
                                                    <td>${booking.customerName != null ? booking.customerName : '-'}</td>
                                                    <td class="text-end fw-bold text-danger">
                                                        <fmt:formatNumber value="${booking.grandTotal}" pattern="#,###" /> ₫
                                                    </td>
                                                    <td class="text-center">
                                                        <a href="payment?bookingId=${booking.bookingId}" 
                                                           class="btn btn-sm btn-primary">
                                                            <i class="fas fa-money-bill me-1"></i>
                                                            Thanh Toán
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <c:if test="${empty pendingBookings && empty bill}">
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle me-2"></i>
                            Không có booking nào cần thanh toán. Bạn có thể tìm kiếm theo Booking ID hoặc số phòng.
                        </div>
                    </c:if>

                    <%-- Search Box --%>
                    <div class="card shadow-sm mb-4">
                        <div class="card-body">
                            <form action="payment" method="get" class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label">Tìm theo Booking
                                        ID:</label>
                                    <input type="number" name="bookingId"
                                        class="form-control"
                                        placeholder="Nhập Booking ID..."
                                        value="${currBookingId}">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Hoặc tìm theo số
                                        phòng:</label>
                                    <input type="text" name="roomNumber"
                                        class="form-control"
                                        placeholder="Nhập số phòng..."
                                        value="${currRoom}">
                                </div>
                                <div class="col-md-4 d-flex align-items-end">
                                    <button type="submit"
                                        class="btn btn-primary w-100">
                                        <i class="fas fa-search"></i> Tìm Kiếm
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <c:if test="${not empty bill}">
                        <div class="row">
                            <%-- Thông Tin Phòng --%>
                            <div class="col-md-4">
                                <div class="card card-custom">
                                    <div
                                        class="card-header-custom bg-gradient-room">
                                        <i class="fas fa-bed me-2"></i> Thông
                                        Tin Phòng
                                    </div>
                                    <div class="card-body text-center">
                                        <h4 class="fw-bold mb-2">Phòng
                                            ${bill.roomNumber}</h4>
                                        <c:if
                                            test="${not empty bill.bookingCode}">
                                            <p class="text-muted mb-2">Mã đơn:
                                                <strong>${bill.bookingCode}</strong></p>
                                        </c:if>
                                        <c:if
                                            test="${not empty bill.customerName}">
                                            <p class="text-muted mb-3">Khách:
                                                <strong>${bill.customerName}</strong></p>
                                        </c:if>

                                        <div class="my-3">
                                            <c:choose>
                                                <c:when test="${bill.paid}">
                                                    <div class="paid-stamp">ĐÃ
                                                        THANH TOÁN</div>
                                                    <div
                                                        class="text-muted mt-2 small">
                                                        <i
                                                            class="far fa-clock"></i>
                                                        <fmt:formatDate
                                                            value="${bill.paymentDate}"
                                                            pattern="HH:mm dd/MM/yyyy" />
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="badge bg-warning text-dark px-3 py-2">
                                                        <i
                                                            class="fas fa-spinner fa-spin me-1"></i>
                                                        Chờ thanh toán
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div
                                            class="bg-light rounded p-3 text-start border mt-3">
                                            <div
                                                class="d-flex justify-content-between mb-2">
                                                <span class="text-muted">Tiền
                                                    phòng:</span>
                                                <span class="fw-bold">
                                                    <fmt:formatNumber
                                                        value="${bill.roomTotalAmount}"
                                                        pattern="#,###" /> ₫
                                                </span>
                                            </div>
                                            <hr class="my-2">
                                            <small
                                                class="text-muted fst-italic">${bill.roomNote}</small>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- Dịch Vụ Đã Dùng (Nhóm theo ServiceOrder) --%>
                            <div class="col-md-8">
                                <div class="card card-custom">
                                    <div
                                        class="card-header-custom bg-gradient-service">
                                        <i
                                            class="fas fa-concierge-bell me-2"></i>
                                        Chi Tiết Dịch Vụ
                                    </div>
                                    <div class="card-body p-0">
                                        <div class="table-responsive"
                                            style="max-height: 400px; overflow-y: auto;">
                                            <c:if test="${empty bill.listServiceDetails}">
                                                <div class="text-center py-5 text-muted">
                                                    Không có dịch vụ phát sinh.
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty bill.listServiceDetails}">
                                                <c:set var="prevOrderId" value="0" />
                                                <c:forEach items="${bill.listServiceDetails}" var="d" varStatus="loop">
                                                    <c:if test="${prevOrderId != d.orderId}">
                                                        <c:if test="${loop.index > 0}">
                                                            </tbody>
                                                        </table>
                                                        </c:if>
                                                        <div class="px-4 pt-3 pb-2 bg-light border-bottom">
                                                            <h6 class="mb-0 fw-bold text-primary">
                                                                <i class="fas fa-shopping-cart me-2"></i>
                                                                ${d.serviceName}
                                                                <small class="text-muted ms-2">
                                                                    <fmt:formatDate value="${d.orderTime}" pattern="dd/MM/yyyy HH:mm" />
                                                                </small>
                                                            </h6>
                                                        </div>
                                                        <table class="table table-hover align-middle mb-0">
                                                            <thead class="bg-light">
                                                                <tr>
                                                                    <th class="ps-4">Tên món</th>
                                                                    <th class="text-center">SL</th>
                                                                    <th class="text-end pe-4">Đơn giá</th>
                                                                    <th class="text-end pe-4">Thành tiền</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                        <c:set var="prevOrderId" value="${d.orderId}" />
                                                    </c:if>
                                                    <tr>
                                                        <td class="ps-4 fw-bold">${d.itemName}</td>
                                                        <td class="text-center">
                                                            <span class="badge bg-light text-dark border">x${d.quantity}</span>
                                                        </td>
                                                        <td class="text-end">
                                                            <fmt:formatNumber value="${d.unitPrice}" pattern="#,###" /> ₫
                                                        </td>
                                                        <td class="text-end pe-4 fw-bold">
                                                            <fmt:formatNumber value="${d.total}" pattern="#,###" /> ₫
                                                        </td>
                                                    </tr>
                                                    <c:if test="${loop.last}">
                                                            </tbody>
                                                        </table>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- Tổng Cộng & Thanh Toán --%>
                            <div class="col-12">
                                <div
                                    class="card card-custom border-top border-4 border-warning">
                                    <div class="card-body py-4">
                                        <div class="row align-items-center">
                                            <div
                                                class="col-md-5 text-center text-md-start ps-md-5">
                                                <h6
                                                    class="text-uppercase text-secondary fw-bold mb-1">Tổng
                                                    cộng</h6>
                                                <h1
                                                    class="display-4 fw-bold mb-0"
                                                    style="color: #e67e22;">
                                                    <fmt:formatNumber
                                                        value="${bill.grandTotal}"
                                                        pattern="#,###" />
                                                    <small
                                                        class="fs-5 text-muted">₫</small>
                                                </h1>
                                            </div>

                                            <div class="col-md-7 mt-4 mt-md-0">
                                                <c:choose>
                                                    <c:when test="${bill.paid}">
                                                        <div
                                                            class="alert alert-success d-flex align-items-center justify-content-center m-0">
                                                           
                                                            <div>
                                                                <h5
                                                                    class="alert-heading fw-bold mb-0">Hóa
                                                                    đơn này đã
                                                                    hoàn
                                                                    tất!</h5>
                                                                <p
                                                                    class="mb-0">Bạn
                                                                    không cần
                                                                    thao tác gì
                                                                    thêm.</p>
                                                            </div>
                                                        </div>
                                                    </c:when>

                                                    <c:otherwise>
                                                        <form action="payment"
                                                            method="post"
                                                            onsubmit="return confirm('Xác nhận thu tiền?')">
                                                            <input type="hidden"
                                                                name="bookingId"
                                                                value="${currBookingId}">
                                                            <input type="hidden"
                                                                name="totalAmount"
                                                                value="${bill.grandTotal}">

                                                            <div
                                                                class="input-group mb-3 shadow-sm">
                                                                <label
                                                                    class="input-group-text bg-white text-secondary">Hình
                                                                    thức:</label>
                                                                <select
                                                                    name="paymentMethod"
                                                                    class="form-select form-select-lg fw-bold text-dark">
                                                                    <option
                                                                        value="Cash">💵
                                                                        Tiền
                                                                        mặt</option>
                                                                    <option
                                                                        value="Card">💳
                                                                        Thẻ /
                                                                        Banking</option>
                                                                    <option
                                                                        value="Momo">🟪
                                                                        Ví
                                                                        Momo</option>
                                                                </select>
                                                                <button
                                                                    type="submit"
                                                                    class="btn btn-warning btn-lg px-5 fw-bold text-dark">
                                                                    THU TIỀN
                                                                    NGAY <i
                                                                        class="fas fa-arrow-right ms-2"></i>
                                                                </button>
                                                            </div>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <jsp:include page="../components/footer.jsp"></jsp:include>
    </body>
</html>
