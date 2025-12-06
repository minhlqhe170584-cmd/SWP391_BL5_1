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
                        <a href="staffRoles?action=add" class="btn btn-primary">➕ Add New Role</a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message"); 
                       if(message != null) { %>
                        <div class="alert alert-info"><%= message %></div>
                    <% request.getSession().removeAttribute("message"); } %>

                    <div class="table-responsive">
                        <table class="table table-striped table-md">
                            <thead>
                                <tr>
                                    <th style="width: 10%">ID</th>
                                    <th style="width: 60%">Name</th>
                                    <th style="width: 30%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty rolesList}">
                                        <tr>
                                            <td colspan="3" class="text-center">Chưa có vai trò nào được tạo.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="role" items="${rolesList}">
                                            <tr>
                                                <td>${role.roleId}</td>
                                                <td>
                                                    <span class="badge badge-light" style="font-size: 14px;">${role.roleName}</span>
                                                </td>
                                                <td>
                                                    <form method="POST" action="staffRoles" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete"/>
                                                        <input type="hidden" name="roleId" value="${role.roleId}"/>
                                                        
                                                        <button type="submit" class="btn btn-danger btn-sm" 
                                                                onclick="return confirm('CẢNH BÁO: Xóa vai trò [${role.roleName}] sẽ ảnh hưởng đến các nhân viên đang giữ chức vụ này.\nBạn có chắc chắn không?');"
                                                                data-toggle="tooltip" title="Xóa">
                                                            <i class="fas fa-trash"></i> Soft Delete
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
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />