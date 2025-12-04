<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Service Detail</h2>

<c:if test="${not empty errorMessage}">
    <div style="color:red">${errorMessage}</div>
</c:if>

<form method="post" action="service">
    <input type="hidden" name="serviceId" value="${service.serviceId}" />

    <div>
        <label>Name:</label>
        <input type="text" name="serviceName" value="${service.serviceName}" />
    </div>

    <div>
        <label>Price:</label>
        <input type="text" name="price" value="${service.price}" />
    </div>

    <div>
        <label>Unit:</label>
        <input type="text" name="unit" value="${service.unit}" />
    </div>

    <div>
        <label>Image URL:</label>
        <input type="text" name="imageUrl" value="${service.imageUrl}" />
    </div>

    <div>
        <label>Active:</label>
        <input type="checkbox" name="isActive"
               <c:if test="${service.isActive}">checked="checked"</c:if> />
    </div>

    <div>
        <label>Category:</label>
        <select name="categoryId">
            <c:forEach items="${categories}" var="c">
                <option value="${c.categoryId}"
                    <c:if test="${service.categoryId == c.categoryId}">selected="selected"</c:if>>
                    ${c.categoryName}
                </option>
            </c:forEach>
        </select>
    </div>

    <button type="submit">Save</button>
    <a href="service">Back to list</a>
</form>
