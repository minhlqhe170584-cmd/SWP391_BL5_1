package models;


public class LaundryOrderDetail {
    private int laundryId;
    private int laundryItemId;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    
    // Many-to-One relationship with LaundryOrders
    private LaundryOrder laundryOrder;
    
    // Many-to-One relationship with LaundryItems
    private LaundryItem  laundryItem;
    
    // Constructors
    public LaundryOrderDetail() {}
    
    public LaundryOrderDetail(int laundryId, int laundryItemId, Integer quantity, Double unitPrice) {
        this.laundryId = laundryId;
        this.laundryItemId = laundryItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }
    
    // Getters and Setters
    public int getLaundryId() { return laundryId; }
    public void setLaundryId(int laundryId) { this.laundryId = laundryId; }
    
    public int getLaundryItemId() { return laundryItemId; }
    public void setLaundryItemId(int laundryItemId) { this.laundryItemId = laundryItemId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { 
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }
    
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    
    public LaundryOrder getLaundryOrder() { return laundryOrder; }
    public void setLaundryOrder(LaundryOrder laundryOrder) { this.laundryOrder = laundryOrder; }
    
    public LaundryItem getLaundryItem() { return laundryItem; }
    public void setLaundryItem(LaundryItem laundryItem) { this.laundryItem = laundryItem; }
    
    private void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            this.subtotal = quantity * unitPrice;
        }
    }
}