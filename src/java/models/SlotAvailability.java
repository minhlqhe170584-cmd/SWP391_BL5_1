package models;

public class SlotAvailability {
    private String timeRange;
    private int availableQty;
    private String status; // "Full", "Available", "Limited"

    public SlotAvailability() {
    }

    public SlotAvailability(String timeRange, int availableQty, String status) {
        this.timeRange = timeRange;
        this.availableQty = availableQty;
        this.status = status;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}