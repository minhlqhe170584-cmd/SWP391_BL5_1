<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Customer Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Customer List</h4>
                    <div class="card-header-action">
                        <a href="service?action=detail" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Service
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message");
                       if(message != null) { %>
                        <div class="alert alert-info"><%= message %></div>
                    <% request.getSession().removeAttribute("message");
                       } %>

                    <form method="get" action="customer" class="form-row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="search" class="form-control" placeholder="Search customer info..." value="${search}">
                        </div>
                        <div class="col-md-3">
                             <select name="status" class="form-control">
                                <option value="">-- Customer Status --</option>                              
                                <option value="true" <c:if test="${status == 'true'}">selected</c:if>>Active</option>
                                <option value="false" <c:if test="${status == 'false'}">selected</c:if>>Inactive</option>   
                                    </option>                                
                            </select>
                        </div>
                        <div class="col-md-3">
                             <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected</c:if>>ID (Oldest)</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>ID (Newest)</option>
                                <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected</c:if>>Name (A-Z)</option>
                                <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected</c:if>>Name (Z-A)</option>
                                <option value="emailAsc" <c:if test="${sort == 'emailAsc'}">selected</c:if>>Email (A-Z)</option>
                                <option value="emailDesc"<c:if test="${sort == 'emailDesc'}">selected</c:if>>Email (Z-A)</option>
                                <option value="dateAsc" <c:if test="${sort == 'dateAsc'}">selected</c:if>>Date (Oldest)</option>
                                <option value="dateDesc"<c:if test="${sort == 'dateDesc'}">selected</c:if>>Date (Newest)</option>
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
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Status</th>
                                <th>Created At</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty list}">
                                <tr>
                                    <td colspan="7" class="text-center">No data found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="c" items="${list}">
                                <tr>
                                    <td>#${c.customerId}</td>
                                    <td class="font-weight-bold">${c.fullName}</td>
                                    <td>${c.email}</td>
                                    <td>${c.phone}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.isActive}">
                                                <div class="badge badge-success">Active</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="badge badge-secondary">Inactive</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${c.createdAtAsDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <div class="d-flex">
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
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                                        <a class="page-link" href="customer?page=${p}&search=${search}&status=${status}&sort=${sort}>${p}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>

                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
