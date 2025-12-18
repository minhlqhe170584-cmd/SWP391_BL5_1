<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Đặt Sự Kiện | Smart Hotel</title>
        <jsp:include page="../components/head.jsp"/>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    </head>

    <body>
        <jsp:include page="../components/navbar.jsp"/>

        <div class="breadcrumb-section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="breadcrumb-text">
                            <h2>Xác Nhận Đặt Sự Kiện</h2>
                            <div class="bt-option">
                                <a href="${pageContext.request.contextPath}/event-booking">Danh sách Sự Kiện</a>
                                <span>Thuê Sự Kiện</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <section class="contact-section spad">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="font-weight-bold mb-3">1. Chọn sự kiện</h5>

                                <c:if test="${empty events}">
                                    <div class="alert alert-warning">
                                        Chưa có sự kiện nào được tải lên.
                                    </div>
                                </c:if>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger text-center mb-4">
                                        <i class="fa fa-exclamation-circle"></i>
                                        ${errorMessage}
                                    </div>
                                </c:if>

                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success text-center mb-4">
                                        <i class="fa fa-check-circle"></i>
                                        ${successMessage}
                                    </div>
                                </c:if>


                                <form method="get" action="${pageContext.request.contextPath}/event-booking">
                                    <select name="eventId"
                                            class="form-control"
                                            onchange="this.form.submit()">
                                        <option value="">-- Chọn sự kiện --</option>

                                        <c:forEach var="e" items="${events}">
                                            <option value="${e.eventId}"
                                                    <c:if test="${event != null && event.eventId == e.eventId}">
                                                        selected
                                                    </c:if>>
                                                ${e.eventName} - <fmt:formatNumber value="${e.pricePerTable}" type="currency" currencySymbol="VND"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </form>
                            </div>
                        </div>
                        <c:if test="${not empty event}">

                            <div class="card mb-4">
                                <div class="card-body">
                                    <h5 class="font-weight-bold text-warning mb-2">
                                        2. Thông tin sự kiện
                                    </h5>
                                    <p><strong>Tên sự kiện:</strong> ${event.eventName}</p>
                                    <p>
                                        <strong>Giá: </strong>
                                        <fmt:formatNumber value="${event.pricePerTable}" type="currency" currencySymbol="VND"/>
                                    </p>
                                </div>
                            </div>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">
                                    ${errorMessage}
                                </div>
                            </c:if>

                            <div class="card">
                                <div class="card-body">
                                    <h5 class="font-weight-bold mb-3">3. Đặt sự kiện</h5>

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/event-booking">

                                        <input type="hidden" name="eventId" value="${event.eventId}"/>
                                        <div class="form-group">
                                            <label>Chọn phòng</label>

                                            <c:forEach var="room" items="${roomList}">
                                                <div class="form-check">
                                                    <input type="checkbox" name="roomIds" value="${room.roomId}" class="form-check-input" id="r_${room.roomId}"
                                                           <c:if test="${paramValues.roomIds != null}">
                                                               <c:forEach var="selectedRoomId" items="${paramValues.roomIds}">
                                                                   <c:if test="${selectedRoomId == room.roomId}">checked</c:if>
                                                               </c:forEach>
                                                           </c:if>>
                                                    <label class="form-check-label" for="r_${room.roomId}">
                                                        Phòng ${room.roomNumber}
                                                    </label>
                                                </div>
                                            </c:forEach>

                                            <c:if test="${empty roomList}">
                                                <p class="text-muted text-small">Không có phòng cụ thể cho gói này (Áp dụng linh hoạt).</p>
                                            </c:if>
                                        </div>
                                        <div class="form-group">
                                            <label>Thời gian bắt đầu (Ngày & Giờ)</label>
                                            <input type="text" id="checkInDate" name="checkInDate" value="${param.checkInDate}"
                                                   class="form-control bg-white"
                                                   placeholder="Chọn ngày giờ bắt đầu..." readonly required>
                                        </div>

                                        <div class="form-group">
                                            <label>Thời gian kết thúc (Ngày & Giờ)</label>
                                            <input type="text" id="checkOutDate" name="checkOutDate" value="${param.checkOutDate}"
                                                   class="form-control bg-white"
                                                   placeholder="Chọn ngày giờ kết thúc..." readonly required>
                                        </div>

                                        <div class="form-group">
                                            <label>Ghi chú</label>
                                            <textarea name="message" class="form-control" rows="4" maxlength="100">${param.message}</textarea>
                                        </div>

                                        <button type="submit"
                                                class="btn btn-warning btn-block">
                                            Xác nhận đặt sự kiện
                                        </button>
                                    </form>
                                </div>
                            </div>

                        </c:if>


                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

        <script>
                                                        document.addEventListener('DOMContentLoaded', function () {
                                                            const checkOutInput = document.getElementById('checkOutDate');
                                                            const checkInInput = document.getElementById('checkInDate');

                                                            // Khởi tạo cho trường KẾT THÚC (checkOutDate)
                                                            const checkOutPicker = flatpickr("#checkOutDate", {
                                                                enableTime: true,
                                                                dateFormat: "Y-m-d H:i",
                                                                time_24hr: true,
                                                                minDate: new Date(), // lấy đúng ngày + giờ hiện tại
                                                                minuteIncrement: 15,
                                                                defaultDate: checkOutInput.value || new Date() // giữ giá trị cũ, nếu rỗng thì giờ hiện tại
                                                            });

                                                            // Khởi tạo cho trường BẮT ĐẦU (checkInDate)
                                                            const checkInPicker = flatpickr("#checkInDate", {
                                                                enableTime: true,
                                                                dateFormat: "Y-m-d H:i",
                                                                time_24hr: true,
                                                                minDate: new Date(), // lấy đúng ngày + giờ hiện tại
                                                                minuteIncrement: 15,
                                                                defaultDate: checkInInput.value || new Date(), // giữ giá trị cũ, nếu rỗng thì giờ hiện tại

                                                                // Ngày kết thúc phải sau ngày bắt đầu
                                                                onChange: function (selectedDates) {
                                                                    if (selectedDates.length > 0) {
                                                                        checkOutPicker.set('minDate', selectedDates[0]);
                                                                    }
                                                                }
                                                            });

                                                            // Nếu checkInDate đã có giá trị từ trước thì đặt minDate cho checkOutPicker
                                                            if (checkInInput.value) {
                                                                checkOutPicker.set('minDate', checkInInput.value);
                                                            }
                                                        });



        </script>
        <jsp:include page="../components/footer.jsp"/>
    </body>
</html>