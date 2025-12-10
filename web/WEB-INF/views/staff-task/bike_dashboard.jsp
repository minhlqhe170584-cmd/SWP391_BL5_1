<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Bike Rental Operations</h1>
        </div>
        <div class="section-body">
            
            <ul class="nav nav-tabs mb-3">
                <li class="nav-item">
                    <a class="nav-link ${view == 'pending' ? 'active' : ''}" href="bike-ops?view=pending">
                        <i class="fas fa-clock"></i> Pending (Handover)
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link ${view == 'active' ? 'active' : ''}" href="bike-ops?view=active">
                        <i class="fas fa-running"></i> Active Rentals (Return)
                    </a>
                </li>
            </ul>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger">Error: Please select at least one bike!</div>
            </c:if>
            <c:if test="${not empty param.msg}">
                <div class="alert alert-success">Action completed successfully!</div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped">
                            <thead class="thead-light">
                                <tr>
                                    <th>Order ID</th>
                                    <th>Room</th>
                                    <th>Schedule</th>
                                    <th>Total</th>
                                    <th>Note</th>
                                    <th style="min-width: 250px;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty orders}">
                                    <tr>
                                        <td colspan="6" class="text-center">No orders found in this list.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="o" items="${orders}">
                                    <tr>
                                        <td>#${o.orderId}</td>
                                        <td><span class="badge badge-info">${o.roomNumber}</span></td>
                                        <td>
                                            <small>
                                                Start: <fmt:formatDate value="${o.bookingStartDate}" pattern="dd/MM HH:mm"/><br>
                                                End: <fmt:formatDate value="${o.bookingEndDate}" pattern="dd/MM HH:mm"/>
                                            </small>
                                        </td>
                                        <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="VND"/></td>
                                        <td>${o.note}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${view == 'pending'}">
                                                    <form action="bike-ops" method="POST">
                                                        <input type="hidden" name="action" value="handover">
                                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                                        
                                                        <div class="form-group mb-2">
                                                            <label class="font-weight-bold text-primary">Assign Bikes:</label><br>
                                                            <div style="max-height: 100px; overflow-y: auto;">
                                                                <c:forEach var="b" items="${bikes}">
                                                                    <div class="custom-control custom-checkbox">
                                                                        <input type="checkbox" class="custom-control-input" 
                                                                               id="bike_${b.bikeId}_${o.orderId}" name="bikeIds" value="${b.bikeId}">
                                                                        <label class="custom-control-label" for="bike_${b.bikeId}_${o.orderId}">
                                                                            ${b.bikeCode} <small class="text-muted">(${b.condition})</small>
                                                                        </label>
                                                                    </div>
                                                                </c:forEach>
                                                                <c:if test="${empty bikes}">
                                                                    <span class="text-danger small">No bikes available!</span>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                        <button type="submit" class="btn btn-primary btn-sm btn-block" ${empty bikes ? 'disabled' : ''}>
                                                            <i class="fas fa-key"></i> Handover
                                                        </button>
                                                    </form>
                                                </c:when>
                                                
                                                <c:when test="${view == 'active'}">
                                                    <form action="bike-ops" method="POST">
                                                        <input type="hidden" name="action" value="return">
                                                        <input type="hidden" name="orderId" value="${o.orderId}">
                                                        <button type="submit" class="btn btn-success btn-sm" 
                                                                onclick="return confirm('Confirm that guest has returned the bike and paid?');">
                                                            <i class="fas fa-check-double"></i> Return & Pay
                                                        </button>
                                                    </form>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />