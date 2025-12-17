<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="drinks" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${drink != null ? "Edit Drink" : "Add New Drink"}</h1>
        </div>

        <div class="section-body">
            
            <c:if test="${not empty sessionScope.message}">
                <c:set var="alertType" value="${fn:contains(sessionScope.message, 'Lỗi') ? 'danger' : 'success'}" />
                <div class="alert alert-${alertType} alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                        <i class="fas fa-${alertType == 'danger' ? 'exclamation-triangle' : 'check-circle'}"></i>
                        ${sessionScope.message}
                    </div>
                </div>
                <c:remove var="message" scope="session"/>
            </c:if>
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header"><h4>Drink Information</h4></div>
                        
                        <form action="drinks" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="${drink != null ? 'update' : 'create'}">
                            <c:if test="${drink != null}">
                                <input type="hidden" name="drinkId" value="${drink.drinkId}">
                            </c:if>

                            <div class="card-body">
                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Name</label>
                                    <div class="col-sm-12 col-md-7">
                                        <input type="text" name="name" class="form-control" 
                                               value="${fn:trim(drink.name)}" required placeholder="Ex: Pepsi, Cocktail...">
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Price ($)</label>
                                    <div class="col-sm-12 col-md-7">
                                        <input type="number" step="0.01" name="price" class="form-control" 
                                               value="${drink.price}" required min="0">
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Volume (ml)</label>
                                    <div class="col-sm-12 col-md-7">
                                        <input type="number" step="1" name="volumeMl" class="form-control" 
                                               value="${drink.volumeMl}" placeholder="e.g. 330">
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Category</label>
                                    <div class="col-sm-12 col-md-7">
                                        <select name="serviceId" class="form-control selectric">
                                            <c:forEach var="s" items="${listServices}">
                                                <option value="${s.serviceId}" ${drink.serviceId == s.serviceId ? 'selected' : ''}>
                                                    ${fn:trim(s.serviceName)}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Alcoholic</label>
                                    <div class="col-sm-12 col-md-7">
                                        <div class="form-check mt-2">
                                            <input class="form-check-input" type="checkbox" name="isAlcoholic" 
                                                   value="true" id="alcoholCheck" 
                                                   ${drink.isAlcoholic ? 'checked' : ''}>
                                            <label class="form-check-label" for="alcoholCheck">
                                                Contains Alcohol (Có cồn)
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Image</label>
                                    <div class="col-sm-12 col-md-7">
                                        <input type="file" name="imageFile" class="form-control" 
                                               accept="image/*" style="height: auto; padding: 6px;">
                                        <small class="text-muted">Supports JPG, PNG, GIF (Max 10MB)</small>

                                        <c:if test="${drink.imageUrl != null}">
                                            <div class="mt-2 p-2 bg-light border rounded" style="width: fit-content;">
                                                <span class="d-block small text-muted mb-1">Current Image:</span>
                                                <img src="${pageContext.request.contextPath}/uploads/${fn:trim(drink.imageUrl)}" 
                                                     height="80" style="object-fit: cover; border-radius: 4px;">
                                            </div>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3">Description</label>
                                    <div class="col-sm-12 col-md-7">
                                        <textarea name="description" class="form-control" style="height: 100px;">${fn:trim(drink.description)}</textarea>
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3"></label>
                                    <div class="col-sm-12 col-md-7">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="fas fa-save"></i> ${drink != null ? "Save Changes" : "Create Drink"}
                                        </button>
                                        <a href="drinks" class="btn btn-secondary btn-lg ml-2">Cancel</a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />