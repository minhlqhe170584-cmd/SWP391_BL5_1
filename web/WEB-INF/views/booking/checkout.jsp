<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Check-out Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a
                        href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item">Front Desk</div>
                <div class="breadcrumb-item active">Check-out</div>
            </div>
        </div>

        <div class="section-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div
                    class="alert alert-danger">${sessionScope.errorMessage}</div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                <div
                    class="alert alert-success">${sessionScope.successMessage}</div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <c:if test="${not empty booking}">
                <div class="row">
                    <div class="col-lg-8 offset-lg-2">
                        <div class="card">
                            <div class="card-header">
                                <h4>Thông tin Booking</h4>
                                <div class="card-header-action">
                                    <a
                                        href="${pageContext.request.contextPath}/admin/frontdesk?view=checkout"
                                        class="btn btn-light">
                                        <i class="fas fa-arrow-left"></i> Về
                                        Front Desk
                                    </a>
                                </div>
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
                                    action="${pageContext.request.contextPath}/admin/checkout"
                                    class="mt-3">
                                    <input type="hidden" name="bookingId"
                                        value="${booking.bookingId}" />
                                    <button type="submit" class="btn btn-danger"
                                        onclick="return confirm('Xác nhận check-out và trả phòng?');">
                                        <i class="fas fa-door-closed"></i> XÁC
                                        NHẬN CHECK-OUT
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
