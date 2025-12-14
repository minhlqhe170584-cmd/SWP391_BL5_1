<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<style>
    /* Status Badge Styling */
    .status-badge {
        padding: 6px 15px;
        border-radius: 30px;
        font-size: 13px;
        font-weight: 600;
        display: inline-block;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }
    .status-PENDING { background-color: #ffc107; color: #000; }
    .status-PROCESSING { background-color: #17a2b8; color: #fff; }
    .status-WASHING { background-color: #007bff; color: #fff; }
    .status-DRYING { background-color: #6c757d; color: #fff; }
    .status-READY { background-color: #28a745; color: #fff; }
    .status-DELIVERED { background-color: #20c997; color: #fff; }
    .status-COMPLETED { background-color: #198754; color: #fff; }
    .status-CANCELLED { background-color: #dc3545; color: #fff; }
    
    /* Custom Info Row Styling */
    .info-row {
        display: flex;
        padding: 12px 0;
        border-bottom: 1px solid #f9f9f9;
        align-items: center;
    }
    .info-row:last-child {
        border-bottom: none;
    }
    .info-label {
        font-weight: 700;
        width: 160px; /* Adjusted for BS4 cards */
        color: #34395e;
        font-size: 14px;
    }
    .info-value {
        flex: 1;
        color: #666;
        font-size: 14px;
    }
    .total-row {
        font-size: 18px;
        font-weight: bold;
        color: #28a745;
        background-color: #fcfcfc;
    }
</style>

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="laundry-order?action=list" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>Order Details #${order.laundryId}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item"><a href="laundry-orders">Laundry</a></div>
                <div class="breadcrumb-item active">Details</div>
            </div>
        </div>

        <div class="section-body">
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        <i class="fas fa-check-circle mr-2"></i>
                        <c:choose>
                            <c:when test="${param.success == 'updated'}">Order updated successfully!</c:when>
                            <c:when test="${param.success == 'status_updated'}">Status updated successfully!</c:when>
                            <c:otherwise>Operation successful!</c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>

            <div class="row">
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4><i class="fas fa-info-circle text-primary mr-2"></i> General Information</h4>
                        </div>
                        <div class="card-body">
                            <div class="info-row">
                                <div class="info-label">Laundry ID</div>
                                <div class="info-value"><strong>#${order.laundryId}</strong></div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Service Order ID</div>
                                <div class="info-value">#${order.orderId}</div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Room ID</div>
                                <div class="info-value">
                                    <c:if test="${not empty order.serviceOrder}">
                                        <span class="badge badge-light">Room ${order.roomNumber}</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Status</div>
                                <div class="info-value">
                                    <span class="status-badge status-${order.serviceOrder.status}">
                                        ${order.serviceOrder.status}
                                    </span>
                                </div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Order Date</div>
                                <div class="info-value">
                                    <c:if test="${not empty order.serviceOrder && not empty order.serviceOrder.orderDate}">
                                        <fmt:formatDate value="${order.serviceOrder.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4><i class="fas fa-clock text-info mr-2"></i> Schedule</h4>
                        </div>
                        <div class="card-body">
                            <div class="info-row">
                                <div class="info-label">Pickup Time</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty order.pickupTime}">
                                             ${order.formattedPickupTime}
                                        </c:when>
                                        <c:otherwise><span class="text-muted text-small">Not set</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Expect Pick Up</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty order.expectedPickupTime}">
                                            <i class="far fa-calendar-alt mr-1"></i> ${order.formattedExpectedPickupTime}
                                        </c:when>
                                        <c:otherwise><span class="text-muted text-small">Not set</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="info-row">
                                <div class="info-label">Expect Return</div>
                                <div class="info-value">
                                    <c:choose>
                                        <c:when test="${not empty order.expectedReturnTime}">
                                            <i class="far fa-calendar-check mr-1"></i> ${order.formattedExpectedReturnTime}
                                        </c:when>
                                        <c:otherwise><span class="text-muted text-small">Not returned yet</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty order.note}">
                        <div class="card mt-4">
                            <div class="card-header">
                                <h4><i class="fas fa-sticky-note text-warning mr-2"></i> Notes</h4>
                            </div>
                            <div class="card-body">
                                <p class="mb-0 text-muted">${order.note}</p>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4><i class="fas fa-list text-success mr-2"></i> Order Items</h4>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-striped table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Item Name</th>                                          
                                            <th>Unit</th>
                                            <th>Qty</th>
                                            <th>Unit Price</th>
                                            <th class="text-right">Subtotal</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="total" value="0" />
                                        <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td><strong>${detail.laundryItem.itemName}</strong></td>
                                                <td>${detail.laundryItem.unit}</td>
                                                <td>${detail.quantity}</td>
                                                <td>                                                   
                                                    <fmt:formatNumber value="${detail.unitPrice}" 
                                                                            type="currency" 
                                                                            currencySymbol="" 
                                                                            maxFractionDigits="0"/> VNĐ
                                                </td>
                                                <td class="text-right">
                                                    <strong>
                                                        <fmt:formatNumber value="${detail.subtotal}" 
                                                                            type="currency" 
                                                                            currencySymbol="" 
                                                                            maxFractionDigits="0"/> VNĐ
                                                    </strong>
                                                </td>
                                            </tr>
                                            <c:set var="total" value="${total + detail.subtotal}" />
                                        </c:forEach>

                                        <c:if test="${empty order.orderDetails}">
                                            <tr>
                                                <td colspan="5" class="text-center py-5">
                                                    <div class="empty-state">
                                                        <div class="empty-state-icon bg-secondary">
                                                            <i class="fas fa-box-open"></i>
                                                        </div>
                                                        <h2>No items in this order</h2>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                    <c:if test="${not empty order.orderDetails}">
                                        <tfoot>
                                            <tr class="total-row">
                                                <td colspan="5" class="text-right">Total Amount:</td>
                                                <td class="text-right">                                                  
                                                    <fmt:formatNumber value="${total}" 
                                                                            type="currency" 
                                                                            currencySymbol="" 
                                                                            maxFractionDigits="0"/> VNĐ
                                                </td>
                                            </tr>
                                        </tfoot>
                                    </c:if>
                                </table>
                            </div>
                        </div>
                            <div class="card-footer bg-whitesmoke text-right">
                            <c:choose>
                                <c:when test="${order.serviceOrder.status.equalsIgnoreCase('pending')}">
                                <div class="card-footer bg-whitesmoke text-right">
                                    <a href="laundry-order?action=updateStatus&id=${order.laundryId}" class="btn btn-warning btn-icon icon-left">
                                        <i class="fas fa-sync"></i> Update Status
                                    </a>
                                    <a href="laundry-order?action=edit&id=${order.laundryId}" class="btn btn-primary btn-icon icon-left">
                                        <i class="fas fa-edit"></i> Edit Order
                                    </a>
                                    <button type="button" class="btn btn-danger btn-icon icon-left" onclick="confirmDelete(${order.laundryId})">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                    <a href="laundry-order" class="btn btn-secondary ">Cancel</a>    
                                </div>
                                </c:when>
                                <c:otherwise>
                                    <a href="laundry-order" class="btn btn-secondary" >Cancel</a>    
                                </c:otherwise>
                            </c:choose> 
                            </div>
                    </div>
                </div>
            </div>
            
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script>
    function confirmDelete(id) {
        if (confirm('Are you sure you want to delete this order? This action cannot be undone.')) {
            window.location.href = 'laundry-order?action=delete&id=' + id;
        }
    }
    
    // Auto-hide alerts (jQuery for BS4)
    $(document).ready(function() {
        setTimeout(function() {
            $(".alert").fadeOut("slow");
        }, 5000);
    });
</script>