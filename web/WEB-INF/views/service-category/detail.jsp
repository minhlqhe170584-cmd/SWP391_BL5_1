<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="service-category" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${empty category.categoryId ? 'Add New Category' : 'Update Category'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="service-category">Category Management</a></div>
                <div class="breadcrumb-item">${empty category.categoryId ? 'Add New' : 'Update'}</div>
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
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4>${empty category.categoryId ? 'Add Category Form' : 'Update Category Form'}</h4>
                        </div>
                        <div class="card-body">
                            
                            <form method="post" action="service-category" class="needs-validation" novalidate="">
                                <input type="hidden" name="categoryId" value="${category.categoryId}" />
                                
                                <div class="form-group">
                                    <label>Category Name <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-tags"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" name="categoryName" 
                                               value="${category.categoryName}" required placeholder="Ex: Food, Laundry...">
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea class="form-control" name="description" rows="4" style="height: 100px;" 
                                              placeholder="Enter detailed description for this category">${category.description}</textarea>
                                </div>
                                
                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> ${empty category.categoryId ? 'Save Category' : 'Save Changes'}
                                    </button>
                                    <a href="service-category" class="btn btn-secondary btn-lg ml-2">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card card-info">
                        <div class="card-header">
                            <h4><i class="fas fa-info-circle"></i> Information</h4>
                        </div>
                        <div class="card-body">
                            <p>Service categories help classify amenities provided by the hotel.</p>
                            <ul>
                                <li><strong>Category Name:</strong> Must be unique and concise.</li>
                                <li><strong>Description:</strong> Helps staff understand the scope of this category.</li>
                            </ul>
                            <div class="alert alert-light">
                                <b>Note:</b> Deleting a category may affect child services belonging to it.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />