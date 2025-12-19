<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Room Type Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Room Types</div>
            </div>
        </div>

        <div class="section-body">

            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                    </div>
                </div>
                <% session.removeAttribute("successMessage"); %>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                        <i class="fas fa-exclamation-triangle"></i> ${sessionScope.errorMessage}
                    </div>
                </div>
                <% session.removeAttribute("errorMessage");%>
            </c:if>

            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4>All Room Types</h4>
                            <div class="card-header-action">
                                <a href="room-types?action=NEW" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add New Type
                                </a>
                            </div>
                        </div>

                        <div class="row mb-3 ml-2 mt-3">
                            <div class="col-md-10">
                                <form action="room-types" method="GET" class="form-inline">
                                    <input type="hidden" name="action" value="LIST">

                                    <input type="text" name="keyword" class="form-control mr-2" 
                                           value="${keyword}" placeholder="Search type name...">
                                    
                                    

                                    <select name="status" class="form-control mr-2">
                                        <option value="">All Status</option>
                                        <option value="Active" ${currentStatus == 'Active' ? 'selected' : ''}>Active</option>
                                        <option value="Inactive" ${currentStatus == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                    </select>

                                    <button type="submit" class="btn btn-info" title="Filter Results">
                                        <i class="fas fa-filter"></i> Filter
                                    </button>

                                    <c:if test="${not empty keyword or not empty currentStatus}">
                                        <a href="room-types?action=LIST" class="btn btn-secondary ml-2" title="Reset all filters">
                                            Reset
                                        </a>
                                    </c:if>
                                </form>
                            </div>
                        </div>

                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-striped table-md">
                                    <thead>
                                        <tr>
                                            <th hidden="">ID</th>
                                            <th>Image</th>
                                            <th>Name</th>
                                            <th>Capacity</th>
                                            <th>Price (Weekday)</th>
                                            <th>Price (Weekend)</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="rt" items="${listRoomTypes}">
                                            <tr>
                                                <td hidden="">${rt.typeId}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty rt.imageUrl}">
                                                            <img alt="image" src="${pageContext.request.contextPath}${rt.imageUrl}" class="rounded" width="50" data-toggle="tooltip" title="${rt.typeName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-light">No Image</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td><strong>${rt.typeName}</strong></td>
                                                <td>
                                                    <i class="fas fa-user-friends text-muted"></i> ${rt.capacity}
                                                </td>
                                                <td>
                                                    <div class="text-primary font-weight-bold">
                                                        <fmt:formatNumber value="${rt.basePriceWeekday}" type="currency" currencySymbol="₫"/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="text-danger font-weight-bold">
                                                        <fmt:formatNumber value="${rt.basePriceWeekend}" type="currency" currencySymbol="₫"/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${rt.isActive}">
                                                            <div class="badge badge-success">Active</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="badge badge-danger">Inactive</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="room-types?action=EDIT&id=${rt.typeId}" class="btn btn-primary btn-sm" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>

                                                    <c:choose>
                                                        <%-- Active -> Show Soft Delete --%>
                                                        <c:when test="${rt.isActive}">
                                                            <a href="room-types?action=DELETE&id=${rt.typeId}" 
                                                               class="btn btn-danger btn-sm" 
                                                               title="Deactivate (Soft Delete)"
                                                               onclick="confirmRoomTypeAction(event, '${rt.typeName}', 'delete', this.href)">
                                                                <i class="fas fa-lock"></i>
                                                            </a>
                                                        </c:when>

                                                        <%-- Inactive -> Show Restore --%>
                                                        <c:otherwise>
                                                            <a href="room-types?action=RESTORE&id=${rt.typeId}" 
                                                               class="btn btn-success btn-sm" 
                                                               title="Restore (Activate)"
                                                               onclick="confirmRoomTypeAction(event, '${rt.typeName}', 'restore', this.href)">
                                                                <i class="fas fa-unlock"></i>
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <c:if test="${empty listRoomTypes}">
                                            <tr>
                                                <td colspan="8" class="text-center text-muted py-4">
                                                    <i class="fas fa-box-open fa-2x mb-2"></i><br>
                                                    No room types found matching your criteria.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>

                            <c:if test="${endPage > 1}">
                                <div class="card-footer text-right">
                                    <nav class="d-inline-block">
                                        <ul class="pagination mb-0">
                                            <c:if test="${tag > 1}">
                                                <li class="page-item">
                                                    <a class="page-link" href="room-types?action=LIST&index=${tag-1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                        <i class="fas fa-chevron-left"></i>
                                                    </a>
                                                </li>
                                            </c:if>

                                            <c:if test="${tag + 2 >= endPage}">
                                        <%-- Đảm bảo begin không nhỏ hơn 1 --%>
                                        <c:set var="startLoop" value="${endPage - 2 < 1 ? 1 : endPage - 2}" />

                                        <c:forEach begin="${startLoop}" end="${endPage}" var="i">
                                            <li class="page-item ${tag == i ? 'active' : ''}">
                                                        <a class="page-link" href="room-types?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                            <c:if test="${tag + 2 < endPage}">
                                                <c:forEach begin="${tag}" end="${tag + 2}" var="i">
                                                    <li class="page-item ${tag == i ? 'active' : ''}">
                                                        <a class="page-link" href="room-types?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                            ${i}
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${tag < endPage}">
                                                <li class="page-item">
                                                    <a class="page-link" href="room-types?action=LIST&index=${tag+1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
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
            </div>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
                                                                   function confirmRoomTypeAction(event, typeName, actionType, link) {
                                                                       event.preventDefault();

                                                                       let titleInfo = "";
                                                                       let textInfo = "";
                                                                       let iconType = "";
                                                                       let confirmColor = "";
                                                                       let btnText = "";

                                                                       if (actionType === 'delete') {
                                                                           titleInfo = "Deactivate " + typeName + "?";
                                                                           textInfo = "This room type will be hidden from new bookings!";
                                                                           iconType = "warning";
                                                                           confirmColor = "#d33";
                                                                           btnText = "Yes";
                                                                       } else {
                                                                           titleInfo = "Restore " + typeName + "?";
                                                                           textInfo = "This room type will be available for booking again!";
                                                                           iconType = "question";
                                                                           confirmColor = "#28a745";
                                                                           btnText = "Yes";
                                                                       }

                                                                       Swal.fire({
                                                                           title: titleInfo,
                                                                           text: textInfo,
                                                                           icon: iconType,
                                                                           showCancelButton: true,
                                                                           confirmButtonColor: confirmColor,
                                                                           cancelButtonColor: '#6c757d',
                                                                           confirmButtonText: btnText,
                                                                           cancelButtonText: 'Cancel'
                                                                       }).then((result) => {
                                                                           if (result.isConfirmed) {
                                                                               window.location.href = link;
                                                                           }
                                                                       });
                                                                   }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />