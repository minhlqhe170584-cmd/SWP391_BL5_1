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
            <h1>${service.serviceId == 0 ? 'Add New Service' : 'Update Service'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="service">Service Management</a></div>
                <div class="breadcrumb-item">${service.serviceId == 0 ? 'Add New' : 'Update'}</div>
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
                            <h4>${service.serviceId == 0 ? 'Add Service Form' : 'Update Service Form'}</h4>
                        </div>
                        <div class="card-body">

                            <form method="POST" action="service" class="needs-validation" novalidate="" enctype="multipart/form-data">
                                <input type="hidden" name="serviceId" value="${service.serviceId}"/>
                                
                                <input type="hidden" name="currentImage" value="${service.imageUrl}"/>

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

                                <div class="form-group">
                                    <label>Service Image</label>
                                    <c:if test="${not empty service.imageUrl}">
                                        <div class="mb-2">
                                            <img src="${pageContext.request.contextPath}/${service.imageUrl}" alt="Current Image" class="img-thumbnail" style="max-height: 150px; max-width: 100%;">
                                            <small class="d-block text-muted">Current image</small>
                                        </div>
                                    </c:if>
                                    <div class="custom-file">
                                        <input type="file" class="custom-file-input" id="customFile" name="imageFile" accept="image/*">
                                        <label class="custom-file-label" for="customFile">Choose file (Leave empty to keep current)</label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Status</div>
                                    <label class="custom-switch mt-2 pl-0">
                                        <input type="checkbox" name="isActive" value="true" class="custom-switch-input" 
                                               <c:if test="${service.serviceId == 0 || service.isActive}">checked</c:if>>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">Active</span>
                                    </label>
                                </div>

                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> ${service.serviceId == 0 ? 'Save Service' : 'Save Changes'}
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
                            <p>Fill in information to identify the service type.</p>
                            <div class="alert alert-secondary">
                                <b>Note:</b> Pricing is now managed in separate configurations (e.g., Bike Rental Options), not directly here.
                            </div>
                            <p>If "Active" is unchecked, this service will be hidden from customer booking lists.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script>
    document.querySelector('.custom-file-input').addEventListener('change', function(e) {
        var fileName = document.getElementById("customFile").files[0].name;
        var nextSibling = e.target.nextElementSibling;
        nextSibling.innerText = fileName;
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />