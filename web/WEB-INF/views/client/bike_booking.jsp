<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4>Book a Bicycle</h4>
        </div>
        <div class="card-body">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <form action="book-bike" method="POST">
                <div class="form-group">
                    <label>Select Package</label>
                    <select class="form-control" name="optionId" required>
                        <c:forEach var="opt" items="${options}">
                            <option value="${opt.itemId}">
                                ${opt.optionName} - <fmt:formatNumber value="${opt.price}" type="currency" currencySymbol="VND"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Quantity</label>
                    <input type="number" class="form-control" name="quantity" value="1" min="1" max="5" required>
                </div>
                <div class="form-group">
                    <label>Start Time</label>
                    <input type="datetime-local" class="form-control" name="startTime" required>
                </div>
                <div class="form-group">
                    <label>Note</label>
                    <textarea class="form-control" name="note" rows="2"></textarea>
                </div>
                <button type="submit" class="btn btn-success btn-block mt-3">Check Availability & Book</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />