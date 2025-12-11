<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/common/sidebar.jsp"></jsp:include>

<div class="content-wrapper">
    <section class="content-header">
        <div class="container-fluid">
            <div class="row mb-2">
                <div class="col-sm-6">
                    <h1>${drink != null ? 'Edit Drink' : 'Add New Drink'}</h1>
                </div>
                <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-right">
                        <li class="breadcrumb-item"><a href="drinks">Drink List</a></li>
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
                        <h3 class="card-title">Drink Information</h3>
                    </div>
                    
                    <form action="drinks" method="post">
                        
                        <div class="card-body">
                            
                            <input type="hidden" name="action" value="${drink != null ? 'update' : 'create'}">
                            
                            <c:if test="${drink != null}">
                                <input type="hidden" name="drinkId" value="${drink.drinkId}">
                            </c:if>

                            <div class="form-group">
                                <label>Drink Name <span class="text-danger">*</span></label>
                                <input type="text" name="name" class="form-control" 
                                        value="${drink != null ? drink.name : ''}" 
                                        required placeholder="Enter drink name">
                            </div>

                            <div class="form-group">
                                <label>Group (Service) <span class="text-danger">*</span></label>
                                <select class="form-control" name="serviceId" required>
                                    <option value="">-- Select Group --</option>
                                    <c:forEach items="${LIST_GROUPS}" var="g">
                                        <option value="${g.serviceId}" 
                                            ${drink != null && drink.serviceId == g.serviceId ? 'selected' : ''}>
                                            ${g.serviceName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label>Price (VNĐ) <span class="text-danger">*</span></label>
                                <input type="number" name="price" class="form-control" 
                                        value="${drink != null ? drink.price : ''}" 
                                        required min="0" step="1000">
                            </div>

                            <div class="form-group">
                                <label>Volume (ml) <span class="text-danger">*</span></label>
                                <input type="number" name="volumeMl" class="form-control" 
                                        value="${drink != null ? drink.volumeMl : ''}" 
                                        required min="1" placeholder="Enter volume in ml">
                            </div>
                            
                            <div class="form-group">
                                <div class="custom-control custom-checkbox">
                                    <input type="checkbox" name="isAlcoholic" class="custom-control-input" id="isAlcoholicCheck" 
                                        ${drink != null && drink.isAlcoholic ? 'checked' : ''}>
                                    <label class="custom-control-label" for="isAlcoholicCheck">Is Alcoholic (Đồ uống có cồn)</label>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Image URL</label>
                                <input type="text" name="image" class="form-control" 
                                        value="${drink != null ? drink.imageUrl : ''}" 
                                        placeholder="https://example.com/image.jpg">
                                
                                <c:if test="${drink != null && drink.imageUrl != null && not empty drink.imageUrl}">
                                    <div class="mt-3">
                                        <p>Current Image:</p>
                                        <img src="${drink.imageUrl}" style="height: 150px; border-radius: 8px; border: 1px solid #ddd; padding: 5px;">
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Description</label>
                                <textarea name="description" class="form-control" rows="4" placeholder="Enter details...">${drink != null ? drink.description : ''}</textarea>
                            </div>
                        </div>

                        <div class="card-footer text-right">
                            <a href="drinks" class="btn btn-secondary mr-2">Cancel</a>
                            
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> ${drink != null ? 'Update Drink' : 'Create Drink'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>