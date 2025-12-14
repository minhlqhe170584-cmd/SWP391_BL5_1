/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Date;

/**
 *
 * @author My Lap
 */
public class EventRequestView {
    private int requestId;
    private String eventName;
    private String customerName;
    private String roomNames;
    private String status;
    private Date checkInDate;
    private Date checkOutDate;
    private String message;
    private Date createdDate;

    public EventRequestView() {
    }

    public EventRequestView(int requestId, String eventName, String customerName, String roomNames, String status, Date checkInDate, Date checkOutDate, String message, Date createdDate) {
        this.requestId = requestId;
        this.eventName = eventName;
        this.customerName = customerName;
        this.roomNames = roomNames;
        this.status = status;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.message = message;
        this.createdDate = createdDate;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNames() {
        return roomNames;
    }

    public void setRoomNames(String roomNames) {
        this.roomNames = roomNames;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "EventRequestView{" + "requestId=" + requestId + ", eventName=" + eventName + ", customerName=" + customerName + ", roomNames=" + roomNames + ", status=" + status + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate + ", message=" + message + ", createdDate=" + createdDate + '}';
    }
    
    
}

