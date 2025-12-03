<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h2>Service Category List</h2>

<form method="get" action="service-category">
    <input type="text" name="search" placeholder="Search name..." value="${search}" />
    <select name="sort">
        <option value="">Sort by</option>
        <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected="selected"</c:if>>Name ASC</option>
        <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected="selected"</c:if>>Name DESC</option>
        <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected="selected"</c:if>>ID ASC</option>
        <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected="selected"</c:if>>ID DESC</option>
    </select>
    <button type="submit">Filter</button>
</form>

<a href="service-category?action=detail">Create New Category</a>

<table border="1" cellspacing="0" cellpadding="8">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Action</th>
    </tr>

    <c:forEach var="c" items="${categories}">
        <tr>
            <td>${c.categoryId}</td>
            <td>${c.categoryName}</td>
            <td>${c.description}</td>
            <td>
                <a href="service-category?action=detail&id=${c.categoryId}">Edit</a>
                <a href="service-category?action=delete&id=${c.categoryId}"
                   onclick="return confirm('Delete this category?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${totalPages > 1}">
    <div>
        <c:forEach begin="1" end="${totalPages}" var="p">
            <c:choose>
                <c:when test="${p == page}">
                    <span>[${p}]</span>
                </c:when>
                <c:otherwise>
                    <a href="service-category?page=${p}&search=${search}&sort=${sort}">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </div>
</c:if>
