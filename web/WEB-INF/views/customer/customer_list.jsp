<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Customer List</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; cursor: pointer; }
        .pagination { margin-top: 20px; text-align: center; }
        .pagination a { padding: 8px 16px; text-decoration: none; border: 1px solid #ddd; margin: 0 4px; }
        .pagination a.active { background-color: #4CAF50; color: white; border: 1px solid #4CAF50; }
        .search-box { margin-bottom: 20px; }
    </style>
</head>
<body>

    <h2>Customer Management</h2>

    <div class="search-box">
        <form action="customers" method="get">
            <input type="text" name="keyword" placeholder="Search name, email..." value="${keyword}">
            <button type="submit">Search</button>
            <a href="customers"><button type="button">Reset</button></a>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>
                    <a href="customers?page=${currentPage}&keyword=${keyword}&sortBy=full_name&sortOrder=${sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                        Full Name ${sortBy == 'full_name' ? (sortOrder == 'ASC' ? '▲' : '▼') : ''}
                    </a>
                </th>
                <th>
                    <a href="customers?page=${currentPage}&keyword=${keyword}&sortBy=email&sortOrder=${sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                        Email ${sortBy == 'email' ? (sortOrder == 'ASC' ? '▲' : '▼') : ''}
                    </a>
                </th>
                <th>Phone</th>
                <th>Status</th>
                <th>
                    <a href="customers?page=${currentPage}&keyword=${keyword}&sortBy=create_at&sortOrder=${sortOrder == 'ASC' ? 'DESC' : 'ASC'}">
                        Created At ${sortBy == 'create_at' ? (sortOrder == 'ASC' ? '▲' : '▼') : ''}
                    </a>
                </th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty customerList}">
                    <c:forEach var="c" items="${customerList}">
                        <tr>
                            <td>${c.customerId}</td>
                            <td>${c.fullName}</td>
                            <td>${c.email}</td>
                            <td>${c.phone}</td>
                            <td>
                                <c:if test="${c.isActive}"><span style="color:green">Active</span></c:if>
                                <c:if test="${!c.isActive}"><span style="color:red">Inactive</span></c:if>
                            </td>
                            <td>${c.createAt}</td>
                            <td>
                                <a href="edit-customer?id=${c.customerId}">Edit</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align:center">No customers found.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <div class="pagination">
        <c:if test="${totalPages > 0}">
            
            <c:if test="${currentPage > 1}">
                <a href="customers?page=${currentPage - 1}&keyword=${keyword}&sortBy=${sortBy}&sortOrder=${sortOrder}">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="customers?page=${i}&keyword=${keyword}&sortBy=${sortBy}&sortOrder=${sortOrder}" 
                   class="${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="customers?page=${currentPage + 1}&keyword=${keyword}&sortBy=${sortBy}&sortOrder=${sortOrder}">Next</a>
            </c:if>
            
        </c:if>
    </div>

</body>
</html>