<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">

<head>
    <title>Dịch Vụ | Smart Hotel</title>
    <%-- INCLUDE HEAD --%>
    <jsp:include page="/WEB-INF/views/components/head.jsp"></jsp:include>
    
    <style>
        /* --- 1. ÉP BUỘC GIAO DIỆN HÀNG NGANG (FIX LỖI) --- */
        .custom-row {
            display: flex;
            flex-wrap: wrap;
            margin-right: -15px;
            margin-left: -15px;
        }

        .custom-col {
            position: relative;
            width: 100%;
            padding-right: 15px;
            padding-left: 15px;
            margin-bottom: 30px;
            
            /* Mặc định Mobile: 1 cột (100%) */
            flex: 0 0 100%;
            max-width: 100%;
        }

        /* Tablet: 2 cột (50%) */
        @media (min-width: 768px) {
            .custom-col {
                flex: 0 0 50%;
                max-width: 50%;
            }
        }

        /* PC (Desktop): 3 cột (33.33%) -> ĐÂY LÀ CHỖ CHIA 3 HÀNG NGANG */
        @media (min-width: 992px) {
            .custom-col {
                flex: 0 0 33.333333%;
                max-width: 33.333333%;
            }
        }

        /* --- 2. STYLE CÁC KHỐI DỊCH VỤ --- */
        .service-item {
            height: 100%;
            display: flex;
            flex-direction: column;
            background: #fff;
            border: 1px solid #e1e1e1;
            border-radius: 8px;
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .service-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }

        /* Ảnh */
        .service-img-box {
            width: 100%;
            height: 220px;
            overflow: hidden;
            position: relative;
            background: #f4f4f4;
        }
        .service-img-box img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.5s;
        }
        .service-item:hover .service-img-box img {
            transform: scale(1.1);
        }

        /* Nội dung bên trong */
        .service-content {
            padding: 20px;
            flex-grow: 1; /* Đẩy nút xuống đáy */
            display: flex;
            flex-direction: column;
            align-items: center; /* Căn giữa nội dung */
            text-align: center;
        }

        .service-icon {
            font-size: 40px;
            color: #dfa974;
            margin-bottom: 15px;
        }

        .service-content h4 {
            font-size: 20px;
            color: #19191a;
            margin-bottom: 10px;
            font-weight: 700;
        }

        .service-content p {
            color: #707070;
            font-size: 14px;
            line-height: 24px;
            margin-bottom: 20px;
        }

        /* Nút bấm */
        .service-btn-wrapper {
            margin-top: auto; /* Đẩy xuống đáy */
            width: 100%;
        }

        .primary-btn {
            display: inline-block;
            font-size: 13px;
            font-weight: 700;
            text-transform: uppercase;
            color: #ffffff;
            letter-spacing: 2px;
            background: #dfa974;
            border: none;
            padding: 12px 10px;
            width: 100%;
            border-radius: 4px;
            text-align: center;
            text-decoration: none;
        }
        .primary-btn:hover {
            background: #19191a;
            color: #fff;
            text-decoration: none;
        }

        /* Chữ Chưa sẵn sàng */
        .not-ready-text {
            display: block;
            width: 100%;
            padding: 10px;
            font-size: 13px;
            font-weight: 600;
            color: #dfa974;
            background: #fff;
            border: 1px dashed #dfa974;
            border-radius: 4px;
            text-transform: uppercase;
            cursor: default;
        }

        /* Phân trang */
        .room-pagination {
            display: flex;
            justify-content: center;
            margin-top: 40px;
            width: 100%;
        }
        .room-pagination a {
            width: 40px; height: 40px;
            background: #fff; border: 1px solid #ebebeb;
            color: #111; border-radius: 50%;
            display: flex; justify-content: center; align-items: center;
            margin: 0 5px;
            text-decoration: none;
        }
        .room-pagination a.active {
            background: #dfa974; color: #fff; border-color: #dfa974;
        }
    </style>
</head>

<body>
    <div id="preloder"><div class="loader"></div></div>

    <%-- NAVBAR --%>
    <jsp:include page="/WEB-INF/views/components/navbar.jsp"></jsp:include>

    <div class="breadcrumb-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Dịch Vụ Tiện Ích</h2>
                        <div class="bt-option">
                            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                            <span>Dịch vụ</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="services-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-title">
                        <span>Trải Nghiệm Đẳng Cấp</span>
                        <h2>Khám Phá Dịch Vụ Của Chúng Tôi</h2>
                    </div>
                </div>
            </div>
            
            <div class="custom-row">
                
                <c:forEach items="${listServices}" var="s">
                    <div class="custom-col">
                        <div class="service-item">
                            
                            <div class="service-img-box">
                                <c:choose>
                                    <c:when test="${not empty s.imageUrl}">
                                        <img src="${s.imageUrl}" alt="${s.categoryName}" 
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/blog/blog-6.jpg';">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/img/blog/blog-6.jpg" alt="Default">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            
                            <div class="service-content">
                                <i class="${not empty s.iconClass ? s.iconClass : 'flaticon-024-towel'} service-icon"></i>
                                
                                <h4>${s.categoryName}</h4>
                                <p>${s.description}</p>
                                
                                <div class="service-btn-wrapper">
                                    <c:choose>
                                        <c:when test="${not empty s.linkUrl and s.linkUrl != '#'}">
                                            <a href="${pageContext.request.contextPath}${s.linkUrl}" class="primary-btn">
                                               ${not empty s.btnText ? s.btnText : 'Xem Chi Tiết'}
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="not-ready-text">
                                                <i class="fa fa-info-circle"></i> Chưa sẵn sàng
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            
                        </div>
                    </div>
                </c:forEach>

                <%-- Thông báo trống --%>
                <c:if test="${empty listServices}">
                    <div style="width: 100%; text-align: center; padding: 50px;">
                        <h4>Hệ thống đang cập nhật dữ liệu...</h4>
                    </div>
                </c:if>

            </div> <c:if test="${totalPages > 1}">
                <div class="room-pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="services?page=${currentPage - 1}"><i class="fa fa-angle-left"></i></a>
                    </c:if>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="services?page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    
                    <c:if test="${currentPage < totalPages}">
                        <a href="services?page=${currentPage + 1}"><i class="fa fa-angle-right"></i></a>
                    </c:if>
                </div>

                <div class="col-lg-3 col-md-6">
                    <div class="service-item">
                        <img src="https://www.vietnambooking.com/wp-content/uploads/2023/08/to-chuc-su-kien-1.jpg" alt="Tổ chức sự kiện">
                        
                        <i class="flaticon-012-cocktail"></i>
                        <h4>Tổ chức sự kiện</h4>
                        <p>Tổ chức trọn gói Tiệc cưới, Sinh nhật và Hội nghị. Sảnh tiệc sang trọng, thực đơn đa dạng và kịch bản chuyên nghiệp.</p>
                        
                        <a href="${pageContext.request.contextPath}/event-booking" class="primary-btn" style="padding: 10px 20px; margin-top: 10px; width: 100%;">Đặt Phòng Ngay</a>
                    </div>
                </div>

            </div>
        </div>
    </section>

    <%-- FOOTER --%>
    <jsp:include page="/WEB-INF/views/components/footer.jsp"></jsp:include>
</body>
</html>
