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
                                        <option value="${opt.itemId}" <c:if test="${opt.itemId == param.optionId}">selected</c:if>>
                                            ${opt.optionName} - <fmt:formatNumber value="${opt.price}" type="currency" currencySymbol="VND"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-lg-6">
                                <label class="font-weight-bold">Số Lượng Xe:</label>
                                <input type="number" name="quantity" value="${param.quantity != null ? param.quantity : 1}" min="1" max="5" required>
                            </div>
                            
                            <div class="col-lg-12 mt-3">
                                <label class="font-weight-bold">Thời Gian Nhận Xe (Dự kiến):</label>
                                <input type="datetime-local" name="startTime" value="${param.startTime}" required>
                            </div>
                            
                            <div class="col-lg-12 mt-4 text-center">
                                <button type="submit" name="action" value="check" class="btn btn-info btn-lg mr-2" style="border-radius: 5px;">
                                    <i class="fa fa-search"></i> Kiểm Tra Còn Xe Không
                                </button>
                            </div>

                            <c:if test="${not empty checkResult}">
                                <div class="col-lg-12 mt-4">
                                    <div class="card bg-light border-0">
                                        <div class="card-body p-3">
                                            <h6 class="text-primary font-weight-bold text-center"><i class="fa fa-clock-o"></i> Tình trạng xe trong khung giờ bạn chọn:</h6>
                                            <table class="table table-sm table-bordered bg-white mt-2 mb-0 text-center">
                                                <thead class="thead-light">
                                                    <tr>
                                                        <th>Khung giờ</th>
                                                        <th>Số xe còn trống</th>
                                                        <th>Trạng thái</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:set var="canBook" value="true" />
                                                    <c:forEach var="slot" items="${checkResult}">
                                                        <tr class="${slot.availableQty < requestQty ? 'table-danger' : 'table-success'}">
                                                            <td>${slot.timeRange}</td>
                                                            <td><strong>${slot.availableQty}</strong></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${slot.availableQty == 0}">
                                                                        <span class="badge badge-danger">Hết xe</span>
                                                                        <c:set var="canBook" value="false" />
                                                                    </c:when>
                                                                    <c:when test="${slot.availableQty < requestQty}">
                                                                        <span class="badge badge-warning">Không đủ</span>
                                                                        <c:set var="canBook" value="false" />
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge badge-success">Sẵn sàng</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            
                                            <div class="text-center mt-3">
                                                <c:choose>
                                                    <c:when test="${canBook}">
                                                        <button type="submit" name="action" value="book" class="primary-btn" style="width: 100%; border-radius: 5px;">
                                                            <i class="fa fa-check-circle"></i> Xác Nhận Đặt Xe Ngay
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="alert alert-warning mb-0">Không đủ xe trong khung giờ này. Vui lòng chọn giờ khác hoặc giảm số lượng.</div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="col-lg-12 mt-3">
                                <label class="font-weight-bold">Ghi Chú Cho Nhân Viên (Tùy chọn):</label>
                                <textarea name="note" placeholder="Ví dụ: Cần xe có giỏ, mũ bảo hiểm trẻ em...">${param.note}</textarea>
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