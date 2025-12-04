<%-- 
    Document   : room-edit
    Created on : Dec 4, 2025, 11:26:30 PM
    Author     : My Lap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${room != null ? "Edit Room" : "Add New Room"}</title>
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background-color: #f4f6f8;
                padding: 40px;
            }
            .form-container {
                max-width: 600px;
                margin: 0 auto;
                background: #fff;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            }
            h2 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 25px;
                border-bottom: 2px solid #eee;
                padding-bottom: 10px;
            }

            .form-group {
                margin-bottom: 15px;
            }
            label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
                color: #555;
            }
            input[type="text"], select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box; /* Fix độ rộng */
            }

            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-weight: bold;
                color: white;
                text-decoration: none;
                display: inline-block;
            }
            .btn-save {
                background-color: #27ae60;
            }
            .btn-save:hover {
                background-color: #219150;
            }
            .btn-cancel {
                background-color: #95a5a6;
                margin-left: 10px;
            }

            /* Checkbox style */
            .checkbox-group {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-top: 10px;
            }
            .checkbox-group input {
                width: 20px;
                height: 20px;
            }
        </style>
    </head>
    <body>

        <div class="form-container">
            <h2>${(room != null && room.roomId > 0) ? "✏ Edit Room" : "➕ Add New Room"}</h2>    
            <c:if test="${not empty error}">
                <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border: 1px solid #f5c6cb; border-radius: 4px; margin-bottom: 15px;">
                    ⚠ ${error}
                </div>
            </c:if>

            <form action="rooms" method="POST">
                <input type="hidden" name="action" value="${(room != null && room.roomId > 0) ? 'UPDATE' : 'CREATE'}">        
                <c:if test="${room != null && room.roomId > 0}">
                    <input type="hidden" name="roomId" value="${room.roomId}">
                </c:if>

                <div class="form-group">
                    <label>Room Number:</label>
                    <input type="text" name="roomNumber" value="${room.roomNumber}" required placeholder="Ex: 101, 205...">
                </div>

                <div class="form-group">
                    <label>Room Type:</label>
                    <select name="typeId">
                        <c:forEach var="t" items="${listType}">
                            <option value="${t.typeId}" ${room.typeId == t.typeId ? 'selected' : ''}>
                                ${t.typeName} (Capacity: ${t.capacity})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>Status:</label>
                    <select name="status">
                        <option value="Available" ${room.status == 'Available' ? 'selected' : ''}>Available</option>
                        <option value="Occupied" ${room.status == 'Occupied' ? 'selected' : ''}>Occupied</option>
                        <option value="Dirty" ${room.status == 'Dirty' ? 'selected' : ''}>Dirty</option>
                        <option value="Maintenance" ${room.status == 'Maintenance' ? 'selected' : ''}>Maintenance</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Room Password (For User Login):</label>
                    <input type="text" name="roomPassword" value="${room.roomPassword}" placeholder="Set a temporary password">
                </div>

                <div class="form-group checkbox-group">
                    <input type="checkbox" name="activeLogin" id="active" ${room.activeLogin ? 'checked' : ''}>
                    <label for="active" style="margin: 0; cursor: pointer;">Is Active Login? (Allow guests to login)</label>
                </div>

                <div style="margin-top: 25px; text-align: center;">
                    <button type="submit" class="btn btn-save">Save Changes</button>
                    <a href="rooms?action=LIST" class="btn btn-cancel">Cancel</a>
                </div>
            </form>
        </div>

    </body>
</html>
