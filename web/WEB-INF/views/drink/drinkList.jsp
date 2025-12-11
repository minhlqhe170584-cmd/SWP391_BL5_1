<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="vi_VN"/>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Drink Management</h1>
        </div>

        <div class="section-body">
            <div class="card">
                
                <div class="card-header">
                    <h4>Drink List</h4>
                    <div class="card-header-form">
                        <form method="GET" action="drinks" class="form-inline">
                            
                            <div class="form-group mr-2">
                                <select name="serviceFilter" class="form-control" onchange="this.form.submit()" 
                                        style="border-radius: 30px; height: 31px; padding: 0 10px; font-size: 12px;">
                                    <option value="">-- Tất cả loại đồ uống --</option>
                                    
                                    <c:forEach var="srv" items="${listServices}">
                                        <option value="${srv.serviceId}" ${param.serviceFilter == srv.serviceId ? 'selected' : ''}>
                                            ${srv.serviceName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="input-group">
                                <input type="text" name="keyword" class="form-control" placeholder="Tìm tên đồ uống..." value="${param.keyword}">
                                <div class="input-group-btn">
                                    <button class="btn btn-primary" type="submit"><i class="fas fa-search"></i></button>
                                </div>
                            </div>
                            
                            <a href="drinks" class="btn btn-light ml-1" title="Làm mới"><i class="fas fa-sync-alt"></i></a>
                        </form>
                    </div>
                    
                    <div class="card-header-action ml-auto">
                        <a href="drinks?action=add" class="btn btn-primary">➕ Thêm đồ uống mới</a>
                    </div>
                </div>

                <div class="card-body">
                    
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
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Ảnh</th>
                                    <th>Tên đồ uống</th>
                                    <th>Giá tiền</th>
                                    <th>Dung tích</th>
                                    <th>Cồn</th>
                                    <th>Loại (Service)</th>
                                    <th>Mô tả</th>
                                    <th>Trạng thái</th>
                                    <th class="text-center">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${empty drinksList}">
                                    <tr>
                                        <td colspan="9" class="text-center">Không tìm thấy đồ uống nào phù hợp.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="drink" items="${drinksList}">
                                    <tr>
                                        <td>
                                            <img alt="img" src="${pageContext.request.contextPath}/${drink.imageUrl != null ? drink.imageUrl : 'admin_screen/assets/img/products/product-1.png'}" 
                                                    class="rounded" width="45" height="45" style="object-fit: cover; border: 1px solid #eee;">
                                        </td>
                                        <td>
                                            <strong>${drink.name}</strong>
                                        </td>
                                        <td>
                                            <span class="text-primary font-weight-bold">
                                                <fmt:formatNumber value="${drink.price}" type="currency" currencySymbol="đ"/>
                                            </span>
                                        </td>
                                        
                                        <td>
                                            ${drink.volumeMl} ml
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${drink.isAlcoholic}"> 
                                                    <div class="badge badge-warning">Có cồn</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="badge badge-info">Không cồn</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        
                                        <td>
                                            <c:forEach var="s" items="${listServices}">
                                                <c:if test="${s.serviceId == drink.serviceId}">
                                                    <div class="badge badge-light">${s.serviceName}</div>
                                                </c:if>
                                            </c:forEach>
                                        </td>
                                        <td>
                                            <span class="d-inline-block text-truncate" style="max-width: 150px; color: #666;">
                                                ${drink.description}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${drink.isActive}"> 
                                                    <div class="badge badge-success">Đang bán</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="badge badge-danger">Ngừng bán</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex justify-content-center">
                                                <a href="drinks?action=edit&drinkId=${drink.drinkId}" class="btn btn-warning btn-sm mr-2" title="Sửa">
                                                    <i class="fas fa-pencil-alt"></i>
                                                </a>

                                                <form method="POST" action="drinks" style="margin:0">
                                                    <input type="hidden" name="drinkId" value="${drink.drinkId}">
                                                    <c:choose>
                                                        <c:when test="${drink.isActive}"> 
                                                            <button type="submit" name="action" value="deactivate" class="btn btn-danger btn-sm" 
                                                                    onclick="return confirm('Bạn có chắc muốn NGỪNG kinh doanh đồ uống này?')" title="Ngừng kinh doanh">
                                                                <i class="fas fa-eye-slash"></i>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="submit" name="action" value="activate" class="btn btn-success btn-sm" 
                                                                    onclick="return confirm('Bạn có chắc muốn MỞ BÁN lại đồ uống này?')" title="Mở bán">
                                                                <i class="fas fa-eye"></i>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                <div class="card-footer text-right">
                    <nav class="d-inline-block">
                        <ul class="pagination mb-0">
                            
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="drinks?page=${currentPage - 1}&keyword=${param.keyword}&serviceFilter=${param.serviceFilter}" tabindex="-1">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="drinks?page=${i}&keyword=${param.keyword}&serviceFilter=${param.serviceFilter}">
                                        ${i}
                                    </a>
                                </li>
                            </c:forEach>

                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="drinks?page=${currentPage + 1}&keyword=${param.keyword}&serviceFilter=${param.serviceFilter}">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>

            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />