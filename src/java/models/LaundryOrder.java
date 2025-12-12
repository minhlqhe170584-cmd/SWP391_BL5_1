package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LaundryOrder {
    private Long laundryId;
    private Long orderId;
    private LocalDateTime pickupTime;
    private LocalDateTime expectedDeliveryTime;
    private LocalDateTime expectedReturnTime;
    private String status;
    private String note;
    
    // One-to-Many relationship with LaundryOrderDetails
    private List<LaundryOrderDetail> orderDetails = new ArrayList<>();
    
    // Many-to-One relationship with ServiceOrders
    private ServiceOrder serviceOrder;
    
    // Constructors
    public LaundryOrder() {}
    
    public LaundryOrder(Long laundryId, Long orderId) {
        this.laundryId = laundryId;
        this.orderId = orderId;
        this.status = "PENDING";
    }
    
    // Getters and Setters
    public Long getLaundryId() { return laundryId; }
    public void setLaundryId(Long laundryId) { this.laundryId = laundryId; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    
    public LocalDateTime getExpectedDeliveryTime() { return expectedDeliveryTime; }
    public void setExpectedDeliveryTime(LocalDateTime expectedDeliveryTime) { 
        this.expectedDeliveryTime = expectedDeliveryTime; 
    }
    
    public LocalDateTime getExpectedReturnTime() { return expectedReturnTime; }
    public void setExpectedReturnTime(LocalDateTime expectedReturnTime) { 
        this.expectedReturnTime = expectedReturnTime; 
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public List<LaundryOrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<LaundryOrderDetail> orderDetails) { 
        this.orderDetails = orderDetails; 
    }
    
    public ServiceOrder getServiceOrder() { return serviceOrder; }
    public void setServiceOrder(ServiceOrder serviceOrder) { this.serviceOrder = serviceOrder; }
}
