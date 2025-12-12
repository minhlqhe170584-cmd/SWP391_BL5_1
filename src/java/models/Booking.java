package models;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int customerId;
    private int roomId;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private String status; // Confirmed, Cancelled, Pending
    
    // Các trường phụ để hiển thị (Join bảng)
    private Room room;
    private Customer customer;

    public Booking() {}

    public Booking(int bookingId, int customerId, int roomId, Timestamp checkInDate, Timestamp checkOutDate, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    // --- GETTER & SETTER ---
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public Timestamp getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Timestamp checkInDate) { this.checkInDate = checkInDate; }

    public Timestamp getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Timestamp checkOutDate) { this.checkOutDate = checkOutDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}