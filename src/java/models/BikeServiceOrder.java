package models;

import java.sql.Timestamp;

public class BikeServiceOrder extends ServiceOrder {
    private Timestamp bookingStartDate;
    private Timestamp bookingEndDate;
    private int quantity;
    private String itemName;
    private int serviceId;

    public BikeServiceOrder() {
    }

    public BikeServiceOrder(int orderId, int roomId, Timestamp orderDate, double totalAmount, String status, String note, String roomNumber, 
                            Timestamp bookingStartDate, Timestamp bookingEndDate, int quantity, String itemName, int serviceId) {
        super(orderId, roomId, orderDate, totalAmount, status, note, roomNumber);
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.quantity = quantity;
        this.itemName = itemName;
        this.serviceId = serviceId;
    }

    public Timestamp getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(Timestamp bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public Timestamp getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(Timestamp bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    
}