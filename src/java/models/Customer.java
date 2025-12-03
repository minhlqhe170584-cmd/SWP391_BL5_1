/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDateTime;

/**
 *
 * @author Acer
 */
public class Customer {
    private int customerId;
    private String fullName;
    private String email;
    private String password;
    private String Phone;
    private boolean isActive;
    private LocalDateTime createAt;

    public Customer() {
    }

    public Customer(int customerId, String fullName, String email, String password, String Phone, boolean isActive, LocalDateTime createAt) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.Phone = Phone;
        this.isActive = isActive;
        this.createAt = createAt;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    
    
}
