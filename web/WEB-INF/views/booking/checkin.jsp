<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Check-in Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a
                        href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item">Front Desk</div>
                <div class="breadcrumb-item active">Check-in</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4>Tìm kiếm Booking</h4>
                            <div class="card-header-action">
                                <a
                                    href="${pageContext.request.contextPath}/receptionist/frontdesk"
                                    class="btn btn-light">
                                    <i class="fas fa-arrow-left"></i> Về Front
                                    Desk
                                </a>
                            </div>
                        </div>
                        <div class="card-body">
                            <h6 class="mb-2 font-weight-bold">Theo mã
                                Booking</h6>
                            <form method="get"
                                action="${pageContext.request.contextPath}/receptionist/checkin"
                                class="mb-4">
                                <div class="form-group">
                                    <label for="code">Mã Booking</label>
                                    <input type="text" id="code" name="code"
                                        class="form-control"
                                        placeholder="Nhập mã booking">
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </button>
                            </form>

                            <h6 class="mb-2 font-weight-bold">Theo tên khách +
                                ngày nhận phòng</h6>
                            <form method="get"
                                action="${pageContext.request.contextPath}/receptionist/checkin">
                                <div class="form-group">
                                    <label for="customer">Tên khách hàng</label>
                                    <input type="text" id="customer"
                                        name="customer" class="form-control"
                                        placeholder="Ví dụ: Nguyễn Văn A">
                                </div>
                                <div class="form-group">
                                    <label for="checkInDate">Ngày nhận
                                        phòng</label>
                                    <input type="date" id="checkInDate"
                                        name="checkInDate" class="form-control">
                                </div>
                                <button type="submit" class="btn btn-secondary">
                                    <i class="fas fa-search"></i> Tìm kiếm
                                </button>
                            </form>

                            <c:if test="${not empty error}">
                                <div
                                    class="alert alert-danger mt-3">${error}</div>
                            </c:if>

                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div
                                    class="alert alert-danger mt-3">${sessionScope.errorMessage}</div>
                                <c:remove var="errorMessage" scope="session" />
                            </c:if>
                            <c:if
                                test="${not empty sessionScope.successMessage}">
                                <div
                                    class="alert alert-success mt-3">${sessionScope.successMessage}</div>
                                <c:remove var="successMessage"
                                    scope="session" />
                            </c:if>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <c:if test="${not empty booking}">
                        <div class="card">
                            <div class="card-header">
                                <h4>Chi tiết Booking</h4>
                            </div>
                            <div class="card-body">
                                <p><strong>Mã Booking:</strong>
                                    ${booking.bookingCode}</p>
                                <p><strong>Khách hàng:</strong>
                                    ${booking.customer.fullName}
                                    (${booking.customer.phone})</p>
                                <p><strong>Phòng:</strong>
                                    ${booking.room.roomNumber} -
                                    ${booking.room.roomType.typeName}</p>
                                <p><strong>Thời gian dự kiến:</strong>
                                    <fmt:formatDate
                                        value="${booking.checkInDate}"
                                        pattern="dd/MM/yyyy HH:mm" /> -
                                    <fmt:formatDate
                                        value="${booking.checkOutDate}"
                                        pattern="dd/MM/yyyy HH:mm" />
                                </p>
                                <p><strong>Trạng thái hiện tại:</strong>
                                    ${booking.status}</p>

                                <form method="post"
                                    action="${pageContext.request.contextPath}/receptionist/checkin"
                                    class="mt-3">
                                    <input type="hidden" name="bookingId"
                                        value="${booking.bookingId}" />
                                    <button type="submit"
                                        class="btn btn-success"
                                        onclick="return confirm('Xác nhận check-in cho khách này?');">
                                        <i class="fas fa-door-open"></i> XÁC
                                        NHẬN CHECK-IN
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty bookingList}">
                        <div class="card mt-4">
                            <div class="card-header">
                                <h4>Danh sách Booking tìm được</h4>
                            </div>
                            <div class="card-body p-0">
                                <div class="table-responsive">
                                    <table
                                        class="table table-striped table-hover mb-0">
                                        <thead>
                                            <tr>
                                                <th>Mã</th>
                                                <th>Khách hàng</th>
                                                <th>Phòng</th>
                                                <th>Check-in</th>
                                                <th>Status</th>
                                                <th>Chọn</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="b"
                                                items="${bookingList}">
                                                <tr>
                                                    <td>${b.bookingCode}</td>
                                                    <td>${b.customer.fullName}</td>
                                                    <td>${b.room.roomNumber}</td>
                                                    <td><fmt:formatDate
                                                            value="${b.checkInDate}"
                                                            pattern="dd/MM/yyyy" /></td>
                                                    <td>${b.status}</td>
                                                    <td>
                                                        <a
                                                            class="btn btn-sm btn-primary"
                                                            href="${pageContext.request.contextPath}/receptionist/checkin?code=${b.bookingCode}">
                                                            Chọn
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
