<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<style>
/*    .status-badge {
        padding: 5px 10px;
        border-radius: 20px;
        font-size: 12px;
        font-weight: 600;
        display: inline-block;
        min-width: 80px;
        text-align: center;
    }*/
    .status-PENDING { background-color: #ffc107; color: #000; }
    .status-PROCESSING { background-color: #17a2b8; color: #fff; }
    .status-WASHING { background-color: #007bff; color: #fff; }
    .status-DRYING { background-color: #6c757d; color: #fff; }
    .status-READY { background-color: #28a745; color: #fff; }
    .status-DELIVERED { background-color: #20c997; color: #fff; }
    .status-COMPLETED { background-color: #198754; color: #fff; }
    .status-CANCELLED { background-color: #dc3545; color: #fff; }
    
    .search-box {
        background: #fff;
        padding: 20px;
        border-radius: 3px;
        margin-bottom: 20px;
        box-shadow: 0 4px 8px rgba(0,0,0,0.03);
    }
</style>

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1><i class="fas fa-tshirt mr-2"></i> Laundry Orders Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Laundry</div>
            </div>
        </div>

        <div class="section-body">
            
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        <i class="fas fa-check-circle mr-1"></i>
                        <c:choose>
                            <c:when test="${param.success == 'deleted'}">Order deleted successfully!</c:when>
                            <c:when test="${param.success == 'updated'}">Order updated successfully!</c:when>
                            <c:when test="${param.success == 'status_updated'}">Status updated successfully!</c:when>
                            <c:otherwise>Operation completed successfully!</c:otherwise>
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
                        <i class="fas fa-exclamation-circle mr-1"></i>
                        <c:choose>
                            <c:when test="${param.error == 'delete_failed'}">Failed to delete order!</c:when>
                            <c:when test="${param.error == 'invalid_id'}">Invalid order ID!</c:when>
                            <c:otherwise>An error occurred!</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
            
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h4>Order List</h4>
                    <div class="card-header-action">
                        <a href="laundry-order?action=add" class="btn btn-primary">
                            <i class="fas fa-plus"></i> New Order
                        </a>
                    </div>
                </div>
            <div class="card-body p-0">
            

            <div class="search-box">
                <form action="laundry-order" method="get">
                    <input type="hidden" name="action" value="list">
                    <input type="hidden" name="view" value="${view}">
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label>Search</label>
                            <input type="text" name="search" class="form-control" 
                                   placeholder="Order ID, Room ID, Note..." 
                                   value="${search}">
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <label>Progress</label>
                            <select name="status" class="form-control">
                                <option value="">All Status</option>
                                <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Pending</option>                             
                                <option value="WASHING" ${status == 'WASHING' ? 'selected' : ''}>Washing</option>                               
                                <option value="READY" ${status == 'READY' ? 'selected' : ''}>Ready</option>
                                <option value="DELIVERED" ${status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                <option value="COMPLETED" ${status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
                                <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                            </select>
                        </div>
                        
                        <div class="col-md-3 mb-3">
                            <label>Sort By</label>
                            <select name="sort" class="form-control">
                                <option value="idDesc" ${sort == 'idDesc' ? 'selected' : ''}>Newest First</option>
                                <option value="idAsc" ${sort == 'idAsc' ? 'selected' : ''}>Oldest First</option>
                                <option value="pickupDesc" ${sort == 'pickupDesc' ? 'selected' : ''}>Pickup Date (Newest)</option>
                                <option value="pickupAsc" ${sort == 'pickupAsc' ? 'selected' : ''}>Pickup Date (Oldest)</option>
                                <option value="statusAsc" ${sort == 'statusAsc' ? 'selected' : ''}>Status (A-Z)</option>
                                <option value="statusDesc" ${sort == 'statusDesc' ? 'selected' : ''}>Status (Z-A)</option>
                            </select>
                        </div>
                        
                        <div class="col-md-2 mb-3 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary btn-block">
                                <i class="fas fa-search"></i> Search
                            </button>
                        </div>
                    </div>
                </form>
            </div>
                            
        <ul class="nav nav-tabs mb-3">
            <li class="nav-item">
                <a class="nav-link ${empty view || view == 'Pending' ? 'active' : ''}" 
                   href="laundry-order?view=Pending">
                    <i class="fas fa-clock"></i> Pending
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link ${view == 'Confirmed' ? 'active' : ''}" 
                   href="laundry-order?view=Confirmed">
                    <i class="fas fa-check-circle"></i> Confirmed
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link ${view == 'Completed' ? 'active' : ''}" 
                   href="laundry-order?view=Completed">
                    <i class="fas fa-check-circle"></i> Completed
                </a>
            </li>
            
            <li class="nav-item">
                <a class="nav-link ${view == 'Canceled' ? 'active' : ''}" 
                   href="laundry-order?view=Canceled">
                    <i class="fas fa-times-circle"></i> Canceled
                </a>
            </li>
        </ul>                
                            
            <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Room</th>
                                    <th>Expected Pickup</th>
                                    <th>Pickup Time</th>
                                    <th>Laundry Progress</th>
                                    <th>Note</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td><strong>#${order.laundryId}</strong></td>
                                        <td>
                                            <c:if test="${not empty order.serviceOrder}">
                                                <span class="badge badge-light">Room ${order.serviceOrder.roomId}</span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:if test="${not empty order.expectedPickupTime}">
                                                ${order.formattedExpectedPickupTime}
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:if test="${not empty order.pickupTime}">
                                                ${order.formattedPickupTime}
                                            </c:if>                                       
                                        </td>
                                        <td>
                                            <span class="status-badge status-${order.status}">
                                                ${order.status}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty order.note}">
                                                    <span data-toggle="tooltip" title="${order.note}">
                                                        ${order.note.length() > 20 ? order.note.substring(0, 20) += '...' : order.note}
                                                    </span>
                                                </c:when>
                                                <c:otherwise>-</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-right">
                                            <div class="btn-group">
                                                <a href="laundry-order?action=view&id=${order.laundryId}" 
                                                   class="btn btn-info btn-sm" data-toggle="tooltip" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="laundry-order?action=updateStatus&id=${order.laundryId}" 
                                                   class="btn btn-warning btn-sm" data-toggle="tooltip" title="Status">
                                                    <i class="fas fa-sync"></i>
                                                </a>
                                                <a href="laundry-order?action=edit&id=${order.laundryId}" 
                                                   class="btn btn-primary btn-sm" data-toggle="tooltip" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <button type="button" class="btn btn-danger btn-sm" 
                                                        onclick="confirmDelete(${order.laundryId})" data-toggle="tooltip" title="Cancel">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty orders}">
                                    <tr>
                                        <td colspan="8" class="text-center py-5">
                                            <div class="empty-state">
                                                <div class="empty-state-icon">
                                                    <i class="fas fa-tshirt"></i>
                                                </div>
                                                <h2>No orders found</h2>
                                                <p class="lead">
                                                    Try adjusting your search criteria or add a new order.
                                                </p>
                                            </div>
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
                                    <a class="page-link" href="laundry-order?action=list&page=${currentPage - 1}&search=${search}&status=${status}&view=${view}&sort=${sort}">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                                
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="laundry-order?action=list&page=${i}&search=${search}&status=${status}&view=${view}&sort=${sort}">
                                                ${i}
                                            </a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                                
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="laundry-order?action=list&page=${currentPage + 1}&search=${search}&status=${status}&view=${view}&sort=${sort}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                        <div class="mt-2 text-muted small">
                            Showing ${(currentPage - 1) * 10 + 1} to 
                            ${currentPage * 10 > totalRecords ? totalRecords : currentPage * 10} 
                            of ${totalRecords} entries
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    function confirmDelete(id) {
        // Stisla often comes with SweetAlert, you can upgrade this if you have it
        if (confirm('Are you sure you want to delete this order? This action cannot be undone.')) {
            window.location.href = 'laundry-order?action=cancel&id=' + id;
        }
    }
    
    // Auto-hide alerts (jQuery for BS4)
    $(document).ready(function() {
        setTimeout(function() {
            $(".alert").fadeOut("slow");
        }, 5000);
        
        // Initialize tooltips (Required for BS4)
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>