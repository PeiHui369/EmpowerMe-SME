package com.empowerme.osgi.api;

public class ContentAsset {
    // Keep it simple POJO for OSGi demo
    private Long id;
    private String title;
    private String type;
    private Long fileSize;
    private Double cost;
    private boolean isLive;

    public ContentAsset() {}

    public ContentAsset(Long id, String title, String type, Long fileSize, Double cost, boolean isLive) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.fileSize = fileSize;
        this.cost = cost;
        this.isLive = isLive;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
    public boolean isLive() { return isLive; }
    public void setLive(boolean live) { isLive = live; }

    @Override
    public String toString() {
        return "Asset [ID=" + id + ", Title=" + title + ", Type=" + type + ", Cost=$" + cost + ", Live=" + isLive + "]";
    }
}
