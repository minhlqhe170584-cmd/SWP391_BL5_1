<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/views/common/header.jsp" />
<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

<div class="main-content">
    <section class="section">
        <div class="section-header">
            <h1>Edit Event Package (ID: ${event.eventId})</h1>
        </div>

        <div class="section-body">
            <div class="row">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="card">
                        <form method="POST" action="event-packages" novalidate>
                            <div class="card-body">

                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger">${errorMessage}</div>
                                    <c:remove var="errorMessage" scope="session"/>
                                </c:if>

                                <input type="hidden" name="action" value="UPDATE">
                                <input type="hidden" name="eventId" value="${event.eventId}">
                                <input type="hidden" name="eventCatId" value="5">

                                <div class="form-group">
                                    <label>Package Name <span class="text-danger">*</span></label>
                                    <input type="text"
                                           name="eventName"
                                           class="form-control"
                                           value="${param.eventName != null ? param.eventName : event.eventName}"
                                           required
                                           readonly="">
                                    <c:if test="${not empty validationErrors.eventName}">
                                        <small class="text-danger">${validationErrors.eventName}</small>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <label>Price per Table (VNĐ) <span class="text-danger">*</span></label>
                                    <input type="number"
                                           name="pricePerTable"
                                           class="form-control"
                                           min="0"
                                           step="1000"
                                           required
                                           value="${param.pricePerTable != null ? param.pricePerTable : event.pricePerTable}">
                                    <c:if test="${not empty validationErrors.pricePerTable}">
                                        <small class="text-danger">${validationErrors.pricePerTable}</small>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <label>Category <span class="text-danger">*</span></label>
                                    <select class="form-control" disabled>
                                        <c:forEach var="cat" items="${categories}">
                                            <c:if test="${cat.eventCatId == 5}">
                                                <option selected>
                                                    ${cat.categoryName}
                                                </option>
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Associated Room Numbers <span class="text-danger">*</span></label>
                                    <select id="roomSelect" class="form-control">
                                        <option value="">-- Select a room --</option>
                                        <c:forEach var="room" items="${rooms}">
                                            <option value="${room.roomId}">
                                                ${room.roomNumber}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty validationErrors.roomIds}">
                                        <small id="roomIdsError" class="text-danger">${validationErrors.roomIds}</small>
                                    </c:if>
                                </div>

                                <div class="form-group">
                                    <div id="selectedRoomsDisplay" 
                                         class="form-control" 
                                         style="min-height: 100px; padding: 5px; cursor: default;">
                                    </div>
                                </div>

                                <div id="selectedRoomsInputs"></div>

                                <small class="form-text text-muted">
                                    Chọn từng phòng trong dropdown, phòng đã chọn sẽ hiển thị bên trên.
                                </small>

                                <div class="form-group">
                                    <label>Status <span class="text-danger">*</span></label>
                                    <select name="status" class="form-control" required>
                                        <c:set var="currentStatus" value="${param.status != null ? param.status : event.status}"/>

                                        <option value="Active" 
                                                <c:if test="${currentStatus == 'Active'}">selected</c:if>>Active</option>
                                                <option value="Inactive" 
                                                <c:if test="${currentStatus == 'Inactive'}">selected</c:if>>Inactive</option>
                                        </select>
                                    </div>

                                </div>

                                <div class="card-footer text-right">
                                    <a href="event-packages?action=LIST" class="btn btn-secondary">Cancel</a>
                                    <button type="submit" class="btn btn-primary">Update Package</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <script>
        const roomSelect = document.getElementById("roomSelect");
        const display = document.getElementById("selectedRoomsDisplay");
        const inputContainer = document.getElementById("selectedRoomsInputs");

        // 1. Lấy tất cả các tùy chọn phòng có sẵn (RoomId -> RoomNumber)
        const availableRooms = new Map();
        for (let i = 0; i < roomSelect.options.length; i++) {
            const option = roomSelect.options[i];
            if (option.value) {
                availableRooms.set(option.value, option.text.trim());
            }
        }

        const selectedRooms = new Map(); // roomId -> roomNumber

        function loadInitialRooms() {
            // MẢNG CHỨA CÁC ID PHÒNG CẦN ĐƯỢC CHỌN BAN ĐẦU
            let initialRoomIds = [];

            // 1. Ưu tiên lấy từ paramValues (khi form bị lỗi và forward lại)
    <c:if test="${not empty paramValues.roomIds}">
        <c:forEach var="roomId" items="${paramValues.roomIds}">
            // Tránh lỗi khi giá trị roomId là null/empty
            if ("${roomId}")
                initialRoomIds.push("${roomId}");
        </c:forEach>
    </c:if>

            // 2. Nếu không có lỗi, lấy từ đối tượng event (khi doGet lần đầu)
    <c:if test="${empty paramValues.roomIds && not empty event.location}">
            // event.location là chuỗi ID "1,2,3"
            const eventLocation = "${event.location}";
            initialRoomIds = eventLocation.split(',').filter(id => id.trim() !== '');
    </c:if>


            // Điền dữ liệu vào map selectedRooms
            initialRoomIds.forEach(id => {
                const roomName = availableRooms.get(id);
                if (roomName) {
                    selectedRooms.set(id, roomName);
                }
            });

            updateUI();
        }

        // Xử lý khi người dùng chọn phòng từ dropdown
        roomSelect.addEventListener("change", function () {
            const roomId = this.value;
            const roomName = this.options[this.selectedIndex]?.text;

            if (!roomId || selectedRooms.has(roomId)) {
                this.value = "";
                return;
            }

            selectedRooms.set(roomId, roomName);
            updateUI();
            this.value = "";
        });

    // Thêm hàm xóa phòng
        function removeRoom(roomId) {
            selectedRooms.delete(roomId);
            updateUI(); // Gọi lại hàm cập nhật giao diện
        }

    // Hàm cập nhật hiển thị và hidden inputs (ĐÃ CHỈNH SỬA)
        function updateUI() {
            // 1. Cập nhật hiển thị danh sách phòng (Tạo Chips)
            const displayContainer = document.getElementById("selectedRoomsDisplay");
            displayContainer.innerHTML = "";

            selectedRooms.forEach((name, id) => {
                // Tạo Chip/Tag
                const chip = document.createElement("span");
                chip.className = "badge badge-primary mr-1 mb-1"; // Sử dụng class Bootstrap/Stisla cho đẹp
                chip.style.padding = "6px 10px";
                chip.style.fontSize = "90%";

                // Thêm tên phòng
                const roomNameText = document.createTextNode(name + " ");
                chip.appendChild(roomNameText);

                // Thêm nút Xóa (X icon)
                const removeBtn = document.createElement("a");
                removeBtn.textContent = "×";
                removeBtn.href = "#";
                removeBtn.style.marginLeft = "5px";
                removeBtn.style.color = "white";
                removeBtn.style.fontWeight = "bold";
                removeBtn.onclick = function (e) {
                    e.preventDefault();
                    removeRoom(id); // Gọi hàm xóa phòng
                };

                chip.appendChild(removeBtn);
                displayContainer.appendChild(chip);
            });

            // 2. Tạo hidden inputs
            inputContainer.innerHTML = "";
            selectedRooms.forEach((name, id) => {
                const input = document.createElement("input");
                input.type = "hidden";
                input.name = "roomIds"; // Rất quan trọng
                input.value = id;
                inputContainer.appendChild(input);
            });

            // 3. Cập nhật/Xóa thông báo lỗi RoomIds
            const errorElement = document.getElementById('roomIdsError');
            if (errorElement) {
                if (selectedRooms.size > 0) {
                    errorElement.textContent = "";
                }
            }
        }

    // Tải lại các phòng đã chọn khi trang được tải
        loadInitialRooms();
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />