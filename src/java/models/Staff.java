/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
/**
 *
 * @author Admin
 */
public class Staff {

    private int staffId;
    private Role role;
    private String fullName;
    private String email;
    private String passWordHash;
    private boolean isActive;
    private LocalDateTime createdAt;
    private List<Task> tasks;

    public Staff() {
    };

    public Staff(int staffId, Role role, String fullName, String email, String passWordHash, boolean isActive, LocalDateTime createdAt) {
        this.staffId = staffId;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.passWordHash = passWordHash;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Staff(int staffId, Role role, String fullName, String email, String passWordHash, boolean isActive, LocalDateTime createdAt, List<Task> tasks) {
        this.staffId = staffId;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.passWordHash = passWordHash;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWordHash() {
        return passWordHash;
    }

    public void setPassWordHash(String passWordHash) {
        this.passWordHash = passWordHash;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Date getCreatedAtAsDate() {
        if (createdAt == null) {
            return null;
        }
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    @Override
    public String toString() {
        return "Staff{"
                + "staffId=" + staffId
                + ", role=" + (role != null ? role.getRoleName() : "N/A")
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", isActive=" + isActive
                + ", createAt=" + createdAt
                + '}';
    }
}
