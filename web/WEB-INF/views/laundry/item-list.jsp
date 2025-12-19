<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Laundry Items Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active">Laundry Item</div>
            </div>
        </div>

        <div class="section-body">
            <!-- Success/Error Messages -->
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        <i class="fas fa-check-circle"></i>
                        <c:choose>
                            <c:when test="${param.success == 'added'}">Successfully Add A New Item</c:when>
                            <c:when test="${param.success == 'updated'}">Update Item Successfully</c:when>
                            <c:when test="${param.success == 'activated'}">Activate the item!</c:when>
                            <c:when test="${param.success == 'deactivated'}">Deactivate the item!</c:when>
                            <c:otherwise>Thao tác thành công!</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        <i class="fas fa-exclamation-circle"></i>
                        <c:choose>
                            <c:when test="${param.error == 'activate_failed'}">Không thể kích hoạt mục!</c:when>
                            <c:when test="${param.error == 'deactivate_failed'}">Không thể vô hiệu hóa mục!</c:when>
                            <c:when test="${param.error == 'invalid_id'}">ID không hợp lệ!</c:when>
                            <c:otherwise>Đã xảy ra lỗi!</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        ${errorMessage}
                    </div>
                </div>
            </c:if>
            
            <!-- Search and Filter -->
            <div class="card">
                <div class="card-header">
                    <h4>Search & filter</h4>
                </div>
                <div class="card-body">
                    <form action="laundry-item" method="get" class="row">
                        <input type="hidden" name="action" value="list">
                        
                        <div class="form-group col-md-3">
                            <label>Search</label>
                            <input type="text" name="search" class="form-control" 
                                   placeholder="Tên mục, mô tả..." 
                                   value="${search}">
                        </div>
                        
                        <div class="form-group col-md-3">
                            <label>Service</label>
                            <select name="serviceId" class="form-control">
                                <option value="">All</option>
                                <c:forEach var="service" items="${services}">
                                    <option value="${service.serviceId}" 
                                            ${serviceId == service.serviceId ? 'selected' : ''}>
                                        ${service.serviceName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="form-group col-md-2">
                            <label>Status</label>
                            <select name="status" class="form-control">
                                <option value="">All</option>
                                <option value="true" ${status == 'true' ? 'selected' : ''}>Active</option>
                                <option value="false" ${status == 'false' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                        
                        <div class="form-group col-md-2">
                            <label>Sort</label>
                            <select name="sort" class="form-control">
                                <option value="">Name (A-Z)</option>
                                <option value="nameDesc" ${sort == 'nameDesc' ? 'selected' : ''}>Name (Z-A)</option>
                                <option value="priceAsc" ${sort == 'priceAsc' ? 'selected' : ''}>Price (Thấp-Cao)</option>
                                <option value="priceDesc" ${sort == 'priceDesc' ? 'selected' : ''}>Price (Cao-Thấp)</option>
                                <option value="serviceAsc" ${sort == 'serviceAsc' ? 'selected' : ''}>Service (A-Z)</option>
                            </select>
                        </div>
                        
                        <div class="form-group col-md-2">
                            <label>&nbsp;</label>
                            <button type="submit" class="btn btn-primary btn-block">
                                <i class="fas fa-search"></i> Search
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Items List -->
            <div class="card">
                <div class="card-header">
                    <h4>Laundry Item List</h4>
                    <div class="card-header-action">
<!--                        <span class="badge badge-primary">
                            Hiển thị ${(currentPage - 1) * 10 + 1} - 
                            ${currentPage * 10 > totalRecords ? totalRecords : currentPage * 10} 
                            / ${totalRecords} mục
                        </span>-->
                            <a href="laundry-item?action=add" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Add New
                            </a>
                    </div>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped table-md">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Item</th>
                                    <th>Service</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Unit</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>${item.laundryItemId}</td>
                                        <td><strong>${item.itemName}</strong></td>
                                        <td>
                                            <c:if test="${not empty item.service}">
                                                <span class="badge badge-info">${item.service.serviceName}</span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty item.description}">
                                                    ${item.description.length() > 50 ? item.description.substring(0, 50) += '...' : item.description}
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <strong>
                                                <fmt:formatNumber value="${item.defaultPrice}" 
                                                                type="currency" 
                                                                currencySymbol="" 
                                                                maxFractionDigits="0"/>VND
                                            </strong>
                                        </td>
                                        <td><span class="badge badge-light">${item.unit}</span></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.isActive}">
                                                    <span class="badge badge-success">Active</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">InActive</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="laundry-item?action=edit&id=${item.laundryItemId}" 
                                               class="btn btn-primary btn-sm" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <c:choose>
                                                <c:when test="${item.isActive}">
                                                    <button type="button" class="btn btn-warning btn-sm" 
                                                            onclick="confirmDeactivate(${item.laundryItemId})" 
                                                            title="Inactive">
                                                        <i class="fas fa-ban"></i>
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button" class="btn btn-success btn-sm" 
                                                            onclick="confirmActivate(${item.laundryItemId})" 
                                                            title="Active">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty items}">
                                    <tr>
                                        <td colspan="8" class="text-center py-4">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">Don't have any item</p>
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <c:if test="${totalPages > 1}">
                    <div class="card-footer text-right">
                        <nav class="d-inline-block">
                            <ul class="pagination mb-0">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="laundry-item?action=list&page=${currentPage - 1}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                                
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="laundry-item?action=list&page=${i}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                                
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="laundry-item?action=list&page=${currentPage + 1}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </div>
        </div>
    </section>
</div>

<script>
    function confirmActivate(id) {
        if (confirm('Are you sure want to activate this item?')) {
            window.location.href = 'laundry-item?action=activate&id=' + id;
        }
    }
    
    function confirmDeactivate(id) {
        if (confirm('Are you sure want to deactivate this item?')) {
            window.location.href = 'laundry-item?action=deactivate&id=' + id;
        }
    }
    
    // Auto-hide alerts
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
