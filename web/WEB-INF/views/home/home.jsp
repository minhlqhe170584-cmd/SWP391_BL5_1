<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Trang Chủ - Smart Hotel</title>
        <jsp:include page="../components/header.jsp"></jsp:include>
    </head>
    <body>
        
        <jsp:include page="../components/navbar.jsp"></jsp:include>

        <div class="container mt-5 text-center">
            <h1 class="text-warning">TEST GIAO DIỆN THÀNH CÔNG!</h1>
            <p>Nếu bạn thấy Menu ở trên và Footer ở dưới màu đen, nghĩa là đã ghép code chuẩn.</p>
            <br>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Thử bấm Đăng nhập</a>
        </div>

        <jsp:include page="../components/footer.jsp"></jsp:include>
        
    </body>
</html>