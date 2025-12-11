/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class Service {
    private int serviceId;
    private String serviceName;
    private String imageUrl;
    private boolean isActive;
    private int categoryId;
    private boolean isDeleted;

    public Service() {
    }

    public Service(int serviceId, String serviceName, String imageUrl, boolean isActive, int categoryId, boolean isDeleted) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }
    public Service(int serviceId, String serviceName) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
    }
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
