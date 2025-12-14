<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>${empty item ? 'Thêm mới' : 'Chỉnh sửa'} mục giặt ủi</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a href="${pageContext.request.contextPath}/laundry-item">Mục giặt ủi</a></div>
                <div class="breadcrumb-item">${empty item ? 'Thêm mới' : 'Chỉnh sửa'}</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12 col-lg-8 offset-lg-2">
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

                    <form action="laundry-item" method="post" id="itemForm">
                        <input type="hidden" name="action" value="${empty item ? 'add' : 'edit'}">
                        <c:if test="${not empty item}">
                            <input type="hidden" name="itemId" value="${item.laundryItemId}">
                        </c:if>

                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-info-circle"></i> Thông tin mục</h4>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label>Dịch vụ <span class="text-danger">*</span></label>
                                    <select name="serviceId" class="form-control" required>
                                        <option value="">Chọn dịch vụ</option>
                                        <c:forEach var="service" items="${services}">
                                            <option value="${service.serviceId}" 
                                                    ${item.serviceId == service.serviceId ? 'selected' : ''}>
                                                ${service.serviceName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Tên mục <span class="text-danger">*</span></label>
                                    <input type="text" name="itemName" class="form-control" 
                                           value="${item.itemName}" required
                                           placeholder="Ví dụ: Áo sơ mi, Quần jean...">
                                </div>

                                <div class="form-group">
                                    <label>Mô tả</label>
                                    <textarea name="description" class="form-control" rows="3" 
                                              placeholder="Nhập mô tả chi tiết về mục...">${item.description}</textarea>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đơn giá <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text">$</span>
                                                </div>
                                                <input type="number" name="defaultPrice" class="form-control" 
                                                       step="0.01" min="0" value="${item.defaultPrice}" required
                                                       placeholder="0.00">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Đơn vị <span class="text-danger">*</span></label>
                                            <select name="unit" class="form-control" required>
                                                <option value="">Chọn đơn vị</option>
                                                <option value="(Cái)" ${item.unit == '(Cái)' ? 'selected' : ''}>(Cái)</option>
                                                <option value="kg" ${item.unit == 'kg' ? 'selected' : ''}>kg</option>
                                                <option value="(Bộ)" ${item.unit == '(Bộ)' ? 'selected' : ''}>(Bộ)</option>
                                                <option value="(Đôi)" ${item.unit == '(Đôi)' ? 'selected' : ''}>(Đôi)</option>
                                                <option value="(Chiếc)" ${item.unit == '(Chiếc)' ? 'selected' : ''}>(Chiếc)</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="custom-control custom-checkbox">
                                        <input type="checkbox" name="isActive" class="custom-control-input" 
                                               id="isActive" ${empty item || item.isActive ? 'checked' : ''}>
                                        <label class="custom-control-label" for="isActive">
                                            Kích hoạt mục này
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer text-right">
                                <a href="laundry-item" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> ${empty item ? 'Thêm mới' : 'Cập nhật'}
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
    // Form validation
    document.getElementById('itemForm').addEventListener('submit', function(e) {
        const itemName = document.querySelector('input[name="itemName"]').value.trim();
        const defaultPrice = parseFloat(document.querySelector('input[name="defaultPrice"]').value);
        
        if (itemName.length < 2) {
            e.preventDefault();
            alert('Tên mục phải có ít nhất 2 ký tự.');
            return false;
        }
        
        if (defaultPrice <= 0) {
            e.preventDefault();
            alert('Đơn giá phải lớn hơn 0.');
            return false;
        }
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />