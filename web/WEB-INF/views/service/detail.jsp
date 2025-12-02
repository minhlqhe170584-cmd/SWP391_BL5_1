<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>${service == null ? "Create Service" : "Update Service"}</h2>

<form method="post" action="service">

    <c:if test="${service != null}">
        <input type="hidden" name="serviceId" value="${service.serviceId}" />
    </c:if>

    Name:<br>
    <input type="text" name="serviceName" value="${service.serviceName}" required /><br><br>

    Price:<br>
    <input type="number" step="0.01" name="price" value="${service.price}" required /><br><br>

    Unit:<br>
    <input type="text" name="unit" value="${service.unit}" /><br><br>

    Image URL:<br>
    <input type="text" name="imageUrl" value="${service.imageUrl}" /><br><br>

    Active:
    <input type="checkbox" name="isActive"
           <c:if test="${service != null && service.isActive}">checked</c:if> /><br><br>

    Category:<br>
    <select name="categoryId">
        <c:forEach items="${categories}" var="c">
            <option value="${c.categoryId}"
                <c:if test="${service != null && c.categoryId == service.categoryId}">
                    selected
                </c:if>
            >
                ${c.categoryName}
            </option>
        </c:forEach>
    </select><br><br>

    <button type="submit">Save</button>
</form>
