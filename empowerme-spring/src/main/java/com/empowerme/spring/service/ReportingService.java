package com.empowerme.spring.service;

import com.empowerme.spring.domain.CampaignEntity;
import com.empowerme.spring.repository.CampaignRepository;
import com.empowerme.spring.repository.CommentRepository;
import com.empowerme.spring.repository.ContentAssetRepository;
import com.empowerme.spring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

    private final ContentAssetRepository contentRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReportingService(ContentAssetRepository contentRepository,
                            CampaignRepository campaignRepository,
                            UserRepository userRepository,
                            CommentRepository commentRepository) {
        this.contentRepository = contentRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Map<String, Object> generateMonthlyReport(Long userId) {
        Map<String, Object> report = new HashMap<>();

        // 1. Content Stats
        long totalAssets = contentRepository.count();

        // 2. Campaign Stats
        List<CampaignEntity> campaigns = campaignRepository.findAll();
        long activeCampaigns = campaigns.size();
        
        // 3. Financial Stats (Mock calculation: Campaigns * $50)
        double totalSpend = campaigns.size() * 50.0;

        // 4. Interaction Stats
        long totalComments = commentRepository.count();
        long flaggedComments = commentRepository.findAll().stream().filter(c -> c.getIsFlagged()).count();

        report.put("userId", userId);
        report.put("totalAssets", totalAssets);
        report.put("activeCampaigns", activeCampaigns);
        report.put("totalSpend", totalSpend);
        report.put("totalEngagement", totalComments);
        report.put("flaggedInteractions", flaggedComments);
        report.put("engagementScore", calculateEngagementScore(totalComments, activeCampaigns));

        return report;
    }

    private int calculateEngagementScore(long comments, long campaigns) {
        if (campaigns == 0) return 0;
        return (int) ((comments * 10) / campaigns); // Simple mock formula
    }

    public String exportToCSV(Long userId) {
        Map<String, Object> data = generateMonthlyReport(userId);
        StringBuilder csv = new StringBuilder();
        csv.append("Metric,Value\n");
        csv.append("Total Assets,").append(data.get("totalAssets")).append("\n");
        csv.append("Active Campaigns,").append(data.get("activeCampaigns")).append("\n");
        csv.append("Total Spend,$").append(data.get("totalSpend")).append("\n");
        csv.append("Total Comments,").append(data.get("totalEngagement")).append("\n");
        csv.append("Flagged Comments,").append(data.get("flaggedInteractions")).append("\n");
        csv.append("Engagement Score,").append(data.get("engagementScore")).append("\n");
        return csv.toString();
    }
}
