/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;

/**
 *
 * @author My Lap
 */
public class RoomType {
    // Thuộc tính ánh xạ từ bảng [RoomTypes]
    private int typeId;
    private String typeName;        
    private int capacity;
    private String description;
    private String imageUrl;
    private BigDecimal basePriceWeekday; 
    private BigDecimal basePriceWeekend; 
    private boolean isActive;       

    public RoomType() {
    }

    public RoomType(int typeId, String typeName, int capacity, String description, String imageUrl, BigDecimal basePriceWeekday, BigDecimal basePriceWeekend, boolean isActive) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.capacity = capacity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePriceWeekday = basePriceWeekday;
        this.basePriceWeekend = basePriceWeekend;
        this.isActive = isActive;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getBasePriceWeekday() {
        return basePriceWeekday;
    }

    public void setBasePriceWeekday(BigDecimal basePriceWeekday) {
        this.basePriceWeekday = basePriceWeekday;
    }

    public BigDecimal getBasePriceWeekend() {
        return basePriceWeekend;
    }

    public void setBasePriceWeekend(BigDecimal basePriceWeekend) {
        this.basePriceWeekend = basePriceWeekend;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
    
    
    
}