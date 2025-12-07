<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        
        <div class="section-header">
            <div class="section-header-back">
                <a href="staffs" class="btn btn-icon"><i class="fas fa-arrow-left"></i></a>
            </div>
            <h1>${staff != null ? 'Cập nhật Nhân viên' : 'Thêm Nhân viên Mới'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="staffs">Nhân viên</a></div>
                <div class="breadcrumb-item">${staff != null ? 'Cập nhật' : 'Thêm mới'}</div>
            </div>
        </div>

        <div class="section-body">
            <h2 class="section-title">${staff != null ? 'Chỉnh sửa thông tin' : 'Tạo tài khoản mới'}</h2>
            <p class="section-lead">
                ${staff != null ? 'Cập nhật thông tin chi tiết cho nhân viên này.' : 'Điền đầy đủ thông tin để tạo nhân viên mới vào hệ thống.'}
            </p>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8">
                    <div class="card">
                        <div class="card-header">
                            <h4>Form Thông tin</h4>
                        </div>
                        
                        <div class="card-body">
                            <form method="POST" action="staffs" class="needs-validation" novalidate="">
                                
                                <input type="hidden" name="action" value="${staff != null ? 'update' : 'create'}">
                                
                                <c:if test="${staff != null}">
                                    <input type="hidden" name="staffId" value="${staff.staffId}">
                                </c:if>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Họ và Tên <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="fullName" required="" 
                                               value="${staff.fullName}" placeholder="Ví dụ: Nguyễn Văn A">
                                        <div class="invalid-feedback">Vui lòng nhập họ tên.</div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Email <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="email" class="form-control" name="email" required="" 
                                               value="${staff.email}" placeholder="email@example.com">
                                        <div class="invalid-feedback">Email không hợp lệ.</div>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Mật khẩu <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <input type="text" class="form-control" name="password" 
                                               ${staff == null ? 'required' : ''} 
                                               value="${staff.passWordHash}"
                                               placeholder="Nhập mật khẩu...">
                                        <c:if test="${staff != null}">
                                            <small class="form-text text-muted">Nhập mật khẩu mới nếu muốn thay đổi, hoặc giữ nguyên mật khẩu cũ.</small>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label class="col-sm-3 col-form-label">Vai trò <span class="text-danger">*</span></label>
                                    <div class="col-sm-9">
                                        <select class="form-control selectric" name="roleId">
                                            <c:forEach var="r" items="${rolesList}">
                                                <option value="${r.roleId}" ${staff.role.roleId == r.roleId ? 'selected' : ''}>
                                                    ${r.roleName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group row mb-4">
                                    <label class="col-form-label text-md-right col-12 col-md-3 col-lg-3"></label>
                                    <div class="col-sm-12 col-md-7">
                                        <button class="btn btn-primary btn-lg" type="submit">
                                            <i class="fas fa-save"></i> ${staff != null ? 'Lưu Thay Đổi' : 'Tạo Nhân Viên'}
                                        </button>
                                        <a href="staffs" class="btn btn-secondary btn-lg ml-2">Hủy bỏ</a>
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
                                <li class="mb-2"><i class="fas fa-check-circle text-success"></i> Vai trò quyết định quyền hạn của nhân viên.</li>
                            </ul>
                        </div>
                    </div>
                </div>
                
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />