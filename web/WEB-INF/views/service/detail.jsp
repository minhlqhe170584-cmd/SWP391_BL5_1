<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=Open+Sans:wght@400;600&display=swap" rel="stylesheet">

<style>
    :root {
        /* Màu lấy từ ảnh screenshot website Smart Hotel */
        --primary-gold: #c59d5f; 
        --dark-navy: #1a1a1d;
        --light-gray: #f8f9fa;
    }

    body {
        font-family: 'Open Sans', sans-serif;
        background-color: #f1f2f6;
        color: #333;
    }

    h2 {
        font-family: 'Playfair Display', serif; /* Font tiêu đề sang trọng */
        color: var(--dark-navy);
        font-weight: 700;
        margin-bottom: 1.5rem;
        border-bottom: 3px solid var(--primary-gold);
        display: inline-block;
        padding-bottom: 5px;
    }

    /* Button Style */
    .btn-primary {
        background-color: var(--primary-gold);
        border-color: var(--primary-gold);
        color: #fff;
    }
    .btn-primary:hover {
        background-color: #b08d55;
        border-color: #b08d55;
    }

    /* Card & Table Style */
    .card {
        border: none;
        box-shadow: 0 5px 15px rgba(0,0,0,0.05);
        border-radius: 8px;
    }
    .table thead th {
        background-color: var(--dark-navy);
        color: #fff;
        border-color: var(--dark-navy);
    }
    .page-item.active .page-link {
        background-color: var(--primary-gold);
        border-color: var(--primary-gold);
    }
    .page-link {
        color: var(--dark-navy);
    }
</style>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <div class="card shadow-lg">
                <div class="card-header bg-dark text-white">
                    <h4 class="mb-0" style="font-family: 'Playfair Display', serif; color: var(--primary-gold);">
                        <i class="fas fa-concierge-bell mr-2"></i> Service Detail
                    </h4>
                </div>
                <div class="card-body">
                    <form method="post" action="service">
                        <input type="hidden" name="serviceId" value="${service.serviceId}" />

                        <div class="form-group">
                            <label class="font-weight-bold">Service Name:</label>
                            <input type="text" class="form-control" name="serviceName" value="${service.serviceName}" required placeholder="Enter service name"/>
                        </div>

                        <div class="form-row">
                            <div class="form-group col-md-6">
                                <label class="font-weight-bold">Price:</label>
                                <div class="input-group">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">$</span>
                                    </div>
                                    <input type="text" class="form-control" name="price" value="${service.price}" placeholder="0.00"/>
                                </div>
                            </div>
                            <div class="form-group col-md-6">
                                <label class="font-weight-bold">Unit:</label>
                                <input type="text" class="form-control" name="unit" value="${service.unit}" placeholder="e.g. Night, Plate, Hour" /> </div>
                        </div>

                        <div class="form-group">
                            <label class="font-weight-bold">Category:</label>
                            <select name="categoryId" class="form-control custom-select">
                                <c:forEach items="${categories}" var="c"> <option value="${c.categoryId}"
                                        <c:if test="${service.categoryId == c.categoryId}">selected="selected"</c:if>>
                                        ${c.categoryName}
                                    </option>
                                </c:forEach> </select>
                        </div>

                        <div class="form-group">
                            <label class="font-weight-bold">Image URL:</label>
                            <input type="text" class="form-control" name="imageUrl" value="${service.imageUrl}" placeholder="http://..." />
                        </div>

                        <div class="form-group">
                            <div class="custom-control custom-switch">
                                <input type="checkbox" class="custom-control-input" id="isActiveSwitch" name="isActive"
                                       <c:if test="${service.isActive}">checked="checked"</c:if> />
                                <label class="custom-control-label" for="isActiveSwitch">Is Active?</label>
                            </div>
                        </div>

                        <hr>

                        <div class="d-flex justify-content-between align-items-center">
                            <a href="service" class="text-secondary"><i class="fas fa-arrow-left"></i> Back to list</a>
                            <button type="submit" class="btn btn-primary px-4 font-weight-bold">
                                <i class="fas fa-save"></i> Save Service
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>