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

                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible show fade">
                                    <div class="alert-body">
                                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                        <i class="fas fa-exclamation-triangle"></i> ${sessionScope.errorMessage}
                                    </div>
                                </div>
                                <c:remove var="errorMessage" scope="session" />
                            </c:if>

                            <div class="table-responsive">
                                <table class="table table-striped table-hover" id="table-1">
                                    <thead>
                                        <tr>
                                            <th class="text-center">#</th>
                                            <th>Package Name</th>
                                            <th>Price per Table</th>
                                            <th class="text-center">Status</th>
                                            <th>Created Date</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${empty packages}">
                                            <tr>
                                                <td colspan="6" class="text-center text-muted py-4">
                                                    <i class="fas fa-box-open fa-3x mb-3"></i><br>
                                                    No packages found. Please create a new one.
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:forEach var="p" items="${packages}" varStatus="status">
                                            <tr>
                                                <td class="text-center">${status.count}</td>
                                                <td>
                                                    <strong>${p.eventName}</strong>
                                                </td>
                                                <td>
                                                    <strong class="text-primary">
                                                        <fmt:formatNumber value="${p.pricePerTable}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                                    </strong>
                                                </td>

                                                <td class="text-center">
                                                    <c:choose>
                                                        <c:when test="${p.status == 'Active'}">
                                                            <div class="badge badge-success">Active</div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="badge badge-secondary">Inactive</div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>
                                                    <fmt:formatDate value="${p.createdDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <a href="event-packages?action=EDIT&id=${p.eventId}" class="btn btn-primary btn-sm mr-1" data-toggle="tooltip" title="Edit">
                                                        <i class="fas fa-pencil-alt"></i>
                                                    </a>

                                                    <c:choose>
                                                        <c:when test="${p.status == 'Active'}">
                                                            <c:url var="deactivateLink" value="event-packages">
                                                                <c:param name="action" value="DEACTIVATE"/>
                                                                <c:param name="id" value="${p.eventId}"/>
                                                            </c:url>
                                                            <a href="${deactivateLink}" 
                                                               class="btn btn-warning btn-sm" 
                                                               onclick="confirmStatusChange(event, '${p.eventName}', 'DEACTIVATE', '${deactivateLink}')"
                                                               data-toggle="tooltip" title="Deactivate">
                                                                <i class="fas fa-ban"></i>
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:url var="activateLink" value="event-packages">
                                                                <c:param name="action" value="ACTIVATE"/>
                                                                <c:param name="id" value="${p.eventId}"/>
                                                            </c:url>
                                                            <a href="${activateLink}" 
                                                               class="btn btn-success btn-sm" 
                                                               onclick="confirmStatusChange(event, '${p.eventName}', 'ACTIVATE', '${activateLink}')"
                                                               data-toggle="tooltip" title="Activate">
                                                                <i class="fas fa-check-circle"></i>
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
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    function confirmStatusChange(event, packageName, action, link) {
        event.preventDefault(); // Prevents the default <a> navigation

        let titleInfo = "";
        let textInfo = "";
        let iconType = "";
        let confirmColor = "";
        let btnText = "";

        if (action === 'DEACTIVATE') {
            titleInfo = "Deactivate Package " + packageName + "?";
            textInfo = "This event package will be hidden and unavailable for new bookings!";
            iconType = "warning";
            confirmColor = "#ffc107"; // Yellow (Warning)
            btnText = "Yes, Deactivate it!";
        } else { // ACTIVATE
            titleInfo = "Activate Package " + packageName + "?";
            textInfo = "This event package will be visible and available for new bookings.";
            iconType = "question";
            confirmColor = "#28a745"; // Green (Success)
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
                // If user confirms, redirect to the provided URL
                window.location.href = link;
            }
        });
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />