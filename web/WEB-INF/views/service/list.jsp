<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Service Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item">Services</div>
            </div>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Service List</h4>
                    <div class="card-header-action">
                        <a href="service?action=detail" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Service
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message");
                       if(message != null) { %>
                        <div class="alert alert-success alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert">
                                    <span>&times;</span>
                                </button>
                                <%= message %>
                            </div>
                        </div>
                    <% request.getSession().removeAttribute("message");
                       } %>

                    <form method="get" action="service" class="form-row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="search" class="form-control" placeholder="Search service name..." value="${search}">
                        </div>
                        <div class="col-md-3">
                             <select name="categoryId" class="form-control">
                                <option value="">-- All Categories --</option>
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.categoryId}" <c:if test="${categoryId == c.categoryId}">selected</c:if>>
                                        ${c.categoryName}
                                    </option>
                                </c:forEach> 
                            </select>
                        </div>
                        <div class="col-md-3">
                             <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected</c:if>>Name (A-Z)</option>
                                <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected</c:if>>Name (Z-A)</option>
                                <option value="priceAsc" <c:if test="${sort == 'priceAsc'}">selected</c:if>>Price (Low-High)</option>
                                <option value="priceDesc"<c:if test="${sort == 'priceDesc'}">selected</c:if>>Price (High-Low)</option>
                                <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected</c:if>>ID (Oldest)</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>ID (Newest)</option>
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
                                <th>Service Name</th>
                                <th>Category ID</th>
                                <th>Price</th>
                                <th>Unit</th>
                                <th>Status</th>
                                <th style="min-width: 150px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="7" class="text-center">No data found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="s" items="${list}">
                                <tr>
                                    <td>#${s.serviceId}</td>
                                    <td class="font-weight-bold">${s.serviceName}</td>
                                    <td>
                                        <div class="badge badge-light">
                                            ${s.categoryId} 
                                        </div>
                                    </td>
                                    <td class="text-danger font-weight-bold">
                                        <fmt:formatNumber value="${s.price}" type="currency" currencySymbol="$"/>
                                    </td>
                                    <td>${s.unit}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.isActive}">
                                                <div class="badge badge-success">Active</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="badge badge-secondary">Inactive</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="d-flex">
                                            <a href="service?action=toggle-status&id=${s.serviceId}" 
                                               class="btn btn-sm mr-2 ${s.isActive ? 'btn-secondary' : 'btn-success'}" 
                                               title="${s.isActive ? 'Deactivate' : 'Activate'}"
                                               style="width: 32px;">
                                                <i class="fas ${s.isActive ? 'fa-eye-slash' : 'fa-eye'}"></i>
                                            </a>

                                            <a href="service?action=detail&id=${s.serviceId}" class="btn btn-warning btn-sm mr-2" title="Edit">
                                                <i class="fas fa-pencil-alt"></i>
                                            </a>

                                            <a href="service?action=delete&id=${s.serviceId}" class="btn btn-danger btn-sm" 
                                               onclick="return confirm('Are you sure you want to delete this service?');" title="Delete">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
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
                                    <a class="page-link" href="service?page=${page - 1}&search=${search}&categoryId=${categoryId}&sort=${sort}">Previous</a>
                                </li>
                                
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                                        <a class="page-link" href="service?page=${p}&search=${search}&categoryId=${categoryId}&sort=${sort}">${p}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item <c:if test='${page >= totalPages}'>disabled</c:if>">
                                    <a class="page-link" href="service?page=${page + 1}&search=${search}&categoryId=${categoryId}&sort=${sort}">Next</a>
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