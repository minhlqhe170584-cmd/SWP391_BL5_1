<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Task Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Task List</h4>
                </div>
                <div class="card-body">

                    <form method="get" action="${pageContext.request.contextPath}/task" class="form-row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="search" class="form-control"
                                   placeholder="Search task / staff / room..."
                                   value="${fn:escapeXml(search)}">
                        </div>
                        <div class="col-md-3">
                            <select name="status" class="form-control">
                                <option value="">-- Task Status --</option>
                                <option value="Assigned" <c:if test="${status == 'Assigned'}">selected</c:if>>Assigned</option>
                                <option value="Pending" <c:if test="${status == 'Pending'}">selected</c:if>>Pending</option>
                                <option value="In Progress" <c:if test="${status == 'In Progress'}">selected</c:if>>In Progress</option>
                                <option value="Completed" <c:if test="${status == 'Completed'}">selected</c:if>>Completed</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected</c:if>>ID (Oldest)</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>ID (Newest)</option>
                                <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected</c:if>>Name (A-Z)</option>
                                <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected</c:if>>Name (Z-A)</option>
                                <option value="staffAsc" <c:if test="${sort == 'staffAsc'}">selected</c:if>>Staff (A-Z)</option>
                                <option value="staffDesc"<c:if test="${sort == 'staffDesc'}">selected</c:if>>Staff (Z-A)</option>
                                <option value="roomAsc"  <c:if test="${sort == 'roomAsc'}">selected</c:if>>Room (A-Z)</option>
                                <option value="roomDesc" <c:if test="${sort == 'roomDesc'}">selected</c:if>>Room (Z-A)</option>
                                <option value="dateAsc"  <c:if test="${sort == 'dateAsc'}">selected</c:if>>Date (Oldest)</option>
                                <option value="dateDesc" <c:if test="${sort == 'dateDesc'}">selected</c:if>>Date (Newest)</option>
                                <option value="statusAsc"  <c:if test="${sort == 'statusAsc'}">selected</c:if>>Status (A-Z)</option>
                                <option value="statusDesc" <c:if test="${sort == 'statusDesc'}">selected</c:if>>Status (Z-A)</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary btn-block">
                                <i class="fas fa-filter"></i> Filter
                            </button>
                        </div>
                    </form>

                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Task Name</th>
                                    <th>Staff</th>
                                    <th>Room</th>
                                    <th>Order ID</th>
                                    <th>Status</th>
                                    <th>Created At</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty list}">
                                    <tr>
                                        <td colspan="7" class="text-center">No tasks found.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="t" items="${list}">
                                    <tr>
                                        <td>#${t.taskId}</td>
                                        <td class="font-weight-bold text-break" style="max-width: 250px;">
                                            ${t.taskName}
                                        </td>
                                        <td class="text-break" style="max-width: 200px;">
                                            <c:out value="${t.staff != null ? t.staff.fullName : 'N/A'}"/>
                                        </td>
                                        <td>
                                            <c:out value="${t.serviceOrder != null ? t.serviceOrder.roomNumber : ''}"/>
                                        </td>
                                        <td>${t.orderId}</td>
                                        <td>
                                            <span class="badge
                                                <c:choose>
                                                    <c:when test="${t.status == 'Completed'}">badge-success</c:when>
                                                    <c:when test="${t.status == 'In Progress'}">badge-warning</c:when>
                                                    <c:otherwise>badge-primary</c:otherwise>
                                                </c:choose>
                                            ">
                                                ${t.status}
                                            </span>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${t.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <li class="page-item" <c:if test="${page <= 1}">disabled</c:if>>
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/task?page=${page-1}&search=${search}&status=${status}&sort=${sort}">
                                        Previous
                                    </a>
                                </li>
                                <c:forEach begin="1" end="${totalPages}" var="p">
                                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/task?page=${p}&search=${search}&status=${status}&sort=${sort}">
                                            ${p}
                                        </a>
                                    </li>
                                </c:forEach>
                                <li class="page-item" <c:if test="${page >= totalPages}">disabled</c:if>>
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/task?page=${page+1}&search=${search}&status=${status}&sort=${sort}">
                                        Next
                                    </a>
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

<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Task Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Task List</h4>
                </div>
                <div class="card-body">
                    
                    <% String message = (String) request.getSession().getAttribute("message");
                       if(message != null) { %>
                            <div class="alert alert-info alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                <%= message %>
                            </div>
                        </div>
                    <% request.getSession().removeAttribute("message");
                       } %>

                    <form method="get" action="task" class="form-row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="search" class="form-control" placeholder="Search customer info..." value="${search}">
                        </div>
                        <div class="col-md-2">
                             <select name="RoomId" class="form-control">
                                <option value="">-- All Rooms --</option>
                                <c:forEach items="${rooms}" var="c">
                                    <option value="${c.roomId}" <c:if test="${roomId == c.roomId}">selected</c:if>>
                                        ${c.roomNumber}
                                    </option>
                                </c:forEach> 
                            </select>
                        </div>
                        <div class="col-md-2">
                             <select name="status" class="form-control">
                                <option value="">-- Task Status --</option>                              
                                <option value="Done" <c:if test="${status == 'Done'}">selected</c:if>>Done</option>
                                <option value="Doing" <c:if test="${status == 'Doing'}">selected</c:if>>Doing</option>  
                                <option value="New" <c:if test="${status == 'New'}">selected</c:if>>New</option>  
                                                                    
                            </select>
                        </div>
                        <div class="col-md-2">
                             <select name="sort" class="form-control">
                                <option value="">Sort by...</option>
                                <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected</c:if>>ID (Oldest)</option>
                                <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected</c:if>>ID (Newest)</option>
                                <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected</c:if>>Name (A-Z)</option>
                                <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected</c:if>>Name (Z-A)</option>
                                <option value="roomAsc" <c:if test="${sort == 'roomAsc'}">selected</c:if>>Room Asc</option>
                                <option value="statusDesc"<c:if test="${sort == 'statusDesc'}">selected</c:if>>Status Desc</option>
                                <option value="statusAsc" <c:if test="${sort == 'statusAsc'}">selected</c:if>>Status Asc</option>
                                <option value="roomDesc"<c:if test="${sort == 'roomDesc'}">selected</c:if>>Room Desc</option>
                                <option value="dateAsc" <c:if test="${sort == 'dateAsc'}">selected</c:if>>Date (Oldest)</option>
                                <option value="dateDesc"<c:if test="${sort == 'dateDesc'}">selected</c:if>>Date (Newest)</option>
                            </select>
                        </div>
                        <div class="col-md-1">
                             <button type="submit" class="btn btn-primary btn-block"><i class="fas fa-filter"></i> Filter</button>
                        </div>
                    </form>

                    <div class="table-responsive">
                       <table class="table table-striped">
                         <thead>
                            <tr>
                                <th>ID</th>
                                <th>Task Name</th>
                                <th>Description</th>
                                <th>Room</th>
                                <th>Staff</th>
                                <th>Status</th>
                                <th>Created At</th>
                                <th>Finished At</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty tasks}">
                                <tr>
                                    <td colspan="7" class="text-center">No data found.</td>
                                </tr>
                            </c:if>

                            <c:forEach var="t" items="${tasks}">
                                <tr>
                                    <td>#${t.taskId}</td>
                                    <td class="font-weight-bold">${t.taskName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.description.length() > 50}">
                                                ${t.description.substring(0, 50)}...
                                            </c:when>
                                            <c:otherwise>
                                                ${t.description}
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.room != null}">
                                                <span class="badge badge-info">${t.room.roomNumber}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.staff != null}">
                                                ${t.staff.fullName}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">Unassigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.status == 'Done'}">
                                                <div class="badge badge-success">Done</div>
                                            </c:when>
                                            <c:when test="${t.status == 'Doing'}">
                                                <div class="badge badge-warning">Doing</div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="badge badge-secondary">New</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                   <td>
                                        <c:if test="${t.createdAt != null}">
                                            <fmt:formatDate value="${t.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${t.finishedAt != null}">
                                            <fmt:formatDate value="${t.finishedAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="d-flex">
                                            <a href="task?action=detail&id=${t.taskId}" class="btn btn-info btn-sm mr-1" title="View Details">
                                                <i class="fas fa-eye"></i>
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
                                        <a class="page-link" href="task?page=${p}&search=${search}&RoomId=${roomId}&status=${status}&sort=${sort}">${p}</a>
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />--%>
