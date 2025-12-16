<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="${pageContext.request.contextPath}/admin/customer" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${customer != null ? 'Update Customer' : 'Add New Customer'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="customer">Customer</a></div>
                <div class="breadcrumb-item">${customer != null ? 'Update' : 'Add'}</div>
            </div>
        </div>

        <div class="section-body">
            <h2 class="section-title">${customer != null ? 'Chỉnh sửa thông tin' : 'Tạo tài khoản mới'}</h2>
            <p class="section-lead">
                ${customer != null ? 'Cập nhật thông tin chi tiết' : 'Điền đầy đủ thông tin để tạo mới vào hệ thống.'}
            </p>
            
                    <% String message = (String) request.getSession().getAttribute("errorMessage");
                       if(message != null) { %>
                            <div class="alert alert-info alert-dismissible show fade">
                            <div class="alert-body">
                                <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                <%= message %>
                            </div>
                        </div>
                    <% request.getSession().removeAttribute("errorMessage");
                       } %>
                       
            <div class="row">
                <div class="col-12 col-md-8 col-lg-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>Info</h4>
                        </div>
                        
                        <div class="card-body">
                            <form method="POST" action="customer" class="needs-validation" novalidate="">
                                
                                <input type="hidden" name="action" value="${customer != null ? 'update' : 'create'}">
                                
                                <c:if test="${customer != null}">
                                    <input type="hidden" name="customerId" value="${customer.customerId}">
                                </c:if>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Full Name <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="fullName" required="" 
                                               value="${customer.fullName}" placeholder="Example: Nguyễn Văn A">
                                        <div class="invalid-feedback">Enter Full Name.</div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Email <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="email" class="form-control" name="email" required="" 
                                               value="${customer.email}" placeholder="email@example.com">
                                        <div class="invalid-feedback">Invalid email.</div>
                                    </div>
                                </div>
                                        
                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Phone <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="tel" class="form-control" name="phone" required 
                                               pattern="[0-9]{10}" maxlength="10"
                                               value="${customer.phone}" placeholder="1234567890">
                                        <div class="invalid-feedback">Phone must be exactly 10 digits.</div>
                                    </div>
                                </div>      

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Password <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="password" 
                                               ${customer == null ? 'required' : ''} 
                                               value="${customer.password}"
                                               placeholder="Enter password...">
                                        <c:if test="${customer != null}">
                                            <small class="form-text text-muted">Keep the old password or enter the new one</small>
                                        </c:if>
                                    </div>
                                </div>               

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3 col-lg-3"></label>
                                    <div class="col-sm-12 col-md-7">
                                        <button class="btn btn-primary btn-lg" type="submit">
                                            <i class="fas fa-save"></i> ${customer != null ? 'Save' : 'Create'}
                                        </button>
                                        <a href="customer" class="btn btn-cancel btn-lg ml-2">Cancel</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                        </div>
                </div>
                
                <div class="col-12 col-md-4 col-lg-4">
                    <div class="card card-warning">
                        <div class="card-header">
                            <h4>Lưu ý</h4>
                        </div>
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li class="mb-2"><i class="fas fa-check-circle text-success"></i> Email phải là duy nhất.</li>
                                <li class="mb-2"><i class="fas fa-check-circle text-success"></i> Mật khẩu nên dài hơn 6 ký tự.</li>
                            </ul>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />