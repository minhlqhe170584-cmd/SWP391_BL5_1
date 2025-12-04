<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Service Category Detail</h2>

<c:if test="${not empty errorMessage}">
    <div style="color:red">${errorMessage}</div>
</c:if>

<form method="post" action="service-category">
    <input type="hidden" name="categoryId" value="${category.categoryId}" />

    <div>
        <label>Name:</label>
        <input type="text" name="categoryName" value="${category.categoryName}" />
    </div>

    <div>
        <label>Description:</label>
        <textarea name="description" rows="4" cols="40">${category.description}</textarea>
    </div>

    <button type="submit">Save</button>
    <a href="service-category">Back to list</a>
</form>
