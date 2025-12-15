package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Event {

    // --- 1. Các trường khớp 100% với Database (Bảng Events) ---
    private int eventId;
    private String eventName;         // DB: name
    private BigDecimal pricePerTable; // DB: price
    private int eventCatId;           // DB: serviceCategoryId
    private String location;          // DB: roomIds (Lưu chuỗi ID phòng, vd: "1,2")
    private Timestamp createdDate;    // DB: created_date
    private Timestamp updatedDate;    // DB: updated_date (Bổ sung thêm cái này)

    // --- 2. Các trường phụ (Relation - Dùng để hiển thị khi Join bảng) ---
    private EventCategory eventCategory; // Để lấy categoryName

    private String status;

    // Constructor rỗng
    public Event() {
    }

    public Event(int eventId, String eventName, BigDecimal pricePerTable, int eventCatId, String location, Timestamp createdDate, Timestamp updatedDate, EventCategory eventCategory, String status) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.pricePerTable = pricePerTable;
        this.eventCatId = eventCatId;
        this.location = location;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.eventCategory = eventCategory;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Getters & Setters ---
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public BigDecimal getPricePerTable() {
        return pricePerTable;
    }

    public void setPricePerTable(BigDecimal pricePerTable) {
        this.pricePerTable = pricePerTable;
    }

    public int getEventCatId() {
        return eventCatId;
    }

    public void setEventCatId(int eventCatId) {
        this.eventCatId = eventCatId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public String toString() {
        return "Event{" + "eventId=" + eventId + ", eventName=" + eventName + ", price=" + pricePerTable + '}';
    }
}
