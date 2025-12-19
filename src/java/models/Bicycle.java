package models;

public class Bicycle {

    private int bikeId;
    private String bikeCode;
    private int serviceId;
    private String status;
    private String condition;
    private String serviceName;

    public Bicycle() {
    }

    public Bicycle(int bikeId, String bikeCode, int serviceId, String status, String condition, String serviceName) {
        this.bikeId = bikeId;
        this.bikeCode = bikeCode;
        this.serviceId = serviceId;
        this.condition = condition;
        this.serviceName = serviceName;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeCode() {
        return bikeCode;
    }

    public void setBikeCode(String bikeCode) {
        this.bikeCode = bikeCode;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
