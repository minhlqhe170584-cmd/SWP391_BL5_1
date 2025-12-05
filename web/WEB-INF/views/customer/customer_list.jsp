<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Customer Management</h1>
        
        <!-- Filter and Search Section -->
        <form action="customermanagement" method="get" class="mb-4">
            <div class="row">
                <div class="col-md-3">
                    <div class="form-group">
                        <label for="search">Search</label>
                        <input type="text" 
                               class="form-control" 
                               id="search" 
                               name="search" 
                               placeholder="Name, email, or phone..." 
                               value="${search != null ? search : ''}">
                    </div>
                </div>
                
                <div class="col-md-2">
                    <div class="form-group">
                        <label for="status">Status</label>
                        <select class="form-control" id="status" name="status">
                            <option value="">All Status</option>
                            <option value="true" ${status == 'true' ? 'selected' : ''}>Active</option>
                            <option value="false" ${status == 'false' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                </div>
                
                <div class="col-md-3">
                    <div class="form-group">
                        <label for="sort">Sort By</label>
                        <select class="form-control" id="sort" name="sort">
                            <option value="idAsc" ${sort == 'idAsc' || sort == null ? 'selected' : ''}>ID (Ascending)</option>
                            <option value="idDesc" ${sort == 'idDesc' ? 'selected' : ''}>ID (Descending)</option>
                            <option value="nameAsc" ${sort == 'nameAsc' ? 'selected' : ''}>Name (A-Z)</option>
                            <option value="nameDesc" ${sort == 'nameDesc' ? 'selected' : ''}>Name (Z-A)</option>
                            <option value="emailAsc" ${sort == 'emailAsc' ? 'selected' : ''}>Email (A-Z)</option>
                            <option value="emailDesc" ${sort == 'emailDesc' ? 'selected' : ''}>Email (Z-A)</option>
                            <option value="dateAsc" ${sort == 'dateAsc' ? 'selected' : ''}>Date (Oldest)</option>
                            <option value="dateDesc" ${sort == 'dateDesc' ? 'selected' : ''}>Date (Newest)</option>
                        </select>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <label>&nbsp;</label>
                    <div>
                        <button type="submit" class="btn btn-primary">Apply Filters</button>
                        <a href="customer_list" class="btn btn-secondary">Clear</a>
                    </div>
                </div>
            </div>
        </form>
        
        <!-- Summary Section -->
        <div class="alert alert-info">
            Showing ${customers.size()} of ${totalCustomers} customers
            <c:if test="${currentPage > 1 || totalPages > 1}">
                (Page ${currentPage} of ${totalPages})
            </c:if>
        </div>
        
        <!-- Customer Table -->
        <c:choose>
            <c:when test="${empty customers}">
                <div class="alert alert-warning text-center">
                    No customers found matching your criteria.
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead class="thead-dark">
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Status</th>
                                <th>Created At</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="customer" items="${customers}">
                                <tr>
                                    <td>${customer.customerId}</td>
                                    <td>${customer.fullName}</td>
                                    <td>${customer.email}</td>
                                    <td>${customer.phone}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${customer.active}">
                                                <span class="badge badge-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Inactive</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${customer.createAt}" pattern="yyyy-MM-dd HH:mm" />
                                    </td>
                                    <td>
                                        <a href="customer-detail?id=${customer.customerId}" 
                                           class="btn btn-info btn-sm">View</a>
                                        <a href="customer-edit?id=${customer.customerId}" 
                                           class="btn btn-warning btn-sm">Edit</a>
                                        <a href="customer-deactivate?id=${customer.customerId}&status=${!customer.active}" 
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Are you sure you want to ${customer.active ? 'deactivate' : 'activate'} this customer?');">
                                            ${customer.active ? 'Deactivate' : 'Activate'}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
        
        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-center">
                    <!-- Previous Button -->
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" 
                           href="customermanagement?page=${currentPage - 1}&search=${search}&status=${status}&sort=${sort}">
                            Previous
                        </a>
                    </li>
                    
                    <!-- Page Numbers -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" 
                               href="customermanagement?page=${i}&search=${search}&status=${status}&sort=${sort}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                    
                    <!-- Next Button -->
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" 
                           href="customermanagement?page=${currentPage + 1}&search=${search}&status=${status}&sort=${sort}">
                            Next
                        </a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>