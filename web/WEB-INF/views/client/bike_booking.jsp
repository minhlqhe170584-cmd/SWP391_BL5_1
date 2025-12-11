<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow border-0">
                <div class="card-header bg-white">
                    <h4 class="mb-0 text-primary">
                        <i class="fas fa-calendar-check mr-2"></i> Book: ${service.serviceName}
                    </h4>
                </div>
                <div class="card-body">
                    
                    <div class="media mb-4">
                        <img src="${service.imageUrl}" class="mr-3 rounded shadow-sm" alt="Bike Img" width="120" height="80" 
                             style="object-fit: cover;" onerror="this.src='https://via.placeholder.com/120'">
                        <div class="media-body align-self-center">
                            <h5 class="mt-0">${service.serviceName}</h5>
                            <a href="book-bike" class="text-muted small"><i class="fas fa-exchange-alt"></i> Change bike type</a>
                        </div>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger shadow-sm">${errorMessage}</div>
                    </c:if>

                    <form action="book-bike" method="POST">
                        <input type="hidden" name="serviceId" value="${service.serviceId}">

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label class="font-weight-bold">Select Package</label>
                                <select class="form-control" name="optionId" required>
                                    <c:forEach var="opt" items="${options}">
                                        <option value="${opt.itemId}">
                                            ${opt.optionName} - <fmt:formatNumber value="${opt.price}" type="currency" currencySymbol="VND"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group col-md-6">
                                <label class="font-weight-bold">Quantity</label>
                                <input type="number" class="form-control" name="quantity" value="1" min="1" max="5" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="font-weight-bold">Start Time (Giờ nhận xe)</label>
                            <input type="datetime-local" class="form-control" name="startTime" required>
                        </div>

                        <div class="form-group">
                            <label>Note to Staff</label>
                            <textarea class="form-control" name="note" rows="2" placeholder="E.g: Prepare 2 helmets for kids..."></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block btn-lg shadow-sm mt-4">
                            Check Availability & Confirm
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />