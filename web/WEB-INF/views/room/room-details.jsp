<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="rooms?action=LIST" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>Room Details</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="rooms?action=LIST">Rooms</a></div>
                <div class="breadcrumb-item">Detail</div>
            </div>
        </div>

        <div class="section-body">
            <h2 class="section-title">Room #${room.roomNumber}</h2>
            <p class="section-lead">
                View detailed information about this room including status, pricing, and configuration.
            </p>

            <div class="row">
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card card-primary">
                        <div class="card-header">
                            <h4><i class="fas fa-info-circle"></i> Basic Information</h4>
                            <div class="card-header-action" hidden="">
                                <c:choose>
                                    <c:when test="${room.status == 'Available'}">
                                        <span class="badge badge-success">Available</span>
                                    </c:when>
                                    <c:when test="${room.status == 'Occupied'}">
                                        <span class="badge badge-danger">Occupied</span>
                                    </c:when>
                                    <c:when test="${room.status == 'Maintenance'}">
                                        <span class="badge badge-warning">Maintenance</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary">${room.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="form-group row">
                                <label class="col-sm-4 col-form-label text-muted">Room Number</label>
                                <div class="col-sm-8">
                                    <p class="font-weight-bold text-dark mb-0" style="font-size: 1.2em;">${room.roomNumber}</p>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-4 col-form-label text-muted">Room Type</label>
                                <div class="col-sm-8">
                                    <p class="mb-0">${room.roomType.typeName}</p>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-4 col-form-label text-muted">Capacity</label>
                                <div class="col-sm-8">
                                    <p class="mb-0"><i class="fas fa-users"></i> ${room.roomType.capacity} Person(s)</p>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-4 col-form-label text-muted">Active Login</label>
                                <div class="col-sm-8">
                                    <c:choose>
                                        <c:when test="${room.activeLogin}">
                                            <span class="badge badge-primary"><i class="fas fa-check"></i> Enabled</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-light text-muted"><i class="fas fa-times"></i> Disabled</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-4 col-form-label text-muted">Current Password</label>
                                <div class="col-sm-8">
                                    <code style="font-size: 1.1em; background-color: #f3f3f3; padding: 4px 8px; border-radius: 4px;">${room.roomPassword}</code>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card card-warning">
                        <div class="card-header">
                            <h4><i class="fas fa-dollar-sign"></i> Pricing & Description</h4>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-6">
                                    <div class="form-group">
                                        <label class="text-muted d-block">Weekday Price</label>
                                        <span class="text-primary font-weight-bold" style="font-size: 1.3em;">
                                            <fmt:formatNumber value="${room.roomType.basePriceWeekday}" type="currency" currencySymbol="$" />
                                        </span>
                                        <small class="text-muted">/ night</small>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="form-group">
                                        <label class="text-muted d-block">Weekend Price</label>
                                        <span class="text-danger font-weight-bold" style="font-size: 1.3em;">
                                            <fmt:formatNumber value="${room.roomType.basePriceWeekend}" type="currency" currencySymbol="$" />
                                        </span>
                                        <small class="text-muted">/ night</small>
                                    </div>
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="form-group">
                                <label class="text-muted">Description</label>
                                <p class="text-justify" style="line-height: 1.6;">
                                    ${room.roomType.description}
                                </p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="text-right mt-4">
                        <a href="rooms?action=LIST" class="btn btn-secondary btn-lg mr-2">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                        <a href="rooms?action=EDIT&id=${room.roomId}" class="btn btn-warning btn-lg">
                            <i class="fas fa-pencil-alt"></i> Edit Information
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />