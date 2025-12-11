package models;

import java.sql.Timestamp;
import java.util.Date;

public class ServiceHistory {
    private Date orderDate;
    private String serviceName;
    private int quantity;
    private double totalPrice; // Thành tiền của món đó (giá * số lượng)
    private String status;

    public ServiceHistory() {
    }

    public ServiceHistory(Date orderDate, String serviceName, int quantity, double totalPrice, String status) {
        this.orderDate = orderDate;
        this.serviceName = serviceName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}