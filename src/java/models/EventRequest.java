/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author My Lap
 */
public class EventRequest {
    private int requestId;
    private int eventId;
    private int customerId;
    private String roomIds;
    private String checkInDate;
    private String checkOutDate;
    private String message;
    private String status;

    public EventRequest() {
    }

    public EventRequest(int requestId, int eventId, String roomIds, String checkInDate, String checkOutDate, String message, String status) {
        this.requestId = requestId;
        this.eventId = eventId;
        this.roomIds = roomIds;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.message = message;
        this.status = status;
    }

    public EventRequest(int requestId, int eventId, int customerId, String roomIds, String checkInDate, String checkOutDate, String message, String status) {
        this.requestId = requestId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.roomIds = roomIds;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.message = message;
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(String roomIds) {
        this.roomIds = roomIds;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}

