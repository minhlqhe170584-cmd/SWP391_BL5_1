package models;

import java.sql.Timestamp;

public class ServiceOrder {
    private int orderId;
    private int roomId;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;
    private String note;
    private String roomNumber;

    public ServiceOrder() {
    }

    public ServiceOrder(int orderId, int roomId, Timestamp orderDate, double totalAmount, String status, String note, String roomNumber) {
        this.orderId = orderId;
        this.roomId = roomId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.note = note;
        this.roomNumber = roomNumber;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}