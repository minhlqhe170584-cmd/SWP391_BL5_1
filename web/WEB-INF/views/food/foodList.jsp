<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<style>
    /* CSS CHỐNG VỠ KHUNG */
    .table-fixed { table-layout: fixed; width: 100%; }
    .table-fixed td { vertical-align: middle !important; }
    .img-thumb { 
        width: 60px; 
        height: 60px; 
        object-fit: cover; 
        border-radius: 5px; 
        border: 1px solid #ddd;
    }
</style>

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Food Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Menu List</h4>
                    <div class="card-header-action">
                        <a href="foods?action=add" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Food
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <form action="foods" method="get" class="row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="keyword" class="form-control" 
                                   placeholder="Search by name..." value="${fn:trim(keyword)}">
                        </div>
                        <div class="col-md-3">
                            <select name="serviceFilter" class="form-control">
                                <option value="-1">-- All Services --</option>
                                <c:forEach var="s" items="${listServices}">
                                    <option value="${s.serviceId}" ${serviceFilter == s.serviceId ? 'selected' : ''}>
                                        ${fn:trim(s.serviceName)}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-primary btn-block"><i class="fas fa-search"></i> Filter</button>
                        </div>
                    </form>

                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert alert-success alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                ${sessionScope.message}
                            </div>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <div class="table-responsive">
                        <table class="table table-striped table-md table-fixed">
                            <thead>
                                <tr>
                                    <th style="width: 80px;">Image</th> <th>Name</th>
                                    <th style="width: 100px;">Price</th>
                                    <th>Service</th>
                                    <th>Status</th>
                                    <th style="width: 150px;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="f" items="${foodsList}">
                                    <tr>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/uploads/${fn:trim(f.imageUrl)}" 
                                                 class="img-thumb" 
                                                 alt="Food Img"
                                                 onerror="this.src='https://via.placeholder.com/60'"> 
                                        </td>
                                        <td>${fn:trim(f.name)}</td>
                                        <td>$${f.price}</td>
                                        <td>
                                            <c:forEach var="s" items="${listServices}">
                                                <c:if test="${s.serviceId == f.serviceId}">
                                                    <span class="badge badge-info">${fn:trim(s.serviceName)}</span>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="badge badge-${f.isActive ? 'success' : 'danger'}">
                                                ${f.isActive ? 'Selling' : 'Stop'}
                                            </span>
                                        </td>
                                        <td>
                                            <a href="foods?action=edit&foodId=${f.foodId}" class="btn btn-warning btn-sm">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <form action="foods" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="${f.isActive ? 'deactivate' : 'activate'}">
                                                <input type="hidden" name="foodId" value="${f.foodId}">
                                                <button class="btn btn-${f.isActive ? 'secondary' : 'success'} btn-sm"
                                                        onclick="return confirm('Change status for ${fn:trim(f.name)}?')">
                                                    <i class="fas fa-power-off"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <div class="card-footer text-right">
                            <nav class="d-inline-block">
                                <ul class="pagination mb-0">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="foods?page=${i}&keyword=${keyword}&serviceFilter=${serviceFilter}">${i}</a>
                                        </li>
                                    </c:forEach>
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