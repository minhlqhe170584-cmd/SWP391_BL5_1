package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDTO {
    private int roomInvoiceId;
    private double roomTotalAmount;
    private String roomNote;
    private List<ServiceDetail> listServiceDetails = new ArrayList<>();
    private List<ServiceInvoiceDetail> listServiceInvoices = new ArrayList<>();
    private double grandTotal;
    
    // [MỚI] Biến kiểm tra trạng thái
    private boolean paid; 
    private Timestamp paymentDate;
    
    // Booking info
    private int bookingId;
    private String roomNumber;
    private String customerName;
    private String bookingCode;

    // Class con lưu thông tin ServiceInvoice
    public static class ServiceInvoiceDetail {
        private int invoiceId;
        private int orderId;
        private double finalAmount;
        private String status;
        private String paymentMethod;
        private Timestamp createdAt;
        private String serviceOrderStatus;
        private Timestamp orderDate;
        
        public ServiceInvoiceDetail() {}
        
        public ServiceInvoiceDetail(int invoiceId, int orderId, double finalAmount, String status, 
                                   String paymentMethod, Timestamp createdAt, String serviceOrderStatus, Timestamp orderDate) {
            this.invoiceId = invoiceId;
            this.orderId = orderId;
            this.finalAmount = finalAmount;
            this.status = status;
            this.paymentMethod = paymentMethod;
            this.createdAt = createdAt;
            this.serviceOrderStatus = serviceOrderStatus;
            this.orderDate = orderDate;
        }
        
        // Getters & Setters
        public int getInvoiceId() { return invoiceId; }
        public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }
        
        public int getOrderId() { return orderId; }
        public void setOrderId(int orderId) { this.orderId = orderId; }
        
        public double getFinalAmount() { return finalAmount; }
        public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        
        public Timestamp getCreatedAt() { return createdAt; }
        public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
        
        public String getServiceOrderStatus() { return serviceOrderStatus; }
        public void setServiceOrderStatus(String serviceOrderStatus) { this.serviceOrderStatus = serviceOrderStatus; }
        
        public Timestamp getOrderDate() { return orderDate; }
        public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    }
    
    // Class con lưu chi tiết món
    public static class ServiceDetail {
        private String serviceName; 
        private String itemName;    
        private int quantity;       
        private double unitPrice;   
        private double total;       
        private Timestamp orderTime;

        public ServiceDetail() {}
        public ServiceDetail(String serviceName, String itemName, int quantity, double unitPrice, double total, Timestamp orderTime) {
            this.serviceName = serviceName;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.total = total;
            this.orderTime = orderTime;
        }

        // Getters & Setters cho ServiceDetail
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
        public Timestamp getOrderTime() { return orderTime; }
        public void setOrderTime(Timestamp orderTime) { this.orderTime = orderTime; }
    }

    // Getters & Setters cho PaymentDTO
    public PaymentDTO() {}
    
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    
    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    public int getRoomInvoiceId() { return roomInvoiceId; }
    public void setRoomInvoiceId(int roomInvoiceId) { this.roomInvoiceId = roomInvoiceId; }
    
    public double getRoomTotalAmount() { return roomTotalAmount; }
    public void setRoomTotalAmount(double roomTotalAmount) { this.roomTotalAmount = roomTotalAmount; }
    
    public String getRoomNote() { return roomNote; }
    public void setRoomNote(String roomNote) { this.roomNote = roomNote; }
    
    public List<ServiceDetail> getListServiceDetails() { return listServiceDetails; }
    public void setListServiceDetails(List<ServiceDetail> listServiceDetails) { this.listServiceDetails = listServiceDetails; }
    
    public double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }
    
    public List<ServiceInvoiceDetail> getListServiceInvoices() { return listServiceInvoices; }
    public void setListServiceInvoices(List<ServiceInvoiceDetail> listServiceInvoices) { this.listServiceInvoices = listServiceInvoices; }
    
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }
}