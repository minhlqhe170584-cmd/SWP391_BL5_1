/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Event {

    private int eventId;
    private int serviceId;      // Link sang bảng Services
    private int eventCatId;     // Link sang EventCategory
    private String eventName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;    // Ví dụ: HALL-A
    private int numberOfGuests;
    private BigDecimal pricePerTable;
    private BigDecimal totalCost;
    private String setupRequirements;
    private String status;      // Planned, Ongoing, Completed...

    // Relation (Để hiển thị tên loại sự kiện dễ dàng)
    private EventCategory eventCategory;

    // Relation (Để hiển thị ảnh từ bảng Services nếu cần)
    private String imageUrl;

    public Event() {
    }

    // --- Getters & Setters ---
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getEventCatId() {
        return eventCatId;
    }

    public void setEventCatId(int eventCatId) {
        this.eventCatId = eventCatId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getPricePerTable() {
        return pricePerTable;
    }

    public void setPricePerTable(BigDecimal pricePerTable) {
        this.pricePerTable = pricePerTable;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getSetupRequirements() {
        return setupRequirements;
    }

    public void setSetupRequirements(String setupRequirements) {
        this.setupRequirements = setupRequirements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
