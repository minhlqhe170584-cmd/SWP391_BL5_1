<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="rooms?action=LIST" class="btn btn-icon">
                    <i class="fas fa-arrow-left"></i>
                </a>
            </div>

            <h1>Add New Room</h1>

            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="rooms?action=LIST">Rooms</a></div>
                <div class="breadcrumb-item">Create</div>
            </div>
        </div>

        <div class="section-body">

            <h2 class="section-title">Create New Room</h2>
            <p class="section-lead">
                Fill in the form below to create a new room in the system.
            </p>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8 mx-auto">
                    <div class="card">

                        <div class="card-header">
                            <h4><i class="fas fa-plus-circle"></i> New Room Form</h4>
                        </div>

                        <div class="card-body">

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible show fade">
                                    <div class="alert-body">
                                        <button class="close" data-dismiss="alert">
                                            <span>&times;</span>
                                        </button>
                                        <i class="fas fa-exclamation-circle"></i>
                                        ${error}
                                    </div>
                                </div>
                            </c:if>

                            <form action="rooms" method="POST">

                                <input type="hidden" name="action" value="CREATE">

                                <div class="form-group">
                                    <label>Room Number <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-door-open"></i>
                                            </div>
                                        </div>
                                        <%-- THÊM value="${roomNumber}" để giữ lại giá trị --%>
                                        <input type="text"
                                               id="roomNumber"
                                               name="roomNumber"
                                               value="${roomNumber}" 
                                               class="form-control"
                                               placeholder="Ex: 101, 205..."
                                               minlength="3">
                                    </div>

                                    <small class="form-text text-muted">
                                        Unique identifier for the room (e.g., 101).
                                    </small>
                                    <small id="errorMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Room Number cannot be null. Please enter the room number!
                                    </small>
                                </div>

                                <div class="form-group">
                                    <label>Room Type</label>
                                    <select name="typeId" class="form-control selectric">
                                        <c:forEach var="t" items="${listType}">
                                            <%-- THÊM logic selected để chọn lại đúng loại vừa chọn --%>
                                            <option value="${t.typeId}" ${t.typeId == typeId ? 'selected' : ''}>
                                                ${t.typeName} (Capacity: ${t.capacity} people)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Status</label>
                                    <select name="status" class="form-control selectric">
                                        <%-- THÊM logic selected --%>
                                        <option value="Available" ${status == 'Available' ? 'selected' : ''}>Available</option>
                                        <option value="Maintenance" ${status == 'Maintenance' ? 'selected' : ''}>Maintenance</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Room Password (For Guest Login) <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-key"></i>
                                            </div>
                                        </div>
                                        <%-- THÊM value="${roomPassword}" --%>
                                        <input type="text"
                                               id="roomPassword"
                                               name="roomPassword"
                                               value="${roomPassword}"
                                               class="form-control"
                                               placeholder="Enter a secure password">
                                    </div>
                                    <small id="errorPassMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Password cannot be empty!
                                    </small>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Settings</div>
                                    <label class="custom-switch mt-2">
                                        <%-- THÊM logic checked --%>
                                        <input type="checkbox"
                                               name="activeLogin"
                                               value="true"
                                               class="custom-switch-input"
                                               ${activeLogin == 'true' ? 'checked' : ''}>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">
                                            Enable Guest Login (Allow guests to access services)
                                        </span>
                                    </label>
                                </div>

                                <div class="form-group text-right">
                                    <a href="rooms?action=LIST" class="btn btn-secondary btn-lg mr-2">
                                        <i class="fas fa-times"></i> Cancel
                                    </a>
                                    <button type="submit" class="btn btn-primary btn-lg" onclick="return validateRoom()">
                                        <i class="fas fa-save"></i> Save
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
    function validateRoom() {
        var inputElement = document.getElementById("roomNumber");
        var errorText = document.getElementById("errorMsg");
        var passElement = document.getElementById("roomPassword");
        var errorPassText = document.getElementById("errorPassMsg");

        if (!inputElement || !errorText) return false;

        var roomInput = inputElement.value.trim();

        // Reset
        errorText.style.display = "none";
        inputElement.classList.remove("is-invalid");

        if (passElement && errorPassText) {
            errorPassText.style.display = "none";
            passElement.classList.remove("is-invalid");
        }

        // Validate Room
        if (roomInput === "") {
            errorText.innerText = "Room Number cannot be null. Please enter the room number!";
            errorText.style.display = "block";
            inputElement.focus();
            inputElement.classList.add("is-invalid"); 
            return false;
        } 
        else if (roomInput.length < 3) {
            errorText.innerText = "Room Number must be at least 3 characters!";
            errorText.style.display = "block";
            inputElement.focus();
            inputElement.classList.add("is-invalid");
            return false;
        }

        // Validate Password
        if (passElement) {
            var passInput = passElement.value.trim();
            if (passInput === "") {
                errorPassText.innerText = "Room Password cannot be empty!";
                errorPassText.style.display = "block";
                passElement.focus();
                passElement.classList.add("is-invalid");
                return false;
            }
        }
        
        return true; 
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />