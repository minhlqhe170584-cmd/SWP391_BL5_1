package models;
import java.util.ArrayList;
import java.util.List;

public class LaundryItem {
    private int laundryItemId;
    private int serviceId;
    private String itemName;
    private String description;
    private Double defaultPrice;
    private String unit;
    private Boolean isActive;
    
    private Service service;
    
    // One-to-Many relationship with LaundryOrderDetails
    private List<LaundryOrderDetail> orderDetails = new ArrayList<>();
    
    // Constructors
    public LaundryItem() {}
    
    public LaundryItem(int laundryItemId, String itemName, Double defaultPrice) {
        this.laundryItemId = laundryItemId;
        this.itemName = itemName;
        this.defaultPrice = defaultPrice;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getLaundryItemId() { return laundryItemId; }
    public void setLaundryItemId(int laundryItemId) { this.laundryItemId = laundryItemId; }
    
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getDefaultPrice() { return defaultPrice; }
    public void setDefaultPrice(Double defaultPrice) { this.defaultPrice = defaultPrice; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
     public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }
    
    public List<LaundryOrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<LaundryOrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}