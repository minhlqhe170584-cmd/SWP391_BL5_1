<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Thanh Toán & Trả Phòng | SmartHotel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .main-container { max-width: 1200px; margin: 30px auto; padding: 0 15px; }
        
        /* Card Styling */
        .card-custom {
            border: none;
            border-radius: 15px;
            box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
            background: white;
            overflow: hidden;
            height: 100%;
        }
        .card-header-custom {
            padding: 20px;
            font-weight: 700;
            text-transform: uppercase;
            color: white;
            letter-spacing: 1px;
        }
        
        /* Màu sắc Header Card */
        .bg-gradient-room { background: linear-gradient(45deg, #f6c23e, #fd7e14); }
        .bg-gradient-service { background: linear-gradient(45deg, #36b9cc, #2c9faf); }
        
        /* Font tiền tệ */
        .text-money { font-family: 'Consolas', monospace; font-weight: bold; }
        
        /* Search Box */
        .search-wrapper {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            margin-bottom: 30px;
        }
        .search-input {
            border-radius: 50px 0 0 50px;
            padding-left: 25px;
            border: 2px solid #eaecf4;
            border-right: none;
        }
        .search-btn {
            border-radius: 0 50px 50px 0;
            padding: 10px 40px;
            font-weight: bold;
        }

        /* Invoice Table */
        .table-invoice th { background-color: #f8f9fc; color: #858796; font-size: 0.85rem; font-weight: 700; border-top: none; }
        .item-name { font-weight: 600; color: #4e73df; }
        .item-time { font-size: 0.8em; color: #b7b9cc; }
        
        /* Navbar Custom */
        .navbar-custom { background: linear-gradient(90deg, #4e73df 10%, #224abe 100%); }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow mb-4 sticky-top">
        <div class="container-fluid px-4">
            <a class="navbar-brand fw-bold" href="#">
                <i class="fas fa-hotel me-2"></i>SmartHotel <span class="fw-light opacity-75">Reception</span>
            </a>

            <div class="d-flex align-items-center">
                <c:if test="${not empty sessionScope.USER}">
                    <div class="text-white me-3 d-none d-md-block text-end">
                        <small class="d-block opacity-75" style="font-size: 0.75rem;">Đang làm việc</small>
                        <span class="fw-bold"><i class="fas fa-user-circle me-1"></i> ${sessionScope.USER.fullName}</span>
                    </div>
                </c:if>

                <a href="${pageContext.request.contextPath}/logout" class="btn btn-light btn-sm fw-bold text-primary shadow-sm px-3">
                    <i class="fas fa-sign-out-alt me-1"></i> Thoát
                </a>
            </div>
        </div>
    </nav>

    <div class="main-container">
        
        <div class="search-wrapper text-center animate__animated animate__fadeInDown">
            <h4 class="fw-bold text-secondary mb-3">THANH TOÁN & TRẢ PHÒNG</h4>
            <form action="payment" method="get" class="d-flex justify-content-center">
                <div class="input-group shadow-sm" style="max-width: 600px;">
                    <input type="text" name="roomNumber" class="form-control form-control-lg search-input" 
                           placeholder="Nhập số phòng (VD: 101, 205...)" 
                           value="${currRoom}" required autocomplete="off">
                    <button type="submit" class="btn btn-primary btn-lg search-btn">
                        <i class="fas fa-search me-2"></i> TRA CỨU
                    </button>
                </div>
            </form>

            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-4 d-inline-block px-5 py-2 rounded-pill shadow-sm">
                    <i class="fas fa-exclamation-triangle me-2"></i> ${error}
                </div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success mt-4 d-inline-block px-5 py-2 rounded-pill shadow-sm">
                    <i class="fas fa-check-circle me-2"></i> ${message}
                </div>
            </c:if>
        </div>

        <c:if test="${not empty bill}">
            <div class="row g-4">
                
                <div class="col-lg-4">
                    <div class="card-custom">
                        <div class="card-header-custom bg-gradient-room">
                            <i class="fas fa-bed me-2"></i> Tiền Phòng
                        </div>
                        <div class="card-body p-4 d-flex flex-column justify-content-center text-center">
                            <div class="mb-4">
                                <div class="display-4 text-warning mb-2"><i class="fas fa-door-open"></i></div>
                                <h3 class="fw-bold text-dark">Phòng ${currRoom}</h3>
                                <span class="badge bg-success rounded-pill px-3">Đang hoạt động</span>
                            </div>
                            
                            <div class="bg-light rounded p-3 text-start">
                                <small class="text-muted d-block mb-1">Ghi chú:</small>
                                <div class="fst-italic text-dark mb-3">${bill.roomNote}</div>
                                <hr>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="fw-bold text-secondary">Thành tiền:</span>
                                    <span class="text-money fs-4 text-warning">
                                        <fmt:formatNumber value="${bill.roomTotalAmount}" pattern="#,###"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-8">
                    <div class="card-custom">
                        <div class="card-header-custom bg-gradient-service">
                            <i class="fas fa-receipt me-2"></i> Chi Tiết Dịch Vụ
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive" style="max-height: 450px; overflow-y: auto;">
                                <table class="table table-hover table-invoice align-middle mb-0">
                                    <thead>
                                        <tr>
                                            <th class="ps-4" style="width: 40%;">Tên món / Dịch vụ</th>
                                            <th class="text-center">Số lượng</th>
                                            <th class="text-end">Đơn giá</th>
                                            <th class="text-end pe-4">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${bill.listServiceDetails}" var="d">
                                            <tr>
                                                <td class="ps-4">
                                                    <div class="item-name">${d.itemName}</div>
                                                    <div class="item-time">
                                                        <i class="far fa-clock me-1"></i>
                                                        <fmt:formatDate value="${d.orderTime}" pattern="HH:mm dd/MM"/>
                                                    </div>
                                                </td>
                                                <td class="text-center">
                                                    <span class="badge bg-light text-dark border px-3">x${d.quantity}</span>
                                                </td>
                                                <td class="text-end text-muted">
                                                    <fmt:formatNumber value="${d.unitPrice}" pattern="#,###"/>
                                                </td>
                                                <td class="text-end pe-4 text-money text-dark">
                                                    <fmt:formatNumber value="${d.total}" pattern="#,###"/>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty bill.listServiceDetails}">
                                            <tr>
                                                <td colspan="4" class="text-center py-5 text-muted">
                                                    <i class="fas fa-coffee fa-3x mb-3 opacity-25"></i>
                                                    <p class="mb-0">Khách hàng chưa sử dụng dịch vụ nào.</p>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12">
                    <div class="card-custom border-top border-4 border-primary mt-2">
                        <div class="card-body py-4">
                            <div class="row align-items-center">
                                <div class="col-md-6 text-center text-md-start ps-md-5">
                                    <h6 class="text-uppercase text-secondary ls-1 mb-1">Tổng cộng thanh toán</h6>
                                    <h1 class="display-4 fw-bold text-primary mb-0">
                                        <fmt:formatNumber value="${bill.grandTotal}" pattern="#,###"/> <span class="fs-4">VNĐ</span>
                                    </h1>
                                </div>
                                
                                <div class="col-md-6 mt-4 mt-md-0 border-start-md">
                                    <form action="payment" method="post" id="paymentForm" onsubmit="return confirmPayment()">
                                        <input type="hidden" name="bookingId" value="${currBookingId}">
                                        <input type="hidden" name="totalAmount" value="${bill.grandTotal}">

                                        <div class="px-md-4">
                                            <label class="fw-bold mb-2 text-dark">Chọn hình thức thanh toán:</label>
                                            <div class="input-group mb-3">
                                                <label class="input-group-text bg-white"><i class="fas fa-wallet text-primary"></i></label>
                                                <select name="paymentMethod" class="form-select form-select-lg">
                                                    <option value="Cash">💵 Tiền mặt (Cash)</option>
                                                    <option value="Card">💳 Thẻ tín dụng (Visa/Master)</option>
                                                    <option value="Transfer">📱 Chuyển khoản (Banking)</option>
                                                    <option value="Momo">🟪 Ví điện tử Momo</option>
                                                    <option value="VNPAY">🟦 VNPAY QR</option>
                                                </select>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold shadow">
                                                <i class="fas fa-file-invoice-dollar me-2"></i> XÁC NHẬN THANH TOÁN
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Hàm xác nhận trước khi submit
        function confirmPayment() {
            return confirm("Xác nhận đã thu đủ tiền và hoàn tất thanh toán cho phòng ${currRoom}?");
        }
    </script>
</body>
</html>