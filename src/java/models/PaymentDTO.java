package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDTO {
    // 1. Tiền Phòng
    private int roomInvoiceId;
    private double roomTotalAmount;
    private String roomNote;
    
    // 2. Danh sách chi tiết món
    private List<ServiceDetail> listServiceDetails = new ArrayList<>();
    
    // 3. Tổng cộng
    private double grandTotal;

    // =================================================================
    // CLASS CON: ServiceDetail (Đã bổ sung Getter/Setter)
    // =================================================================
    public static class ServiceDetail {
        private String serviceName; 
        private String itemName;    
        private int quantity;       
        private double unitPrice;   
        private double total;       
        private Timestamp orderTime;

        public ServiceDetail() {
        }

        public ServiceDetail(String serviceName, String itemName, int quantity, double unitPrice, double total, Timestamp orderTime) {
            this.serviceName = serviceName;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.total = total;
            this.orderTime = orderTime;
        }

        // --- BẮT BUỘC PHẢI CÓ CÁC HÀM GET NÀY THÌ JSP MỚI HIỂU ---
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

    // =================================================================
    // GETTERS & SETTERS CỦA PaymentDTO
    // =================================================================
    public PaymentDTO() {}

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