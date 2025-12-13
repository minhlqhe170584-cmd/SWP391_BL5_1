<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
<style>
    .status-card {
        padding: 20px;
        border: 2px solid #dee2e6;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;
        text-align: center;
        margin-bottom: 15px;
        background: white;
    }
    
    .status-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    
    .status-card input[type="radio"] {
        display: none;
    }
    
    .status-card input[type="radio"]:checked + label {
        font-weight: bold;
    }
    
    .status-card.selected {
        border-color: #6777ef;
        background-color: #f3f4ff;
    }
    
    .status-PENDING { border-color: #ffc107; }
    .status-PROCESSING { border-color: #17a2b8; }
    .status-WASHING { border-color: #007bff; }
    .status-DRYING { border-color: #6c757d; }
    .status-READY { border-color: #28a745; }
    .status-DELIVERED { border-color: #20c997; }
    .status-COMPLETED { border-color: #198754; }
    .status-CANCELLED { border-color: #dc3545; }
    
    .status-icon {
        font-size: 48px;
        margin-bottom: 10px;
    }
</style>

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Cập nhật trạng thái đơn</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a href="${pageContext.request.contextPath}/laundry-order">Đơn giặt ủi</a></div>
                <div class="breadcrumb-item"><a href="${pageContext.request.contextPath}/laundry-order?action=view&id=${order.laundryId}">Chi tiết #${order.laundryId}</a></div>
                <div class="breadcrumb-item">Cập nhật trạng thái</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12 col-lg-10 offset-lg-1">
                    <!-- Error Message -->
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert">
                                    <span>&times;</span>
                                </button>
                                <i class="fas fa-exclamation-circle"></i> ${error}
                            </div>
                        </div>
                    </c:if>

                    <!-- Current Status -->
                    <div class="card card-primary">
                        <div class="card-header">
                            <h4>Trạng thái hiện tại</h4>
                        </div>
                        <div class="card-body">
                            <div class="d-flex align-items-center">
                                <i class="fas fa-info-circle fa-3x text-primary mr-3"></i>
                                <div>
                                    <h5 class="mb-1">Đơn #${order.laundryId}</h5>
                                    <div class="badge badge-primary badge-lg">${order.status}</div>
                                    <c:if test="${not empty order.serviceOrder}">
                                        <p class="mb-0 text-muted mt-2">Phòng ${order.serviceOrder.roomId}</p>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Status Update Form -->
                    <form action="laundry-order" method="post" id="statusForm">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="laundryId" value="${order.laundryId}">

                        <div class="card">
                            <div class="card-header">
                                <h4>Chọn trạng thái mới</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <!-- PENDING -->
                                    <div class="col-md-6">
                                        <div class="status-card status-PENDING ${order.status == 'PENDING' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'PENDING')">
                                            <input type="radio" name="status" value="PENDING" id="statusPENDING" 
                                                   ${order.status == 'PENDING' ? 'checked' : ''}>
                                            <label for="statusPENDING" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-clock text-warning"></i>
                                                </div>
                                                <h5>CHỜ XỬ LÝ</h5>
                                                <p class="text-muted mb-0">Đơn đã nhận, đang chờ xử lý</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- PROCESSING -->
                                    <div class="col-md-6">
                                        <div class="status-card status-PROCESSING ${order.status == 'PROCESSING' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'PROCESSING')">
                                            <input type="radio" name="status" value="PROCESSING" id="statusPROCESSING"
                                                   ${order.status == 'PROCESSING' ? 'checked' : ''}>
                                            <label for="statusPROCESSING" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-tasks text-info"></i>
                                                </div>
                                                <h5>ĐANG XỬ LÝ</h5>
                                                <p class="text-muted mb-0">Đang chuẩn bị giặt</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- WASHING -->
                                    <div class="col-md-6">
                                        <div class="status-card status-WASHING ${order.status == 'WASHING' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'WASHING')">
                                            <input type="radio" name="status" value="WASHING" id="statusWASHING"
                                                   ${order.status == 'WASHING' ? 'checked' : ''}>
                                            <label for="statusWASHING" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-water text-primary"></i>
                                                </div>
                                                <h5>ĐANG GIẶT</h5>
                                                <p class="text-muted mb-0">Đang trong quá trình giặt</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- DRYING -->
                                    <div class="col-md-6">
                                        <div class="status-card status-DRYING ${order.status == 'DRYING' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'DRYING')">
                                            <input type="radio" name="status" value="DRYING" id="statusDRYING"
                                                   ${order.status == 'DRYING' ? 'checked' : ''}>
                                            <label for="statusDRYING" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-wind text-secondary"></i>
                                                </div>
                                                <h5>ĐANG SẤY</h5>
                                                <p class="text-muted mb-0">Đang trong quá trình sấy khô</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- READY -->
                                    <div class="col-md-6">
                                        <div class="status-card status-READY ${order.status == 'READY' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'READY')">
                                            <input type="radio" name="status" value="READY" id="statusREADY"
                                                   ${order.status == 'READY' ? 'checked' : ''}>
                                            <label for="statusREADY" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-check-circle text-success"></i>
                                                </div>
                                                <h5>SẴN SÀNG</h5>
                                                <p class="text-muted mb-0">Sẵn sàng để nhận/giao</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- DELIVERED -->
                                    <div class="col-md-6">
                                        <div class="status-card status-DELIVERED ${order.status == 'DELIVERED' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'DELIVERED')">
                                            <input type="radio" name="status" value="DELIVERED" id="statusDELIVERED"
                                                   ${order.status == 'DELIVERED' ? 'checked' : ''}>
                                            <label for="statusDELIVERED" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-shipping-fast" style="color: #20c997;"></i>
                                                </div>
                                                <h5>ĐÃ GIAO</h5>
                                                <p class="text-muted mb-0">Đã giao cho khách hàng</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- COMPLETED -->
                                    <div class="col-md-6">
                                        <div class="status-card status-COMPLETED ${order.status == 'COMPLETED' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'COMPLETED')">
                                            <input type="radio" name="status" value="COMPLETED" id="statusCOMPLETED"
                                                   ${order.status == 'COMPLETED' ? 'checked' : ''}>
                                            <label for="statusCOMPLETED" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-check-double" style="color: #198754;"></i>
                                                </div>
                                                <h5>HOÀN THÀNH</h5>
                                                <p class="text-muted mb-0">Đơn hàng đã hoàn thành</p>
                                            </label>
                                        </div>
                                    </div>

                                    <!-- CANCELLED -->
                                    <div class="col-md-6">
                                        <div class="status-card status-CANCELLED ${order.status == 'CANCELLED' ? 'selected' : ''}" 
                                             onclick="selectStatus(this, 'CANCELLED')">
                                            <input type="radio" name="status" value="CANCELLED" id="statusCANCELLED"
                                                   ${order.status == 'CANCELLED' ? 'checked' : ''}>
                                            <label for="statusCANCELLED" class="w-100 mb-0">
                                                <div class="status-icon">
                                                    <i class="fas fa-times-circle text-danger"></i>
                                                </div>
                                                <h5>ĐÃ HỦY</h5>
                                                <p class="text-muted mb-0">Đơn hàng đã bị hủy</p>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer text-right">
                                <a href="laundry-order?action=view&id=${order.laundryId}" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> Cập nhật trạng thái
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>

<script>
    function selectStatus(element, status) {
        // Remove selected class from all options
        document.querySelectorAll('.status-card').forEach(function(card) {
            card.classList.remove('selected');
        });

        // Add selected class to clicked option
        element.classList.add('selected');

        // Check the radio button
        document.getElementById('status' + status).checked = true;
    }

    // Form validation
    document.getElementById('statusForm').addEventListener('submit', function(e) {
        const selectedStatus = document.querySelector('input[name="status"]:checked');
        if (!selectedStatus) {
            e.preventDefault();
            alert('Vui lòng chọn trạng thái.');
            return false;
        }

        // Confirm status change
        const currentStatus = '${order.status}';
        if (selectedStatus.value === currentStatus) {
            e.preventDefault();
            alert('Trạng thái được chọn giống với trạng thái hiện tại.');
            return false;
        }

        return confirm('Bạn có chắc chắn muốn thay đổi trạng thái thành ' + selectedStatus.value + '?');
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />