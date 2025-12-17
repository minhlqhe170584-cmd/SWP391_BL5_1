<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Xác Nhận Đặt Phòng | Smart Hotel</title>
    
    <%-- SỬA ĐƯỜNG DẪN Ở ĐÂY: Dùng ../ thay vì ../../ --%>
    <jsp:include page="../components/head.jsp"></jsp:include>
    
    <style>
        .booking-card { background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 5px 15px rgba(0,0,0,0.05); }
        .room-info { background: #f8f9fa; padding: 20px; border-radius: 8px; border: 1px solid #eee; }
        .price { color: #dfa974; font-size: 20px; font-weight: bold; }
        .btn-submit { background: #111; color: #fff; width: 100%; padding: 12px; font-weight: bold; border: none; cursor: pointer; transition: 0.3s; }
        .btn-submit:hover { background: #dfa974; color: #fff; }
        .form-control[readonly] { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <%-- Navbar --%>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Đặt Phòng</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Home</a>
                            <span>Booking</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <div class="booking-card">
                        <h4 class="mb-4">Thông tin đặt phòng</h4>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form action="booking" method="post">
                            <input type="hidden" name="action" value="client_booking">
                            <input type="hidden" name="roomId" value="${room.roomId}">

                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label>Họ tên</label>
                                    <input type="text" class="form-control" value="${sessionScope.USER.fullName}" readonly>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label>Số điện thoại</label>
                                    <input type="text" class="form-control" value="${sessionScope.USER.phone}" readonly>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label>Ngày Check-in</label>
                                    <input type="date" name="checkIn" id="checkIn" class="form-control" required>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label>Ngày Check-out</label>
                                    <input type="date" name="checkOut" id="checkOut" class="form-control" required>
                                </div>
                            </div>

                            <button type="submit" class="btn-submit mt-4">XÁC NHẬN ĐẶT PHÒNG</button>
                            <div class="text-center mt-3">
                                <a href="${pageContext.request.contextPath}/listRooms" class="text-muted">Quay lại danh sách</a>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="room-info">
                        <%-- Ảnh demo, bạn có thể thay bằng dynamic image nếu có --%>
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-1.jpg" class="img-fluid mb-3 rounded" alt="Room Image">
                        
                        <h5>${room.roomType.typeName}</h5>
                        <p>Phòng số: <span class="badge badge-warning text-white">#${room.roomNumber}</span></p>
                        <hr>
                        <ul class="list-unstyled">
                            <li><i class="fa fa-user"></i> Sức chứa: ${room.roomType.capacity} người</li>
                        </ul>
                        
                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span>Giá:</span>
                            <span class="price">
                                <fmt:formatNumber value="${room.roomType.basePriceWeekday}" pattern="#,###"/> đ/đêm
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <%-- Footer --%>
    <jsp:include page="../components/footer.jsp"></jsp:include>
    
    <script>
        // Script chặn ngày quá khứ
        const today = new Date().toISOString().split('T')[0];
        const checkInInput = document.getElementById('checkIn');
        const checkOutInput = document.getElementById('checkOut');

        checkInInput.setAttribute('min', today);
        
        checkInInput.addEventListener('change', function() {
            checkOutInput.setAttribute('min', this.value);
            // Nếu ngày checkout hiện tại nhỏ hơn ngày checkin mới -> xóa trắng để khách chọn lại
            if(checkOutInput.value && checkOutInput.value <= this.value) {
                checkOutInput.value = "";
            }
        });
    </script>
</body>
</html>