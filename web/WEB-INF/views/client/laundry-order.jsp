<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Đặt Dịch Vụ Giặt Ủi | Smart Hotel</title>
    <jsp:include page="../components/head.jsp"></jsp:include>
    <style>
        .service-group-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 20px;
            border-radius: 8px;
            margin-top: 30px;
            margin-bottom: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .service-group-header h4 {
            margin: 0;
            color: white;
        }
        
        .item-card {
            background: white;
            border: 2px solid #e3e6f0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 15px;
            transition: all 0.3s;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        
        .item-card:hover {
            border-color: #667eea;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        
        .item-checkbox {
            width: 20px;
            height: 20px;
            margin-right: 10px;
            cursor: pointer;
        }
        
        .item-info {
            flex-grow: 1;
        }
        
        .item-name {
            font-size: 18px;
            font-weight: 600;
            color: #333;
            margin-bottom: 5px;
        }
        
        .item-description {
            color: #6c757d;
            font-size: 14px;
            margin-bottom: 10px;
        }
        
        .price-badge {
            background: #667eea;
            color: white;
            padding: 8px 20px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 16px;
            display: inline-block;
        }
        
        .unit-badge {
            background: #f8f9fa;
            color: #6c757d;
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 13px;
            margin-left: 10px;
        }
        
        .quantity-input {
            width: 100px;
            height: 45px;
            border: 2px solid #e3e6f0;
            border-radius: 5px;
            text-align: center;
            font-size: 16px;
            font-weight: 600;
        }
        
        .breadcrumb-section {
/*            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);*/
            padding: 60px 0;
        }
        
        .room-info-box {
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 30px;
        }
        
        .alert-custom {
            border-radius: 8px;
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .primary-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 15px 40px;
            font-size: 16px;
            font-weight: 600;
            border-radius: 8px;
            transition: all 0.3s;
        }
        
        .primary-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        
        .form-control, textarea {
            border: 2px solid #e3e6f0;
            border-radius: 5px;
            height: 50px;
            padding: 10px 15px;
        }
        
        textarea {
            height: 100px !important;
        }
        
        .item-selection-wrapper {
            display: flex;
            align-items: center;
            gap: 15px;
        }
        
        .quantity-wrapper {
            text-align: center;
        }
        
        .quantity-label {
            font-size: 13px;
            color: #6c757d;
            margin-bottom: 5px;
        }
    </style>
</head>

<body>
    <jsp:include page="../components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2 style="color:white;">Đặt Dịch Vụ Giặt Ủi</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/room/dashboard" style="color: rgba(255,255,255,0.8);">Trang chủ</a>
                            <span style="color: white;">Giặt ủi</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="contact-section spad">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-10">
                    
                    <div class="room-info-box">
                        <div class="row align-items-center">
                            <div class="col-md-8">
                                <h4 class="mb-2"><i class="fa fa-door-open"></i> Mã Phòng ${roomId}</h4>
                                <p class="mb-0 text-muted">Vui lòng chọn các mục giặt ủi bạn muốn đặt và điền số lượng</p>
                            </div>
                            <div class="col-md-4 text-right">
<!--                                <a href="laundry?action=history" class="btn btn-outline-primary">
                                    <i class="fa fa-history"></i> Lịch sử đơn hàng
                                </a>-->
                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-custom text-center mb-4">
                            <i class="fa fa-exclamation-circle"></i> ${error}
                        </div>
                    </c:if>

                    <form action="laundry-book" method="POST" class="contact-form">
                        <input type="hidden" name="action" value="order">
                        <input type="hidden" name="serviceId" value="${service.serviceId}">
                        <c:choose>
                            <c:when test="${not empty items}">
                                <c:set var="currentService" value="" />
                                <c:forEach var="item" items="${items}">
                                    
                                    <c:if test="${item.service.serviceName != currentService}">               
                                        <div class="service-group-header">
                                            <h4><i class="fa fa-tags"></i> ${item.service.serviceName}</h4>
                                        </div>
                                        
                                        <div class="service-items"> <c:set var="currentService" value="${item.service.serviceName}" />
                                    </c:if>

                                    <div class="item-card">
                                        <div class="item-selection-wrapper">
                                            <div>
                                                <input type="checkbox" 
                                                       class="item-checkbox" 
                                                       name="selected_${item.laundryItemId}" 
                                                       value="true"
                                                       id="item_${item.laundryItemId}">
                                            </div>

                                            <div class="item-info">
                                                <label for="item_${item.laundryItemId}" style="cursor: pointer; margin: 0;">
                                                    <div class="item-name">${item.itemName}</div>
                                                    <c:if test="${not empty item.description}">
                                                        <div class="item-description">${item.description}</div>
                                                    </c:if>
                                                    <div class="mt-2">
                                                        <span class="price-badge">
                                                            <fmt:formatNumber value="${item.defaultPrice}" 
                                                                            type="currency" 
                                                                            currencySymbol="" 
                                                                            maxFractionDigits="0"/> VNĐ
                                                        </span>
                                                        <span class="unit-badge">/${item.unit}</span>
                                                    </div>
                                                </label>
                                                
                                                <input type="hidden" name="itemId" value="${item.laundryItemId}">
                                                <input type="hidden" name="price" value="${item.defaultPrice}">
                                            </div>

                                            <div class="quantity-wrapper">
                                                <div class="quantity-label">Số lượng</div>
                                                <input type="number" 
                                                       name="quantity" 
                                                       class="quantity-input form-control" 
                                                       min="1" 
                                                       value="1">
                                            </div>
                                        </div>
                                    </div>

                                </c:forEach>
                                </div> <div class="service-group-header" style="background: #28a745;">
                                    <h4><i class="fa fa-info-circle"></i> Thông tin bổ sung</h4>
                                </div>

                                <div class="row">
                                    <div class="col-lg-12">
                                        <label class="font-weight-bold">Thời gian lấy đồ (Tùy chọn):</label>
                                        <input type="datetime-local" name="pickupTime" class="form-control">
                                        <small class="text-muted">Để trống nếu nhân viên sẽ liên hệ sau</small>
                                    </div>
                                    
                                    <div class="col-lg-12 mt-3">
                                        <label class="font-weight-bold">Thời gian nhận đồ (Tùy chọn):</label>
                                        <input type="datetime-local" name="returnTime" class="form-control">
                                        <small class="text-muted">Để trống nếu nhân viên sẽ liên hệ sau</small>
                                    </div>
                                    
                                    <div class="col-lg-12 mt-3">
                                        <label class="font-weight-bold">Ghi chú cho nhân viên (Tùy chọn):</label>
                                        <textarea name="note" class="form-control" placeholder="Ví dụ: Giặt riêng đồ trắng, không dùng nước xả vải..."></textarea>
                                    </div>
                                    
                                    <div class="col-lg-12 text-center mt-4">
                                        <button type="submit" class="primary-btn" style="width: 100%;">
                                            <i class="fa fa-check-circle"></i> Xác Nhận Đặt Đơn Giặt Ủi
                                        </button>
                                    </div>
                                </div>
                            </c:when>

                            <c:otherwise>
                                <div class="alert alert-warning text-center mt-4" style="padding: 30px;">
                                    <h4><i class="fa fa-search"></i> Không tìm thấy dịch vụ</h4>
                                    <p>Hiện tại khách sạn chưa có dịch vụ giặt ủi nào khả dụng.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </form>
                        
                    <div class="alert alert-info alert-custom mt-4">
                        <strong><i class="fa fa-lightbulb"></i> Lưu ý:</strong>
                        <ul class="mb-0 mt-2">
                            <li>Chọn các mục bạn muốn giặt bằng cách tích vào ô vuông</li>
                            <li>Điều chỉnh số lượng cho từng mục nếu cần</li>
                            <li>Thời gian xử lý: 24-48 giờ tùy loại dịch vụ</li>
                            <li>Nhân viên sẽ liên hệ xác nhận sau khi nhận đơn</li>
                        </ul>
                    </div>
                        
                </div>
            </div>
        </div>
    </section>

    <jsp:include page="../components/footer.jsp"></jsp:include>
</body>
</html>