<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Hồ Sơ | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    
    <style>
        .profile-card { 
            background: #fff; 
            padding: 40px; 
            border-radius: 15px; 
            box-shadow: 0 10px 30px rgba(0,0,0,0.1); 
            margin-bottom: 50px; 
        }
        .profile-header {
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .profile-header h3 { 
            color: #e67e22; 
            font-weight: 700; 
            margin: 0;
        }
        
        .table-history th { background-color: #f8f9fa; color: #666; font-weight: 600; text-transform: uppercase; font-size: 0.9rem; }
        
        /* Box hiển thị tổng tiền (đã đổi màu cho nhẹ nhàng hơn) */
        .total-box {
            background: #e3f2fd; /* Màu xanh nhẹ */
            color: #0d47a1;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
            margin-bottom: 30px;
            border: 1px solid #bbdefb;
        }
        .total-amount { font-size: 2rem; font-weight: bold; color: #0277bd; }
    </style>
</head>
<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>
    
    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Hồ Sơ</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                            <span>
                                <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">Thông tin cá nhân</c:if>
                                <c:if test="${sessionScope.ROLE == 'ROOM'}">Nhật ký dịch vụ</c:if>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="spad">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="profile-card">
                        
                        <c:if test="${not empty message}">
                            <div class="alert alert-success alert-dismissible fade show">
                                <i class="fa fa-check-circle"></i> ${message}
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                            </div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show">
                                <i class="fa fa-exclamation-circle"></i> ${error}
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                            </div>
                        </c:if>

                        <c:if test="${sessionScope.ROLE == 'CUSTOMER'}">
                            <div class="profile-header"><h3><i class="fa fa-user-circle"></i> Thông Tin Cá Nhân</h3></div>
                            <form action="${pageContext.request.contextPath}/profile" method="post">
                                <div class="form-row">
                                    <div class="form-group col-md-2">
                                        <label class="font-weight-bold text-muted">Mã KH</label>
                                        <input type="text" class="form-control" value="#${sessionScope.USER.customerId}" readonly style="background:#f8f9fa;">
                                    </div>
                                    <div class="form-group col-md-10">
                                        <label class="font-weight-bold">Họ và Tên</label>
                                        <input type="text" name="fullName" class="form-control" value="${sessionScope.USER.fullName}" required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label class="font-weight-bold text-muted">Email</label>
                                        <input type="text" class="form-control" value="${sessionScope.USER.email}" readonly style="background:#f8f9fa;">
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label class="font-weight-bold">Số điện thoại</label>
                                        <input type="text" name="phone" class="form-control" value="${sessionScope.USER.phone}" required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="font-weight-bold">Mật khẩu</label>
                                    <input type="text" name="password" class="form-control" value="${sessionScope.USER.password}" required>
                                </div>
                                <div class="text-center mt-4">
                                    <button class="btn btn-warning btn-lg px-5 font-weight-bold text-white shadow">LƯU THAY ĐỔI</button>
                                </div>
                            </form>
                        </c:if>

                        <c:if test="${sessionScope.ROLE == 'ROOM'}">
                            <div class="profile-header d-flex justify-content-between align-items-center">
                                <h3><i class="fa fa-list-alt"></i> Nhật Ký Dịch Vụ - Phòng ${sessionScope.USER.roomNumber}</h3>
                                <span class="badge badge-success px-3 py-2">Đang hoạt động</span>
                            </div>

                            <div class="total-box">
                                <h5 class="mb-2 text-uppercase ls-1">Tổng giá trị dịch vụ đã sử dụng</h5>
                                <div class="total-amount">
                                    <fmt:formatNumber value="${totalPending}" pattern="#,###"/> VNĐ
                                </div>
                            </div>

                            <h5 class="mb-3 font-weight-bold text-secondary">Chi tiết các món đã gọi:</h5>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-history">
                                    <thead class="thead-light">
                                        <tr>
                                            <th>Thời gian gọi</th>
                                            <th>Tên Món / Dịch Vụ</th>
                                            <th class="text-center">Đơn giá</th>
                                            <th class="text-center">SL</th>
                                            <th class="text-right">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${serviceHistory}" var="h">
                                            <tr>
                                                <td class="align-middle">
                                                    <i class="fa fa-clock-o text-muted mr-1"></i>
                                                    <fmt:formatDate value="${h.orderDate}" pattern="HH:mm dd/MM"/>
                                                </td>
                                                <td class="align-middle font-weight-bold text-primary">
                                                    ${h.itemName}
                                                </td>
                                                <td class="text-center align-middle">
                                                    <fmt:formatNumber value="${h.unitPrice}" pattern="#,###"/>
                                                </td>
                                                <td class="text-center align-middle">
                                                    <span class="badge badge-light border">x${h.quantity}</span>
                                                </td>
                                                <td class="text-right align-middle font-weight-bold">
                                                    <fmt:formatNumber value="${h.subtotal}" pattern="#,###"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                        <c:if test="${empty serviceHistory}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5 text-muted">
                                                    <i class="fa fa-coffee fa-3x mb-3 text-black-50"></i>
                                                    <p>Quý khách chưa sử dụng dịch vụ nào.</p>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            
                            <div class="text-center mt-4">
                                <a href="${pageContext.request.contextPath}/services" class="btn btn-primary btn-lg shadow">
                                    <i class="fa fa-plus-circle"></i> ĐẶT THÊM DỊCH VỤ
                                </a>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>