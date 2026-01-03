package com.empowerme.spring.service;

import com.empowerme.spring.domain.CampaignEntity;
import com.empowerme.spring.domain.ContentAsset;
import com.empowerme.spring.domain.ContentStatus;
import com.empowerme.spring.repository.CampaignRepository;
import com.empowerme.spring.repository.ContentAssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserService userService;
    private final ContentAssetRepository contentAssetRepository;

    private static final double CAMPAIGN_COST = 50.0;

    public CampaignService(CampaignRepository campaignRepository, 
                           UserService userService, 
                           ContentAssetRepository contentAssetRepository) {
        this.campaignRepository = campaignRepository;
        this.userService = userService;
        this.contentAssetRepository = contentAssetRepository;
    }

    @Transactional
    public CampaignEntity createCampaign(CampaignEntity campaign) {
        // 1. Verify Content exists and is LIVE
        ContentAsset asset = contentAssetRepository.findById(campaign.getContentId())
                .orElseThrow(() -> new RuntimeException("Content Asset not found"));
        
        if (asset.getStatus() != ContentStatus.LIVE) {
            throw new RuntimeException("Content Asset must be LIVE to be used in a campaign.");
        }

        // 2. Verify User Balance & Deduct
        if (!userService.checkBalance(campaign.getUserId(), CAMPAIGN_COST)) {
            throw new RuntimeException("Insufficient funds. Campaign creation costs $" + CAMPAIGN_COST);
        }

        userService.deductBalance(campaign.getUserId(), CAMPAIGN_COST);

        // 3. Save Campaign
        return campaignRepository.save(campaign);
    }
    
    public List<CampaignEntity> getAllCampaigns() {
        return campaignRepository.findAll();
    }
}
