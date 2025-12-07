<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty service ? 'Add New Service' : 'Update Service'}</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .form-container { background: white; padding: 20px; border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 600px; margin: 0 auto; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input[type=text], 
        .form-group input[type=number],
        .form-group select { width: 100%; padding: 8px; box-sizing: border-box;
            border: 1px solid #ccc; border-radius: 4px; }
        .btn-submit { background-color: #28a745; color: white;
            border: none; padding: 10px 20px; cursor: pointer; border-radius: 3px; font-size: 16px; }
        .btn-cancel { background-color: #6c757d; color: white; text-decoration: none; padding: 10px 20px;
            border-radius: 3px; margin-left: 10px; font-size: 16px; }
        h2 { text-align: center; color: #333; }
        .error-msg { color: red; text-align: center; margin-bottom: 15px; }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>${empty service ? '➕ Add Service' : '✏️ Update Service'}</h2>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-msg">${errorMessage}</div>
        </c:if>
        
        <form method="POST" action="service">
            <input type="hidden" name="serviceId" value="${service.serviceId}"/>
            
            <div class="form-group">
                <label>Service Name (*):</label>
                <input type="text" name="serviceName" value="${service.serviceName}" required placeholder="Enter service name">
            </div>
            
            <div class="form-group">
                <label>Category (*):</label>
                <select name="categoryId" required>
                    <option value="">-- Select Category --</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}" <c:if test="${not empty service && service.categoryId == cat.categoryId}">selected</c:if>>
                            ${cat.categoryName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label>Price (*):</label>
                <input type="number" step="0.01" name="price" value="${service.price}" required placeholder="0.00">
            </div>

            <div class="form-group">
                <label>Unit:</label>
                <input type="text" name="unit" value="${service.unit}" placeholder="e.g. Per Hour, Per Item">
            </div>

            <div class="form-group">
                <label>Image URL:</label>
                <input type="text" name="imageUrl" value="${service.imageUrl}" placeholder="http://example.com/image.jpg">
            </div>
            
            <div class="form-group" style="display: flex; align-items: center;">
                <input type="checkbox" name="isActive" id="isActive" value="true" style="width: auto; margin-right: 10px;" 
                    <c:if test="${empty service || service.isActive}">checked</c:if>>
                <label for="isActive" style="margin-bottom: 0;">Active</label>
            </div>
            
            <div style="text-align: center; margin-top: 20px;">
                <button type="submit" class="btn-submit">Save</button>
                <a href="service" class="btn-cancel">Cancel</a>
            </div>
        </form>
    </div>

</body>
</html>