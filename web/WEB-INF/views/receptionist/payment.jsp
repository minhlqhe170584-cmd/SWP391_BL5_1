<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh Toán | SmartHotel</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    
    <style>
        body { background-color: #fff8f0; font-family: 'Segoe UI', sans-serif; } /* Nền cam nhạt */
        .main-container { max-width: 1200px; margin: 30px auto; padding: 0 15px; }
        
        /* Orange Theme Colors */
        :root {
            --primary-orange: #ff6b35;
            --dark-orange: #e85d04;
            --light-orange: #ffb703;
        }

        /* Card Styling */
        .card-custom {
            border: none;
            border-radius: 16px;
            box-shadow: 0 8px 30px rgba(255, 107, 53, 0.1);
            background: white;
            overflow: hidden;
            height: 100%;
            transition: transform 0.3s ease;
        }
        .card-header-custom {
            padding: 18px 25px;
            font-weight: 700;
            text-transform: uppercase;
            color: white;
            letter-spacing: 1px;
        }
        
        /* Gradients Cam */
        .bg-gradient-room { background: linear-gradient(135deg, #ff9f1c, #ff6b35); }
        .bg-gradient-service { background: linear-gradient(135deg, #2ec4b6, #20a4f3); } /* Giữ xanh cho dịch vụ để tương phản */
        .bg-gradient-total { background: linear-gradient(135deg, #ff6b35, #d00000); }
        
        .navbar-custom { background: linear-gradient(90deg, #ff6b35, #ff9f1c); }
        
        .text-money { font-family: 'Consolas', monospace; font-weight: bold; }
        
        /* Search Box */
        .search-wrapper {
            background: white;
            padding: 30px;
            border-radius: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            margin-bottom: 30px;
            border: 1px solid #ffe5d9;
        }
        .search-input {
            border-radius: 50px 0 0 50px;
            border: 2px solid #ffdecb;
            padding-left: 25px;
        }
        .search-input:focus { border-color: var(--primary-orange); box-shadow: 0 0 0 0.25rem rgba(255, 107, 53, 0.25); }
        .search-btn {
            border-radius: 0 50px 50px 0;
            background-color: var(--primary-orange);
            border: none;
            padding: 0 40px;
            font-weight: bold;
        }
        .search-btn:hover { background-color: var(--dark-orange); }

        /* Status Badge */
        .paid-stamp {
            border: 3px solid #198754;
            color: #198754;
            font-weight: 900;
            text-transform: uppercase;
            padding: 10px 20px;
            border-radius: 10px;
            display: inline-block;
            transform: rotate(-5deg);
            font-size: 1.5rem;
            letter-spacing: 2px;
            background-color: rgba(25, 135, 84, 0.1);
        }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom shadow-sm mb-4 sticky-top">
        <div class="container-fluid px-4">
            <a class="navbar-brand fw-bold" href="#">
                <i class="fas fa-hotel me-2"></i>SmartHotel
            </a>
            <div class="d-flex align-items-center">
                <c:if test="${not empty sessionScope.USER}">
                    <div class="text-white me-3 d-none d-md-block text-end">
                        <small class="d-block opacity-75" style="font-size: 12px;">Nhân viên</small>
                        <span class="fw-bold">${sessionScope.USER.fullName}</span>
                    </div>
                </c:if>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-light btn-sm fw-bold text-danger shadow-sm px-3 rounded-pill">
                    <i class="fas fa-sign-out-alt me-1"></i> Thoát
                </a>
            </div>
        </div>
    </nav>

    <div class="main-container">
        
        <div class="search-wrapper text-center">
            <h4 class="fw-bold text-secondary mb-3" style="color: var(--dark-orange) !important;">THANH TOÁN & TRẢ PHÒNG</h4>
            <form action="payment" method="get" class="d-flex justify-content-center">
                <div class="input-group input-group-lg" style="max-width: 600px;">
                    <input type="text" name="roomNumber" class="form-control search-input" 
                           placeholder="Nhập số phòng..." value="${currRoom}" required autocomplete="off">
                    <button type="submit" class="btn btn-primary search-btn">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </form>
            
            <c:if test="${not empty message}">
                <div class="alert alert-success mt-4 d-inline-block px-4 rounded-pill shadow-sm">
                    <i class="fas fa-check-circle me-2"></i> ${message}
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-4 d-inline-block px-4 rounded-pill shadow-sm">
                    <i class="fas fa-exclamation-triangle me-2"></i> ${error}
                </div>
            </c:if>
        </div>

        <c:if test="${not empty bill}">
            <div class="row g-4 animate__animated animate__fadeIn">
                
                <div class="col-lg-4">
                    <div class="card-custom">
                        <div class="card-header-custom bg-gradient-room">
                            <i class="fas fa-bed me-2"></i> Thông Tin Phòng
                        </div>
                        <div class="card-body p-4 text-center">
                            <h2 class="fw-bold text-dark mb-1">Phòng ${currRoom}</h2>
                            
                            <div class="my-3">
                                <c:choose>
                                    <c:when test="${bill.paid}">
                                        <div class="paid-stamp">ĐÃ THANH TOÁN</div>
                                        <div class="text-muted mt-2 small">
                                            <i class="far fa-clock"></i> <fmt:formatDate value="${bill.paymentDate}" pattern="HH:mm dd/MM/yyyy"/>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fs-6">
                                            <i class="fas fa-spinner fa-spin me-1"></i> Chờ thanh toán
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="bg-light rounded p-3 text-start border mt-4">
                                <div class="d-flex justify-content-between">
                                    <span class="text-muted">Tiền phòng:</span>
                                    <span class="fw-bold"><fmt:formatNumber value="${bill.roomTotalAmount}" pattern="#,###"/></span>
                                </div>
                                <hr class="my-2">
                                <small class="text-muted fst-italic">${bill.roomNote}</small>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-lg-8">
                    <div class="card-custom">
                        <div class="card-header-custom bg-gradient-service">
                            <i class="fas fa-concierge-bell me-2"></i> Dịch Vụ Đã Dùng
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="bg-light">
                                        <tr>
                                            <th class="ps-4 text-secondary">Tên món</th>
                                            <th class="text-center text-secondary">SL</th>
                                            <th class="text-end text-secondary pe-4">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${bill.listServiceDetails}" var="d">
                                            <tr>
                                                <td class="ps-4 fw-bold text-primary">${d.itemName}</td>
                                                <td class="text-center"><span class="badge bg-light text-dark border">x${d.quantity}</span></td>
                                                <td class="text-end pe-4 fw-bold">
                                                    <fmt:formatNumber value="${d.total}" pattern="#,###"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty bill.listServiceDetails}">
                                            <tr>
                                                <td colspan="3" class="text-center py-5 text-muted">Không có dịch vụ phát sinh.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12">
                    <div class="card-custom border-top border-4 border-warning mt-2">
                        <div class="card-body py-4">
                            <div class="row align-items-center">
                                <div class="col-md-5 text-center text-md-start ps-md-5">
                                    <h6 class="text-uppercase text-secondary fw-bold mb-1">Tổng cộng</h6>
                                    <h1 class="display-3 fw-bold mb-0" style="color: var(--dark-orange);">
                                        <fmt:formatNumber value="${bill.grandTotal}" pattern="#,###"/> <small class="fs-4 text-muted">₫</small>
                                    </h1>
                                </div>
                                
                                <div class="col-md-7 mt-4 mt-md-0">
                                    <c:choose>
                                        <c:when test="${bill.paid}">
                                            <div class="alert alert-success d-flex align-items-center justify-content-center m-0">
                                                <i class="fas fa-check-circle fa-2x me-3"></i>
                                                <div>
                                                    <h5 class="alert-heading fw-bold mb-0">Hóa đơn này đã hoàn tất!</h5>
                                                    <p class="mb-0">Bạn không cần thao tác gì thêm.</p>
                                                </div>
                                            </div>
                                        </c:when>
                                        
                                        <c:otherwise>
                                            <form action="payment" method="post" onsubmit="return confirm('Xác nhận thu tiền?')">
                                                <input type="hidden" name="bookingId" value="${currBookingId}">
                                                <input type="hidden" name="totalAmount" value="${bill.grandTotal}">

                                                <div class="input-group mb-3 shadow-sm">
                                                    <label class="input-group-text bg-white text-secondary">Hình thức:</label>
                                                    <select name="paymentMethod" class="form-select form-select-lg fw-bold text-dark">
                                                        <option value="Cash">💵 Tiền mặt</option>
                                                        <option value="Card">💳 Thẻ / Banking</option>
                                                        <option value="Momo">🟪 Ví Momo</option>
                                                    </select>
                                                    <button type="submit" class="btn btn-warning btn-lg px-5 fw-bold text-dark">
                                                        THU TIỀN NGAY <i class="fas fa-arrow-right ms-2"></i>
                                                    </button>
                                                </div>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </c:if>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>