<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <div class="section-header-back">
                <a href="room-types?action=LIST" class="btn btn-icon">
                    <i class="fas fa-arrow-left"></i>
                </a>
            </div>
            <h1>Edit Room Type</h1>
            <div class="section-header-breadcrumb">
                <div class="breadcrumb-item active"><a href="#">Dashboard</a></div>
                <div class="breadcrumb-item"><a href="room-types?action=LIST">Room Types</a></div>
                <div class="breadcrumb-item">Edit</div>
            </div>
        </div>

        <div class="section-body">
            <h2 class="section-title">Update Room Type</h2>
            <p class="section-lead">
                Modify the details below to update the room category.
            </p>

            <div class="row">
                <div class="col-12 col-md-8 col-lg-8 mx-auto">
                    <div class="card">
                        <div class="card-header">
                            <h4><i class="fas fa-edit"></i> Edit Form</h4>
                        </div>
                        <div class="card-body">

                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">
                                    ${error}
                                </div>
                            </c:if>

                            <form id="editForm" action="room-types" method="POST" enctype="multipart/form-data" onsubmit="return validateForm()">
                                <input type="hidden" name="action" value="UPDATE">
                                <input type="hidden" name="typeId" value="${roomType.typeId}">
                                <input type="hidden" name="oldImageUrl" value="${roomType.imageUrl}">

                                <div class="form-group">
                                    <label>Type Name <span class="text-danger">*</span></label>
                                    <input type="text" id="typeName" name="typeName" 
                                           class="form-control" 
                                           value="${roomType.typeName}">
                                    <small id="errorName" class="text-danger" style="display:none;">Type Name cannot be empty!</small>
                                </div>

                                <div class="form-group">
                                    <label>Capacity (Person) <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fas fa-users"></i></div>
                                        </div>
                                        <input type="number" id="capacity" name="capacity" 
                                               class="form-control" 
                                               value="${roomType.capacity}">
                                    </div>
                                    <small id="errorCapacity" class="text-danger" style="display:none;">Capacity must be greater than 0!</small>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Base Price (Weekday) <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text">₫</div>
                                                </div>
                                                <input type="number" id="priceWeekday" name="basePriceWeekday" 
                                                       class="form-control" 
                                                       value="${roomType.basePriceWeekday}">
                                            </div>
                                            <small id="errorPrice1" class="text-danger" style="display:none;">Price must be greater than 0!</small>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label>Base Price (Weekend) <span class="text-danger">*</span></label>
                                            <div class="input-group">
                                                <div class="input-group-prepend">
                                                    <div class="input-group-text">₫</div>
                                                </div>
                                                <input type="number" id="priceWeekend" name="basePriceWeekend" 
                                                       class="form-control" 
                                                       value="${roomType.basePriceWeekend}">
                                            </div>
                                            <small id="errorPrice2" class="text-danger" style="display:none;">Price must be greater than 0!</small>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Room Image</label>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <div style="width: 100px; height: 100px; border: 1px dashed #ddd; padding: 5px; display: flex; align-items: center; justify-content: center;">
                                                <img id="imagePreview" 
                                                     src="${not empty roomType.imageUrl ? pageContext.request.contextPath.concat(roomType.imageUrl) : 'https://placehold.co/100x100?text=No+Img'}" 
                                                     style="max-width: 100%; max-height: 100%; object-fit: cover;">
                                            </div>
                                        </div>
                                        <div class="col-md-9">
                                            <div class="custom-file mt-3">
                                                <input type="file" class="custom-file-input" id="customFile" name="imageFile" accept="image/*" onchange="previewImage(this)">
                                                <label class="custom-file-label" for="customFile">Choose file from device...</label>
                                            </div>
                                            <small class="form-text text-muted">Max size: 2MB.</small>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label>Description</label>
                                    <textarea name="description" class="form-control" style="height: 100px;">${roomType.description}</textarea>
                                </div>

                                <div class="form-group">
                                    <div class="control-label">Status</div>
                                    <label class="custom-switch mt-2">
                                        <input type="checkbox" name="isActive" class="custom-switch-input" 
                                               ${roomType.isActive ? 'checked' : ''}>
                                        <span class="custom-switch-indicator"></span>
                                        <span class="custom-switch-description">Active (Available for booking)</span>
                                    </label>
                                </div>

                                <div class="form-group text-right">
                                    <a href="room-types?action=LIST" class="btn btn-secondary mr-2">Cancel</a>
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="fas fa-save"></i> Update Changes
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
    // Preview Image
    function previewImage(input) {
        if (input.files && input.files[0]) {
            var fileSize = input.files[0].size;
            var maxSize = 2 * 1024 * 1024; // 2MB

            if (fileSize > maxSize) {
                alert("File size must be less than 2MB.");
                return false;
            }



            // Kiểm tra loại file
            var allowedMIMETypes = ['image/x-bmp', 'image/tiff', 'image/gif', 'image/jpeg', 'image/png', 'image/svg+xml', 'image/webp', 'image/heif', 'image/avif'];

            if (!allowedMIMETypes.includes(input.files[0].type)) {
                alert("Only image files are allowed.");
                return false;
            }



            // Continue with the rest of your code
            var reader = new FileReader();
            reader.onload = function (e) {
                document.getElementById('imagePreview').src = e.target.result;
            }
            reader.readAsDataURL(input.files[0]);
            var fileName = input.files[0].name;
            input.nextElementSibling.innerText = fileName;
        }
    }

    // Validate Form (Đã nâng cấp)
    function validateForm() {
        let isValid = true;

        // 1. Get Elements
        let nameInput = document.getElementById("typeName");
        let capInput = document.getElementById("capacity");
        let p1Input = document.getElementById("priceWeekday");
        let p2Input = document.getElementById("priceWeekend");

        // 2. Reset Errors
        resetError(nameInput, "errorName");
        resetError(capInput, "errorCapacity");
        resetError(p1Input, "errorPrice1");
        resetError(p2Input, "errorPrice2");

        // 3. Validate Logic

        // --- Type Name ---
        if (nameInput.value.trim() === "") {
            showError(nameInput, "errorName", "Type Name cannot be empty!");
            isValid = false;
        }

        // --- Capacity ---
        let capVal = capInput.value;
        if (capVal === "" || isNaN(capVal)) {
            showError(capInput, "errorCapacity", "Capacity must be a valid number!");
            isValid = false;
        } else if (!Number.isInteger(Number(capVal))) {
            showError(capInput, "errorCapacity", "Capacity must be an integer (no decimals)!");
            isValid = false;
        } else if (parseInt(capVal) <= 0) {
            showError(capInput, "errorCapacity", "Capacity must be greater than 0!");
            isValid = false;
        }

        // --- Price Weekday ---
        let p1Val = p1Input.value;
        if (p1Val === "" || isNaN(p1Val)) {
            showError(p1Input, "errorPrice1", "Price must be a valid number!");
            isValid = false;
        } else if (parseFloat(p1Val) <= 0) {
            showError(p1Input, "errorPrice1", "Price must be greater than 0!");
            isValid = false;
        }

        // --- Price Weekend ---
        let p2Val = p2Input.value;
        if (p2Val === "" || isNaN(p2Val)) {
            showError(p2Input, "errorPrice2", "Price must be a valid number!");
            isValid = false;
        } else if (parseFloat(p2Val) <= 0) {
            showError(p2Input, "errorPrice2", "Price must be greater than 0!");
            isValid = false;
        }

        return isValid;
    }

    // Hàm phụ trợ hiện lỗi (Có tham số message động)
    function showError(input, errorId, message) {
        input.classList.add("is-invalid");
        let errorTag = document.getElementById(errorId);
        errorTag.innerText = message; // Cập nhật nội dung lỗi
        errorTag.style.display = "block";
    }

    // Hàm phụ trợ ẩn lỗi
    function resetError(input, errorId) {
        input.classList.remove("is-invalid");
        document.getElementById(errorId).style.display = "none";
    }
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp" />