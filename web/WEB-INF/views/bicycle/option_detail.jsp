<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="bike-options" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${option.itemId == 0 ? 'Create New Option' : 'Update Option'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="bike-options">Bike Options</a></div>
                <div class="breadcrumb-item">${option.itemId == 0 ? 'Create' : 'Update'}</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12 col-md-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>Option Details</h4>
                        </div>
                        <div class="card-body">
                            
                            <form method="POST" action="bike-options">
                                <input type="hidden" name="itemId" value="${option.itemId}"/>
                                
                                <div class="form-group">
                                    <label>Service Type <span class="text-danger">*</span></label>
                                    <select class="form-control select2" name="serviceId" required>
                                        <option value="">-- Select Bike Type --</option>
                                        <c:forEach var="s" items="${services}">
                                            <option value="${s.serviceId}" <c:if test="${option.serviceId == s.serviceId}">selected</c:if>>
                                                ${s.serviceName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Option Name <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="optionName" 
                                           value="${option.optionName}" required placeholder="Ex: Gói 1 Giờ, Gói Cuối Tuần...">
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Duration (Minutes) <span class="text-danger">*</span></label>
                                            <input type="number" class="form-control" name="duration" 
                                                   value="${option.durationMinutes}" required min="1">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Price (VND) <span class="text-danger">*</span></label>
                                            <input type="number" class="form-control" name="price" 
                                                   value="${option.price}" required min="0">
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Status</div>
                                    <label class="custom-switch mt-2 pl-0">
                                        <input type="checkbox" name="isActive" value="true" class="custom-switch-input" 
                                            <c:if test="${option.itemId == 0 || option.active}">checked</c:if>>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">Active (Available for booking)</span>
                                    </label>
                                </div>
                                
                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> Save Changes
                                    </button>
                                    <a href="bike-options" class="btn btn-secondary btn-lg ml-2">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />