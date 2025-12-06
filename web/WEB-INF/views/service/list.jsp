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
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Service List</h2>
        <a href="service?action=detail" class="btn btn-success">
            <i class="fas fa-plus"></i> Create New Service
        </a>
    </div>

    <div class="card mb-4">
        <div class="card-body p-3">
            <form method="get" action="service" class="form-row align-items-center">
                <div class="col-md-4 my-1">
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <div class="input-group-text"><i class="fas fa-search"></i></div>
                        </div>
                        <input type="text" class="form-control" name="search" placeholder="Search name..." value="${search}" />
                    </div>
                </div>

                <div class="col-md-3 my-1">
                    <select name="categoryId" class="form-control custom-select">
                        <option value="">All Categories</option>
                        <c:forEach items="${categories}" var="c">
                            <option value="${c.categoryId}" <c:if test="${categoryId == c.categoryId}">selected="selected"</c:if>>
                                ${c.categoryName}
                            </option>
                        </c:forEach> </select>
                </div>

                <div class="col-md-3 my-1">
                    <select name="sort" class="form-control custom-select">
                        <option value="">Sort by...</option>
                        <option value="nameAsc"  <c:if test="${sort == 'nameAsc'}">selected="selected"</c:if>>Name (A-Z)</option>
                        <option value="nameDesc" <c:if test="${sort == 'nameDesc'}">selected="selected"</c:if>>Name (Z-A)</option>
                        <option value="priceAsc" <c:if test="${sort == 'priceAsc'}">selected="selected"</c:if>>Price (Low-High)</option>
                        <option value="priceDesc"<c:if test="${sort == 'priceDesc'}">selected="selected"</c:if>>Price (High-Low)</option>
                        <option value="idAsc"    <c:if test="${sort == 'idAsc'}">selected="selected"</c:if>>ID (Oldest)</option>
                        <option value="idDesc"   <c:if test="${sort == 'idDesc'}">selected="selected"</c:if>>ID (Newest)</option> </select>
                </div>

                <div class="col-md-2 my-1">
                    <button type="submit" class="btn btn-primary btn-block">Filter</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Unit</th>
                            <th class="text-center">Active</th>
                            <th class="text-right">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${list}"> <tr>
                                <td>#${s.serviceId}</td>
                                <td class="font-weight-bold text-dark">${s.serviceName}</td>
                                <td class="text-danger font-weight-bold">${s.price}</td>
                                <td><span class="badge badge-secondary">${s.unit}</span></td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${s.isActive}">
                                            <i class="fas fa-check-circle text-success"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-times-circle text-muted"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-right">
                                    <a href="service?action=detail&id=${s.serviceId}" class="btn btn-sm btn-info" title="Edit">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="service?action=delete&id=${s.serviceId}" class="btn btn-sm btn-danger" 
                                       onclick="return confirm('Are you sure you want to delete this service?')" title="Delete">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${totalPages}" var="p">
                    <li class="page-item <c:if test='${p == page}'>active</c:if>">
                        <c:choose>
                            <c:when test="${p == page}"> <span class="page-link">${p}</span>
                            </c:when>
                            <c:otherwise>
                                <a class="page-link" href="service?page=${p}&search=${search}&categoryId=${categoryId}&sort=${sort}">${p}</a>
                            </c:otherwise>
                        </c:choose> </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</div>