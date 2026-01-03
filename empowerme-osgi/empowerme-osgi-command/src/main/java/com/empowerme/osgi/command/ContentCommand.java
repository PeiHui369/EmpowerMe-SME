package com.empowerme.osgi.command;

import com.empowerme.osgi.api.ContentAsset;
import com.empowerme.osgi.api.ContentService;
import com.empowerme.osgi.user.api.UserService;
import com.empowerme.osgi.campaign.api.CampaignService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(property = {
        "osgi.command.scope=content",
        "osgi.command.function=list",
        "osgi.command.function=add",
        "osgi.command.function=balance",
        "osgi.command.function=campaign",
        "osgi.command.function=comment",
        "osgi.command.function=report"
}, service = Object.class)
public class ContentCommand {

    private ContentService contentService;
    private UserService userService;
    private CampaignService campaignService;
    private com.empowerme.osgi.interaction.api.InteractionService interactionService;
    private com.empowerme.osgi.report.api.ReportingService reportingService;

    @Reference
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    @Reference
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Reference
    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Reference
    public void setInteractionService(com.empowerme.osgi.interaction.api.InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Reference
    public void setReportingService(com.empowerme.osgi.report.api.ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    public void list() {
        System.out.println("--- EmpowerMe Content Inventory ---");
        for (ContentAsset asset : contentService.getAssets()) {
            System.out.println(asset);
        }
        System.out.println("-----------------------------------");
    }

    public void add(String title, String type, long size) {
        ContentAsset asset = new ContentAsset();
        asset.setTitle(title);
        asset.setType(type);
        asset.setFileSize(size);
        asset.setId(System.currentTimeMillis()); 
        
        contentService.addAsset(asset);
        System.out.println("Asset added.");
    }

    public void balance(String userId) {
        System.out.println("Checking balance for User: " + userId);
        boolean hasFunds = userService.checkBalance(userId, 0); 
        System.out.println("User Active: " + hasFunds);
    }

    public void campaign(String name, String contentTitle, String userId) {
        campaignService.createCampaign(name, contentTitle, userId);
    }

    public void comment(String campaignId, String text, String author) {
        interactionService.postComment(campaignId, text, author);
    }

    public void report() {
        reportingService.generateReport();
    }
}
