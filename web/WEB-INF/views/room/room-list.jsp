<%-- 
    Document   : room-list
    Created on : Dec 2, 2025, 10:32:56 PM
    Author     : My Lap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Room Management</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { width: 90%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>

    <h1>🏨 Room List Management</h1>
    
    <p><a href="rooms?action=NEW">➕ Add New Room</a></p>

    <c:if test="${not empty errorMessage}">
        <p style="color: red; font-weight: bold;">Error: ${errorMessage}</p>
    </c:if>

    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Room Number</th>
                <th>Room Type</th>
                <th>Capacity</th>
                <th>Weekday Price</th>
                <th>Status</th>
                <th>Login Active</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="room" items="${roomsList}">
                <tr>
                    <td><c:out value="${room.roomId}"/></td>
                    <td><c:out value="${room.roomNumber}"/></td>
                    
                    <td><c:out value="${room.roomTypeDetail.typeName}"/></td>
                    <td><c:out value="${room.roomTypeDetail.capacity}"/></td>
                    <td><c:out value="${room.roomTypeDetail.basePriceWeekday}"/></td>
                    
                    <td><c:out value="${room.status.displayValue}"/></td> 
                    
                    <td><c:out value="${room.activeLogin ? 'Yes' : 'No'}"/></td>
                    
                    <td>
                        <a href="rooms?action=EDIT&id=${room.roomId}">Edit</a>
                        |
                        <a href="rooms?action=DELETE&id=${room.roomId}" onclick="return confirm('Are you sure you want to delete Room ${room.roomNumber}?');">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
