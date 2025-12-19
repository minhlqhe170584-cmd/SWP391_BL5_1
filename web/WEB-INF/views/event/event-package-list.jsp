<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Event Packages Management</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Event Packages</div>
            </div>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h4>List of Event Packages</h4>
                            <div class="card-header-action">
                                <a href="event-packages?action=NEW" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Create New Package
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

                            <%-- ========== START: SEARCH & FILTER FORM ========== --%>
                            <form action="event-packages" method="GET" class="mb-4">
                                <input type="hidden" name="action" value="LIST">
                                <div class="form-row align-items-end">
                                    
                                    <div class="form-group col-md-5">
                                        <label>Search Package Name</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <div class="input-group-text"><i class="fas fa-search"></i></div>
                                            </div>
                                            <input type="text" name="keyword" value="${keyword}" class="form-control" placeholder="Enter package name...">
                                        </div>
                                    </div>

                                    <div class="form-group col-md-3">
                                        <label>Status</label>
                                        <select name="status" class="form-control">
                                            <option value="">-- All Status --</option>
                                            <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                                            <option value="Inactive" ${status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                        </select>
                                    </div>

                                    <div class="form-group col-md-4 text-right">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-filter"></i> Filter
                                        </button>
                                        
                                        <%-- Reset Button: Show only if filtering --%>
                                        <c:if test="${not empty keyword or not empty status}">
                                            <a href="event-packages?action=LIST" class="btn btn-secondary ml-2">
                                                Reset
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </form>
                            <%-- ========== END: SEARCH & FILTER FORM ========== --%>

                            <div class="table-responsive">
                                <table class="table table-striped table-hover">
                                    <thead>
                                        <tr>
                                            <th>#ID</th>
                                            <th>Package Name</th>
                                            <th>Price</th>
                                            <th>Category</th>
                                            <th>Rooms</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        
                                        <c:if test="${empty listEventPackages}">
                                            <tr>
                                                <td colspan="7" class="text-center text-muted py-4">No event packages found.</td>
                                            </tr>
                                        </c:if>

                                        <c:forEach var="e" items="${listEventPackages}">
                                            <tr>
                                                <td>${e.eventId}</td>
                                                <td><strong>${e.eventName}</strong></td>
                                                <td class="text-primary font-weight-bold">
                                                    <fmt:formatNumber value="${e.pricePerTable}" type="currency" currencySymbol="₫"/>
                                                </td>
                                                <td>
                                                    <div class="badge badge-light">${e.eventCategory.categoryName}</div>
                                                </td>
                                                <td>
                                                    <small class="text-muted"><i class="fas fa-door-open"></i> ${e.location}</small>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${e.status == 'Active'}">
                                                            <div class="badge badge-success">Active</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="badge badge-danger">Inactive</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="event-packages?action=EDIT&id=${e.eventId}" class="btn btn-warning btn-sm" title="Edit">
                                                        <i class="fas fa-pencil-alt"></i>
                                                    </a>
                                                    
                                                    <c:choose>
                                                        <c:when test="${e.status == 'Active'}">
                                                            <a href="event-packages?action=DEACTIVATE&id=${e.eventId}" 
                                                               class="btn btn-danger btn-sm"
                                                               onclick="confirmEventAction(event, '${e.eventName}', 'DEACTIVATE', this.href)"
                                                               title="Deactivate">
                                                                <i class="fas fa-lock"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="event-packages?action=ACTIVATE&id=${e.eventId}" 
                                                               class="btn btn-success btn-sm"
                                                               onclick="confirmEventAction(event, '${e.eventName}', 'ACTIVATE', this.href)"
                                                               title="Activate">
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
                        </div>

                        <%-- ========== START: PAGINATION ========== --%>
                    <c:if test="${endPage > 1}">
                        <div class="card-footer text-right">
                            <nav class="d-inline-block">
                                <ul class="pagination mb-0">
                                    <c:if test="${tag > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="event-packages?action=LIST&index=${tag-1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                <i class="fas fa-chevron-left"></i>
                                            </a>
                                        </li>
                                    </c:if>

                                    <c:if test="${tag + 2 >= endPage}">
                                        <%-- Đảm bảo begin không nhỏ hơn 1 --%>
                                        <c:set var="startLoop" value="${endPage - 2 < 1 ? 1 : endPage - 2}" />

                                        <c:forEach begin="${startLoop}" end="${endPage}" var="i">
                                            <li class="page-item ${tag == i ? 'active' : ''}">
                                                <a class="page-link" href="event-packages?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${tag + 2 < endPage}">
                                        <c:forEach begin="${tag}" end="${tag + 2}" var="i">
                                            <li class="page-item ${tag == i ? 'active' : ''}">
                                                <a class="page-link" href="event-packages?action=LIST&index=${i}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${tag < endPage}">
                                        <li class="page-item">
                                            <a class="page-link" href="event-packages?action=LIST&index=${tag+1}&keyword=${keyword}&floor=${currentFloor}&typeId=${currentType}&active=${currentActive}">
                                                <i class="fas fa-chevron-right"></i>
                                            </a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                    </c:if>
                        <%-- ========== END: PAGINATION ========== --%>

                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    function confirmEventAction(event, packageName, action, link) {
        event.preventDefault();

        let titleInfo = "";
        let textInfo = "";
        let iconType = "";
        let confirmColor = "";
        let btnText = "";

        if (action === 'DEACTIVATE') {
            titleInfo = "Deactivate Package " + packageName + "?";
            textInfo = "This package will be hidden from new bookings!";
            iconType = "warning";
            confirmColor = "#ffc107";
            btnText = "Yes, Deactivate it!";
        } else { 
            titleInfo = "Activate Package " + packageName + "?";
            textInfo = "This package will be visible for new bookings.";
            iconType = "question";
            confirmColor = "#28a745";
            btnText = "Yes, Activate it!";
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />