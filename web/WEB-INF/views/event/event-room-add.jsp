<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="event-rooms?action=LIST" class="btn btn-icon">
                    <i class="fas fa-arrow-left"></i>
                </a>
            </div>
            <h1>Add New Event Hall</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="event-rooms?action=LIST">Event Rooms</a></div>
                <div class="breadcrumb-item">Create</div>
            </div>
        </div>

        <div class="section-body">
            <h2 class="section-title">Create New Hall</h2>
            <p class="section-lead">
                Fill in the form below to add a new event hall or conference room.
            </p>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8 mx-auto">
                    <div class="card">
                        <div class="card-header">
                            <h4><i class="fas fa-plus-circle"></i> New Hall Form</h4>
                        </div>
                        <div class="card-body">

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible show fade">
                                    <div class="alert-body">
                                        <button class="close" data-dismiss="alert"><span>&times;</span></button>
                                        <i class="fas fa-exclamation-circle"></i> ${error}
                                    </div>
                                </div>
                            </c:if>

                            <form action="event-rooms" method="POST" id="addForm">
                                <input type="hidden" name="action" value="CREATE">

                                <div class="form-group">
                                    <label>Hall Name / Number <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-place-of-worship"></i>
                                            </div>
                                        </div>
                                        <input type="text" id="roomNumber" name="roomNumber" 
                                               class="form-control ${not empty roomNumberError ? 'is-invalid' : ''}" 
                                               value="${room.roomNumber}" 
                                               placeholder="Ex: HALL-A, Grand Ballroom...">
                                    </div>
                                    <small class="form-text text-muted">Unique name for the event space.</small>
                                    
                                    <small id="nameErrorMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Hall Name is required!
                                    </small>
                                    
                                    <c:if test="${not empty roomNumberError}">
                                        <div class="invalid-feedback d-block">
                                            ${roomNumberError}
                                        </div>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <label>Hall Type <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-layer-group"></i></div>
                                        </div>
                                        <select name="typeId" class="form-control">
                                            <c:forEach var="t" items="${listType}">
                                                <option value="${t.typeId}" ${room.typeId == t.typeId ? 'selected' : ''}>
                                                    ${t.typeName} (Capacity: ${t.capacity} guests)
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Access Password <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-key"></i></div>
                                        </div>
                                        <input type="text" id="roomPassword" name="roomPassword" 
                                               class="form-control" 
                                               value="${room.roomPassword}" 
                                               placeholder="Password for service login">
                                    </div>
                                    <small id="passErrorMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Password is required!
                                    </small>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Login Settings</div>
                                    <label class="custom-switch mt-2">
                                        <input type="checkbox" name="activeLogin" value="true" class="custom-switch-input" checked>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">Allow login to order services?</span>
                                    </label>
                                </div>

                                <div class="form-group text-right">
                                    <a href="event-rooms?action=LIST" class="btn btn-secondary btn-lg mr-2">
                                        <i class="fas fa-times"></i> Cancel
                                    </a>
                                    <button type="submit" class="btn btn-primary btn-lg" onclick="return validateHall()">
                                        <i class="fas fa-save"></i> Create Hall
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<script>
    function validateHall() {
        let isValid = true;

        // 1. Validate Room Number
        let inputName = document.getElementById("roomNumber");
        let errorName = document.getElementById("nameErrorMsg");
        let valName = inputName.value.trim();

        // Reset style
        inputName.classList.remove("is-invalid");
        errorName.style.display = "none";

        if (valName === "") {
            errorName.innerText = "Hall Name cannot be empty!";
            errorName.style.display = "block";
            inputName.classList.add("is-invalid");
            inputName.focus();
            isValid = false;
        } else if (valName.length < 3) {
            errorName.innerText = "Hall Name must be at least 3 characters!";
            errorName.style.display = "block";
            inputName.classList.add("is-invalid");
            inputName.focus();
            isValid = false;
        }

        // 2. Validate Password (Optional validation logic)
        let inputPass = document.getElementById("roomPassword");
        let errorPass = document.getElementById("passErrorMsg");
        let valPass = inputPass.value.trim();
        
        inputPass.classList.remove("is-invalid");
        errorPass.style.display = "none";

        if (valPass === "") {
            errorPass.innerText = "Password cannot be empty!";
            errorPass.style.display = "block";
            inputPass.classList.add("is-invalid");
            if (isValid) inputPass.focus(); // Chỉ focus nếu lỗi trên chưa focus
            isValid = false;
        }

        return isValid;
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />