<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="bicycle" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${bicycle.bikeId == 0 ? 'Add New Bicycle' : 'Update Bicycle'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="bicycle">Bicycle Management</a></div>
                <div class="breadcrumb-item">${bicycle.bikeId == 0 ? 'Add New' : 'Update'}</div>
            </div>
        </div>

        <div class="section-body">
            
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

            <div class="row">
                <div class="col-12 col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>${bicycle.bikeId == 0 ? 'Add Bicycle Form' : 'Update Bicycle Form'}</h4>
                        </div>
                        <div class="card-body">
                            
                            <form method="POST" action="bicycle" class="needs-validation" novalidate="">
                                <input type="hidden" name="bikeId" value="${bicycle.bikeId}"/>
                                
                                <div class="form-group">
                                    <label>Bike Code <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-bicycle"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="bikeCode" 
                                               value="${bicycle.bikeCode}" required placeholder="Ex: XE-01, MTN-05...">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Service Type (Category) <span class="text-danger">*</span></label>
                                    <select class="form-control select2" name="serviceId" required>
                                        <option value="">-- Select Service Type --</option>
                                        <c:forEach var="s" items="${services}">
                                            <option value="${s.serviceId}" <c:if test="${bicycle.serviceId == s.serviceId}">selected</c:if>>
                                                ${s.serviceName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Current Status</label>
                                            <div class="form-control-plaintext font-weight-bold">
                                                <c:choose>
                                                    <c:when test="${bicycle.bikeId == 0}">
                                                        <span class="badge badge-success">Available (Default)</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${bicycle.status == 'Available'}"><span class="badge badge-success">Available</span></c:when>
                                                            <c:when test="${bicycle.status == 'Rented'}"><span class="badge badge-info">Rented</span></c:when>
                                                            <c:when test="${bicycle.status == 'Maintenance'}"><span class="badge badge-warning">Maintenance</span></c:when>
                                                            <c:when test="${bicycle.status == 'Deleted'}"><span class="badge badge-danger">Deleted</span></c:when>
                                                            <c:otherwise><span class="badge badge-secondary">${bicycle.status}</span></c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <input type="hidden" name="status" value="${bicycle.bikeId == 0 ? 'Available' : bicycle.status}">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Condition</label>
                                            <input type="text" class="form-control" name="condition" 
                                                   value="${bicycle.condition}" placeholder="Ex: New, Scratch on handle...">
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> ${bicycle.bikeId == 0 ? 'Save Bicycle' : 'Save Changes'}
                                    </button>
                                    <a href="bicycle" class="btn btn-secondary btn-lg ml-2">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-4">
                    <div class="card card-info">
                        <div class="card-header">
                            <h4><i class="fas fa-lightbulb"></i> Instructions</h4>
                        </div>
                        <div class="card-body">
                            <p>Manage physical assets (Bicycles) in the warehouse.</p>
                            <ul>
                                <li><strong>Bike Code:</strong> Unique identifier printed on the bike.</li>
                                <li><strong>Service Type:</strong> Links to the pricing model.</li>
                                <li><strong>Status:</strong> Managed automatically. Use the list view to Delete/Restore.</li>
                            </ul>
                            <div class="alert alert-warning">
                                <b>Note:</b> If a bike is broken or lost, use the "Delete" button in the list view to Soft Delete it.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />