package models;

public class Drink {

    private int drinkId;
    private int serviceId;
    private String name;
    private double price;
    private String description;
    private int volumeMl;
    private boolean isAlcoholic;
    private String imageUrl;
    private boolean isActive;

    public Drink() {
    }

    public Drink(int drinkId, int serviceId, String name, double price, String description, int volumeMl, boolean isAlcoholic, String imageUrl, boolean isActive) {
        this.drinkId = drinkId;
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.volumeMl = volumeMl;
        this.isAlcoholic = isAlcoholic;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    // --- GETTER & SETTER ---
    public int getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(int drinkId) {
        this.drinkId = drinkId;
    }

    // Các getter/setter cơ bản giống Food...
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Getter/Setter cho 2 trường đặc biệt của Drink
    public int getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(int volumeMl) {
        this.volumeMl = volumeMl;
    }

    public boolean getIsAlcoholic() {
        return isAlcoholic;
    }

    public void setIsAlcoholic(boolean isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
