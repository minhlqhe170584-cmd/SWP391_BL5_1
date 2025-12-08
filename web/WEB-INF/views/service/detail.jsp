<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="service" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${empty service ? 'Add New Service' : 'Update Service'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="service">Service Management</a></div>
                <div class="breadcrumb-item">${empty service ? 'Add New' : 'Update'}</div>
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
                            <h4>${empty service ? 'Add Service Form' : 'Update Service Form'}</h4>
                        </div>
                        <div class="card-body">
                            
                            <form method="POST" action="service" class="needs-validation" novalidate="">
                                <input type="hidden" name="serviceId" value="${service.serviceId}"/>
                                
                                <div class="form-group">
                                    <label>Service Name <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-concierge-bell"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="serviceName" 
                                               value="${service.serviceName}" required placeholder="Ex: Motorbike rental, Laundry...">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Category <span class="text-danger">*</span></label>
                                    <select class="form-control select2" name="categoryId" required>
                                        <option value="">-- Select Category --</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.categoryId}" <c:if test="${not empty service && service.categoryId == cat.categoryId}">selected</c:if>>
                                                ${cat.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Price <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text">
                                                        <i class="fas fa-dollar-sign"></i>
                                                    </div>
                                                </div>
                                                <input type="number" step="0.01" class="form-control" name="price" 
                                                       value="${service.price}" required placeholder="0.00">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Unit</label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text">
                                                        <i class="fas fa-balance-scale"></i>
                                                    </div>
                                                </div>
                                                <input type="text" class="form-control" name="unit" 
                                                       value="${service.unit}" placeholder="Ex: Hour, Item, Time...">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Image URL</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-image"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="imageUrl" 
                                               value="${service.imageUrl}" placeholder="http://example.com/image.jpg">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Status</div>
                                    <label class="custom-switch mt-2 pl-0">
                                        <input type="checkbox" name="isActive" value="true" class="custom-switch-input" 
                                            <c:if test="${empty service || service.isActive}">checked</c:if>>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">Active</span>
                                    </label>
                                </div>
                                
                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> ${empty service ? 'Save Service' : 'Save Changes'}
                                    </button>
                                    <a href="service" class="btn btn-secondary btn-lg ml-2">Cancel</a>
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
                            <p>Fill in all information to create a new service.</p>
                            <div class="alert alert-secondary">
                                <b>Service Price:</b> Enter accurately for automatic invoice calculation.
                            </div>
                            <div class="alert alert-secondary">
                                <b>Unit:</b> Helps customers understand pricing (e.g., per hour, per item, or per quantity).
                            </div>
                            <p>If "Active" is unchecked, this service will be hidden from customer booking lists but remains in the system.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />