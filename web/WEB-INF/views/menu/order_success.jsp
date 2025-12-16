<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Đặt Thành Công | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Hoàn Tất</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="about-us-page spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <div class="about-text">
                        <div class="section-title">
                            <span class="text-success" style="font-size: 80px;"><i class="fa fa-check-circle"></i></span>
                            <h2>Cảm Ơn Quý Khách!</h2>
                        </div>
                        <p class="f-para">${message}</p>
                        <p class="s-para">Yêu cầu gọi món của quý khách đã được gửi đến bộ phận lễ tân.Rất hân hạnh được phục vụ.</p>
                        
                        <div class="mt-5">
                            <a href="order" class="primary-btn mr-3">Menu</a>
                            <a href="${pageContext.request.contextPath}/home" class="primary-btn" style="background: #333;">Về Trang Chủ</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>