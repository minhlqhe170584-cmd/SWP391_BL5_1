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

            <h1>Edit Room Information</h1>

            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="rooms?action=LIST">Rooms</a></div>
                <div class="breadcrumb-item">Edit</div>
            </div>
        </div>

        <div class="section-body">

            <h2 class="section-title">Update Room Details</h2>
            <p class="section-lead">
                Modify the information below to update the room.
            </p>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8 mx-auto">
                    <div class="card">

                        <div class="card-header">
                            <h4><i class="fas fa-edit"></i> Edit Form</h4>
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

                                <input type="hidden" name="action" value="UPDATE">
                                <input type="hidden" name="roomId" value="${room.roomId}">

                                <div class="form-group">
                                    <label>Room Number <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-door-open"></i>
                                            </div>
                                        </div>

                                        <input type="text"
                                               id="roomNumber"
                                               name="roomNumber"
                                               value="${room.roomNumber}"
                                               class="form-control"
                                               readonly>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Room Type</label>
                                    <select name="typeId" class="form-control selectric">
                                        <c:forEach var="t" items="${listType}">
                                            <option value="${t.typeId}"
                                                    <c:if test="${room.typeId == t.typeId}">selected</c:if>>
                                                ${t.typeName} (Capacity: ${t.capacity} people)
                                            </option>
                                        </c:forEach>
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
                                        <input type="text"
                                               id="roomPassword"
                                               name="roomPassword"
                                               value="${room.roomPassword}"
                                               class="form-control">
                                    </div>
                                    <small id="errorPassMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Password cannot be empty!
                                    </small>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Settings</div>
                                    <label class="custom-switch mt-2">
                                        <input type="checkbox"
                                               name="activeLogin"
                                               value="true"
                                               class="custom-switch-input"
                                               ${room.activeLogin ? 'checked' : ''}>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">
                                            Enable Guest Login
                                        </span>
                                    </label>
                                </div>

                                <div class="form-group text-right">
                                    <a href="rooms?action=LIST" class="btn btn-secondary btn-lg mr-2">
                                        <i class="fas fa-times"></i> Cancel
                                    </a>
                                    <button type="submit" class="btn btn-primary btn-lg" onclick="return validateRoom()">
                                        <i class="fas fa-save"></i> Update
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
        // 1. Lấy phần tử input và thẻ lỗi
        // (Ở màn hình Edit, roomNumber là readonly nhưng vẫn lấy để tránh lỗi script, 
        //  tuy nhiên ta chủ yếu validate Password)
        var inputElement = document.getElementById("roomNumber");
        var errorText = document.getElementById("errorMsg"); // Có thể không có div này ở Edit nếu bạn không copy sang, tôi check kỹ bên dưới

        var passElement = document.getElementById("roomPassword");
        var errorPassText = document.getElementById("errorPassMsg");

        // Validate Password (Quan trọng nhất ở Edit)
        if (passElement) {
            // Reset lỗi password
            if (errorPassText) errorPassText.style.display = "none";
            passElement.classList.remove("is-invalid");

            var passInput = passElement.value.trim();
            if (passInput === "") {
                if (errorPassText) {
                    errorPassText.innerText = "Room Password cannot be empty!";
                    errorPassText.style.display = "block";
                }
                passElement.focus();
                passElement.classList.add("is-invalid");
                return false;
            }
        }
        
        // Hợp lệ
        return true; 
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />