<%-- 
    Document   : listRooms
    Description: Danh sách phòng (Đã bỏ hiển thị trạng thái & Cho phép đặt mọi lúc)
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Phòng Nghỉ | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>

    <style>
        /* Style nút mặc định (Đặt Ngay) */
        .btn-booking-custom {
            display: inline-block;
            font-size: 13px;
            font-weight: 500;
            padding: 12px 24px;
            color: #111;
            background: #f5f5f5;
            text-transform: uppercase;
            letter-spacing: 1px;
            border: 1px solid #e1e1e1;
            border-radius: 4px;
            transition: all 0.3s ease;
        }
        /* Hiệu ứng hover cho nút Đặt Ngay */
        .btn-booking-custom:not(.disabled):hover {
            background: #dfa974;
            color: #fff;
            border-color: #dfa974;
            text-decoration: none;
            box-shadow: 0 5px 15px rgba(223, 169, 116, 0.3);
            cursor: pointer;
        }

        /* Style nút Vô hiệu hóa (Role Room) */
        .btn-booking-custom.disabled {
            opacity: 0.6;
            cursor: not-allowed;
            pointer-events: none;
            background: #e9ecef;
            color: #495057;
        }

        /* Phân trang */
        .room-pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 50px;
            gap: 10px;
        }
        .room-pagination a {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 45px;
            height: 45px;
            background: #ffffff;
            border: 1px solid #ebebeb;
            color: #707070;
            font-size: 16px;
            font-weight: 500;
            border-radius: 50%;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        .room-pagination a:hover,
        .room-pagination a.active {
            background: #dfa974;
            color: #ffffff;
            border-color: #dfa974;
            box-shadow: 0 4px 10px rgba(223, 169, 116, 0.4);
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
                        <h2>Phòng Nghỉ</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                            <span>Danh sách phòng</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="rooms-section spad">
        <div class="container">
            <div class="row">
                
                <c:forEach items="${listRooms}" var="r" varStatus="vs">
                    <div class="col-lg-4 col-md-6">
                        <div class="room-item">
                            <c:set var="imgIndex" value="${vs.index % 6 + 1}" />
                            <img src="${pageContext.request.contextPath}/assets/img/room/room-${imgIndex}.jpg" alt="Room Image">
                            
                            <div class="ri-text">
                                <h4>${r.roomType.typeName} <span style="font-size: 0.7em; color: #999;">#${r.roomNumber}</span></h4>
                                <h3>
                                    <fmt:formatNumber value="${r.roomType.basePriceWeekday}" type="currency" currencySymbol="đ"/>
                                    <span>/đêm</span>
                                </h3>
                                
                                <table>
                                    <tbody>
                                        <tr>
                                            <td class="r-o">Sức chứa:</td>
                                            <td>${r.roomType.capacity} người</td>
                                        </tr>
                                        <tr>
                                            <td class="r-o">Mô tả:</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${r.roomType.description.length() > 25}">
                                                        ${r.roomType.description.substring(0, 25)}...
                                                    </c:when>
                                                    <c:otherwise>${r.roomType.description}</c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <%-- ĐÃ XÓA PHẦN HIỂN THỊ TRẠNG THÁI Ở ĐÂY --%>
                                    </tbody>
                                </table>
                                
                                <c:choose>
                                    <%-- 1. Nếu là ROLE ROOM -> Nút Disabled (Chỉ xem) --%>
                                    <c:when test="${sessionScope.ROLE == 'ROOM'}">
                                        <a href="#" class="btn-booking-custom disabled">Chi Tiết</a>
                                    </c:when>

                                    <%-- 2. Nếu là CUSTOMER / GUEST -> Luôn hiện nút Đặt Ngay --%>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/booking?roomId=${r.roomId}" class="btn-booking-custom">
                                            Đặt Ngay
                                        </a>
                                    </c:otherwise>
                                </c:choose>

                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty listRooms}">
                    <div class="col-12 text-center py-5">
                        <h4>Hiện tại chưa có phòng nào trong hệ thống.</h4>
                    </div>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <div class="col-lg-12">
                        <div class="room-pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="listRooms?page=${currentPage - 1}"><i class="fa fa-angle-left"></i></a>
                            </c:if>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="listRooms?page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <a href="listRooms?page=${currentPage + 1}"><i class="fa fa-angle-right"></i></a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
                
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        // Hiển thị thông báo thành công (Booking Success)
        <c:if test="${not empty sessionScope.bookingSuccess}">
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: '${sessionScope.bookingSuccess}',
                confirmButtonColor: '#dfa974',
                confirmButtonText: 'Tiếp tục đặt phòng'
            });
            <c:remove var="bookingSuccess" scope="session"/>
        </c:if>

        // Hiển thị thông báo lỗi (Nếu có)
        <c:if test="${not empty sessionScope.errorMessage}">
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: '${sessionScope.errorMessage}',
                confirmButtonColor: '#d33'
            });
            <c:remove var="errorMessage" scope="session"/>
        </c:if>
    </script>
</body>
</html>