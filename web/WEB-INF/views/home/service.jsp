<%-- 
    Document   : services
    Description: Trang Dịch vụ (Laundry, Food, Bike, Decor)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Dịch Vụ | Smart Hotel</title>
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
                        <h2>Dịch Vụ Tiện Ích</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="services-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Trải Nghiệm Đẳng Cấp</span>
                        <h2>Khám Phá Dịch Vụ Của Chúng Tôi</h2>
                    </div>
                </div>
            </div>
            
            <div class="row">
                
                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <i class="flaticon-024-towel"></i>
                        <h4>Giặt Là Cao Cấp</h4>
                        <p>Dịch vụ giặt ủi, giặt khô lấy ngay trong ngày. Quần áo được chăm sóc kỹ lưỡng, hương thơm dịu nhẹ, giao nhận tận phòng.</p>
                        </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <i class="flaticon-033-dinner"></i>
                        <h4>Phục Vụ Đồ Ăn</h4>
                        <p>Thực đơn đa dạng từ Á sang Âu phục vụ 24/7. Thưởng thức bữa ăn nóng hổi, ngon miệng ngay tại không gian riêng tư.</p>
                        
                        <c:if test="${sessionScope.ROLE == 'ROOM'}">
                            <a href="${pageContext.request.contextPath}/order" class="primary-btn" style="padding: 10px 20px; font-size: 12px;">Gọi Món Ngay</a>
                        </c:if>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <i class="fa fa-bicycle" style="font-size: 60px; color: #e67e22; margin-bottom: 20px;"></i>
                        <h4>Thuê Xe Đạp</h4>
                        <p>Khám phá khuôn viên khách sạn và khu vực lân cận với dịch vụ thuê xe đạp địa hình, xe đạp đôi. Miễn phí mũ bảo hiểm.</p>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <i class="flaticon-012-cocktail"></i>
                        <h4>Trang Trí Phòng</h4>
                        <p>Setup phòng trăng mật, sinh nhật, kỷ niệm với nến, hoa tươi và rượu vang. Tạo nên những khoảnh khắc lãng mạn khó quên.</p>
                    </div>
                </div>

            </div>
        </div>
    </section>
    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>