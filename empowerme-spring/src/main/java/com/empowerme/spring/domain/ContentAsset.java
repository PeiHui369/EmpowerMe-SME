package com.empowerme.spring.domain;

import jakarta.persistence.*;

@Entity
public class ContentAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    // Using String to store extension like ".jpg", ".mp4" for validation
    // or MIME type if preferred, but requirement asks to check extension
    private String fileExtension; 
    
    private String type; // VIDEO, IMAGE

    // File size in bytes
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    private ContentStatus status;

    private Double productionCost;

    public ContentAsset() {
    }

    public ContentAsset(Long id, String title, String fileExtension, String type, Long fileSize, ContentStatus status, Double productionCost) {
        this.id = id;
        this.title = title;
        this.fileExtension = fileExtension;
        this.type = type;
        this.fileSize = fileSize;
        this.status = status;
        this.productionCost = productionCost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFileExtension() { return fileExtension; }
    public void setFileExtension(String fileExtension) { this.fileExtension = fileExtension; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public ContentStatus getStatus() { return status; }
    public void setStatus(ContentStatus status) { this.status = status; }

    public Double getProductionCost() { return productionCost; }
    public void setProductionCost(Double productionCost) { this.productionCost = productionCost; }

    public static ContentAssetBuilder builder() {
        return new ContentAssetBuilder();
    }

    public static class ContentAssetBuilder {
        private Long id;
        private String title;
        private String fileExtension;
        private String type;
        private Long fileSize;
        private ContentStatus status;
        private Double productionCost;

        ContentAssetBuilder() { }

        public ContentAssetBuilder id(Long id) { this.id = id; return this; }
        public ContentAssetBuilder title(String title) { this.title = title; return this; }
        public ContentAssetBuilder fileExtension(String fileExtension) { this.fileExtension = fileExtension; return this; }
        public ContentAssetBuilder type(String type) { this.type = type; return this; }
        public ContentAssetBuilder fileSize(Long fileSize) { this.fileSize = fileSize; return this; }
        public ContentAssetBuilder status(ContentStatus status) { this.status = status; return this; }
        public ContentAssetBuilder productionCost(Double productionCost) { this.productionCost = productionCost; return this; }

        public ContentAsset build() {
            return new ContentAsset(id, title, fileExtension, type, fileSize, status, productionCost);
        }
    }
}
