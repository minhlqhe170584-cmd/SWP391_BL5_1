<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty category.categoryId ? 'Add Category' : 'Update Category'}</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f6f9; }
        .form-container { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 500px; margin: 0 auto; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group textarea { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        .btn-submit { background-color: #28a745; color: white; border: none; padding: 10px 20px; cursor: pointer; border-radius: 3px; font-size: 16px; }
        .btn-cancel { background-color: #6c757d; color: white; text-decoration: none; padding: 10px 20px; border-radius: 3px; margin-left: 10px; font-size: 16px; }
        h2 { text-align: center; color: #333; }
        .error-msg { color: red; text-align: center; margin-bottom: 15px; }
    </style>
</head>
<body>

    <div class="form-container">
        <h2>${empty category.categoryId ? '➕ Add Category' : '✏️ Update Category'}</h2>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-msg">${errorMessage}</div>
        </c:if>
        
        <form method="post" action="service-category">
            <input type="hidden" name="categoryId" value="${category.categoryId}" />
            
            <div class="form-group">
                <label>Category Name (*):</label>
                <input type="text" name="categoryName" value="${category.categoryName}" required placeholder="Enter category name">
            </div>
            
            <div class="form-group">
                <label>Description:</label>
                <textarea name="description" rows="4" placeholder="Enter description">${category.description}</textarea>
            </div>
            
            <div style="text-align: center; margin-top: 20px;">
                <button type="submit" class="btn-submit">Save</button>
                <a href="service-category" class="btn-cancel">Cancel</a>
            </div>
        </form>
    </div>

</body>
</html>