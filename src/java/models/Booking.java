package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Booking {
    
    // === 1. CÁC CỘT KHỚP VỚI BẢNG DATABASE (BOOKINGS) ===
    private int bookingId;
    private int customerId;
    private int roomId;
    private Date checkInDate;       // Ngày dự kiến đến (lấy từ form)
    private Date checkOutDate;      // Ngày dự kiến đi (lấy từ form)
    private String status;          // 'Pending', 'CheckedIn', 'CheckedOut', 'Cancelled'
    private String bookingCode;     // Mã code (VD: BK-A123)
    private double totalAmount;     // Tổng tiền
    
    // Thời gian thực tế (Dùng cho Lễ tân check-in/out)
    private Timestamp realCheckIn;  
    private Timestamp realCheckOut; 

    // === 2. CÁC BIẾN PHỤ (KHÔNG CÓ TRONG BẢNG BOOKING) ===
    // Dùng để hứng dữ liệu khi JOIN với bảng Customers và Rooms
    private String customerName;
    private String roomNumber;

    // Constructor mặc định
    public Booking() {
    }

    // === 3. GETTERS & SETTERS ===

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getRealCheckIn() {
        return realCheckIn;
    }

    public void setRealCheckIn(Timestamp realCheckIn) {
        this.realCheckIn = realCheckIn;
    }

    public Timestamp getRealCheckOut() {
        return realCheckOut;
    }

    public void setRealCheckOut(Timestamp realCheckOut) {
        this.realCheckOut = realCheckOut;
    }

    // --- Getter & Setter cho biến phụ (Quan trọng để hiển thị) ---
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}