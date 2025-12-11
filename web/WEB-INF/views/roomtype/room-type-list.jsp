<%-- 
    Document   : room-type-list
    Created on : Dec 11, 2025, 2:11:15 PM
    Author     : My Lap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Room Type Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Room Types</div>
            </div>
        </div>

        <div class="section-body">
            
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                    </div>
                </div>
                <% session.removeAttribute("successMessage"); %>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                        <i class="fas fa-exclamation-triangle"></i> ${sessionScope.errorMessage}
                    </div>
                </div>
                <% session.removeAttribute("errorMessage"); %>
            </c:if>

            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4>All Room Types</h4>
                            <div class="card-header-action">
                                <a href="room-types?action=NEW" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add New Type
                                </a>
                            </div>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-striped table-md">
                                    <thead>
                                        <tr>
                                            <th hidden="">ID</th>
                                            <th>Image</th>
                                            <th>Name</th>
                                            <th>Capacity</th>
                                            <th>Price (Weekday)</th>
                                            <th>Price (Weekend)</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="rt" items="${listRoomTypes}">
                                            <tr>
                                                <td hidden="">${rt.typeId}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty rt.imageUrl}">
                                                            <img alt="image" src="${pageContext.request.contextPath}${rt.imageUrl}" class="rounded" width="50" data-toggle="tooltip" title="${rt.typeName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-light">No Image</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td><strong>${rt.typeName}</strong></td>
                                                <td>
                                                    <i class="fas fa-user-friends text-muted"></i> ${rt.capacity}
                                                </td>
                                                <td>
                                                    <div class="text-primary font-weight-bold">
                                                        <fmt:formatNumber value="${rt.basePriceWeekday}" type="currency" currencySymbol="₫"/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="text-danger font-weight-bold">
                                                        <fmt:formatNumber value="${rt.basePriceWeekend}" type="currency" currencySymbol="₫"/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${rt.isActive}">
                                                            <div class="badge badge-success">Active</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="badge badge-danger">Inactive</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="room-types?action=EDIT&id=${rt.typeId}" class="btn btn-primary btn-sm" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    
                                                    <a href="room-types?action=DELETE&id=${rt.typeId}" class="btn btn-danger btn-sm" title="Delete"
                                                       onclick="return confirm('Are you sure you want to delete this room type?');">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        
                                        <c:if test="${empty listRoomTypes}">
                                            <tr>
                                                <td colspan="8" class="text-center text-muted py-4">
                                                    <i class="fas fa-box-open fa-2x mb-2"></i><br>
                                                    No room types found. Please add a new one.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
