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
                            <h4>List of Halls & Conference Rooms <span class="badge badge-primary ml-2">${totalRooms} found</span></h4>
                            <div class="card-header-action">
                                <a href="event-rooms?action=NEW" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add New Hall
                                </a>
                            </div>
                        </div>
                        <div class="card-body">
                            
                            <form action="event-rooms" method="GET" class="mb-4">
                                <input type="hidden" name="action" value="LIST">
                                <div class="form-row">
                                    
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

                                    <div class="form-group col-md-2 d-flex align-items-end">
                                        <button type="submit" class="btn btn-info btn-block mr-1" title="Search & Filter">
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
                                <table class="table table-striped" id="table-1">
                                    <thead>
                                        <tr>
                                            <th class="text-center">#</th>
                                            <th>Hall Name</th>
                                            <th>Type</th>
                                            <th>Capacity</th>
                                            <th hidden>Status</th>
                                            <th>Base Price (Weekday)</th>
                                            <th>Base Price (Weekend)</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${empty eventRooms}">
                                            <tr>
                                                <td colspan="7" class="text-center text-muted py-4">
                                                    <i class="fas fa-inbox fa-3x mb-3"></i><br>
                                                    No event rooms found matching your criteria.
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:forEach var="room" items="${eventRooms}" varStatus="status">
                                            <tr>
                                                <td class="text-center">${status.count}</td>
                                                <td>
                                                    <i class="fas fa-place-of-worship text-warning mr-2"></i>
                                                    <strong>${room.roomNumber}</strong>
                                                </td>
                                                <td>${room.roomType.typeName}</td>
                                                <td>
                                                    <span class="badge badge-light">
                                                        <i class="fas fa-users"></i> ${room.roomType.capacity}
                                                    </span>
                                                </td>
                                                <td hidden>
                                                    </td>
                                                <td>
                                                    <strong class="text-primary">
                                                        <fmt:formatNumber value="${room.roomType.basePriceWeekday}" type="currency" currencySymbol="$" />
                                                    </strong>
                                                </td>
                                                <td>
                                                    <strong class="text-primary">
                                                        <fmt:formatNumber value="${room.roomType.basePriceWeekend}" type="currency" currencySymbol="$" />
                                                    </strong>
                                                </td>
                                                <td>
                                                    <a href="event-rooms?action=EDIT&id=${room.roomId}" class="btn btn-primary btn-action mr-1" data-toggle="tooltip" title="Edit">
                                                        <i class="fas fa-pencil-alt"></i>
                                                    </a>
                                                    <a href="#" class="btn btn-danger btn-action" onclick="return confirm('Delete this hall?');">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
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
                                                    <a class="page-link" href="event-rooms?action=LIST&index=${tag-1}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
                                                        <i class="fas fa-chevron-left"></i>
                                                    </a>
                                                </li>
                                            </c:if>

                                            <c:forEach begin="1" end="${endPage}" var="i">
                                                <li class="page-item ${tag == i ? 'active' : ''}">
                                                    <a class="page-link" href="event-rooms?action=LIST&index=${i}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
                                                        ${i}
                                                    </a>
                                                </li>
                                            </c:forEach>

                                            <c:if test="${tag < endPage}">
                                                <li class="page-item">
                                                    <a class="page-link" href="event-rooms?action=LIST&index=${tag+1}&keyword=${keyword}&typeId=${currentType}&minCapacity=${minCapacity}&maxCapacity=${maxCapacity}">
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

<jsp:include page="/WEB-INF/views/common/footer.jsp" />