<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Rental Options Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item">Bike Options</div>
            </div>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Options List</h4>
                    <div class="card-header-action">
                        <a href="bike-options?action=detail" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Option
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message");
                       if(message != null) { %>
                        <div class="alert alert-success alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                <%= message %>
                            </div>
                        </div>
                    <% request.getSession().removeAttribute("message"); } %>

                    <form method="get" action="bike-options" class="form-row mb-3">
                        <div class="col-md-3">
                            <input type="text" name="search" class="form-control" placeholder="Search option name..." value="${search}">
                        </div>
                        <div class="col-md-3">
                             <select name="serviceId" class="form-control">
                                <option value="">-- All Bike Types --</option>
                                <c:forEach items="${services}" var="s">
                                    <option value="${s.serviceId}" <c:if test="${serviceId == s.serviceId}">selected</c:if>>${s.serviceName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                             <select name="status" class="form-control">
                                <option value="">-- Status --</option>
                                <option value="active" <c:if test="${status == 'active'}">selected</c:if>>Active</option>
                                <option value="inactive" <c:if test="${status == 'inactive'}">selected</c:if>>Inactive</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                             <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="priceAsc" <c:if test="${sort == 'priceAsc'}">selected</c:if>>Price Low-High</option>
                                <option value="priceDesc" <c:if test="${sort == 'priceDesc'}">selected</c:if>>Price High-Low</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                             <button type="submit" class="btn btn-primary btn-block"><i class="fas fa-filter"></i> Filter</button>
                        </div>
                    </form>

                    <div class="table-responsive">
                       <table class="table table-striped">
                         <thead>
                            <tr>
                                <th>ID</th>
                                <th>Option Name</th>
                                <th>Service Type</th>
                                <th>Duration</th>
                                <th>Price</th>
                                <th>Status</th>
                                <th style="min-width: 150px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="7" class="text-center">No options found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="o" items="${list}">
                                <tr>
                                    <td>#${o.itemId}</td>
                                    <td class="font-weight-bold">${o.optionName}</td>
                                    <td><span class="badge badge-light">${o.serviceName}</span></td>
                                    <td>${o.durationMinutes} mins</td>
                                    <td><fmt:formatNumber value="${o.price}" type="currency" currencySymbol="VND"/></td>
                                    <td>
                                        <a href="bike-options?action=quick-status&id=${o.itemId}&newStatus=${!o.active}" 
                                           class="btn btn-sm ${o.active ? 'btn-success' : 'btn-secondary'}"
                                           style="min-width: 80px; font-weight: bold;"
                                           title="Click to toggle status">
                                            ${o.active ? 'Active' : 'Inactive'}
                                        </a>
                                    </td>
                                    <td>
                                        <a href="bike-options?action=detail&id=${o.itemId}" class="btn btn-warning btn-sm" title="Edit">
                                            <i class="fas fa-pencil-alt"></i> Edit
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>  
                       </table>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <li class="page-item <c:if test='${page <= 1}'>disabled</c:if>">
                                    <a class="page-link" href="bike-options?page=${page - 1}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">Previous</a>
                                </li>
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                                        <a class="page-link" href="bike-options?page=${p}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">${p}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item <c:if test='${page >= totalPages}'>disabled</c:if>">
                                    <a class="page-link" href="bike-options?page=${page + 1}&search=${search}&serviceId=${serviceId}&status=${status}&sort=${sort}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>

                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />