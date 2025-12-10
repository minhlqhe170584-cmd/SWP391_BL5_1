<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Staff Management</h1>
        </div>
        <div class="section-body">
            <div class="card">

                <div class="card-header">
                    <h4>Staff List</h4>
                    <div class="card-header-form">
                        <form method="GET" action="staffs" class="form-inline">

                            <div class="form-group mr-2">
                                <select name="roleFilter" class="form-control" onchange="this.form.submit()" style="border-radius: 30px; height: 31px; padding: 0 10px; font-size: 12px;">
                                    <option value="">-- All Roles --</option>
                                    <c:forEach var="role" items="${rolesList}">
                                        <option value="${role.roleId}" ${param.roleFilter == role.roleId ? 'selected' : ''}>
                                            ${role.roleName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="input-group">
                                <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên..." value="${param.keyword}">
                                <div class="input-group-btn">
                                    <button class="btn btn-primary" type="submit"><i class="fas fa-search"></i></button>
                                </div>
                            </div>

                            <a href="staffs" class="btn btn-light ml-1" title="Làm mới"><i class="fas fa-sync-alt"></i></a>
                        </form>
                    </div>

                    <div class="card-header-action ml-auto">
                        <a href="staffs?action=add" class="btn btn-primary">➕ Add New Staff</a>
                    </div>
                </div>

                <div class="card-body">

                    <% String message = (String) request.getSession().getAttribute("message");
                        if (message != null) {%>
                    <div class="alert alert-success alert-dismissible show fade">
                        <div class="alert-body">
                            <button class="close" data-dismiss="alert"><span>&times;</span></button>
                            <%= message%>
                        </div>
                    </div>
                    <% request.getSession().removeAttribute("message");
                        }%>

                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Full Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Create At</th>
                                    <th>Status</th>
                                    <th class="text-center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty staffsList}">
                                    <tr>
                                        <td colspan="6" class="text-center">Không tìm thấy dữ liệu phù hợp.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="staff" items="${staffsList}">
                                    <tr>
                                        <td class="text-break" style="max-width: 200px;">
                                            <img alt="image" src="${pageContext.request.contextPath}/admin_screen/assets/img/avatar/avatar-1.png" class="rounded-circle mr-1" width="30">
                                            ${staff.fullName}
                                        </td>
                                        <td class="text-break" style="max-width: 250px;">
                                            ${staff.email}</td>
                                        <td><div class="badge badge-info">${staff.role.roleName}</div></td>
                                        <td>
                                            <c:catch>
                                                <fmt:formatDate value="${staff.createdAtAsDate}" pattern="dd/MM/yyyy"/>
                                            </c:catch>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${staff.isActive()}">
                                                    <div class="badge badge-success">Active</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="badge badge-danger">Locked</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex justify-content-center">
                                                <a href="staffs?action=edit&staffId=${staff.staffId}" class="btn btn-warning btn-sm mr-2" title="Sửa">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>

                                                <form method="POST" action="staffs" style="margin:0">
                                                    <input type="hidden" name="staffId" value="${staff.staffId}">
                                                    <c:choose>
                                                        <c:when test="${staff.isActive()}">
                                                            <button type="submit" name="action" value="deactivate" class="btn btn-danger btn-sm" 
                                                                    onclick="return confirm('Khóa tài khoản này?')" title="Khóa">
                                                                <i class="fas fa-lock"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="submit" name="action" value="activate" class="btn btn-success btn-sm" 
                                                                    onclick="return confirm('Mở khóa tài khoản này?')" title="Mở">
                                                                <i class="fas fa-unlock"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="card-footer text-right">
                        <nav class="d-inline-block">
                            <ul class="pagination mb-0">

                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="staffs?page=${currentPage - 1}&keyword=${param.keyword}&roleFilter=${param.roleFilter}" tabindex="-1">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="staffs?page=${i}&keyword=${param.keyword}&roleFilter=${param.roleFilter}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="staffs?page=${currentPage + 1}&keyword=${param.keyword}&roleFilter=${param.roleFilter}">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>

                </div>
            </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
