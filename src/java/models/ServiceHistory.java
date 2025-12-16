package models;

import java.sql.Timestamp; // Dùng Timestamp chuẩn hơn Date cho SQL

public class ServiceHistory {
    
    // --- 1. Dữ liệu trực tiếp từ bảng [OrderDetails] ---
    private int detailId;       // [detail_id]
    private int orderId;        // [order_id]
    private int serviceId;      // [service_id]
    private String itemName;    // [item_name] - Tên món cụ thể (Phở, Pepsi...)
    private int quantity;       // [quantity]
    private double unitPrice;   // [unit_price]
    private double subtotal;    // [subtotal] - Thành tiền (qty * unit_price)

    // --- 2. Dữ liệu nối từ bảng [ServiceOrders] (Cần thiết để hiển thị Lịch sử) ---
    private Timestamp orderDate; // Ngày gọi món
    private String status;       // Trạng thái đơn (Paid/Pending...)

    // Constructor không tham số
    public ServiceHistory() {
    }

    // Constructor đầy đủ
    public ServiceHistory(int detailId, int orderId, int serviceId, String itemName, 
                          int quantity, double unitPrice, double subtotal, 
                          Timestamp orderDate, String status) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.serviceId = serviceId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
        this.orderDate = orderDate;
        this.status = status;
    }

    // --- Getters and Setters ---

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}