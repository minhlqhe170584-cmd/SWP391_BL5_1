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
            <h1>Drink Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                <div class="card-header">
                    <h4>Drinks Menu</h4>
                    <div class="card-header-action">
                        <a href="drinks?action=add" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Drink
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    
                    <form action="drinks" method="get" class="row mb-3">
                        <div class="col-md-4">
                            <input type="text" name="keyword" class="form-control" 
                                   placeholder="Search by name..." value="${param.keyword}">
                        </div>
                        <div class="col-md-3">
                            <select name="serviceFilter" class="form-control">
                                <option value="-1">-- All Categories --</option>
                                <c:forEach var="s" items="${listServices}">
                                    <option value="${s.serviceId}" ${param.serviceFilter == s.serviceId ? 'selected' : ''}>
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
                        <c:set var="alertType" value="${fn:contains(sessionScope.message, 'Lỗi') ? 'danger' : 'success'}" />
                        <div class="alert alert-${alertType} alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                ${sessionScope.message}
                            </div>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <div class="table-responsive">
                        <table class="table table-striped table-md">
                            <thead>
                                <tr>
                                    <th style="width: 80px;">Image</th>
                                    <th>Name</th>
                                    <th>Info (Vol/Type)</th> <th>Price</th>
                                    <th>Category</th>
                                    <th>Status</th>
                                    <th style="width: 150px;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="d" items="${drinksList}">
                                    <tr>
                                        <td>
                                            <img src="${pageContext.request.contextPath}/uploads/${fn:trim(d.imageUrl)}" 
                                                 class="img-thumb" 
                                                 alt="Drink Img"
                                                  
                                        </td>
                                        <td>
                                            <strong>${fn:trim(d.name)}</strong>
                                        </td>
                                        <td>
                                            <span><i class="fas fa-wine-bottle"></i> ${d.volumeMl} ml</span>
                                            <br>
                                            <c:if test="${d.isAlcoholic}">
                                                <span class="badge badge-danger badge-alcohol">Alcoholic</span>
                                            </c:if>
                                            <c:if test="${!d.isAlcoholic}">
                                                <span class="badge badge-success badge-alcohol">Non-Alcoholic</span>
                                            </c:if>
                                        </td>
                                        <td class="text-primary font-weight-bold">$${d.price}</td>
                                        <td>
                                            <c:forEach var="s" items="${listServices}">
                                                <c:if test="${s.serviceId == d.serviceId}">
                                                    <span class="badge badge-light">${fn:trim(s.serviceName)}</span>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="badge badge-${d.isActive ? 'success' : 'secondary'}">
                                                ${d.isActive ? 'Active' : 'Inactive'}
                                            </span>
                                        </td>
                                        <td>
                                            <a href="drinks?action=edit&drinkId=${d.drinkId}" class="btn btn-warning btn-sm" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            
                                            <form action="drinks" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="${d.isActive ? 'deactivate' : 'activate'}">
                                                <input type="hidden" name="drinkId" value="${d.drinkId}">
                                                <button class="btn btn-${d.isActive ? 'secondary' : 'success'} btn-sm"
                                                        title="${d.isActive ? 'Deactivate' : 'Activate'}"
                                                        onclick="return confirm('Change status for ${fn:trim(d.name)}?')">
                                                    <i class="fas fa-power-off"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty drinksList}">
                                    <tr>
                                        <td colspan="7" class="text-center">No drinks found.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <div class="card-footer text-right">
                            <nav class="d-inline-block">
                                <ul class="pagination mb-0">
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="drinks?page=${i}&keyword=${param.keyword}&serviceFilter=${param.serviceFilter}">${i}</a>
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