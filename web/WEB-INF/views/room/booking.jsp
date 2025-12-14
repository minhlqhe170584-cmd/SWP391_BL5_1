<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đặt Phòng | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Xác Nhận Đặt Phòng</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="room-details-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-6">
                    <div class="room-details-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-1.jpg" alt="" class="mb-4 rounded">
                        <div class="rd-text">
                            <div class="rd-title">
                                <h3>Phòng ${room.roomNumber}</h3>
                                <div class="rdt-right">
                                    <div class="rating">
                                        <i class="icon_star"></i><i class="icon_star"></i><i class="icon_star"></i><i class="icon_star"></i><i class="icon_star-half_alt"></i>
                                    </div>
                                </div>
                            </div>
                            <h2><fmt:formatNumber value="${room.roomType.basePriceWeekday}" type="currency" currencySymbol="đ"/><span>/Đêm</span></h2>
                            <table>
                                <tbody>
                                    <tr>
                                        <td class="r-o">Loại phòng:</td>
                                        <td>${room.roomType.typeName}</td>
                                    </tr>
                                    <tr>
                                        <td class="r-o">Sức chứa:</td>
                                        <td>${room.roomType.capacity} người</td>
                                    </tr>
                                    <tr>
                                        <td class="r-o">Mô tả:</td>
                                        <td>${room.roomType.description}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="booking-form-warp" style="background: #f9f9f9; padding: 30px; border-radius: 5px;">
                        <h3 class="mb-4">Thông Tin Đặt Phòng</h3>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form action="booking" method="post">
                            <input type="hidden" name="roomId" value="${room.roomId}">
                            
                            <div class="form-group">
                                <label for="guestName">Khách hàng:</label>
                                <input type="text" class="form-control" value="${sessionScope.USER.fullName}" readonly style="background-color: #e9ecef;">
                            </div>

                            <div class="form-group">
                                <label for="date-in">Ngày nhận phòng (Check-in):</label>
                                <input type="date" name="checkIn" id="date-in" class="form-control" required>
                            </div>

                            <div class="form-group">
                                <label for="date-out">Ngày trả phòng (Check-out):</label>
                                <input type="date" name="checkOut" id="date-out" class="form-control" required>
                            </div>

                            <div class="form-group mt-4">
                                <button type="submit" class="btn btn-primary btn-block" style="background-color: #dfa974; border: none; padding: 12px;">XÁC NHẬN ĐẶT PHÒNG</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
    
    <script>
        var today = new Date().toISOString().split('T')[0];
        document.getElementById("date-in").setAttribute('min', today);
        document.getElementById("date-out").setAttribute('min', today);
    </script>
</body>
</html>