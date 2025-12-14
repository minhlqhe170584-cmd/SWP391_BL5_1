package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LaundryOrder {

    private int laundryId;
    private int orderId;
    private LocalDateTime pickupTime;
    private LocalDateTime expectedPickupTime;
    private LocalDateTime expectedReturnTime;
    private String status;
    private String note;
    private String roomNumber;

    // One-to-Many relationship with LaundryOrderDetails
    private List<LaundryOrderDetail> orderDetails = new ArrayList<>();

    // Many-to-One relationship with ServiceOrders
    private ServiceOrder serviceOrder;

    // Constructors
    public LaundryOrder() {
    }

    public LaundryOrder(int laundryId, int orderId) {
        this.laundryId = laundryId;
        this.orderId = orderId;      
    }
    
      public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Getters and Setters
    public int getLaundryId() {
        return laundryId;
    }

    public void setLaundryId(int laundryId) {
        this.laundryId = laundryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getFormattedPickupTime() {
        if (this.pickupTime == null) {
            return "";
        }
        return this.pickupTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public LocalDateTime getExpectedPickupTime() {
        return expectedPickupTime;
    }

    public void setExpectedPickupTime(LocalDateTime expectedPickupTime) {
        this.expectedPickupTime = expectedPickupTime;
    }

    public String getFormattedExpectedPickupTime() {
        if (this.expectedPickupTime == null) {
            return "";
        }
        return this.expectedPickupTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public LocalDateTime getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(LocalDateTime expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime;
    }

    public String getFormattedExpectedReturnTime() {
        if (this.expectedReturnTime == null) {
            return "";
        }
        return this.expectedReturnTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
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

    public List<LaundryOrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<LaundryOrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }
}
