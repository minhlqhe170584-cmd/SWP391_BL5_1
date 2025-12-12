<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Thuê Xe Đạp | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>

<body>
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Dịch Vụ Thuê Xe</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                            <a href="${pageContext.request.contextPath}/services">Dịch vụ</a>
                            <span>Thuê xe</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="room-details-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Lựa Chọn Phương Tiện</span>
                        <h2>Các Loại Xe Có Sẵn</h2>
                    </div>
                </div>
            </div>

            <div class="row">
                <c:if test="${empty bikeServices}">
                    <div class="col-12 text-center">
                        <div class="alert alert-warning">Hiện tại chưa có dịch vụ xe đạp nào khả dụng.</div>
                    </div>
                </c:if>

                <c:forEach var="s" items="${bikeServices}">
                    <div class="col-lg-4 col-md-6 mb-4">
                        <div class="room-item">
                            <img src="${s.imageUrl}" alt="${s.serviceName}" 
                                 style="height: 240px; object-fit: cover; width: 100%;" 
                                 onerror="this.src='https://via.placeholder.com/300x200?text=Bike+Image'">
                            <div class="ri-text">
                                <h4>${s.serviceName}</h4>
                                <h3>Giá ưu đãi<span>/giờ</span></h3>
                                <table>
                                    <tbody>
                                        <tr>
                                            <td class="r-o">Phù hợp:</td>
                                            <td>Du lịch, Dã ngoại</td>
                                        </tr>
                                        <tr>
                                            <td class="r-o">Tình trạng:</td>
                                            <td>Sẵn sàng</td>
                                        </tr>
                                    </tbody>
                                </table>
                                <a href="book-bike?serviceId=${s.serviceId}" class="primary-btn">Thuê Ngay</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-link text-secondary">
                    <i class="fa fa-arrow-left"></i> Quay về Trang chủ
                </a>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>