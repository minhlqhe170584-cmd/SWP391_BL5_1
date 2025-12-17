<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="foods" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${food != null ? "Edit Item" : "Add New Item"}</h1>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12 col-md-8 offset-md-2">
                    <div class="card">
                        <div class="card-header">
                            <h4>Item Information</h4>
                        </div>
<!--                                thêm thuộc tính enctype="multipart/form-data"-->
<!--                                đổi từ type="text" sang type="file"-->
                        <form action="foods" method="post" enctype="multipart/form-data">

                            <input type="hidden" name="action" value="${food != null ? 'update' : 'create'}">
                            <c:if test="${food != null}">
                                <input type="hidden" name="foodId" value="${food.foodId}">
                            </c:if>

                            <div class="card-body">
                                <div class="form-group">
                                    <label>Item Name</label>
                                    <input type="text" name="name" class="form-control" 
                                           value="${fn:trim(food.name)}" required>
                                </div>

                                <div class="form-group">
                                    <label>Price ($)</label>
                                    <input type="number" step="0.01" name="price" class="form-control" 
                                           value="${food.price}" required min="0">
                                </div>

                                <div class="form-group">
                                    <label>Service Category</label>
                                    <select name="serviceId" class="form-control selectric">
                                        <c:forEach var="s" items="${listServices}"> <option value="${s.serviceId}" ${food.serviceId == s.serviceId ? 'selected' : ''}>
                                                ${fn:trim(s.serviceName)}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Item Image</label>
                                    <div class="custom-file">
                                        <input type="file" name="imageFile" class="custom-file-input" id="customFile" accept="image/*">
                                        <label class="custom-file-label" for="customFile">Choose file</label>
                                    </div>
                                    <small class="form-text text-muted">Max size: 10MB. Formats: JPG, PNG.</small>

                                    <c:if test="${food.imageUrl != null}">
                                        <div class="mt-3">
                                            <p class="mb-1">Current Image:</p>
                                            <img src="${pageContext.request.contextPath}/uploads/${fn:trim(food.imageUrl)}" 
                                                 width="120" class="img-thumbnail" 
                                                 style="object-fit: cover;">
                                        </div>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea name="description" class="form-control" style="height: 100px;">${fn:trim(food.description)}</textarea>
                                </div>

                                <div class="form-group text-right">
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        ${food != null ? "Save Changes" : "Create Item"}
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script>
    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />