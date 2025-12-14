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
                        <img src="https://www.frasershospitality.com/en/united-kingdom/london/fraser-suites-queensgate-london/facilities/laundry-suite/_jcr_content/root/container/column_controller_11/column-1-wrapper/image_copy.coreimg.jpeg/1732549991161/laundry1.jpeg" alt="Giặt là">
                        
                        <i class="flaticon-024-towel"></i>
                        <h4>Giặt Là Cao Cấp</h4>
                        <p>Dịch vụ giặt ủi, giặt khô lấy ngay trong ngày. Quần áo được chăm sóc kỹ lưỡng, hương thơm dịu nhẹ.</p>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <img src="https://images.unsplash.com/photo-1525610553991-2bede1a236e2?q=80&w=400&auto=format&fit=crop" alt="Đồ ăn">
                        
                        <i class="flaticon-033-dinner"></i>
                        <h4>Phục Vụ Đồ Ăn</h4>
                        <p>Thực đơn đa dạng từ Á sang Âu phục vụ 24/7. Thưởng thức bữa ăn nóng hổi, ngon miệng ngay tại phòng.</p>
                        
                        <a href="${pageContext.request.contextPath}/order" class="primary-btn" style="padding: 10px 20px; margin-top: 10px; width: 100%;">Gọi Món Ngay</a>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <img src="https://images.unsplash.com/photo-1507035895480-2b3156c31fc8?q=80&w=400&auto=format&fit=crop" alt="Thuê xe">
                        
                        <i class="fa fa-bicycle" style="font-size: 60px; color: #e67e22; margin-bottom: 20px;"></i>
                        <h4>Thuê Xe Đạp</h4>
                        <p>Khám phá khuôn viên khách sạn và khu vực lân cận với dịch vụ thuê xe đạp địa hình, xe đạp đôi.</p>
                        
                        <a href="${pageContext.request.contextPath}/book-bike" class="primary-btn" style="padding: 10px 20px; margin-top: 10px; width: 100%;">Thuê Xe Ngay</a>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <img src="https://www.vietnambooking.com/wp-content/uploads/2023/08/to-chuc-su-kien-1.jpg" alt="Tổ chức sự kiện">
                        
                        <i class="flaticon-012-cocktail"></i>
                        <h4>Tổ chức sự kiện</h4>
                        <p>Tổ chức trọn gói Tiệc cưới, Sinh nhật và Hội nghị. Sảnh tiệc sang trọng, thực đơn đa dạng và kịch bản chuyên nghiệp.</p>
                        
                        <a href="${pageContext.request.contextPath}/book-bike" class="primary-btn" style="padding: 10px 20px; margin-top: 10px; width: 100%;">Đặt Phòng Ngay</a>
                    </div>
                </div>

            </div>
        </div>
    </section>
    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>