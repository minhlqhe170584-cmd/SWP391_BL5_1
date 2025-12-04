<%-- 
    Document   : home
    Description: Trang chủ Sona (Đã bỏ Booking Form Nhanh)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Trang Chủ | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>

<body>
    <div id="preloder">
        <div class="loader"></div>
    </div>

    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <section class="hero-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <div class="hero-text">
                        <h1>Smart Hotel Luxury</h1>
                        <p>Trải nghiệm nghỉ dưỡng đẳng cấp 5 sao với công nghệ thông minh. 
                           Đặt phòng dễ dàng, dịch vụ tận tâm chỉ với một chạm.</p>
                        <a href="${pageContext.request.contextPath}/rooms" class="primary-btn">Khám Phá Ngay</a>
                    </div>
                </div>
                </div>
        </div>
        
        <div class="hero-slider owl-carousel">
            <div class="hs-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/hero/hero-1.jpg"></div>
            <div class="hs-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/hero/hero-2.jpg"></div>
            <div class="hs-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/hero/hero-3.jpg"></div>
        </div>
    </section>
    <section class="aboutus-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <div class="about-text">
                        <div class="section-title">
                            <span>Về Chúng Tôi</span>
                            <h2>Smart Hotel <br />Hòa Lạc</h2>
                        </div>
                        <p class="f-para">SmartHotel.com là nền tảng đặt phòng trực tuyến hàng đầu. Chúng tôi đam mê du lịch và công nghệ. Mỗi ngày, chúng tôi truyền cảm hứng và tiếp cận hàng triệu du khách qua 90 trang web địa phương bằng 41 ngôn ngữ.</p>
                        <p class="s-para">Dù bạn tìm kiếm một kỳ nghỉ dưỡng sang trọng hay một chuyến công tác, chúng tôi luôn sẵn sàng phục vụ.</p>
                        <a href="#" class="primary-btn about-btn">Đọc Thêm</a>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="about-pic">
                        <div class="row">
                            <div class="col-sm-6">
                                <img src="${pageContext.request.contextPath}/assets/img/about/about-1.jpg" alt="">
                            </div>
                            <div class="col-sm-6">
                                <img src="${pageContext.request.contextPath}/assets/img/about/about-2.jpg" alt="">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <section class="services-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Tiện Ích</span>
                        <h2>Khám Phá Dịch Vụ</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-036-parking"></i>
                        <h4>Kế Hoạch Du Lịch</h4>
                        <p>Hỗ trợ lên lịch trình tour du lịch trọn gói.</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-033-dinner"></i>
                        <h4>Dịch Vụ Ăn Uống</h4>
                        <p>Nhà hàng 5 sao phục vụ tại phòng 24/7.</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-026-bed"></i>
                        <h4>Giữ Trẻ</h4>
                        <p>Dịch vụ trông trẻ chuyên nghiệp, an toàn.</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-024-towel"></i>
                        <h4>Giặt Là</h4>
                        <p>Dịch vụ giặt ủi, giặt khô lấy ngay.</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-044-clock-1"></i>
                        <h4>Thuê Xe</h4>
                        <p>Cung cấp xe đưa đón sân bay và xe tự lái.</p>
                    </div>
                </div>
                <div class="col-lg-4 col-sm-6">
                    <div class="service-item">
                        <i class="flaticon-012-cocktail"></i>
                        <h4>Quầy Bar</h4>
                        <p>Quầy Bar sân thượng với view toàn cảnh.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="hp-room-section">
        <div class="container-fluid">
            <div class="hp-room-items">
                <div class="row">
                    <div class="col-lg-3 col-md-6">
                        <div class="hp-room-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/room/room-b1.jpg">
                            <div class="hr-text">
                                <h3>Double Room</h3>
                                <h2>199$<span>/Đêm</span></h2>
                                <table>
                                    <tbody>
                                        <tr><td class="r-o">Size:</td><td>30 m2</td></tr>
                                        <tr><td class="r-o">Capacity:</td><td>Max 5</td></tr>
                                        <tr><td class="r-o">Bed:</td><td>King Beds</td></tr>
                                        <tr><td class="r-o">Services:</td><td>Wifi, TV...</td></tr>
                                    </tbody>
                                </table>
                                <a href="#" class="primary-btn">Chi Tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <div class="hp-room-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/room/room-b2.jpg">
                            <div class="hr-text">
                                <h3>Premium King</h3>
                                <h2>159$<span>/Đêm</span></h2>
                                <table>
                                    <tbody>
                                        <tr><td class="r-o">Size:</td><td>30 m2</td></tr>
                                        <tr><td class="r-o">Capacity:</td><td>Max 5</td></tr>
                                        <tr><td class="r-o">Bed:</td><td>King Beds</td></tr>
                                        <tr><td class="r-o">Services:</td><td>Wifi, TV...</td></tr>
                                    </tbody>
                                </table>
                                <a href="#" class="primary-btn">Chi Tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <div class="hp-room-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/room/room-b3.jpg">
                            <div class="hr-text">
                                <h3>Deluxe Room</h3>
                                <h2>198$<span>/Đêm</span></h2>
                                <table>
                                    <tbody>
                                        <tr><td class="r-o">Size:</td><td>30 m2</td></tr>
                                        <tr><td class="r-o">Capacity:</td><td>Max 5</td></tr>
                                        <tr><td class="r-o">Bed:</td><td>King Beds</td></tr>
                                        <tr><td class="r-o">Services:</td><td>Wifi, TV...</td></tr>
                                    </tbody>
                                </table>
                                <a href="#" class="primary-btn">Chi Tiết</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <div class="hp-room-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/room/room-b4.jpg">
                            <div class="hr-text">
                                <h3>Family Room</h3>
                                <h2>299$<span>/Đêm</span></h2>
                                <table>
                                    <tbody>
                                        <tr><td class="r-o">Size:</td><td>30 m2</td></tr>
                                        <tr><td class="r-o">Capacity:</td><td>Max 5</td></tr>
                                        <tr><td class="r-o">Bed:</td><td>King Beds</td></tr>
                                        <tr><td class="r-o">Services:</td><td>Wifi, TV...</td></tr>
                                    </tbody>
                                </table>
                                <a href="#" class="primary-btn">Chi Tiết</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="testimonial-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Đánh Giá</span>
                        <h2>Khách Hàng Nói Gì?</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-8 offset-lg-2">
                    <div class="testimonial-slider owl-carousel">
                        <div class="ts-item">
                            <p>Khách sạn tuyệt vời, sạch sẽ và hiện đại.</p>
                            <div class="ti-author">
                                <div class="rating">
                                    <i class="icon_star"></i><i class="icon_star"></i><i class="icon_star"></i><i class="icon_star"></i><i class="icon_star-half_alt"></i>
                                </div>
                                <h5> - Alexander Vasquez</h5>
                            </div>
                            <img src="${pageContext.request.contextPath}/assets/img/testimonial-logo.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="blog-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Tin Tức</span>
                        <h2>Blog & Sự Kiện</h2>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4">
                    <div class="blog-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/blog/blog-1.jpg">
                        <div class="bi-text">
                            <span class="b-tag">Du Lịch</span>
                            <h4><a href="#">Tremblant Tại Canada</a></h4>
                            <div class="b-time"><i class="icon_clock_alt"></i> 15th April, 2025</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="blog-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/blog/blog-2.jpg">
                        <div class="bi-text">
                            <span class="b-tag">Cắm Trại</span>
                            <h4><a href="#">Chọn Xe Nhà Lưu Động</a></h4>
                            <div class="b-time"><i class="icon_clock_alt"></i> 15th April, 2025</div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="blog-item set-bg" data-setbg="${pageContext.request.contextPath}/assets/img/blog/blog-3.jpg">
                        <div class="bi-text">
                            <span class="b-tag">Sự Kiện</span>
                            <h4><a href="#">Hẻm Núi Đồng</a></h4>
                            <div class="b-time"><i class="icon_clock_alt"></i> 21th April, 2025</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>

</html>