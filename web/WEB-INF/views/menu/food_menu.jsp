<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Menu Đồ Ăn & Đồ Uống | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"></jsp:include>

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">

        <style>
            /* --- Custom Styles cho Menu đẹp hơn --- */
            .menu-section {
                padding-top: 50px;
                padding-bottom: 80px;
                background-color: #fdfdfd; /* Nền sáng nhẹ */
            }
            .section-title h2 {
                font-weight: 700;
                color: #333;
                margin-bottom: 10px;
            }
            .section-title p {
                font-style: italic;
                color: #777;
            }

            /* Card món ăn */
            .menu-item-inner {
                background: #fff;
                border-radius: 10px;
                padding: 15px;
                border: 1px solid #eee;
                box-shadow: 0 2px 5px rgba(0,0,0,0.05);
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: space-between;
                height: 100%;
            }
            .menu-item-inner:hover {
                transform: translateY(-3px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                border-color: #dfa974; /* Màu cam chủ đạo của hotel */
            }

            /* Ảnh món ăn */
            .service-image {
                width: 90px;
                height: 90px;

                /* THÊM 3 DÒNG NÀY */
                min-width: 90px;       /* Bắt buộc giữ chiều rộng tối thiểu, không cho co lại */
                min-height: 90px;      /* Bắt buộc giữ chiều cao */
                background-color: #eee; /* Nếu ảnh lỗi thì hiện nền màu xám nhạt cho đẹp */

                object-fit: cover;
                border-radius: 8px;
                border: 1px solid #f0f0f0;
                margin-right: 20px;
                flex-shrink: 0;/* Không bị co lại trên mobile */
            }

            /* Thông tin món ăn */
            .food-info h5 {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 5px;
                color: #19191a;
            }
            .food-desc {
                font-size: 13px;
                color: #888;
                margin-bottom: 8px;
                display: -webkit-box;
                -webkit-line-clamp: 2; /* Giới hạn 2 dòng mô tả */
                -webkit-box-orient: vertical;
                overflow: hidden;
            }
            .food-price {
                font-size: 16px;
                font-weight: 700;
                color: #e74c3c; /* Màu đỏ cho giá */
            }

            /* Input số lượng */
            .quantity-wrapper {
                text-align: center;
            }
            .quantity-label {
                font-size: 11px;
                color: #999;
                margin-bottom: 2px;
                display: block;
            }
            .quantity-input {
                width: 60px;
                height: 40px;
                text-align: center;
                border: 2px solid #eee;
                border-radius: 6px;
                font-weight: bold;
                font-size: 16px;
                color: #333;
            }
            .quantity-input:focus {
                border-color: #dfa974;
                outline: none;
            }

            /* Badge (Nhãn Chay/Cồn) */
            .custom-badge {
                font-size: 11px;
                padding: 3px 8px;
                border-radius: 4px;
                vertical-align: middle;
                margin-left: 5px;
                font-weight: normal;
                letter-spacing: 0.5px;
            }
            .badge-veg {
                background-color: #e8f5e9;
                color: #2e7d32;
                border: 1px solid #c8e6c9;
            }
            .badge-alcohol {
                background-color: #fff3e0;
                color: #ef6c00;
                border: 1px solid #ffe0b2;
            }

            /* Form nhập thông tin */
            .order-form-box {
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 20px rgba(0,0,0,0.08);
                border-top: 4px solid #dfa974;
            }
            .btn-confirm {
                background-color: #dfa974;
                color: white;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 1px;
                border: none;
                transition: 0.3s;
            }
            .btn-confirm:hover {
                background-color: #c98d50;
                color: white;
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
                                <h2>Thực Đơn Tại Phòng</h2>
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
                <div class="row mb-5">
                    <div class="col-lg-12 text-center">
                        <div class="section-title">
                            <h2>Thưởng Thức Ẩm Thực</h2>
                            <p>Chọn món yêu thích và chúng tôi sẽ phục vụ tận phòng ngay lập tức.</p>
                        </div>
                    </div>
                </div>

                <%-- Thông báo lỗi --%>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger text-center font-weight-bold" role="alert">
                        ${sessionScope.errorMessage}
                    </div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <form action="checkout-order" method="POST">

                    <div class="d-flex align-items-center mb-4 mt-2">
                        <span style="font-size: 24px; margin-right: 10px;">🍔</span>
                        <h3 class="m-0 font-weight-bold text-dark">Món Ăn Hấp Dẫn</h3>
                    </div>

                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.foodList}">
                                <c:forEach var="food" items="${requestScope.foodList}">
                                    <div class="col-lg-6 col-md-12 mb-4">
                                        <div class="menu-item-inner">
                                            <div class="d-flex align-items-center" style="flex: 1;">
                                                <img src="${pageContext.request.contextPath}/uploads/${food.imageUrl}" 
                                                     alt="${food.name}" 
                                                     class="service-image"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/uploads/default.jpg';">
                                                     <div class="food-info pr-2">
                                                    <h5>
                                                        ${food.name}
                                                        <c:if test="${food.isVegetarian}">
                                                            <span class="custom-badge badge-veg">CHAY</span>
                                                        </c:if>
                                                    </h5>
                                                    <p class="food-desc" title="${food.description}">${food.description}</p>
                                                    <p class="food-price">
                                                        <fmt:formatNumber value="${food.price}" type="currency" currencyCode="USD" maxFractionDigits="0"/>
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="quantity-wrapper">
                                                <span class="quantity-label">Số lượng</span>
                                                <input type="number" 
                                                       name="item_serviceId_${food.foodId}" 
                                                       value="0" 
                                                       min="0" 
                                                       class="form-control quantity-input">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-12 text-center py-5">
                                    <p class="text-muted">Hiện chưa có món ăn nào.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <hr class="my-5">

                    <div class="d-flex align-items-center mb-4">
                        <span style="font-size: 24px; margin-right: 10px;">🍹</span>
                        <h3 class="m-0 font-weight-bold text-dark">Đồ Uống Giải Khát</h3>
                    </div>

                    <div class="row">
                        <c:choose>
                            <c:when test="${not empty requestScope.drinkList}">
                                <c:forEach var="drink" items="${requestScope.drinkList}">
                                    <div class="col-lg-6 col-md-12 mb-4">
                                        <div class="menu-item-inner">
                                            <div class="d-flex align-items-center" style="flex: 1;">
                                                <img src="${pageContext.request.contextPath}/uploads/${drink.imageUrl}" 
                                                     alt="${drink.name}" 
                                                     class="service-image"
                                                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/uploads/default.jpg';">

                                                     <div class="food-info pr-2">
                                                    <h5>
                                                        ${drink.name}
                                                        <c:if test="${drink.isAlcoholic}">
                                                            <span class="custom-badge badge-alcohol">CÓ CỒN</span>
                                                        </c:if>
                                                    </h5>
                                                    <p class="food-desc">Dung tích: ${drink.volumeMl} ml</p>
                                                    <p class="food-price">
                                                        <fmt:formatNumber value="${drink.price}" type="currency" currencyCode="USD" maxFractionDigits="0"/>
                                                    </p>
                                                </div>
                                            </div>

                                            <div class="quantity-wrapper">
                                                <span class="quantity-label">Số lượng</span>
                                                <input type="number" 
                                                       name="item_serviceId_${drink.drinkId}" 
                                                       value="0" 
                                                       min="0" 
                                                       class="form-control quantity-input">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="col-12 text-center py-5">
                                    <p class="text-muted">Hiện chưa có đồ uống nào.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="row justify-content-center mt-5">
                        <div class="col-lg-10">
                            <div class="order-form-box">
                                <h4 class="mb-4 text-center font-weight-bold">Xác Nhận Đặt Hàng</h4>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="roomNumber" class="font-weight-bold">Số phòng của bạn <span class="text-danger">*</span></label>
                                            <input type="text" id="roomNumber" name="roomNumber" class="form-control form-control-lg" placeholder="Ví dụ: 101" required style="font-size: 14px;">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="note" class="font-weight-bold">Ghi chú thêm</label>
                                            <textarea id="note" name="note" class="form-control" rows="1" placeholder="Ít đá, không hành..." style="font-size: 14px; height: 48px;"></textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center mt-4">
                                    <button type="submit" class="btn btn-confirm btn-lg px-5 py-3 shadow">
                                        <i class="fa fa-paper-plane mr-2"></i> Gửi Yêu Cầu
                                    </button>
                                </div>
                            </div>
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