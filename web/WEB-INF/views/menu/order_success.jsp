<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zxx">
<head>
    <title>Đặt hàng Thành công | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>
<body>
    <div id="preloder"><div class="loader"></div></div>
    <jsp:include page="../components/navbar.jsp"></jsp:include>
        
    <div class="container text-center py-5" style="min-height: 50vh;">
        <h1 class="text-success mb-4 mt-5">✅ ĐẶT HÀNG THÀNH CÔNG</h1>
        <p class="lead">Cảm ơn quý khách. Đơn hàng của quý khách đã được ghi nhận.</p>
        <p>Bộ phận phục vụ sẽ giao hàng đến phòng ${param.roomNumber} trong thời gian sớm nhất.</p>
        <a href="${pageContext.request.contextPath}/order" class="primary-btn btn-lg mt-4">Tiếp tục đặt món</a>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-lg mt-4">Về trang chủ</a>
    </div>
        
    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>