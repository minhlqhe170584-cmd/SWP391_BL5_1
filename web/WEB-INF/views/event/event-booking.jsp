<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Đặt Sự Kiện | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"/>
    </head>

    <body>
        <jsp:include page="../components/navbar.jsp"/>

        <div class="breadcrumb-section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="breadcrumb-text">
                            <h2>Xác Nhận Đặt Sự Kiện</h2>
                            <div class="bt-option">
                                <a href="${pageContext.request.contextPath}/event-booking">Danh sách Sự Kiện</a>
                                <span>Thuê Sự Kiện</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <section class="contact-section spad">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="font-weight-bold mb-3">1. Chọn sự kiện</h5>

                                <c:if test="${empty events}">
                                    <div class="alert alert-warning">
                                        Chưa có sự kiện nào được tải lên.
                                    </div>
                                </c:if>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger text-center mb-4">
                                        <i class="fa fa-exclamation-circle"></i>
                                        ${errorMessage}
                                    </div>
                                </c:if>

                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success text-center mb-4">
                                        <i class="fa fa-check-circle"></i>
                                        ${successMessage}
                                    </div>
                                </c:if>


                                <form method="get" action="${pageContext.request.contextPath}/event-booking">
                                    <select name="eventId"
                                            class="form-control"
                                            onchange="this.form.submit()">
                                        <option value="">-- Chọn sự kiện --</option>

                                        <c:forEach var="e" items="${events}">
                                            <option value="${e.eventId}"
                                                    <c:if test="${event != null && event.eventId == e.eventId}">
                                                        selected
                                                    </c:if>>
                                                ${e.eventName} - 
                                                <fmt:formatNumber value="${e.pricePerTable}"
                                                                  type="currency"
                                                                  currencySymbol="VND"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </div>
                        </div>
                        <!-- Thông tin sự kiện -->
                        <c:if test="${not empty event}">

                            <div class="card mb-4">
                                <div class="card-body">
                                    <h5 class="font-weight-bold text-warning mb-2">
                                        2. Thông tin sự kiện
                                    </h5>
                                    <p><strong>Tên sự kiện:</strong> ${event.eventName}</p>
                                    <p>
                                        <strong>Giá: </strong>
                                        <fmt:formatNumber value="${event.pricePerTable}"
                                                          type="currency"
                                                          currencySymbol="VND"/>
                                    </p>
                                </div>
                            </div>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">
                                    ${errorMessage}
                                </div>
                            </c:if>

                            <div class="card">
                                <div class="card-body">
                                    <h5 class="font-weight-bold mb-3">3. Đặt sự kiện</h5>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/event-booking">

                                        <input type="hidden" name="eventId" value="${event.eventId}"/>
                                        <div class="form-group">
                                            <label>Chọn phòng</label>

                                            <c:forEach var="rid" items="${roomIds}">
                                                <div class="form-check">
                                                    <input type="checkbox" name="roomIds" value="${rid}">
                                                    Phòng ${rid}
                                                </div>
                                            </c:forEach>
                                        </div>

                                        <div class="form-group">
                                            <label>Ngày tổ chức</label>
                                            <input type="date" name="checkInDate"
                                                   class="form-control" required>
                                        </div>

                                        <div class="form-group">
                                            <label>Ngày kết thúc</label>
                                            <input type="date" name="checkOutDate"
                                                   class="form-control" required>
                                        </div>

                                        <div class="form-group">
                                            <label>Ghi chú</label>
                                            <textarea name="message" class="form-control" rows="4" required>${param.note}</textarea>
                                        </div>

                                        <button type="submit"
                                                class="btn btn-warning btn-block">
                                            Xác nhận đặt sự kiện
                                        </button>
                                    </form>
                                </div>
                            </div>

                        </c:if>


                    </div>
                </div>
            </div>
        </section>

        <jsp:include page="../components/footer.jsp"/>
    </body>
</html>
