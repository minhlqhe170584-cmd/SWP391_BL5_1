<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Front Desk Dashboard</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a
                        href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item active">Front Desk</div>
            </div>
        </div>

        <div class="section-body">
            <!-- Stats Cards -->
            <div class="row">
                <div class="col-lg-4 col-md-6">
                    <div class="card card-statistic-1">
                        <div class="card-icon bg-success">
                            <i class="fas fa-bed"></i>
                        </div>
                        <div class="card-wrap">
                            <div class="card-header">
                                <h4>Phòng trống</h4>
                            </div>
                            <div class="card-body">
                                ${availableCount}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="card card-statistic-1">
                        <div class="card-icon bg-warning">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <div class="card-wrap">
                            <div class="card-header">
                                <h4>Đã đặt trước</h4>
                            </div>
                            <div class="card-body">
                                ${reservedCount}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4 col-md-6">
                    <div class="card card-statistic-1">
                        <div class="card-icon bg-danger">
                            <i class="fas fa-door-open"></i>
                        </div>
                        <div class="card-wrap">
                            <div class="card-header">
                                <h4>Đang sử dụng</h4>
                            </div>
                            <div class="card-body">
                                ${occupiedCount}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-12">
                    <form method="get"
                        action="${pageContext.request.contextPath}/admin/frontdesk"
                        class="form-row">
                        <div class="col-md-3 mb-2">
                            <label>Ngày</label>
                            <input type="date" name="date" class="form-control"
                                value="${selectedDate}">
                        </div>
                        <div class="col-md-4 mb-2">
                            <label>Tìm kiếm</label>
                            <input type="text" name="search"
                                class="form-control"
                                placeholder="Mã booking, tên khách, số phòng..."
                                value="${search}">
                        </div>
                        <div class="col-md-3 mb-2">
                            <label>Loại danh sách</label>
                            <select name="view" class="form-control">
                                <option value="checkin" ${view == 'checkin' ?
                                    'selected' : ''}>Cần Check-in</option>
                                <option value="checkout" ${view == 'checkout' ?
                                    'selected' : ''}>Cần Check-out</option>
                            </select>
                        </div>
                        <div class="col-md-2 mb-2 d-flex align-items-end">
                            <button type="submit"
                                class="btn btn-primary btn-block">
                                <i class="fas fa-search"></i> Lọc
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Tabs giống bicycle/list.jsp -->
            <ul class="nav nav-tabs mb-3">
                <li class="nav-item">
                    <a class="nav-link ${view == 'checkin' ? 'active' : ''}"
                        href="${pageContext.request.contextPath}/admin/frontdesk?view=checkin&date=${selectedDate}&search=${search}">
                        Cần Check-in
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${view == 'checkout' ? 'active' : ''}"
                        href="${pageContext.request.contextPath}/admin/frontdesk?view=checkout&date=${selectedDate}&search=${search}">
                        Cần Check-out
                    </a>
                </li>
            </ul>

            <div class="card">
                <div class="card-header">
                    <h4>
                        <c:choose>
                            <c:when test="${view == 'checkout'}">Danh sách
                                Booking cần Check-out</c:when>
                            <c:otherwise>Danh sách Booking cần
                                Check-in</c:otherwise>
                        </c:choose>
                    </h4>
                    <div class="card-header-action">
                        <a href="${pageContext.request.contextPath}/listRooms"
                            class="btn btn-success">
                            <i class="fas fa-plus"></i> Đặt phòng mới
                        </a>
                    </div>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Phòng</th>
                                    <th>Khách hàng</th>
                                    <th>
                                        <c:choose>
                                            <c:when
                                                test="${view == 'checkout'}">Check-out</c:when>
                                            <c:otherwise>Check-in</c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty bookingList}">
                                    <tr>
                                        <td colspan="5"
                                            class="text-center text-muted py-4">
                                            Không có booking nào phù hợp bộ lọc.
                                        </td>
                                    </tr>
                                </c:if>

                                    <c:forEach var="b" items="${bookingList}">
                                    <tr>
                                        <td>${b.room.roomNumber}</td>
                                        <td>${b.customer.fullName}</td>
                                        <td>
                                            <c:choose>
                                                <c:when
                                                    test="${view == 'checkout'}">
                                                    <fmt:formatDate
                                                        value="${b.checkOutDate}"
                                                        pattern="dd/MM/yyyy HH:mm" />
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatDate
                                                        value="${b.checkInDate}"
                                                        pattern="dd/MM/yyyy HH:mm" />
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${b.status}</td>
                                        <td>
                                            <c:choose>
                                                <c:when
                                                    test="${view == 'checkout'}">
                                                    <a
                                                        class="btn btn-sm btn-danger"
                                                        href="${pageContext.request.contextPath}/admin/checkout?bookingId=${b.bookingId}">
                                                        Check-out
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a
                                                        class="btn btn-sm btn-primary"
                                                        href="${pageContext.request.contextPath}/admin/checkin?code=${b.bookingCode}">
                                                        Check-in
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
