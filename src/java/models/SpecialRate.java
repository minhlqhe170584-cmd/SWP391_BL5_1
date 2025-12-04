/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author My Lap
 */
public class SpecialRate {
    private int rateId;
    private int roomTypeId;         // room_type_id (Khóa ngoại)
    private String rateName;
    private Date startDate;         // start_date
    private Date endDate;           // end_date
    private BigDecimal specialPrice;// special_price (decimal)
    private Date createdAt;         // created_at (datetime)

    public SpecialRate() {
    }

    public SpecialRate(int rateId, int roomTypeId, String rateName, Date startDate, Date endDate, BigDecimal specialPrice, Date createdAt) {
        this.rateId = rateId;
        this.roomTypeId = roomTypeId;
        this.rateName = rateName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.specialPrice = specialPrice;
        this.createdAt = createdAt;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
    
    
}
