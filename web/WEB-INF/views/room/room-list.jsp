<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Room Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Room List</h4>
                    <div class="card-header-action">
                        <a href="rooms?action=NEW" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Room
                        </a>
                    </div>
                </div>

                <div class="card-body">

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                            </div>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                <i class="fas fa-exclamation-triangle"></i> ${errorMessage}
                            </div>
                        </div>
                    </c:if>

                    <form action="rooms" method="GET" class="mb-4">
                        <input type="hidden" name="action" value="LIST">
                        <div class="form-row align-items-end">
                            <div class="form-group col-md-4">
                                <label>Search Keyword</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <div class="input-group-text"><i class="fas fa-search"></i></div>
                                    </div>
                                    <input type="text" name="keyword" value="${keyword}" class="form-control" placeholder="Room number...">
                                </div>
                            </div>

                            <div class="form-group col-md-3">
                                <label>Floor</label>
                                <select name="floor" class="form-control">
                                    <option value="">-- All Floors --</option>
                                    <c:forEach var="f" items="${listFloors}">
                                        <option value="${f}" ${currentFloor == f ? 'selected' : ''}>Floor ${f}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group col-md-3">
                                <label>Type</label>
                                <select name="typeId" class="form-control">
                                    <option value="">All Types</option>
                                    <c:forEach var="t" items="${listType}">
                                        <option value="${t.typeId}" ${currentType == t.typeId ? 'selected' : ''}>${t.typeName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group col-md-2">
                                <label>Login Status</label>
                                <select name="active" class="form-control">
                                    <option value="">All</option>
                                    <option value="true" ${currentActive == 'true' ? 'selected' : ''}>Unlocked (Active)</option>
                                    <option value="false" ${currentActive == 'false' ? 'selected' : ''}>Locked (Inactive)</option>
                                </select>
                            </div>

                            <div class="form-group col-md-12 text-right">
                                <button type="submit" class="btn btn-info" title="Filter Results">
                                    <i class="fas fa-filter"></i> Filter
                                </button>
                                <c:if test="${isFiltering}">
                                    <a href="rooms?action=LIST" class="btn btn-secondary ml-2">Reset</a>
                                </c:if>
                            </div>
                        </div>
                    </form>

                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Room No.</th>
                                    <th>Room Type</th>
                                    <th class="text-center">Login Access</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty roomsList}">
                                    <tr>
                                        <td colspan="4" class="text-center">No rooms found matching your criteria.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="room" items="${roomsList}">
                                    <tr>
                                        <td><strong>${room.roomNumber}</strong></td>
                                        <td>${room.roomType.typeName}</td>

                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${room.activeLogin}">
                                                    <div class="badge badge-success"><i class="fas fa-check"></i> Allowed</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="badge badge-danger"><i class="fas fa-lock"></i> Locked</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <div class="d-flex">
                                                <a href="rooms?action=VIEW&id=${room.roomId}" class="btn btn-info btn-sm mr-2" title="View Details">
                                                    <i class="fas fa-eye"></i>
                                                </a>

                                                <a href="rooms?action=EDIT&id=${room.roomId}" class="btn btn-warning btn-sm mr-2" title="Edit">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>

                                                <c:choose>
                                                    <c:when test="${room.activeLogin}">
                                                        <a href="rooms?action=LOCK&id=${room.roomId}" 
                                                           class="btn btn-secondary btn-sm" 
                                                           onclick="confirmLock(event, '${room.roomNumber}', 'lock', this.href)"
                                                           title="Lock Room (Disable Login)">
                                                            <i class="fas fa-lock"></i>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="rooms?action=LOCK&id=${room.roomId}" 
                                                           class="btn btn-success btn-sm" 
                                                           onclick="confirmLock(event, '${room.roomNumber}', 'unlock', this.href)"
                                                           title="Unlock Room (Enable Login)">
                                                            <i class="fas fa-unlock"></i>
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

                    <c:if test="${endPage > 1}">
                        <div class="card-footer text-right">
                            <nav class="d-inline-block">
                                <ul class="pagination mb-0">
                                    <c:if test="${tag > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="rooms?action=LIST&index=${tag-1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                <i class="fas fa-chevron-left"></i>
                                            </a>
                                        </li>
                                    </c:if>

                                    <c:if test="${tag + 2 >= endPage}">
                                        <c:forEach begin="${endPage - 2}" end="${endPage}" var="i">
                                            <li class="page-item ${tag == i ? 'active' : ''}">
                                                <a class="page-link" href="rooms?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${tag + 2 < endPage}">
                                        <c:forEach begin="${tag}" end="${tag + 2}" var="i">
                                            <li class="page-item ${tag == i ? 'active' : ''}">
                                                <a class="page-link" href="rooms?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${tag < endPage}">
                                        <li class="page-item">
                                            <a class="page-link" href="rooms?action=LIST&index=${tag+1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                <i class="fas fa-chevron-right"></i>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </c:if>

                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
                                                               function confirmLock(event, roomNumber, action, link) {
                                                                   event.preventDefault();

                                                                   let titleInfo = "";
                                                                   let textInfo = "";
                                                                   let iconType = "";
                                                                   let confirmColor = "";
                                                                   let btnText = "";

                                                                   if (action === 'lock') {
                                                                       titleInfo = "Lock Room " + roomNumber + "?";
                                                                       textInfo = "Room account will NOT be able to login!";
                                                                       iconType = "warning";
                                                                       confirmColor = "#6c757d"; // Màu xám cho nút khóa
                                                                       btnText = "Yes, Lock it!";
                                                                   } else {
                                                                       titleInfo = "Unlock Room " + roomNumber + "?";
                                                                       textInfo = "Room account will be able to login again!";
                                                                       iconType = "question";
                                                                       confirmColor = "#28a745"; // Màu xanh cho nút mở
                                                                       btnText = "Yes, Unlock it!";
                                                                   }

                                                                   Swal.fire({
                                                                       title: titleInfo,
                                                                       text: textInfo,
                                                                       icon: iconType,
                                                                       showCancelButton: true,
                                                                       confirmButtonColor: confirmColor,
                                                                       cancelButtonColor: '#d33',
                                                                       confirmButtonText: btnText,
                                                                       cancelButtonText: 'Cancel'
                                                                   }).then((result) => {
                                                                       if (result.isConfirmed) {
                                                                           window.location.href = link;
                                                                       }
                                                                   });
                                                               }
</script>