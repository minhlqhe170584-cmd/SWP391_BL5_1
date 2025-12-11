<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"></jsp:include>

<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>${food != null ? 'Edit Food' : 'Add New Food'}</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="foods">Food List</a></li>
                        <li class="breadcrumb-item active">Detail</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section class="content">
        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card card-primary">
                    <div class="card-header">
                        <h3 class="card-title">Food Information</h3>
                    </div>
                    
                    <form action="foods" method="post">
                        
                        <div class="card-body">
                            
                            <input type="hidden" name="action" value="${food != null ? 'update' : 'create'}">
                            
                            <c:if test="${food != null}">
                                <input type="hidden" name="foodId" value="${food.foodId}">
                            </c:if>

                            <div class="form-group">
                                <label>Food Name <span class="text-danger">*</span></label>
                                <input type="text" name="name" class="form-control" 
                                       value="${food != null ? food.name : ''}" 
                                       required placeholder="Enter food name">
                            </div>

                            <div class="form-group">
                                <label>Group (Service) <span class="text-danger">*</span></label>
                                <select class="form-control" name="serviceId" required>
                                    <option value="">-- Select Group --</option>
                                    <c:forEach items="${LIST_GROUPS}" var="g">
                                        <option value="${g.serviceId}" 
                                            ${food != null && food.serviceId == g.serviceId ? 'selected' : ''}>
                                            ${g.serviceName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Price (VNĐ) <span class="text-danger">*</span></label>
                                <input type="number" name="price" class="form-control" 
                                       value="${food != null ? food.price : ''}" 
                                       required min="0" step="1000">
                            </div>

                            <div class="form-group">
                                <label>Image URL</label>
                                <input type="text" name="image" class="form-control" 
                                       value="${food != null ? food.imageUrl : ''}" 
                                       placeholder="https://example.com/image.jpg">
                                
                                <c:if test="${food != null && food.imageUrl != null && not empty food.imageUrl}">
                                    <div class="mt-3">
                                        <p>Current Image:</p>
                                        <img src="${food.imageUrl}" style="height: 150px; border-radius: 8px; border: 1px solid #ddd; padding: 5px;">
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Description</label>
                                <textarea name="description" class="form-control" rows="4" placeholder="Enter details...">${food != null ? food.description : ''}</textarea>
                            </div>
                        </div>

                        <div class="card-footer text-right">
                            <a href="foods" class="btn btn-secondary mr-2">Cancel</a>
                            
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> ${food != null ? 'Update Food' : 'Create Food'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>