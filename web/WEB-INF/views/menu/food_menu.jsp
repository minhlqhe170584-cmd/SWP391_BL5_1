<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zxx">
    <head>
        <title>Menu Đồ Ăn & Đồ Uống | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>
        
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">

        <style>
            .menu-section {
                padding-top: 50px;
                padding-bottom: 80px;
            }
            .menu-item-inner {
                border-radius: 8px;
                transition: background-color 0.3s;
            }
            .menu-item-inner:hover {
                background-color: #f7f7f7;
            }
            .quantity-input {
                border-radius: 5px;
                border: 1px solid #ccc;
            }
            .service-image {
                width: 80px;
                height: 80px;
                object-fit: cover;
                border-radius: 5px;
                border: 1px solid #ddd;
                margin-right: 15px;
            }
        </style>
    </head>
    <body>
        <div id="preloder"><div class="loader"></div></div>
        <jsp:include page="../components/navbar.jsp"></jsp:include>

        <div class="breadcrumb-section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="breadcrumb-text">
                            <h2>Thực Đơn Phục Vụ Tại Phòng</h2>
                            <div class="bt-option">
                                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                                <span>Menu</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <section class="menu-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>Chọn món và Xác nhận đặt hàng</h2>
                            <p>Vui lòng nhập số lượng (tối thiểu 1) cho các món bạn muốn đặt.</p>
                        </div>
                    </div>
                </div>

                <%-- Hiển thị thông báo lỗi nếu có --%>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div style="color: red; font-weight: bold; padding: 10px; border: 1px solid red; margin-bottom: 20px;">
                        ${sessionScope.errorMessage}
                    </div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <form action="checkout-order" method="POST">
                    
                    <h3 class="mt-4 mb-3">🍔 Đồ Ăn (Foods)</h3>
                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.foodList}">
                                <c:forEach var="food" items="${requestScope.foodList}">
                                    <div class="col-lg-6 menu-item">
                                        <div class="menu-item-inner d-flex justify-content-between align-items-center mb-4 p-3 border rounded shadow-sm">
                                            
                                            <div class="menu-item-left d-flex align-items-center">
                                                <img src="${pageContext.request.contextPath}/uploads/${food.imageUrl}" 
                                                     alt="${food.name}" 
                                                     class="service-image"
                                                     onerror="this.src='${pageContext.request.contextPath}/uploads/default.jpg'">
                                                
                                                <div>
                                                    <h5 class="mb-0">${food.name} 
                                                        <c:if test="${food.isVegetarian}"> 
                                                            <span class="badge badge-success" style="background-color: #28a745; color: white;">CHAY</span>
                                                        </c:if>
                                                    </h5>
                                                    <p class="text-muted mb-0">${food.description}</p>
                                                    <p class="font-weight-bold text-danger mb-0">
                                                        <fmt:formatNumber value="${food.price}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="menu-item-right">
                                                <input type="number" 
                                                       name="item_serviceId_${food.serviceId}" 
                                                       value="0" 
                                                       min="0" 
                                                       class="form-control quantity-input" 
                                                       style="width: 80px; text-align: center;">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-lg-12 text-center text-muted">Không có món ăn nào đang hoạt động.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <hr>

                    <h3 class="mt-4 mb-3">🍹 Đồ Uống (Drinks)</h3>
                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.drinkList}">
                                <c:forEach var="drink" items="${requestScope.drinkList}">
                                    <div class="col-lg-6 menu-item">
                                        <div class="menu-item-inner d-flex justify-content-between align-items-center mb-4 p-3 border rounded shadow-sm">
                                            
                                            <div class="menu-item-left d-flex align-items-center">
                                                <img src="${pageContext.request.contextPath}/uploads/${drink.imageUrl}" 
                                                     alt="${drink.name}" 
                                                     class="service-image"
                                                     onerror="this.src='${pageContext.request.contextPath}/uploads/default.jpg'">
                                                
                                                <div>
                                                    <h5 class="mb-0">${drink.name} 
                                                        <c:if test="${drink.isAlcoholic}"> 
                                                            <span class="badge badge-warning" style="background-color: #ffc107; color: black;">CÓ CỒN</span>
                                                        </c:if>
                                                    </h5>
                                                    <p class="text-muted mb-0">(${drink.volumeMl} ml)</p>
                                                    <p class="font-weight-bold text-danger mb-0">
                                                        <fmt:formatNumber value="${drink.price}" type="currency" currencyCode="VND" maxFractionDigits="0"/>
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="menu-item-right">
                                                <input type="number" 
                                                       name="item_serviceId_${drink.serviceId}" 
                                                       value="0" 
                                                       min="0" 
                                                       class="form-control quantity-input" 
                                                       style="width: 80px; text-align: center;">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-lg-12 text-center text-muted">Không có đồ uống nào đang hoạt động.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <hr class="mt-5">

                    <div class="row mt-5 p-4 border rounded bg-light">
                        <div class="col-lg-12">
                            <h4 class="mb-4">Chi tiết Đặt hàng</h4>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="roomNumber" class="font-weight-bold">Số phòng:</label>
                                <input type="text" id="roomNumber" name="roomNumber" class="form-control" placeholder="Ví dụ: 101" required>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="note" class="font-weight-bold">Ghi chú (Tùy chọn):</label>
                                <textarea id="note" name="note" class="form-control" rows="3" placeholder="Yêu cầu đặc biệt..."></textarea>
                            </div>
                        </div>
                        <div class="col-lg-12 d-flex justify-content-end mt-4">
                            <button type="submit" class="primary-btn btn-lg" style="padding: 15px 50px;">XÁC NHẬN ĐẶT HÀNG</button>
                        </div>
                    </div>

                </form>
            </div>
        </section>

        <jsp:include page="../components/footer.jsp"></jsp:include>
        
        <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </body>
</html>