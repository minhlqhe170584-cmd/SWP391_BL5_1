<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />

<div class="container mt-5">
    <div class="text-center mb-5">
        <h2 class="text-primary font-weight-bold">Select Your Bike Type</h2>
        <p class="text-muted">Choose the perfect bicycle for your journey around the city.</p>
    </div>

    <div class="row">
        <c:if test="${empty bikeServices}">
            <div class="col-12 text-center">
                <div class="alert alert-warning">No bike services are currently available.</div>
            </div>
        </c:if>

        <c:forEach var="s" items="${bikeServices}">
            <div class="col-md-4 mb-4">
                <div class="card shadow-sm h-100 border-0">
                    <img src="${s.imageUrl}" class="card-img-top" alt="${s.serviceName}" 
                         style="height: 200px; object-fit: cover;" 
                         onerror="this.src='https://via.placeholder.com/300x200?text=Bike+Image'">
                    <div class="card-body text-center">
                        <h5 class="card-title font-weight-bold">${s.serviceName}</h5>
                        <p class="card-text text-muted">Explore our premium collection.</p>
                        <a href="book-bike?serviceId=${s.serviceId}" class="btn btn-outline-primary btn-block rounded-pill">
                            Book Now <i class="fas fa-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    
    <div class="text-center mt-4">
        <a href="home" class="btn btn-link text-secondary">Back to Home</a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />