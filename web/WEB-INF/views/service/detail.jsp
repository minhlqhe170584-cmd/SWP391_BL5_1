<%@page import="models.Service"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<h2>${service == null ? "Create Service" : "Update Service"}</h2>

<form method="post" action="service">

    <c:if test="${service != null}">
        <input type="hidden" name="serviceId" value="${service.serviceId}" />
    </c:if>

    Name:
    <input type="text" name="serviceName" value="${service.serviceName}" required /><br>

    Price:
    <input type="number" step="0.01" name="price" value="${service.price}" required /><br>

    Unit:
    <input type="text" name="unit" value="${service.unit}" /><br>

    Image URL:
    <input type="text" name="imageUrl" value="${service.imageUrl}" /><br>

    Active:
    <input type="checkbox" name="isActive" ${service != null && service.isActive ? "checked" : ""}><br>

    Category:
    <select name="categoryId">
        <c:forEach items="${categories}" var="c">
            <option value="${c.categoryId}" 
                ${service != null && c.categoryId == service.categoryId ? "selected" : ""}>
                ${c.categoryName}
            </option>
        </c:forEach>
    </select><br><br>

    <button type="submit">Save</button>
</form>
