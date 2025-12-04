<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Room Management</title>
        <style>
            /* --- 1. Cấu trúc chung & Layout --- */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f0f2f5;
                margin: 0;
                padding: 40px 20px;
                color: #444;
            }

            .container {
                max-width: 1100px;
                margin: 0 auto;
                background: #fff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            }

            h1 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 30px;
                font-size: 28px;
                border-bottom: 2px solid #eee;
                padding-bottom: 15px;
            }

            /* --- 2. Nút Thêm mới (Add New) --- */
            .btn-add {
                display: inline-flex;
                align-items: center;
                background-color: #27ae60;
                color: white;
                padding: 12px 24px;
                text-decoration: none;
                border-radius: 6px;
                font-weight: 600;
                transition: all 0.3s ease;
                box-shadow: 0 2px 5px rgba(39, 174, 96, 0.3);
                margin-bottom: 25px;
            }

            .btn-add:hover {
                background-color: #219150;
                transform: translateY(-2px);
            }

            /* --- 3. Bảng dữ liệu (Table) --- */
            table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 0 0 1px #eee;
            }

            th, td {
                padding: 16px 20px;
                text-align: left;
                border-bottom: 1px solid #f1f1f1;
            }

            th {
                background-color: #34495e;
                color: white;
                text-transform: uppercase;
                font-size: 13px;
                font-weight: 600;
                letter-spacing: 0.5px;
            }

            tr:last-child td {
                border-bottom: none;
            }

            tr:hover {
                background-color: #f8f9fa;
            }

            /* --- 4. Trạng thái & Login Active --- */
            .status-badge {
                display: inline-block;
                padding: 5px 10px;
                border-radius: 20px;
                font-size: 12px;
                font-weight: bold;
            }

            .status-available {
                background-color: #e8f5e9;
                color: #2e7d32;
            }
            .status-occupied {
                background-color: #ffebee;
                color: #c62828;
            }
            .status-other {
                background-color: #fff3e0;
                color: #ef6c00;
            }

            /* --- 5. Nhóm các nút Action (View - Edit - Delete) --- */
            .action-links {
                display: flex;
                gap: 8px; /* Khoảng cách giữa các nút */
            }

            .btn-action {
                padding: 6px 12px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 13px;
                font-weight: 600;
                transition: opacity 0.2s;
                color: white;
                text-align: center;
                min-width: 50px;
            }

            .btn-action:hover {
                opacity: 0.85;
            }

            /* Màu sắc từng nút */
            .btn-view {
                background-color: #3498db;
            } /* Xanh dương */
            .btn-edit {
                background-color: #f39c12;
            } /* Cam */
            .btn-delete {
                background-color: #e74c3c;
            } /* Đỏ */

            /* --- 6. Thông báo lỗi --- */
            .error-msg {
                background-color: #fde8e8;
                color: #c53030;
                padding: 15px;
                border: 1px solid #fbd5d5;
                border-radius: 6px;
                margin-bottom: 20px;
            }
        </style>

        <!--    css phân trang-->
        <style>
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
            }

            .pagination a {
                color: black;
                float: left;
                padding: 8px 16px;
                text-decoration: none;
                transition: background-color .3s;
                border: 1px solid #ddd;
                margin: 0 4px;
                border-radius: 5px;
            }

            /* Hiệu ứng khi di chuột */
            .pagination a:hover:not(.active) {
                background-color: #ddd;
            }

            /* Trang hiện tại (Active) */
            .pagination a.active {
                background-color: #27ae60; /* Màu xanh lá cùng tông */
                color: white;
                border: 1px solid #27ae60;
            }
        </style>

        <!--    Thẻ style css cho thanh tìm kiếm-->
        <style>
            /* --- 7. CSS Cho Thanh Tìm Kiếm (Toolbar) --- */
            .toolbar {
                display: flex;
                justify-content: space-between; /* Đẩy nút Add sang trái, Search sang phải */
                align-items: center;
                margin-bottom: 25px;
            }

            /* Chỉnh lại nút Add New để không bị margin thừa */
            .btn-add {
                margin-bottom: 0;
            }

            .search-form {
                display: flex;
                gap: 8px;
            }

            .search-input {
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                width: 220px;
                outline: none;
            }

            .btn-search {
                padding: 10px 15px;
                background-color: #34495e;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-weight: bold;
            }
            .btn-search:hover {
                background-color: #2c3e50;
            }

            /* Nút quay lại danh sách đầy đủ */
            .btn-back {
                display: inline-block;
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #95a5a6;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-weight: bold;
            }
            .btn-back:hover {
                background-color: #7f8c8d;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h1>🏨 Room List Management</h1>

            <a href="rooms?action=NEW" class="btn-add">➕ Add New Room</a>

           

            <!--    Thanh tìm kiếm theo Room Number-->
            <div class="toolbar">

                <form action="rooms" method="GET" class="search-form">
                    <input type="hidden" name="action" value="LIST">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Search room number..." class="search-input">
                    <button type="submit" class="btn-search">Search</button>
                </form>
            </div>

            <!--    Thanh tìm kiếm theo Room Number-->


            <c:if test="${not empty errorMessage}">
                <div class="error-msg">
                    Error: ${errorMessage}
                </div>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th hidden="">ID</th>
                        <th width="15%">Room No.</th>
                        <th width="20%">Room Type</th>
                        <th width="15%">Status</th>
                        <th width="15%" style="text-align: center;">Active</th>
                        <th width="35%">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="room" items="${roomsList}">
                        <tr>
                            <td hidden="">${room.roomId}</td>

                            <td><strong>${room.roomNumber}</strong></td>

                            <td>${room.roomType.typeName}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${room.status == 'Available'}">
                                        <span class="status-badge status-available">${room.status}</span>
                                    </c:when>
                                    <c:when test="${room.status == 'Occupied'}">
                                        <span class="status-badge status-occupied">${room.status}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge status-other">${room.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td style="text-align: center;">
                                <c:choose>
                                    <c:when test="${room.activeLogin}">
                                        <span style="color: #27ae60; font-weight: bold; font-size: 1.2em;">✅</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #e74c3c; font-weight: bold; font-size: 1.2em;">❌</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <div class="action-links">
                                    <a href="rooms?action=VIEW&id=${room.roomId}" class="btn-action btn-view">View</a>

                                    <a href="rooms?action=EDIT&id=${room.roomId}" class="btn-action btn-edit">Edit</a>

                                    <a href="rooms?action=DELETE&id=${room.roomId}"
                                       class="btn-action btn-delete"
                                       onclick="return confirm('Are you sure you want to delete Room ${room.roomNumber}?')">
                                        Delete
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${not empty keyword}">
                <div style="text-align: center;">
                    <a href="rooms?action=LIST" class="btn-back">&larr; Back to Full List</a>
                </div>
            </c:if>

            <c:if test="${empty keyword}">
                <div class="pagination">
                    <c:if test="${tag > 1}">
                        <a href="rooms?action=LIST&index=${tag-1}">&laquo; Previous</a>
                    </c:if>

                    <c:forEach begin="1" end="${endPage}" var="i">
                        <c:choose>
                            <c:when test="${i == 1 || i == endPage || (i >= tag - 2 && i <= tag + 2)}">
                                <a href="rooms?action=LIST&index=${i}" class="${tag == i ? 'active' : ''}">${i}</a>
                            </c:when>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${tag < endPage}">
                        <a href="rooms?action=LIST&index=${tag+1}">Next &raquo;</a>
                    </c:if>
                </div>
            </c:if>
        </div>

    </body>
</html>