<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Service Category Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Category List</h4>
                    <div class="card-header-action">
                        <a href="service-category?action=detail" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Category
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message");
                       if(message != null) { %>
                        <div class="alert alert-info"><%= message %></div>
                    <% request.getSession().removeAttribute("message");
                       } %>

                    <form method="get" action="service-category" class="form-inline mb-3">
                        <div class="input-group">
                            <input type="text" name="search" class="form-control" placeholder="Search category..." value="${search}">
                            <select name="sort" class="form-control ml-2">
                                <option value="">Sort by...</option>
                                <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected</c:if>>Name (A-Z)</option>
                                <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected</c:if>>Name (Z-A)</option>
                                <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected</c:if>>ID (Oldest)</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>ID (Newest)</option>
                            </select>
                            <div class="input-group-append ml-2">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filter</button>
                            </div>
                        </div>
                    </form>

                    <div class="table-responsive">
                       <table class="table table-striped">
                         <thead>
                            <tr>
                                <th>ID</th>
                                <th>Category Name</th>
                                <th>Description</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty categories}">
                                <tr>
                                    <td colspan="4" class="text-center">No categories found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="c" items="${categories}">
                                <tr>
                                    <td>#${c.categoryId}</td>
                                    <td class="font-weight-bold">${c.categoryName}</td>
                                    <td>${c.description}</td>
                                    <td>
                                        <div class="d-flex">
                                            <a href="service-category?action=detail&id=${c.categoryId}" class="btn btn-warning btn-sm mr-2" title="Edit">
                                                <i class="fas fa-pencil-alt"></i>
                                            </a>
                                            <a href="service-category?action=delete&id=${c.categoryId}" class="btn btn-danger btn-sm" 
                                               onclick="return confirm('Are you sure you want to delete this category?');" title="Delete">
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
                                        <a class="page-link" href="service-category?page=${p}&search=${search}&sort=${sort}">${p}</a>
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