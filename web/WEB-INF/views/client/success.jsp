<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Success</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body class="text-center mt-5 bg-light">
    <div class="container">
        <div class="card p-5 shadow-sm mx-auto" style="max-width: 500px;">
            <div class="card-body">
                <h1 class="text-success display-4 mb-3">✓</h1>
                <h2 class="card-title">Thank You!</h2>
                <p class="card-text text-muted">${message}</p>
                <hr>
                <a href="service-order?serviceId=1" class="btn btn-primary">Order More</a>
                <a href="home" class="btn btn-outline-secondary">Back to Home</a>
            </div>
        </div>
    </div>
</body>
</html>