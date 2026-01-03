package com.empowerme.spring.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    
    private Long contentId; // "Foreign Key" to Content Module
    private Long userId;    // "Foreign Key" to User Module

    public CampaignEntity() {
    }

    public CampaignEntity(Long id, String name, LocalDate startDate, LocalDate endDate, Long contentId, Long userId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contentId = contentId;
        this.userId = userId;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Long getContentId() { return contentId; }
    public Long getUserId() { return userId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public void setUserId(Long userId) { this.userId = userId; }

    // Builder
    public static CampaignEntityBuilder builder() {
        return new CampaignEntityBuilder();
    }

    public static class CampaignEntityBuilder {
        private Long id;
        private String name;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long contentId;
        private Long userId;

        CampaignEntityBuilder() { }

        public CampaignEntityBuilder id(Long id) { this.id = id; return this; }
        public CampaignEntityBuilder name(String name) { this.name = name; return this; }
        public CampaignEntityBuilder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public CampaignEntityBuilder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public CampaignEntityBuilder contentId(Long contentId) { this.contentId = contentId; return this; }
        public CampaignEntityBuilder userId(Long userId) { this.userId = userId; return this; }

        public CampaignEntity build() {
            return new CampaignEntity(id, name, startDate, endDate, contentId, userId);
        }
    }
}
