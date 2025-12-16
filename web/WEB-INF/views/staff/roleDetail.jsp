<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">

        <div class="section-header">
            <div class="section-header-back">
                <a href="staffRoles" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${role != null ? 'Cập nhật Vai trò' : 'Thêm Vai trò Mới'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="staffRoles">Quản lý Vai trò</a></div>
                <div class="breadcrumb-item">${role != null ? 'Cập nhật' : 'Thêm mới'}</div>
            </div>
        </div>

        <div class="section-body">

            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-${sessionScope.message.contains('LỖI') ? 'danger' : 'success'} alert-dismissible show fade">
                    <div class="alert-body">
                        <button class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                        ${sessionScope.message}
                    </div>
                </div>
                <c:remove var="message" scope="session" />
            </c:if>

            <div class="row">
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4>Form Nhập Liệu</h4>
                        </div>
                        <div class="card-body">

                            <form method="POST" action="staffRoles" class="needs-validation" novalidate="">

                                <input type="hidden" name="action" value="${role != null ? 'update' : 'create'}">

                                <c:if test="${role != null}">
                                    <input type="hidden" name="roleId" value="${role.roleId}">
                                </c:if>

                                <div class="form-group">
                                    <label>Tên Vai trò (Role Name) <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-user-tag"></i>
                                            </div>
                                        </div>
                                        <input type="text" 
                                               class="form-control" 
                                               name="roleName" 
                                               required="" 
                                               pattern=".*\S+.*" 
                                               title="Vui lòng không để trống hoặc chỉ nhập khoảng trắng!"
                                               maxlength="50" 
                                               value="${role.roleName}" 
                                               placeholder="Nhập tên vai trò (VD: Admin, Staff...)"
                                               >

                                        <div class="invalid-feedback">
                                            Tên vai trò không hợp lệ (Không được để trống).
                                        </div>
                                    </div>
                                    <small class="form-text text-muted">Tối đa 50 ký tự.</small>
                                </div>

                                <div class="form-group text-right">
                                    <button class="btn btn-primary btn-lg" type="submit">
                                        <i class="fas fa-save"></i> ${role != null ? 'Lưu Thay Đổi' : 'Tạo Mới'}
                                    </button>
                                    <a href="staffRoles" class="btn btn-secondary btn-lg ml-2">Hủy bỏ</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card card-info">
                        <div class="card-header">
                            <h4><i class="fas fa-info-circle"></i> Thông tin</h4>
                        </div>
                        <div class="card-body">
                            <p>Vai trò (Role) được sử dụng để phân quyền trong hệ thống.</p>
                            <ul>
                                <li><strong>Admin:</strong> Quản lý toàn bộ hệ thống.</li>
                                <li><strong>Staff:</strong> Chỉ thực hiện các tác vụ bán hàng.</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />