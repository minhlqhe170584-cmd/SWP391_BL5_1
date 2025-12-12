<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Đặt Xe: ${service.serviceName} | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Xác Nhận Đặt Xe</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/book-bike">Danh sách xe</a>
                            <span>${service.serviceName}</span>
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
                    
                    <div class="row mb-5">
                        <div class="col-lg-4">
                            <c:set var="imgUrl" value="${service.imageUrl}" />
                            <c:if test="${empty imgUrl}">
                                <c:set var="imgUrl" value="https://via.placeholder.com/150?text=No+Image" />
                            </c:if>
                            <img src="${imgUrl}" class="rounded shadow-sm" alt="Bike Img" 
                                 style="width: 100%; height: 150px; object-fit: cover;" 
                                 onerror="this.src='https://via.placeholder.com/150'">
                        </div>
                        <div class="col-lg-8 d-flex flex-column justify-content-center">
                            <h3 class="text-warning font-weight-bold">${service.serviceName}</h3>
                            <p class="text-muted">Vui lòng điền thông tin bên dưới để nhân viên chuẩn bị xe cho bạn.</p>
                            <a href="book-bike" class="text-primary"><i class="fa fa-exchange"></i> Đổi loại xe khác</a>
                        </div>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger shadow-sm text-center mb-4">
                            <i class="fa fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </c:if>

                    <form action="book-bike" method="POST" class="contact-form">
                        <input type="hidden" name="serviceId" value="${service.serviceId}">

                        <div class="row">
                            <div class="col-lg-6">
                                <label class="font-weight-bold">Chọn Gói Thuê:</label>
                                <select class="form-control" name="optionId" required style="height: 50px;">
                                    <c:forEach var="opt" items="${options}">
                                        <option value="${opt.itemId}">
                                            ${opt.optionName} - <fmt:formatNumber value="${opt.price}" type="currency" currencySymbol="VND"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-lg-6">
                                <label class="font-weight-bold">Số Lượng Xe:</label>
                                <input type="number" name="quantity" value="1" min="1" max="5" required>
                            </div>
                            
                            <div class="col-lg-12 mt-3">
                                <label class="font-weight-bold">Thời Gian Nhận Xe (Dự kiến):</label>
                                <input type="datetime-local" name="startTime" required>
                            </div>
                            
                            <div class="col-lg-12 mt-3">
                                <label class="font-weight-bold">Ghi Chú Cho Nhân Viên (Tùy chọn):</label>
                                <textarea name="note" placeholder="Ví dụ: Cần xe có giỏ, mũ bảo hiểm trẻ em..."></textarea>
                            </div>
                            
                            <div class="col-lg-12 text-center mt-4">
                                <button type="submit" class="primary-btn" style="width: 100%; border-radius: 5px;">
                                    <i class="fa fa-check-circle"></i> Xác Nhận Đặt Xe
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>