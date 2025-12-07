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

            <c:choose>
                <c:when test="${room != null && room.roomId > 0}">
                    <h1>Edit Room Information</h1>
                </c:when>
                <c:otherwise>
                    <h1>Add New Room</h1>
                </c:otherwise>
            </c:choose>

            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="rooms?action=LIST">Rooms</a></div>
                <c:choose>
                    <c:when test="${room != null && room.roomId > 0}">
                        <div class="breadcrumb-item">Edit</div>
                    </c:when>
                    <c:otherwise>
                        <div class="breadcrumb-item">Create</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="section-body">
            <c:choose>
                <c:when test="${room != null && room.roomId > 0}">
                    <h2 class="section-title">Update Room Details</h2>
                    <p class="section-lead">
                        Modify the information below to update the room.
                    </p>
                </c:when>
                <c:otherwise>
                    <h2 class="section-title">Create New Room</h2>
                    <p class="section-lead">
                        Fill in the form below to create a new room in the system.
                    </p>
                </c:otherwise>
            </c:choose>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8 mx-auto">
                    <div class="card">

                        <div class="card-header">
                            <c:choose>
                                <c:when test="${room != null && room.roomId > 0}">
                                    <h4><i class="fas fa-edit"></i> Edit Form</h4>
                                </c:when>
                                <c:otherwise>
                                    <h4><i class="fas fa-plus-circle"></i> New Room Form</h4>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="card-body">

                            <!-- ERROR MESSAGE -->
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

                                <c:choose>
                                    <c:when test="${room != null && room.roomId > 0}">
                                        <input type="hidden" name="action" value="UPDATE">
                                        <input type="hidden" name="roomId" value="${room.roomId}">
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" name="action" value="CREATE">
                                    </c:otherwise>
                                </c:choose>

                                <!-- ROOM NUMBER -->
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
                                               placeholder="Ex: 101, 205..."
                                               <c:if test="${room != null && room.roomId > 0}">
                                                   readonly
                                               </c:if>
                                               >
                                    </div>
                                    <small class="form-text text-muted">
                                        Unique identifier for the room (e.g., 101).
                                    </small>

                                    <!--Thông báo nếu trường Room Number trống-->
                                    <small id="errorMsg" class="text-danger" style="display: none; margin-top: 5px;">
                                        Room Number can not be null. Please enter the room number!
                                    </small>
                                </div>

                                <!-- ROOM TYPE -->
                                <div class="form-group">
                                    <label>Room Type</label>
                                    <select name="typeId" class="form-control selectric">
                                        <c:forEach var="t" items="${listType}">
                                            <option value="${t.typeId}"
                                                    <c:if test="${room != null && room.typeId == t.typeId}">
                                                        selected
                                                    </c:if>>
                                                ${t.typeName} (Capacity: ${t.capacity} people)
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- STATUS -->
                                <div class="form-group">
                                    <label>Status</label>
                                    <select name="status" class="form-control selectric">
                                        <option value="Available" ${room.status == 'Available' ? 'selected' : ''}>Available</option>
                                        <option value="Occupied" ${room.status == 'Occupied' ? 'selected' : ''}>Occupied</option>
                                        <option value="Dirty" ${room.status == 'Dirty' ? 'selected' : ''}>Dirty</option>
                                        <option value="Maintenance" ${room.status == 'Maintenance' ? 'selected' : ''}>Maintenance</option>
                                    </select>
                                </div>

                                <!-- PASSWORD -->
                                <div class="form-group">
                                    <label>Room Password (For Guest Login)</label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="fas fa-key"></i>
                                            </div>
                                        </div>
                                        <input type="text"
                                               name="roomPassword"
                                               value="${room.roomPassword}"
                                               class="form-control"
                                               placeholder="Enter a secure password">
                                    </div>
                                </div>

                                <!-- ACTIVE LOGIN -->
                                <div class="form-group">
                                    <div class="control-label">Settings</div>
                                    <label class="custom-switch mt-2">
                                        <input type="checkbox"
                                               name="activeLogin"
                                               class="custom-switch-input"
                                               ${room.activeLogin ? 'checked' : ''}>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">
                                            Enable Guest Login (Allow guests to access services)
                                        </span>
                                    </label>
                                </div>

                                <!-- BUTTONS -->
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

<!--Validate trương room Number khi trống                                        -->
<script>
    function validateRoom() {
        // 1. Lấy phần tử input và thẻ lỗi
        var inputElement = document.getElementById("roomNumber");
        var errorText = document.getElementById("errorMsg");

        // Kiểm tra an toàn để tránh lỗi Javascript nếu ID bị sai
        if (!inputElement || !errorText) return false;

        var roomInput = inputElement.value;

        // 2. Kiểm tra điều kiện
        if (roomInput.trim() === "") {
            // Hiện lỗi
            errorText.style.display = "block";
            // Focus lại vào ô nhập liệu để người dùng biết cần nhập ở đâu
            inputElement.focus();
            inputElement.classList.add("is-invalid"); // Thêm viền đỏ của Bootstrap (nếu muốn đẹp hơn)
            return false; // Chặn form submit
        } else {
            // Ẩn lỗi
            errorText.style.display = "none";
            inputElement.classList.remove("is-invalid");
            return true; // Cho phép form submit
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
