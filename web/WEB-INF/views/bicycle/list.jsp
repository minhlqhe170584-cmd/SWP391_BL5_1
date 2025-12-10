<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Bicycle Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item">Bicycles</div>
            </div>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Bicycle List</h4>
                    <div class="card-header-action">
                        <a href="bicycle?action=detail" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Bicycle
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

                    <form method="get" action="bicycle" class="form-row mb-3">
                        <div class="col-md-3">
                            <input type="text" name="search" class="form-control" placeholder="Search code or service..." value="${search}">
                        </div>
                        <div class="col-md-3">
                             <select name="status" class="form-control">
                                <option value="">-- Active Statuses --</option>
                                <option value="All" <c:if test="${status == 'All'}">selected</c:if>>-- Include Deleted --</option>
                                <option value="Available" <c:if test="${status == 'Available'}">selected</c:if>>Available</option>
                                <option value="Rented" <c:if test="${status == 'Rented'}">selected</c:if>>Rented</option>
                                <option value="Maintenance" <c:if test="${status == 'Maintenance'}">selected</c:if>>Maintenance</option>
                                <option value="Deleted" <c:if test="${status == 'Deleted'}">selected</c:if>>Deleted</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                             <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="codeAsc"  <c:if test="${sort == 'codeAsc'}">selected</c:if>>Code (A-Z)</option>
                                <option value="codeDesc" <c:if test="${sort == 'codeDesc'}">selected</c:if>>Code (Z-A)</option>
                                <option value="statusAsc" <c:if test="${sort == 'statusAsc'}">selected</c:if>>Status</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>Newest Input</option>
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
                                <th>Bike Code</th>
                                <th>Service Type</th>
                                <th>Condition</th>
                                <th>Status</th>
                                <th style="min-width: 150px;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="6" class="text-center">No bicycles found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="b" items="${list}">
                                <tr class="${b.status == 'Deleted' ? 'text-muted' : ''}">
                                    <td>#${b.bikeId}</td>
                                    <td class="font-weight-bold">${b.bikeCode}</td>
                                    <td>${b.serviceName}</td>
                                    <td><small>${b.condition}</small></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${b.status == 'Available'}">
                                                <div class="badge badge-success">Available</div>
                                            </c:when>
                                            <c:when test="${b.status == 'Rented'}">
                                                <div class="badge badge-info">Rented</div>
                                            </c:when>
                                            <c:when test="${b.status == 'Maintenance'}">
                                                <div class="badge badge-warning">Maintenance</div>
                                            </c:when>
                                            <c:when test="${b.status == 'Deleted'}">
                                                <div class="badge badge-danger">Deleted</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="badge badge-secondary">${b.status}</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="d-flex">
                                            <c:if test="${b.status != 'Deleted'}">
                                                <a href="bicycle?action=detail&id=${b.bikeId}" class="btn btn-warning btn-sm mr-2" title="Edit">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>
                                            </c:if>

                                            <c:choose>
                                                <c:when test="${b.status == 'Deleted'}">
                                                    <a href="bicycle?action=delete&id=${b.bikeId}" class="btn btn-info btn-sm" 
                                                       onclick="return confirm('Do you want to RESTORE this bicycle to Available status?');" 
                                                       title="Restore">
                                                        <i class="fas fa-trash-restore"></i> Restore
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="bicycle?action=delete&id=${b.bikeId}" class="btn btn-danger btn-sm" 
                                                       onclick="return confirm('Are you sure you want to DELETE this bicycle? It will be moved to trash.');" 
                                                       title="Delete">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
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
                                    <a class="page-link" href="bicycle?page=${page - 1}&search=${search}&status=${status}&sort=${sort}">Previous</a>
                                </li>
                                
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                                        <a class="page-link" href="bicycle?page=${p}&search=${search}&status=${status}&sort=${sort}">${p}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item <c:if test='${page >= totalPages}'>disabled</c:if>">
                                    <a class="page-link" href="bicycle?page=${page + 1}&search=${search}&status=${status}&sort=${sort}">Next</a>
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