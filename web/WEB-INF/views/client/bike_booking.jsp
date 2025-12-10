<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Rent a Bicycle | Smart Hotel</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-info text-white text-center">
                        <h3><i class="fas fa-bicycle"></i> Bicycle Rental</h3>
                    </div>
                    <div class="card-body">
                        
                        <p class="text-center text-muted">Select a rental package below to explore the city.</p>

                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">${errorMessage}</div>
                        </c:if>

                        <form action="book-bike" method="POST">
                            
                            <div class="form-group">
                                <label class="font-weight-bold">Select Package</label>
                                <select class="form-control form-control-lg" name="optionId" required>
                                    <c:forEach var="opt" items="${options}">
                                        <option value="${opt.itemId}">
                                            ${opt.optionName} - <fmt:formatNumber value="${opt.price}" type="currency" currencySymbol="VND"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="font-weight-bold">Quantity (Bikes)</label>
                                <input type="number" class="form-control form-control-lg" name="quantity" value="1" min="1" max="5" required>
                            </div>

                            <div class="form-group">
                                <label>Special Request</label>
                                <textarea class="form-control" name="note" rows="3" placeholder="Ex: Need a lock, kid's helmet..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-info btn-block btn-lg mt-4">
                                <i class="fas fa-check-circle"></i> Confirm Booking
                            </button>
                            
                            <div class="text-center mt-3">
                                <a href="home" class="text-muted">Cancel and go back</a>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>