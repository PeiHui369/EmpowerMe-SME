package com.empowerme.osgi.campaign.impl;

import com.empowerme.osgi.campaign.api.CampaignService;
import com.empowerme.osgi.api.ContentService; // Module 1
import com.empowerme.osgi.user.api.UserService;    // Module 3
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = CampaignService.class)
public class CampaignServiceImpl implements CampaignService {

    private ContentService contentService;
    private UserService userService;

    @Reference
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    @Reference
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void createCampaign(String name, String contentTitle, String userId) {
        System.out.println("OSGi Campaign: Starting creation for '" + name + "'");
        
        // 1. Verify Content (Mock check for demo as we don't have ID lookups in simple OSGi demo)
        System.out.println("OSGi Campaign: Verifying content availability via ContentService...");
        // In a real app we'd call contentService.get(id), here we just confirm service presence
        if (contentService != null) {
            System.out.println("OSGi Campaign: Content Service is ACTIVE. Content '" + contentTitle + "' linked.");
        }

        // 2. Verify User & Deduct
        System.out.println("OSGi Campaign: charging user via UserService...");
        if (userService.checkBalance(userId, 50.0)) {
            userService.deductBalance(userId, 50.0);
            System.out.println("OSGi Campaign: Campaign '" + name + "' Created Successfully!");
        } else {
            System.out.println("OSGi Campaign: Creation FAILED. Insufficient funds.");
        }
    }
}
