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
            <h1>${role != null ? 'Cập nhật Vai trò' : 'Add New Role'}</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="dashboard">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="staffRoles">Roles Management</a></div>
                <div class="breadcrumb-item">${role != null ? 'Cập nhật' : 'Thêm mới'}</div>
            </div>
        </div>

        <div class="section-body">

            <div class="row">
                <div class="col-12 col-md-6 col-lg-6">
                    <div class="card">
                        <div class="card-header">
                            <h4>Form Add Role</h4>
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
                                        <input type="text" class="form-control" name="roleName" required="" 
                                               value="${role.roleName}" placeholder="Ví dụ: Receptionist, Manager...">
                                    </div>
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
                                <li><strong>Manager:</strong> Quản lý nhân viên và báo cáo.</li>
                                <li><strong>Staff:</strong> Chỉ thực hiện các tác vụ bán hàng/đặt phòng.</li>
                            </ul>
                            <div class="alert alert-light">
                                <b>Lưu ý:</b> Xóa một vai trò có thể ảnh hưởng đến các nhân viên đang nắm giữ vai trò đó.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />