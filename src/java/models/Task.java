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
    private String taskName;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private Staff staff;
    private Room room;

    public Task(int taskId, String taskName, String description, String status, LocalDateTime createdAt, LocalDateTime finishedAt, Staff staff, Room room) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.staff = staff;
        this.room = room;
    }  

    public Task(int taskId, String taskName, String description, String status, LocalDateTime createdAt, LocalDateTime finishedAt, Staff staff) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.finishedAt = finishedAt;
        this.staff = staff;
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

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public Date getFinishedAtAsDate() {
        if (finishedAt == null) {
            return null;
        }
        return Date.from(finishedAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    @Override
public String toString() {
    return "Task{" +
            "taskId=" + taskId +
            ", taskName='" + taskName + '\'' +
            ", description='" + description + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            ", finishedAt=" + finishedAt +
            ", staff=" + (staff != null ? staff.getFullName(): "N/A") +
            ", room=" + (room != null ? room.getRoomNumber(): "N/A") +
            '}';
}
}
