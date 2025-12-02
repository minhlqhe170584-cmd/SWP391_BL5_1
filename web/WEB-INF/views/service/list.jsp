<%@page import="models.Service"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<h2>Service List</h2>

<form method="get" action="service">
    <input type="text" name="search" placeholder="Search name..." />
    <select name="categoryId">
        <option value="">All Categories</option>
        <c:forEach items="${categories}" var="c">
            <option value="${c.categoryId}">${c.categoryName}</option>
        </c:forEach>
    </select>
    <button type="submit">Filter</button>
</form>

<a href="service?action=detail">+ Create New Service</a>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Unit</th>
        <th>Active</th>
        <th>Action</th>
    </tr>

    <c:forEach items="${list}" var="s">
        <tr>
            <td>${s.serviceId}</td>
            <td>${s.serviceName}</td>
            <td>${s.price}</td>
            <td>${s.unit}</td>
            <td>${s.isActive}</td>
            <td>
                <a href="service?action=detail&id=${s.serviceId}">Edit</a>
                <a href="service?action=delete&id=${s.serviceId}"
                   onclick="return confirm('Delete?')">Delete</a>
            </td>
        </tr>
    </c:forEach>

</table>
