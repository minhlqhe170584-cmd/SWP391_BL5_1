/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Acer
 */
public class Task {
    private int taskId;
    private int orderId;
    private int staffId;
    private String taskName;
    private String description;
    private String status;
    private Staff staff;
    private ServiceOrder serviceOrder;
    private LocalDateTime createdAt;
    
    public Task(){
        
    }

    public Task(int taskId, int orderId, int staffId, String taskName, String description, String status, Staff staff, ServiceOrder serviceOrder, LocalDateTime createdAt) {
        this.taskId = taskId;
        this.orderId = orderId;
        this.staffId = staffId;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.staff = staff;
        this.serviceOrder = serviceOrder;
        this.createdAt = createdAt;
    }

    

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
    
    

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public Date getCreatedAtAsDate() {
        if (createdAt == null) {
            return null;
        }
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}
