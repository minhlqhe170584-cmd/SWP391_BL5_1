<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Event Room Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Event Rooms</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4>List of Halls & Conference Rooms 
                                <c:if test="${not empty totalRooms}">
                                    <span class="badge badge-primary ml-2">${totalRooms} found</span>
                                </c:if>
                            </h4>
                            <div class="card-header-action">
                                <a href="event-rooms?action=NEW" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add New Hall
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

                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible show fade">
                                    <div class="alert-body">
                                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                        <i class="fas fa-exclamation-triangle"></i> ${sessionScope.errorMessage}
                                    </div>
                                </div>
                                <c:remove var="errorMessage" scope="session" />
                            </c:if>

                            <form action="event-rooms" method="GET" class="mb-4">
                                <input type="hidden" name="action" value="LIST">
                                <div class="form-row align-items-end">
                                    <div class="form-group col-md-3">
                                        <label>Hall Name</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-search"></i></div>
                                            </div>
                                            <input type="text" name="keyword" value="${keyword}" class="form-control" placeholder="Search name...">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label>Hall Type</label>
                                        <select name="typeId" class="form-control">
                                            <option value="">-- All Types --</option>
                                            <c:forEach var="t" items="${listType}">
                                                <option value="${t.typeId}" ${currentType == t.typeId ? 'selected' : ''}>
                                                    ${t.typeName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Min Cap</label>
                                        <input type="number" name="minCapacity" value="${minCapacity}" class="form-control" placeholder="Min">
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label>Max Cap</label>
                                        <input type="number" name="maxCapacity" value="${maxCapacity}" class="form-control" placeholder="Max">
                                    </div>
                                    <div class="form-group col-md-2 d-flex justify-content-between">
                                        <button type="submit" class="btn btn-info flex-fill mr-1" title="Search & Filter">
                                            <i class="fas fa-filter"></i> Apply
                                        </button>
                                        <c:if test="${isFiltering}">
                                            <a href="event-rooms?action=LIST" class="btn btn-secondary" title="Clear All">
                                                <i class="fas fa-undo"></i>
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </form>

                            <div class="table-responsive">
                                <table class="table table-striped table-hover" id="table-1">
                                    <thead>
                                        <tr>
                                            <th class="text-center">#</th>
                                            <th>Hall Name</th>
                                            <th>Type</th>
                                            <th class="text-center">Capacity</th>
                                            <th class="text-center">Login Access</th> <th hidden>Status</th>
                                            <th>Price (Weekday)</th>
                                            <th>Price (Weekend)</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${empty eventRooms}">
                                            <tr>
                                                <td colspan="8" class="text-center text-muted py-4">
                                                    No event rooms found matching your criteria.
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:forEach var="room" items="${eventRooms}" varStatus="status">
                                            <tr>
                                                <td class="text-center">${status.count + (tag-1)*5}</td>
                                                <td>
                                                    <i class="fas fa-place-of-worship text-warning mr-2"></i>
                                                    <strong>${room.roomNumber}</strong>
                                                </td>
                                                <td>${room.roomType.typeName}</td>
                                                <td class="text-center">
                                                    <span class="badge badge-light">
                                                        <i class="fas fa-users"></i> ${room.roomType.capacity}
                                                    </span>
                                                </td>

                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${room.activeLogin}">
                                                            <div class="badge badge-success" data-toggle="tooltip" title="Account Active">
                                                                <i class="fas fa-check-circle"></i> Active
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="badge badge-danger" data-toggle="tooltip" title="Account Locked">
                                                                <i class="fas fa-lock"></i> Locked
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td hidden>
                                                </td>
                                                <td>
                                                    <strong class="text-primary">
                                                        <fmt:formatNumber value="${room.roomType.basePriceWeekday}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <strong class="text-success">
                                                        <fmt:formatNumber value="${room.roomType.basePriceWeekend}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <a href="event-rooms?action=EDIT&id=${room.roomId}" class="btn btn-primary btn-sm mr-1" data-toggle="tooltip" title="Edit">
                                                        <i class="fas fa-pencil-alt"></i>
                                                    </a>

                                                    <c:choose>
                                                        <c:when test="${room.activeLogin}">
                                                            <a href="event-rooms?action=LOCK&id=${room.roomId}" 
                                                               class="btn btn-secondary btn-sm" 
                                                               onclick="confirmLock(event, '${room.roomNumber}', 'lock', this.href)"
                                                               data-toggle="tooltip" title="Lock Room (Disable Login)">
                                                                <i class="fas fa-lock"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="event-rooms?action=LOCK&id=${room.roomId}" 
                                                               class="btn btn-success btn-sm" 
                                                               onclick="confirmLock(event, '${room.roomNumber}', 'unlock', this.href)"
                                                               data-toggle="tooltip" title="Unlock Room (Enable Login)">
                                                                <i class="fas fa-unlock"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
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
                                            <li class="page-item ${tag <= 1 ? 'disabled' : ''}">
                                                <a class="page-link" href="event-rooms?action=LIST&index=${tag-1}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
                                                    <i class="fas fa-chevron-left"></i>
                                                </a>
                                            </li>
                                            <c:forEach begin="1" end="${endPage}" var="i">
                                                <li class="page-item ${tag == i ? 'active' : ''}">
                                                    <a class="page-link" href="event-rooms?action=LIST&index=${i}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
                                                        ${i}
                                                    </a>
                                                </li>
                                            </c:forEach>
                                            <li class="page-item ${tag >= endPage ? 'disabled' : ''}">
                                                <a class="page-link" href="event-rooms?action=LIST&index=${tag+1}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
                                                    <i class="fas fa-chevron-right"></i>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
                                                                   function confirmLock(event, roomNumber, action, link) {
                                                                       event.preventDefault(); // Chặn chuyển trang ngay lập tức

                                                                       let titleInfo = "";
                                                                       let textInfo = "";
                                                                       let iconType = "";
                                                                       let confirmColor = "";
                                                                       let btnText = "";

                                                                       if (action === 'lock') {
                                                                           titleInfo = "Lock Hall " + roomNumber + "?";
                                                                           textInfo = "This account will NOT be able to login to order services!";
                                                                           iconType = "warning";
                                                                           confirmColor = "#6c757d"; // Màu xám
                                                                           btnText = "Yes, Lock it!";
                                                                       } else {
                                                                           titleInfo = "Unlock Hall " + roomNumber + "?";
                                                                           textInfo = "This account will be able to login again!";
                                                                           iconType = "question";
                                                                           confirmColor = "#28a745"; // Màu xanh
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
                                                                               window.location.href = link; // Chuyển trang khi bấm Yes
                                                                           }
                                                                       });
                                                                   }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />