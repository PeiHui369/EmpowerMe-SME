package com.empowerme.spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long campaignId; // Links to Campaign
    private String text;
    private String author; // Simple string for now (e.g., "User123")
    private boolean isFlagged;

    public CommentEntity() {}

    public CommentEntity(Long campaignId, String text, String author, boolean isFlagged) {
        this.campaignId = campaignId;
        this.text = text;
        this.author = author;
        this.isFlagged = isFlagged;
    }
    
    // Manual Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public boolean getIsFlagged() { return isFlagged; }
    public void setIsFlagged(boolean isFlagged) { this.isFlagged = isFlagged; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long campaignId;
        private String text;
        private String author;
        private boolean isFlagged;

        public Builder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public Builder text(String text) { this.text = text; return this; }
        public Builder author(String author) { this.author = author; return this; }
        public Builder isFlagged(boolean isFlagged) { this.isFlagged = isFlagged; return this; }

        public CommentEntity build() {
            return new CommentEntity(campaignId, text, author, isFlagged);
        }
    }
}
