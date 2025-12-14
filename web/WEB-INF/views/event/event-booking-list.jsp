<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Event Booking Requests</h1>
        </div>

    <div class="section-body">
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
                                <th>Event</th>
                                <th>Customer</th>
                                <th>Rooms</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                                <th>Status</th>
                                <th>Created</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:if test="${empty eventRequests}">
                                <tr>
                                    <td colspan="8" class="text-center text-muted">
                                        No booking requests found
                                    </td>
                                </tr>
                            </c:if>

                            <c:forEach var="r" items="${eventRequests}" varStatus="st">
                                <tr>
                                    <td>${st.count}</td>

                                    <td>
                                        <strong>${r.eventName}</strong>
                                    </td>

                                    <td>
                                        <i class="fas fa-user"></i>
                                        ${r.customerName}
                                    </td>

                                    <td>
                                        <span class="badge badge-info">
                                            ${r.roomNames}
                                        </span>
                                    </td>

                                    <td>
                                        <fmt:formatDate value="${r.checkInDate}" pattern="dd/MM/yyyy"/>
                                    </td>

                                    <td>
                                        <fmt:formatDate value="${r.checkOutDate}" pattern="dd/MM/yyyy"/>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${r.status == 'PENDING'}">
                                                <span class="badge badge-warning">Pending</span>
                                            </c:when>
                                            <c:when test="${r.status == 'ACCEPT'}">
                                                <span class="badge badge-success">Approved</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Rejected</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <fmt:formatDate value="${r.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
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
