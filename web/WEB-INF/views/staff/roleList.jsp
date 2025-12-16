<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Staff Roles Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Roles List</h4>
                    <div class="card-header-action">
                        <a href="staffRoles?action=add" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Role
                        </a>
                    </div>
                </div>
                
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <form method="GET" action="staffRoles">
                                <%-- Giữ lại action=list để Servlet biết đường điều hướng --%>
                                <input type="hidden" name="action" value="list">
                                <div class="input-group">
                                    <input type="text" name="search" class="form-control" 
                                           placeholder="Search role name..." value="${searchQuery}">
                                    <div class="input-group-append">
                                        <button class="btn btn-primary" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-info alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert">
                                    <span>&times;</span>
                                </button>
                                ${sessionScope.message}
                            </div>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <div class="table-responsive">
                        <table class="table table-striped table-md">
                            <thead>
                                <tr>
                                    <th style="width: 10%">ID</th>
                                    <th style="width: 60%">Role Name</th>
                                    <th style="width: 30%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty rolesList}">
                                        <tr>
                                            <td colspan="3" class="text-center">No roles found.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="role" items="${rolesList}">
                                            <tr>
                                                <td>${role.roleId}</td>
                                                <td>
                                                    <span class="badge badge-light" style="font-size: 14px;">
                                                        ${role.roleName}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="staffRoles?action=edit&roleId=${role.roleId}" 
                                                       class="btn btn-warning btn-sm" 
                                                       data-toggle="tooltip" title="Edit">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </a>

                                                    <form method="POST" action="staffRoles" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete"/>
                                                        <input type="hidden" name="roleId" value="${role.roleId}"/>
                                                        <button type="submit" class="btn btn-danger btn-sm" 
                                                                onclick="return confirm('WARNING: Are you sure you want to delete [${role.roleName}]?');"
                                                                data-toggle="tooltip" title="Delete">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="card-footer text-right">
                        <nav class="d-inline-block">
                            <ul class="pagination mb-0">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="staffRoles?action=list&search=${searchQuery}&page=${currentPage - 1}" tabindex="-1">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="staffRoles?action=list&search=${searchQuery}&page=${i}">
                                            ${i} <c:if test="${currentPage == i}"><span class="sr-only">(current)</span></c:if>
                                        </a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="staffRoles?action=list&search=${searchQuery}&page=${currentPage + 1}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </c:if>

            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />