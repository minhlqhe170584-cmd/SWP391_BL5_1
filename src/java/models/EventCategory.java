/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author My Lap
 */
public class EventCategory {
    
    private int eventCatId;
    private String categoryName;
    private String description;

    public EventCategory() {
    }

    public EventCategory(int eventCatId, String categoryName, String description) {
        this.eventCatId = eventCatId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public int getEventCatId() {
        return eventCatId;
    }

    public void setEventCatId(int eventCatId) {
        this.eventCatId = eventCatId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
