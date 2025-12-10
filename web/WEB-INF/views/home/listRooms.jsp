<%-- 
    Document   : listRooms
    Description: Trang Danh sách phòng (Đã bỏ nút Chi Tiết)
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
    <title>Phòng Nghỉ | Smart Hotel</title>
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
                
                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-1.jpg" alt="">
                        <div class="ri-text">
                            <h4>Premium King Room</h4>
                            <h3>1.500.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>30 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 3 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>King Beds</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, TV, Bồn tắm...</td></tr>
                                </tbody>
                            </table>
                            </div>
                    </div>
                </div>
                
                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-2.jpg" alt="">
                        <div class="ri-text">
                            <h4>Deluxe Room</h4>
                            <h3>2.000.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>45 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 5 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>2 King Beds</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, View biển...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-3.jpg" alt="">
                        <div class="ri-text">
                            <h4>Double Room</h4>
                            <h3>1.200.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>25 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 2 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>1 Queen Bed</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, Tủ lạnh...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-4.jpg" alt="">
                        <div class="ri-text">
                            <h4>Luxury Room</h4>
                            <h3>1.800.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>35 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 2 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>King Beds</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, Bồn tắm...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-5.jpg" alt="">
                        <div class="ri-text">
                            <h4>Room With View</h4>
                            <h3>1.600.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>30 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 2 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>King Beds</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, Ban công...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-4 col-md-6">
                    <div class="room-item">
                        <img src="${pageContext.request.contextPath}/assets/img/room/room-6.jpg" alt="">
                        <div class="ri-text">
                            <h4>Small View</h4>
                            <h3>1.300.000đ<span>/đêm</span></h3>
                            <table>
                                <tbody>
                                    <tr><td class="r-o">Diện tích:</td><td>20 m2</td></tr>
                                    <tr><td class="r-o">Sức chứa:</td><td>Max 2 người</td></tr>
                                    <tr><td class="r-o">Giường:</td><td>Queen Bed</td></tr>
                                    <tr><td class="r-o">Tiện ích:</td><td>Wifi, TV...</td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </section>
    <jsp:include page="../components/footer.jsp"></jsp:include>
    
</body>
</html>