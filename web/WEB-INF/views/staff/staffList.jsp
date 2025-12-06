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
                    <div class="card-header-action">
                        <a href="staffs?action=add" class="btn btn-primary">➕ Thêm mới</a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message"); 
                       if(message != null) { %>
                        <div class="alert alert-info"><%= message %></div>
                    <% request.getSession().removeAttribute("message"); } %>

                    <div class="table-responsive">
                       <table class="table table-striped">
                         <thead>
            <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Create At</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:if test="${empty staffsList}">
                <tr>
                    <td colspan="7" class="text-center">Không có dữ liệu nhân viên.</td>
                </tr>
            </c:if>

            <c:forEach var="staff" items="${staffsList}">
                <tr>
                    <td>${staff.staffId}</td>
                    <td>${staff.fullName}</td>
                    <td>${staff.email}</td>
                    <td>
                        <div class="badge badge-info">${staff.role.roleName}</div>
                    </td>
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
                    <td>
                        <div class="d-flex">
                            <a href="staffs?action=edit&staffId=${staff.staffId}" class="btn btn-warning btn-sm mr-2" title="Sửa">
                                <i class="fas fa-pencil-alt"></i> Edit
                            </a>

                            <form method="POST" action="staffs" style="margin: 0;">
                                <input type="hidden" name="staffId" value="${staff.staffId}"/>
                                
                                <c:choose>
                                    <c:when test="${staff.isActive()}">
                                        <button type="submit" name="action" value="deactivate" class="btn btn-danger btn-sm" 
                                                onclick="return confirm('Bạn có chắc chắn muốn KHÓA nhân viên này?');" title="Khóa">
                                            <i class="fas fa-lock"></i> Lock
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" name="action" value="activate" class="btn btn-success btn-sm" 
                                                onclick="return confirm('Mở khóa tài khoản này?');" title="Mở khóa">
                                            <i class="fas fa-unlock"></i> UnLock
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

                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />