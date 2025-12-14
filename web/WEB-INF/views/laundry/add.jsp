<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>${empty order ? 'Thêm mới' : 'Chỉnh sửa'} đơn giặt ủi</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a href="${pageContext.request.contextPath}/laundry-order">Đơn giặt ủi</a></div>
                <div class="breadcrumb-item">${empty order ? 'Thêm mới' : 'Chỉnh sửa'}</div>
            </div>
        </div>

        <div class="section-body">
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

            <form action="laundry-order" method="post" id="orderForm">
                <input type="hidden" name="action" value="${empty order ? 'add' : 'edit'}">
                <c:if test="${not empty order}">
                    <input type="hidden" name="laundryId" value="${order.laundryId}">
                </c:if>

                <div class="row">
                    <div class="col-12">
                        <!-- Basic Information -->
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-info-circle"></i> Thông tin cơ bản</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Mã đơn dịch vụ <span class="text-danger">*</span></label>
                                            <input type="number" name="orderId" class="form-control" 
                                                   value="${order.orderId}" required
                                                   ${not empty order ? 'readonly' : ''}>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Trạng thái <span class="text-danger">*</span></label>
                                            <select name="status" class="form-control" required>
                                                <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                                                <option value="PROCESSING" ${order.status == 'PROCESSING' ? 'selected' : ''}>Đang xử lý</option>
                                                <option value="WASHING" ${order.status == 'WASHING' ? 'selected' : ''}>Đang giặt</option>
                                                <option value="DRYING" ${order.status == 'DRYING' ? 'selected' : ''}>Đang sấy</option>
                                                <option value="READY" ${order.status == 'READY' ? 'selected' : ''}>Sẵn sàng</option>
                                                <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Đã giao</option>
                                                <option value="COMPLETED" ${order.status == 'COMPLETED' ? 'selected' : ''}>Hoàn thành</option>
                                                <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Time Information -->
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-clock"></i> Thông tin thời gian</h4>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Thời gian nhận đồ</label>
                                            <input type="datetime-local" name="pickupTime" class="form-control"
                                                   value="<fmt:formatDate value='${order.pickupTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>">
                                        </div>
                                    </div>

                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Dự kiến giao hàng</label>
                                            <input type="datetime-local" name="deliveryTime" class="form-control"
                                                   value="<fmt:formatDate value='${order.expectedPickupTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>">
                                        </div>
                                    </div>

                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label>Dự kiến trả về</label>
                                            <input type="datetime-local" name="returnTime" class="form-control"
                                                   value="<fmt:formatDate value='${order.expectedReturnTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Note -->
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-sticky-note"></i> Ghi chú</h4>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <textarea name="note" class="form-control" rows="3" 
                                              placeholder="Nhập ghi chú hoặc yêu cầu đặc biệt...">${order.note}</textarea>
                                </div>
                            </div>
                        </div>

                        <!-- Order Items -->
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-list"></i> Chi tiết đơn hàng</h4>
                                <div class="card-header-action">
                                    <button type="button" class="btn btn-success" onclick="addItem()">
                                        <i class="fas fa-plus"></i> Thêm mục
                                    </button>
                                </div>
                            </div>
                            <div class="card-body">
                                <div id="itemsContainer">
                                    <c:choose>
                                        <c:when test="${not empty order.orderDetails}">
                                            <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                                <div class="card mb-3">
                                                    <div class="card-body">
                                                        <div class="row">
                                                            <div class="col-md-4">
                                                                <div class="form-group">
                                                                    <label>Tên mục</label>
                                                                    <select name="itemId" class="form-control item-select" required 
                                                                            onchange="updatePrice(this)">
                                                                        <option value="">Chọn mục</option>
                                                                        <c:forEach var="item" items="${items}">
                                                                            <option value="${item.laundryItemId}" 
                                                                                    data-price="${item.defaultPrice}"
                                                                                    data-unit="${item.unit}"
                                                                                    ${detail.laundryItemId == item.laundryItemId ? 'selected' : ''}>
                                                                                ${item.itemName} (${item.unit})
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                            </div>

                                                            <div class="col-md-2">
                                                                <div class="form-group">
                                                                    <label>Số lượng</label>
                                                                    <input type="number" name="quantity" class="form-control" 
                                                                           min="1" value="${detail.quantity}" required
                                                                           onchange="calculateSubtotal(this)">
                                                                </div>
                                                            </div>

                                                            <div class="col-md-2">
                                                                <div class="form-group">
                                                                    <label>Đơn giá</label>
                                                                    <input type="number" name="price" class="form-control" 
                                                                           step="0.01" min="0" value="${detail.unitPrice}" required
                                                                           onchange="calculateSubtotal(this)">
                                                                </div>
                                                            </div>

                                                            <div class="col-md-2">
                                                                <div class="form-group">
                                                                    <label>Tạm tính</label>
                                                                    <input type="text" class="form-control subtotal" readonly
                                                                           value="${detail.subtotal}">
                                                                </div>
                                                            </div>

                                                            <div class="col-md-2">
                                                                <div class="form-group">
                                                                    <label>&nbsp;</label>
                                                                    <button type="button" class="btn btn-danger btn-block" 
                                                                            onclick="removeItem(this)">
                                                                        <i class="fas fa-trash"></i> Xóa
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Empty state -->
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="alert alert-light">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h5 class="mb-0">Tổng tiền:</h5>
                                        <h4 class="mb-0 text-success" id="totalAmount">$0.00</h4>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Submit Buttons -->
                        <div class="card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <a href="laundry-order" class="btn btn-secondary">
                                        <i class="fas fa-times"></i> Hủy
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save"></i> ${empty order ? 'Tạo đơn' : 'Cập nhật'}
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>

<!-- Item Template (hidden) -->
<div id="itemTemplate" style="display: none;">
    <div class="card mb-3">
        <div class="card-body">
            <div class="row">
                <div class="col-md-4">
                    <div class="form-group">
                        <label>Tên mục</label>
                        <select name="itemId" class="form-control item-select" required 
                                onchange="updatePrice(this)">
                            <option value="">Chọn mục</option>
                            <c:forEach var="item" items="${items}">
                                <option value="${item.laundryItemId}" 
                                        data-price="${item.defaultPrice}"
                                        data-unit="${item.unit}">
                                    ${item.itemName} (${item.unit})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="form-group">
                        <label>Số lượng</label>
                        <input type="number" name="quantity" class="form-control" 
                               min="1" value="1" required onchange="calculateSubtotal(this)">
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="form-group">
                        <label>Đơn giá</label>
                        <input type="number" name="price" class="form-control" 
                               step="0.01" min="0" value="0" required onchange="calculateSubtotal(this)">
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="form-group">
                        <label>Tạm tính</label>
                        <input type="text" class="form-control subtotal" readonly value="0.00">
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="form-group">
                        <label>&nbsp;</label>
                        <button type="button" class="btn btn-danger btn-block" onclick="removeItem(this)">
                            <i class="fas fa-trash"></i> Xóa
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Add new item row
    function addItem() {
        const template = document.getElementById('itemTemplate');
        const clone = template.cloneNode(true);
        clone.style.display = 'block';
        clone.removeAttribute('id');
        document.getElementById('itemsContainer').appendChild(clone.firstElementChild);
    }

    // Remove item row
    function removeItem(button) {
        const itemRow = button.closest('.card');
        itemRow.remove();
        calculateTotal();
    }

    // Update price when item is selected
    function updatePrice(select) {
        const selectedOption = select.options[select.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        const row = select.closest('.card-body');
        const priceInput = row.querySelector('input[name="price"]');
        priceInput.value = price || 0;
        calculateSubtotal(select);
    }

    // Calculate subtotal for a row
    function calculateSubtotal(element) {
        const row = element.closest('.card-body');
        const quantity = parseFloat(row.querySelector('input[name="quantity"]').value) || 0;
        const price = parseFloat(row.querySelector('input[name="price"]').value) || 0;
        const subtotal = quantity * price;
        row.querySelector('.subtotal').value = subtotal.toFixed(2);
        calculateTotal();
    }

    // Calculate total amount
    function calculateTotal() {
        let total = 0;
        document.querySelectorAll('.subtotal').forEach(function(input) {
            total += parseFloat(input.value) || 0;
        });
        document.getElementById('totalAmount').textContent = '$' + total.toFixed(2);
    }

    // Form validation
    document.getElementById('orderForm').addEventListener('submit', function(e) {
        const items = document.querySelectorAll('#itemsContainer .card');
        if (items.length === 0) {
            e.preventDefault();
            alert('Vui lòng thêm ít nhất một mục vào đơn hàng.');
            return false;
        }
    });

    // Calculate initial total on page load
    document.addEventListener('DOMContentLoaded', function() {
        calculateTotal();
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" /> 