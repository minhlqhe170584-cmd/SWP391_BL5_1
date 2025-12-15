<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Event Booking Requests</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item">Event Requests</div>
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
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <div class="card">
                <div class="card-header">
                    <h4>List of Event Requests</h4>
                </div>

                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Event Package</th>
                                    <th>Customer</th>
                                    <th>Rooms</th>
                                    <th>Time</th>
                                    <th class="text-center">Status</th>
                                    <th>Message</th>
                                    <th style="width: 150px;">Action</th> 
                                </tr>
                            </thead>
                            <tbody>

                                <c:if test="${empty eventRequests}">
                                    <tr>
                                        <td colspan="8" class="text-center text-muted py-4">
                                            No booking requests found.
                                        </td>
                                    </tr>
                                </c:if>

                                <c:forEach var="r" items="${eventRequests}" varStatus="status">
                                    <tr>
                                        <td>${status.count}</td>
                                        <td class="font-weight-bold text-primary">${r.eventName}</td>
                                        <td>${r.customerName}</td>
                                        <td>
                                            <c:if test="${not empty r.roomNames}">
                                                <i class="fas fa-door-open text-muted"></i> ${r.roomNames}
                                            </c:if>
                                        </td>
                                        <td>
                                            <div class="text-small">
                                                In: <strong>${r.checkInDate}</strong><br>
                                                Out: <strong>${r.checkOutDate}</strong>
                                            </div>
                                        </td>

                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${r.status == 'PENDING'}">
                                                    <span class="badge badge-warning">Pending</span>
                                                </c:when>
                                                <c:when test="${r.status == 'ACCEPT'}">
                                                    <span class="badge badge-success">Approved</span>
                                                </c:when>
                                                <c:when test="${r.status == 'REJECT'}">
                                                    <span class="badge badge-danger">Rejected</span>
                                                </c:when>
                                                <c:when test="${r.status == 'COMPLETED'}">
                                                    <span class="badge badge-info">Completed</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-secondary">${r.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        
                                        <td>
                                            <small class="text-muted">${r.message}</small>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <%-- PENDING: Approve / Reject --%>
                                                <c:when test="${r.status == 'PENDING'}">
                                                    <a href="event-booking-list?action=APPROVE&id=${r.requestId}" 
                                                       class="btn btn-success btn-sm" 
                                                       onclick="confirmAction(event, 'Approve', 'Do you want to accept this request?', this.href)"
                                                       data-toggle="tooltip" title="Approve">
                                                        <i class="fas fa-check"></i>
                                                    </a>
                                                    <a href="event-booking-list?action=REJECT&id=${r.requestId}" 
                                                       class="btn btn-danger btn-sm" 
                                                       onclick="confirmAction(event, 'Reject', 'Do you want to reject this request?', this.href)"
                                                       data-toggle="tooltip" title="Reject">
                                                        <i class="fas fa-times"></i>
                                                    </a>
                                                </c:when>

                                                <%-- ACCEPT: Finish --%>
                                                <c:when test="${r.status == 'ACCEPT'}">
                                                    <a href="event-booking-list?action=FINISH&id=${r.requestId}" 
                                                       class="btn btn-info btn-sm btn-block" 
                                                       onclick="confirmAction(event, 'Finish', 'Mark this event as completed?', this.href)">
                                                        Finish Event
                                                    </a>
                                                </c:when>

                                                <c:otherwise>
                                                    <span class="text-muted small"><i class="fas fa-lock"></i> Locked</span>
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
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    function confirmAction(event, actionName, message, link) {
        event.preventDefault(); // Chặn chuyển trang

        let iconType = 'question';
        let confirmColor = '#3085d6';
        let btnText = 'Yes, do it!';

        // Tùy chỉnh màu sắc và icon theo hành động
        if (actionName === 'Approve') {
            iconType = 'success';
            confirmColor = '#28a745'; // Xanh lá
            btnText = 'Yes, Approve!';
        } else if (actionName === 'Reject') {
            iconType = 'warning';
            confirmColor = '#dc3545'; // Đỏ
            btnText = 'Yes, Reject!';
        } else if (actionName === 'Finish') {
            iconType = 'info';
            confirmColor = '#17a2b8'; // Xanh dương
            btnText = 'Yes, Finish!';
        }

        Swal.fire({
            title: actionName + ' Request?',
            text: message,
            icon: iconType,
            showCancelButton: true,
            confirmButtonColor: confirmColor,
            cancelButtonColor: '#d33',
            confirmButtonText: btnText,
            cancelButtonText: 'Cancel'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = link; // Chuyển trang nếu bấm Yes
            }
        });
    }
</script>