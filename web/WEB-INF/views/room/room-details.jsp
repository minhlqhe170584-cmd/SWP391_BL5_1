<%-- 
    Document   : room-details
    Created on : Dec 4, 2025, 8:00:45 PM
    Author     : My Lap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Room Details - ${room.roomNumber}</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background-color: #f4f6f8; padding: 40px; color: #333; }
        .container { max-width: 800px; margin: 0 auto; }
        
        /* Giao diện Card */
        .card { background: #fff; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); overflow: hidden; }
        .card-header { background: #34495e; color: #fff; padding: 20px 30px; display: flex; justify-content: space-between; align-items: center; }
        .card-header h2 { margin: 0; font-size: 24px; }
        .badge { background: #f39c12; padding: 5px 12px; border-radius: 20px; font-weight: bold; color: #fff; font-size: 14px; }
        
        .card-body { padding: 30px; display: flex; gap: 40px; }
        .col { flex: 1; } /* Chia đôi màn hình */
        .info-item { margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 8px; }
        .label { font-weight: bold; color: #7f8c8d; font-size: 13px; text-transform: uppercase; display: block; margin-bottom: 4px; }
        .value { font-size: 18px; font-weight: 500; color: #2c3e50; }
        .price { color: #e74c3c; font-weight: bold; }
        
        .card-footer { background: #f9f9f9; padding: 20px 30px; text-align: right; border-top: 1px solid #eee; }
        .btn { padding: 10px 20px; text-decoration: none; border-radius: 4px; font-weight: bold; margin-left: 10px; display: inline-block; }
        .btn-back { background: #95a5a6; color: #fff; }
        .btn-edit { background: #f39c12; color: #fff; }
        .btn:hover { opacity: 0.9; }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-header">
            <h2>Room Details</h2>
            <span class="badge">Room ${room.roomNumber}</span>
        </div>

        <div class="card-body">
            <div class="col">
                <div class="info-item">
                    <span class="label">Status</span>
                    <span class="value" style="color: ${room.status == 'Available' ? 'green' : 'orange'}">${room.status}</span>
                </div>
                <div class="info-item">
                    <span class="label">Type Name</span>
                    <span class="value">${room.roomType.typeName}</span>
                </div>
                <div class="info-item">
                    <span class="label">Capacity</span>
                    <span class="value">${room.roomType.capacity} Person(s)</span>
                </div>
                <div class="info-item">
                    <span class="label">Active Login</span>
                    <span class="value">${room.activeLogin ? '✅ Yes' : '❌ No'}</span>
                </div>
            </div>

            <div class="col">
                <div class="info-item">
                    <span class="label">Current Password</span>
                    <span class="value" style="font-family: monospace; background: #eee; padding: 2px 6px; border-radius: 4px;">${room.roomPassword}</span>
                </div>
                <div class="info-item">
                    <span class="label">Weekday Price</span>
                    <span class="value price">
                        <fmt:formatNumber value="${room.roomType.basePriceWeekday}" type="currency" currencySymbol="$" />
                    </span>
                </div>
                <div class="info-item">
                    <span class="label">Weekend Price</span>
                    <span class="value price">
                        <fmt:formatNumber value="${room.roomType.basePriceWeekend}" type="currency" currencySymbol="$" />
                    </span>
                </div>
                <div class="info-item">
                    <span class="label">Description</span>
                    <span class="value" style="font-size: 15px;">${room.roomType.description}</span>
                </div>
            </div>
        </div>

        <div class="card-footer">
            <a href="rooms?action=LIST" class="btn btn-back">Back to List</a>
            <a href="rooms?action=EDIT&id=${room.roomId}" class="btn btn-edit">Edit Room</a>
        </div>
    </div>
</div>

</body>
</html>