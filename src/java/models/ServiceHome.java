package models;

public class ServiceHome {
    private int categoryId;
    private String categoryName;
    private String description;
    // Các trường mới thêm
    private String imageUrl;
    private String iconClass;
    private String linkUrl;
    private String btnText;

    public ServiceHome() {
    }

    public ServiceHome(int categoryId, String categoryName, String description, String imageUrl, String iconClass, String linkUrl, String btnText) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.iconClass = iconClass;
        this.linkUrl = linkUrl;
        this.btnText = btnText;
    }

    // --- Getters & Setters ---
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getIconClass() { return iconClass; }
    public void setIconClass(String iconClass) { this.iconClass = iconClass; }

    public String getLinkUrl() { return linkUrl; }
    public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }

    public String getBtnText() { return btnText; }
    public void setBtnText(String btnText) { this.btnText = btnText; }
}