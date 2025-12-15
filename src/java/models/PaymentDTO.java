package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDTO {
    private int roomInvoiceId;
    private double roomTotalAmount;
    private String roomNote;
    private List<ServiceDetail> listServiceDetails = new ArrayList<>();
    private double grandTotal;
    
    // [MỚI] Biến kiểm tra trạng thái
    private boolean paid; 
    private Timestamp paymentDate;

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
}