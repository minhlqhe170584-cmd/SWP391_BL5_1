package models;

public class BikeRentalOption {
    private int itemId;
    private int serviceId;
    private String optionName;
    private int durationMinutes;
    private double price;

    public BikeRentalOption() {
    }

    public BikeRentalOption(int itemId, int serviceId, String optionName, int durationMinutes, double price) {
        this.itemId = itemId;
        this.serviceId = serviceId;
        this.optionName = optionName;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}